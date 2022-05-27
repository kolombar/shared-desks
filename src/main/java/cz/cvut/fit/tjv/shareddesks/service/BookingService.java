package cz.cvut.fit.tjv.shareddesks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Booking;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.BookingId;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BookingRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.SharedDeskRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import static cz.cvut.fit.tjv.shareddesks.service.utils.FileUtils.addFileToZip;

@Service
public class BookingService {

  @Autowired private BookingRepository bookingRepository;

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private SharedDeskRepository sharedDeskRepository;

  @Value("${error-messages.employee-has-a-booking}")
  private String employeeHasABookingError;

  @Value("${error-messages.desk-is-already-booked}")
  private String deskIsBookedError;

  @Value("${error-messages.branch-mismatch}")
  private String branchMismatchError;

  @Value("${error-messages.premium-desk-booking}")
  private String premiumDeskBookingError;

  @Value("${error-messages.unexpected}")
  private String unexpectedError;

  @Value("${error-messages.missing-date}")
  private String missingDateError;

  @Value("${error-messages.date-before-today}")
  private String dateBeforeTodayError;

  public List<String> saveBooking(Booking booking) {
    List<String> errors = new ArrayList<>();

    if (booking.getId().getBookingTime() == null) {
      errors.add(missingDateError);
      return errors;
    }

    if (booking.getId().getBookingTime().isBefore(LocalDate.now())) {
      errors.add(dateBeforeTodayError);
      return errors;
    }

    Long employeeId = booking.getId().getEmployeeId();
    Long deskId = booking.getId().getDeskId();

    Optional<Booking> deskBooking =
        bookingRepository.findBookingByDeskAndDate(deskId, booking.getId().getBookingTime());

    Optional<Booking> employeeBooking =
        bookingRepository.findBookingByEmployeeAndDate(
            employeeId, booking.getId().getBookingTime());

    Optional<Branch> employeeBranch = employeeRepository.getEmployeeBranch(employeeId);
    Optional<Branch> deskBranch = sharedDeskRepository.getDeskLocation(deskId);

    Optional<Employee> employee = employeeRepository.findById(employeeId);
    Optional<SharedDesk> desk = sharedDeskRepository.findById(deskId);

    if (employeeBooking.isPresent() && deskBooking.isPresent()) {
      errors.add(employeeHasABookingError);
      errors.add(deskIsBookedError);
    } else if (employeeBooking.isPresent()) {
      errors.add(employeeHasABookingError);
    } else if (deskBooking.isPresent()) {
      errors.add(deskIsBookedError);
    } else if (!employeeBranch
        .orElse(new Branch())
        .getCity()
        .equals(deskBranch.orElse(new Branch()).getCity())) {
      errors.add(branchMismatchError);
    } else if (employee.isPresent() && desk.isPresent()) {
      if (!employee.get().getIsManager() && desk.get().getEquipment().equals(Equipment.PREMIUM)) {
        errors.add(premiumDeskBookingError);
      } else {
        bookingRepository.save(booking);
      }
    } else {
      errors.add(unexpectedError);
    }

    return errors;
  }

  private void getZip(
      OutputStream outStream, JpaRepository<Booking, BookingId> repository, String fileName)
      throws IOException {
    Pageable pageable = PageRequest.of(0, 30);
    ZipOutputStream zipOutputStream = new ZipOutputStream(outStream);
    List<Booking> bookings = repository.findAll(pageable).stream().toList();
    List<BookingId> entities =
        bookings.stream().map(Booking::getId).collect(Collectors.toList());

    while (!entities.isEmpty()) {

      ObjectWriter ow =
          new ObjectMapper().findAndRegisterModules().writer().withDefaultPrettyPrinter();
      String jsonResults = ow.writeValueAsString(entities);
      byte[] jsonBytes = jsonResults.getBytes(StandardCharsets.UTF_8);
      addFileToZip(zipOutputStream, jsonBytes, fileName);

      pageable = pageable.next();
      bookings = repository.findAll(pageable).stream().toList();
      entities =
              bookings.stream().map(Booking::getId).collect(Collectors.toList());
    }

    zipOutputStream.finish();
    zipOutputStream.flush();
    IOUtils.closeQuietly(zipOutputStream);
  }

  public void getBookingsZip(OutputStream outStream) throws IOException {
    getZip(outStream, bookingRepository, "bookings.json");
  }
}

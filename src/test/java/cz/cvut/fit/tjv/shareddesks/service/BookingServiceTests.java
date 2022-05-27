package cz.cvut.fit.tjv.shareddesks.service;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Booking;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BookingRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.SharedDeskRepository;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@SpringBootTest
public class BookingServiceTests {

  @Mock private BookingRepository bookingRepository;

  @Mock private EmployeeRepository employeeRepository;

  @Mock private SharedDeskRepository sharedDeskRepository;

  @Value("${error-messages.branch-mismatch}")
  private String branchMismatchError;

  @Value("${error-messages.desk-is-already-booked}")
  private String deskIsBookedError;

  @Value("${error-messages.employee-has-a-booking}")
  private String employeeHasABookingError;

  @InjectMocks private BookingService bookingService;

  @Test
  void saveBooking() {
    Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
    employee.setId(Long.valueOf(1l));
    SharedDesk desk = new SharedDesk(Equipment.STANDARD, new Branch("Prague"));
    desk.setId(Long.valueOf(1l));
    Booking booking = new Booking(employee, desk, LocalDate.now());

    Mockito.when(bookingRepository.findBookingByDeskAndDate(desk.getId(), LocalDate.now()))
        .thenReturn(Optional.empty());
    Mockito.when(bookingRepository.findBookingByEmployeeAndDate(employee.getId(), LocalDate.now()))
        .thenReturn(Optional.empty());
    Mockito.when(employeeRepository.getEmployeeBranch(employee.getId()))
        .thenReturn(Optional.of(new Branch("Prague")));
    Mockito.when(sharedDeskRepository.getDeskLocation(desk.getId()))
        .thenReturn(Optional.of(new Branch("Prague")));
    Mockito.when(employeeRepository.findById(1l)).thenReturn(Optional.of(employee));
    Mockito.when(sharedDeskRepository.findById(1l)).thenReturn(Optional.of(desk));

    List<String> messages = bookingService.saveBooking(booking);

    assertThat(messages, empty());
  }

  @Test
  void saveBookingBranchMismatch() {
    Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
    employee.setId(Long.valueOf(1l));
    SharedDesk desk = new SharedDesk(Equipment.STANDARD, new Branch("Prague"));
    desk.setId(Long.valueOf(1l));
    Booking booking = new Booking(employee, desk, LocalDate.now());

    Mockito.when(bookingRepository.findBookingByDeskAndDate(desk.getId(), LocalDate.now()))
            .thenReturn(Optional.empty());
    Mockito.when(bookingRepository.findBookingByEmployeeAndDate(employee.getId(), LocalDate.now()))
            .thenReturn(Optional.empty());
    Mockito.when(employeeRepository.getEmployeeBranch(employee.getId()))
            .thenReturn(Optional.of(new Branch("Prague")));
    Mockito.when(sharedDeskRepository.getDeskLocation(desk.getId()))
            .thenReturn(Optional.of(new Branch("Olomouc")));
    Mockito.when(employeeRepository.findById(1l)).thenReturn(Optional.of(employee));
    Mockito.when(sharedDeskRepository.findById(1l)).thenReturn(Optional.of(desk));

    ReflectionTestUtils.setField(bookingService, "branchMismatchError", branchMismatchError);

    List<String> messages = bookingService.saveBooking(booking);

    assertThat(messages, IsCollectionWithSize.hasSize(1));
    assertThat(messages, contains(branchMismatchError));
  }

  @Test
  void saveBookingDeskAlreadyBooked() {
    Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
    employee.setId(Long.valueOf(1l));
    SharedDesk desk = new SharedDesk(Equipment.STANDARD, new Branch("Prague"));
    desk.setId(Long.valueOf(1l));
    Booking booking = new Booking(employee, desk, LocalDate.now());

    Mockito.when(bookingRepository.findBookingByDeskAndDate(desk.getId(), LocalDate.now()))
            .thenReturn(Optional.of(booking));
    Mockito.when(bookingRepository.findBookingByEmployeeAndDate(employee.getId(), LocalDate.now()))
            .thenReturn(Optional.empty());
    Mockito.when(employeeRepository.getEmployeeBranch(employee.getId()))
            .thenReturn(Optional.of(new Branch("Prague")));
    Mockito.when(sharedDeskRepository.getDeskLocation(desk.getId()))
            .thenReturn(Optional.of(new Branch("Prague")));
    Mockito.when(employeeRepository.findById(1l)).thenReturn(Optional.of(employee));
    Mockito.when(sharedDeskRepository.findById(1l)).thenReturn(Optional.of(desk));

    ReflectionTestUtils.setField(bookingService, "deskIsBookedError", deskIsBookedError);

    List<String> messages = bookingService.saveBooking(booking);

    assertThat(messages, IsCollectionWithSize.hasSize(1));
    assertThat(messages, contains(deskIsBookedError));
  }

  @Test
  void saveBookingEmployeeHasABooking() {
    Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
    employee.setId(Long.valueOf(1l));
    SharedDesk desk = new SharedDesk(Equipment.STANDARD, new Branch("Prague"));
    desk.setId(Long.valueOf(1l));
    Booking booking = new Booking(employee, desk, LocalDate.now());

    Mockito.when(bookingRepository.findBookingByDeskAndDate(desk.getId(), LocalDate.now()))
            .thenReturn(Optional.empty());
    Mockito.when(bookingRepository.findBookingByEmployeeAndDate(employee.getId(), LocalDate.now()))
            .thenReturn(Optional.of(booking));
    Mockito.when(employeeRepository.getEmployeeBranch(employee.getId()))
            .thenReturn(Optional.of(new Branch("Prague")));
    Mockito.when(sharedDeskRepository.getDeskLocation(desk.getId()))
            .thenReturn(Optional.of(new Branch("Prague")));
    Mockito.when(employeeRepository.findById(1l)).thenReturn(Optional.of(employee));
    Mockito.when(sharedDeskRepository.findById(1l)).thenReturn(Optional.of(desk));

    ReflectionTestUtils.setField(bookingService, "employeeHasABookingError", employeeHasABookingError);

    List<String> messages = bookingService.saveBooking(booking);

    assertThat(messages, IsCollectionWithSize.hasSize(1));
    assertThat(messages, contains(employeeHasABookingError));
  }
}

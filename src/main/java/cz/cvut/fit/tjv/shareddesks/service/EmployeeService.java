package cz.cvut.fit.tjv.shareddesks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BookingRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import cz.cvut.fit.tjv.shareddesks.service.dto.EmployeesZipInfo;
import cz.cvut.fit.tjv.shareddesks.service.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import static cz.cvut.fit.tjv.shareddesks.service.utils.FileUtils.addFileToZip;

@Slf4j
@Service
public class EmployeeService {

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private BookingRepository bookingRepository;

  @Value("${error-messages.missing-first-name}")
  private String missingFirstNameError;

  @Value("${error-messages.missing-last-name}")
  private String missingLastNameError;

  private void getZip(
      OutputStream outStream, JpaRepository<Employee, Long> repository, String fileName)
      throws IOException {
    Pageable pageable = PageRequest.of(0, 30);
    ZipOutputStream zipOutputStream = new ZipOutputStream(outStream);
    List<Employee> employees = repository.findAll(pageable).stream().toList();
    List<EmployeesZipInfo> entities =
        employees.stream().map(EmployeesZipInfo::new).collect(Collectors.toList());
    ;

    while (!entities.isEmpty()) {

      ObjectWriter ow =
          new ObjectMapper().findAndRegisterModules().writer().withDefaultPrettyPrinter();
      String jsonResults = ow.writeValueAsString(entities);
      byte[] jsonBytes = jsonResults.getBytes(StandardCharsets.UTF_8);
      addFileToZip(zipOutputStream, jsonBytes, fileName);

      pageable = pageable.next();
      employees = repository.findAll(pageable).stream().toList();
      entities = employees.stream().map(EmployeesZipInfo::new).collect(Collectors.toList());
      ;
    }

    zipOutputStream.finish();
    zipOutputStream.flush();
    IOUtils.closeQuietly(zipOutputStream);
  }

  public void getEmployeesZip(OutputStream outStream) throws IOException {
    getZip(outStream, employeeRepository, "employees.json");
  }

  public List<String> saveEmployee(Employee employee) {
    List<String> errors = new ArrayList<>();

    if (employee.getFirstName() == null || employee.getFirstName().isBlank()) {
      errors.add(missingFirstNameError);
    }

    if (employee.getLastName() == null || employee.getLastName().isBlank()) {
      errors.add(missingLastNameError);
    }

    if (errors.isEmpty()) {
      employeeRepository.save(employee);
    }

    return errors;
  }

  @Transactional
  public List<String> editEmployee(Employee employee) {
    Optional<Employee> currEmployeeOpt = employeeRepository.findById(employee.getId());

    if (currEmployeeOpt.isPresent()) {
      Employee currEmployee = currEmployeeOpt.get();
      currEmployee.setFirstName(employee.getFirstName());
      currEmployee.setLastName(employee.getLastName());
      currEmployee.setEmploymentDurationInYears(employee.getEmploymentDurationInYears());
      if (!currEmployee.getBelongsTo().getCity().equals(employee.getBelongsTo().getCity())) {
        bookingRepository.deleteAllById_EmployeeId(currEmployee.getId());
      }
      currEmployee.setBelongsTo(employee.getBelongsTo());

      if (currEmployee.getIsManager() && !employee.getIsManager()) {
        bookingRepository.deleteAllById_EmployeeIdAndAndDesk_Equipment(
            currEmployee.getId(), Equipment.PREMIUM);
      }

      currEmployee.setIsManager(employee.getIsManager());

      return saveEmployee(currEmployee);
    }

    return Collections.emptyList();
  }
}

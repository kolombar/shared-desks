package cz.cvut.fit.tjv.shareddesks.service;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BookingRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@SpringBootTest
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Value("${error-messages.missing-first-name}")
    private String missingFirstNameError;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void saveEmployee() {
        Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
        employee.setId(1l);

        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        List<String> messages = employeeService.saveEmployee(employee);

        Assertions.assertEquals(0, messages.size());
    }

    @Test
    void saveEmployeeWithoutName() {
        Employee employee = new Employee(null, "lastName", 1, new Branch("Prague"), true);
        employee.setId(1l);

        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        ReflectionTestUtils.setField(employeeService, "missingFirstNameError", missingFirstNameError);

        List<String> messages = employeeService.saveEmployee(employee);

        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(missingFirstNameError, messages.get(0));
    }

    @Test
    void editEmployee() {
        Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
        employee.setId(1l);

        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        List<String> messages = employeeService.editEmployee(employee);

        Assertions.assertEquals(0, messages.size());
    }
}

package cz.cvut.fit.tjv.shareddesks.web;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EmployeesControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockBean
  private EmployeeRepository employeeRepository;

  @Test
  void getEmployees() throws Exception {
    List<Employee> defaultEmployeeList =
        List.of(new Employee("firstName", "lastName", 1, new Branch("Prague"), true));
    Mockito.when(employeeRepository.findAll()).thenReturn(defaultEmployeeList);

    mockMvc
        .perform(get("/employees"))
        .andExpect(status().isOk())
        .andExpect(view().name("EmployeesTemplate"))
        .andExpect(model().attribute("employees", defaultEmployeeList));
  }

  @Test
  void addEmployee() throws Exception {
    Employee employeeToAdd = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
    List<Employee> defaultEmployeeList = List.of(employeeToAdd);
    Mockito.when(employeeRepository.findAll()).thenReturn(defaultEmployeeList);

    mockMvc
            .perform(post("/employees/add").flashAttr("employee", employeeToAdd))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/employees"));
  }

  @Test
  void deleteEmployee() throws Exception {
    mockMvc
            .perform(get("/employees/delete").param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/employees"));
  }
}

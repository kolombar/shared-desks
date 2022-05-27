package cz.cvut.fit.tjv.shareddesks.web;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BranchRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import cz.cvut.fit.tjv.shareddesks.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/employees")

public class EmployeesController {

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private EmployeeService employeeService;

  @Autowired private BranchRepository branchRepository;

  @GetMapping
  public String allEmployees(Model model) {
    model.addAttribute("employees", employeeRepository.findAll());
    model.addAttribute("branches", branchRepository.findAll());
    if (!model.containsAttribute("employee")) {
      model.addAttribute("employee", new Employee());
    }
    if (!model.containsAttribute("message")) {
      model.addAttribute("message", "");
    }
    return "EmployeesTemplate";
  }

  @PostMapping("/add")
  public String addEmployee(
      @ModelAttribute Employee employee,
      BindingResult result,
      RedirectAttributes redirectAttributes) {
    List<String> messages = employeeService.saveEmployee(employee);

    if (!messages.isEmpty()) {
      for (String message : messages) {
        log.info("Error detected: {}", message);
        ObjectError error = new ObjectError("globalError", message);
        result.addError(error);
      }

      redirectAttributes.addFlashAttribute(
          "org.springframework.validation.BindingResult.employee", result);
      redirectAttributes.addFlashAttribute("employee", employee);
      redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
    } else {
      log.info("Adding employee with name {} {} from branch {}.", employee.getFirstName(), employee.getLastName(), employee.getBelongsTo().getCity());
      redirectAttributes.addFlashAttribute("message", "Employee added.");
    }

    return "redirect:/employees";
  }

  @RequestMapping(value = "/download", produces = "application/zip")
  public ResponseEntity<StreamingResponseBody> zipAnnotations(HttpServletResponse response) {

    response.addHeader("Content-Disposition", "attachment; filename=\"employees.zip\"");

    StreamingResponseBody stream = out -> employeeService.getEmployeesZip(out);

    return new ResponseEntity<>(stream, HttpStatus.OK);
  }

  @GetMapping("/delete")
  public String deleteEmployee(@RequestParam Long id) {
    log.info("Deleting employee with ID {}.", id);
    employeeRepository.deleteById(id);
    return "redirect:/employees";
  }

  @GetMapping("/edit")
  public String showEditForm(@RequestParam Long id, Model model) {
    Employee employee = employeeRepository.findById(id).get();
    model.addAttribute("editedEmployee", employee);
    model.addAttribute("branches", branchRepository.findAll());
    return "EditEmployeeForm";
  }

  @PostMapping("/edit")
  public String editEmployee(@RequestParam Long id, @ModelAttribute("editedEmployee") Employee employee, Model model, BindingResult result, RedirectAttributes redirectAttributes) {
    List<String> messages = employeeService.editEmployee(employee);

      if (!messages.isEmpty()) {
        for (String message : messages) {
          log.info("Error detected: {}", message);
          ObjectError error = new ObjectError("globalError", message);
          result.addError(error);
        }

        model.addAttribute("editedEmployee", employee);
        model.addAttribute("branches", branchRepository.findAll());
        return "EditEmployeeForm";
      } else {
        log.info("Employee with ID {} successfuly edited.", id);
        redirectAttributes.addFlashAttribute("message", "Employee successfuly edited.");
      }

    return "redirect:/employees";
  }
}

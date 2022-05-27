package cz.cvut.fit.tjv.shareddesks.web;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Booking;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.BookingId;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BookingRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.SharedDeskRepository;
import cz.cvut.fit.tjv.shareddesks.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/bookings")
public class BookingController {

  @Autowired private BookingRepository bookingRepository;

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private SharedDeskRepository sharedDeskRepository;

  @Autowired private BookingService bookingService;

  @GetMapping
  public String allBookings(Model model) {
    model.addAttribute("bookings", bookingRepository.findAll());
    model.addAttribute("employees", employeeRepository.findAll());
    model.addAttribute("desks", sharedDeskRepository.findAll());
    if (!model.containsAttribute("booking")) {
      model.addAttribute("booking", new Booking());
    }
    if (!model.containsAttribute("message")) {
      model.addAttribute("message", "");
    }

    return "BookingTemplate";
  }

  @PostMapping("/add")
  public String addBooking(
      @Valid @ModelAttribute("booking") Booking booking, BindingResult result, RedirectAttributes redirectAttributes) {
    List<String> messages = bookingService.saveBooking(booking);

    if (!messages.isEmpty()) {
      for (String message : messages) {
        log.info("Error detected: {}", message);
        ObjectError error = new ObjectError("globalError", message);
        result.addError(error);
      }

      redirectAttributes.addFlashAttribute(
          "org.springframework.validation.BindingResult.booking", result);
      redirectAttributes.addFlashAttribute("booking", booking);
      redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
    } else {
      log.info("Booking saved. {}", booking);
      redirectAttributes.addFlashAttribute("message", "Booking saved.");
    }

    return "redirect:/bookings";
  }

  @GetMapping("/delete")
  public String deleteBooking(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
      @RequestParam Long employeeId,
      @RequestParam Long deskId,
      RedirectAttributes redirectAttributes) {
    log.info("Deleting booking with date {}, employee ID {} and desk ID {}.", date, employeeId, deskId);
    bookingRepository.deleteById(new BookingId(deskId, employeeId, date));
    redirectAttributes.addFlashAttribute("message", "Booking deleted.");
    return "redirect:/bookings";
  }

  @RequestMapping(value = "/download", produces = "application/zip")
  public ResponseEntity<StreamingResponseBody> zipAnnotations(HttpServletResponse response) {

    response.addHeader("Content-Disposition", "attachment; filename=\"bookings.zip\"");

    StreamingResponseBody stream = out -> bookingService.getBookingsZip(out);

    return new ResponseEntity<>(stream, HttpStatus.OK);
  }
}

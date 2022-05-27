package cz.cvut.fit.tjv.shareddesks.web;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Booking;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BookingRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.EmployeeRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.SharedDeskRepository;
import cz.cvut.fit.tjv.shareddesks.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private SharedDeskRepository sharedDeskRepository;

    @MockBean
    private BookingService bookingService;

    @Test
    void getBookings() throws Exception {
        Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
        employee.setId(Long.valueOf(1l));
        SharedDesk desk = new SharedDesk(Equipment.STANDARD, new Branch("Olomouc"));
        desk.setId(Long.valueOf(1l));
        Booking booking = new Booking(employee, desk, LocalDate.now());
        List<Booking> defaultBookingList = List.of(booking);
        Mockito.when(bookingRepository.findAll()).thenReturn(defaultBookingList);
        Mockito.when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(sharedDeskRepository.findAll()).thenReturn(Collections.emptyList());


        mockMvc
                .perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(view().name("BookingTemplate"))
                .andExpect(model().attribute("bookings", defaultBookingList));
    }

    @Test
    void addBooking() throws Exception {
        Employee employee = new Employee("firstName", "lastName", 1, new Branch("Prague"), true);
        employee.setId(Long.valueOf(1l));
        SharedDesk desk = new SharedDesk(Equipment.STANDARD, new Branch("Olomouc"));
        desk.setId(Long.valueOf(1l));
        Booking booking = new Booking(employee, desk, LocalDate.now());
        List<Booking> defaultBookingList =
                List.of(booking);
        Mockito.when(bookingRepository.findAll()).thenReturn(defaultBookingList);
        Mockito.when(bookingService.saveBooking(booking)).thenReturn(Collections.emptyList());

        mockMvc
                .perform(post("/bookings/add").flashAttr("booking", new Booking(employee, desk, LocalDate.now())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bookings"));
    }

    @Test
    void deleteBooking() throws Exception {
    mockMvc
        .perform(get("/bookings/delete").param("date", LocalDate.now().toString())
                .param("employeeId", "1")
                .param("deskId", "1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/bookings"));
    }
}

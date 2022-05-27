package cz.cvut.fit.tjv.shareddesks.service.dto;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.BookingId;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class EmployeesZipInfo {
    public EmployeesZipInfo (Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.employmentDurationInYears = employee.getEmploymentDurationInYears();
        this.belongsTo = employee.getBelongsTo().getCity();
        this.isManager = employee.getIsManager();

        employee.getBookings().forEach(booking -> {
            bookings.add(booking.getId());
        });
    }

    private Long id;
    private Set<BookingId> bookings = new HashSet<>();
    private String firstName;
    private String lastName;
    private Integer employmentDurationInYears;
    private String belongsTo;
    private Boolean isManager;
}

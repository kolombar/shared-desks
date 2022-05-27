package cz.cvut.fit.tjv.shareddesks.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking {

  public Booking(Employee employee, SharedDesk desk, LocalDate bookingTime) {
    this.id = new BookingId(desk.getId(), employee.getId(), bookingTime);

    this.employee = employee;
    this.desk = desk;

    employee.getBookings().add(this);
    desk.getBookings().add(this);
  }

  @EmbeddedId private BookingId id;

  @ManyToOne
  @JoinColumn(name = "fk_desk", insertable = false, updatable = false)
  private SharedDesk desk;

  @ManyToOne
  @JoinColumn(name = "fk_employee", insertable = false, updatable = false)
  private Employee employee;

  @Override
  public String toString() {
    String date;
    if (id.bookingTime == null) {
      date = "";
    } else {
      date = id.bookingTime.toString();
    }

    String employeeId;
    if (id.getEmployeeId() == null) {
      employeeId = "";
    } else {
      employeeId = id.getEmployeeId().toString();
    }

    String deskId;
    if (id.getDeskId() == null) {
      deskId = "";
    } else {
      deskId = id.getDeskId().toString();
    }
    return "Date: "
        + date
        + ", Employee: "
        + employeeId
        + ", Desk: "
        + deskId;
  }
}

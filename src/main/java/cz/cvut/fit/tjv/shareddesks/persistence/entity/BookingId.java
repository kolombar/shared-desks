package cz.cvut.fit.tjv.shareddesks.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class BookingId implements Serializable {
  @Column(name = "fk_desk")
  private Long deskId;

  @Column(name = "fk_employee")
  private Long employeeId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "Missing booking date!")
  LocalDate bookingTime;

  public BookingId(Long deskId, Long employeeId, LocalDate bookingTime) {
    this.deskId = deskId;
    this.employeeId = employeeId;
    this.bookingTime = bookingTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((deskId == null) ? 0 : deskId.hashCode());
    result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
    result = prime * result + ((bookingTime == null) ? 0 : bookingTime.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;

    BookingId other = (BookingId) obj;

    if (deskId == null) {
      if (other.deskId != null) return false;
    } else if (!deskId.equals(other.deskId)) return false;

    if (employeeId == null) {
      if (other.employeeId != null) return false;
    } else if (!employeeId.equals(other.employeeId)) return false;

    if (bookingTime == null) {
      if (other.bookingTime != null) return false;
    } else if (!bookingTime.equals(other.bookingTime)) return false;

    return true;
  }
}

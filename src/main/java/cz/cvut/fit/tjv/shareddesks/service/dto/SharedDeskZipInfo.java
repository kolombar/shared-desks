package cz.cvut.fit.tjv.shareddesks.service.dto;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.BookingId;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SharedDeskZipInfo {
  public SharedDeskZipInfo(SharedDesk desk) {
    this.id = desk.getId();
    this.equipment = desk.getEquipment();
    this.isLocatedAt = desk.getIsLocatedAt().getCity();

    desk.getBookings()
        .forEach(
            booking -> {
              bookings.add(booking.getId());
            });
  }

  private Long id;
  private Set<BookingId> bookings = new HashSet<>();
  private Equipment equipment;
  private String isLocatedAt;
}

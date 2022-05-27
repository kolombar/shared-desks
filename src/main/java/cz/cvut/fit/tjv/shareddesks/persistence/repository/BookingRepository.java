package cz.cvut.fit.tjv.shareddesks.persistence.repository;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Booking;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.BookingId;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import org.hibernate.loader.plan.build.internal.spaces.EntityQuerySpaceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, BookingId> {

    @Query("select b from Booking b where b.id.deskId = ?1 and b.id.bookingTime = ?2")
    Optional<Booking> findBookingByDeskAndDate(Long deskId, LocalDate date);

    @Query("select b from Booking b where b.id.employeeId = ?1 and b.id.bookingTime = ?2")
    Optional<Booking> findBookingByEmployeeAndDate(Long employeeId, LocalDate date);

    void deleteAllById_EmployeeId(Long employeeId);

    void deleteAllById_EmployeeIdAndAndDesk_Equipment(Long employeeId, Equipment equipment);

}


package de.limehome.backendchallengejava;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value =
            """
            SELECT *
            FROM   bookings b
            WHERE  b.unitid = :unitID
              AND  b.id    <> :id
              AND  b.check_in_date < :newEnd
              AND  date(b.check_in_date, '+' || b.number_of_nights || ' days') > :currentEnd
            """,
            nativeQuery = true)
    List<Booking> findConflictingBookings(@Param("unitID")   String   unitID,
                                          @Param("currentEnd") String currentEnd,
                                          @Param("newEnd")     String newEnd,
                                          @Param("id")         Long      id);


}

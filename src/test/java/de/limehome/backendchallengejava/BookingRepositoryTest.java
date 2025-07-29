package de.limehome.backendchallengejava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void findConflictingBookings_returnsConflict_whenOverlapExists() {
        // Existing booking: July 1 to July 5
        Booking existing = new Booking("guestA",
                "unit1",
                LocalDate.of(2025, 7, 1),
                4);
        bookingRepository.save(existing);

        // Check: extending another booking to July 3 – July 6 should overlap
        String currentEnd = LocalDate.of(2025, 7, 3).format(formatter);
        String newEnd = LocalDate.of(2025, 7, 6).format(formatter);

        List<Booking> conflicts =
                bookingRepository.findConflictingBookings("unit1", currentEnd, newEnd,2L);
        assertThat(conflicts).hasSize(1);
    }

    @Test
    void findConflictingBookings_returnsEmpty_whenNoOverlap() {
        // Existing booking: July 1 to July 5
        Booking existing = new Booking("guestA", "unit1", LocalDate.of(2025, 7, 1), 4);
        bookingRepository.save(existing);

        // Check: extending from July 5 to July 7 should NOT conflict
        String currentEnd = LocalDate.of(2025, 7, 5).format(formatter);
        String newEnd = LocalDate.of(2025, 7, 7).format(formatter);

        List<Booking> conflicts = bookingRepository.findConflictingBookings("unit1", currentEnd, newEnd, -1L);
        assertThat(conflicts).isEmpty();
    }

    @Test
    void findConflictingBookings_returnsEmpty_whenNewBookingBeforeExisting() {
        Booking existing = new Booking("guestA", "unit1", LocalDate.of(2025, 7, 10), 3); // starts 10th
        bookingRepository.save(existing);

        String currentEnd = LocalDate.of(2025, 7, 5).format(formatter);
        String newEnd = LocalDate.of(2025, 7, 9).format(formatter); // ends before 10th
        List<Booking> conflicts = bookingRepository.findConflictingBookings("unit1", currentEnd, newEnd, -1L);
        assertThat(conflicts).isEmpty();
    }

}

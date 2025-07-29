package de.limehome.backendchallengejava;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Bookings")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Booking {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NonNull
    String guestName;

    @NonNull
    String unitID;

    @NonNull
    LocalDate checkInDate;

    @NonNull
    Integer numberOfNights;

    Booking(BookingInput input) {
        this(input.guestName, input.unitID, input.checkInDate, input.numberOfNights);
    }
}


@Data
@AllArgsConstructor
class BookingInput {
    @NonNull
    String guestName;

    @NonNull
    String unitID;

    @NonNull
    LocalDate checkInDate;

    int numberOfNights;
}
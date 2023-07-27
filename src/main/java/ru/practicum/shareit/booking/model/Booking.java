package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "start_date")
    LocalDateTime startDate;

    @Column(name = "end_date")
    LocalDateTime endDate;

    @Column(name = "item_id")
    Item item;

    @Column(name = "booker_id")
    User booker;

    @Enumerated(EnumType.STRING)
    Status status;
}

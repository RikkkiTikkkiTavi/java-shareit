package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items", schema = "public")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "owner_id")
    private User ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

//    @Column(name = "request_id")
//    private long requestId;
}

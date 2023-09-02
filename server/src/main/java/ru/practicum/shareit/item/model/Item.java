package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @ManyToOne
    @JoinTable(
            name = "item_requests",
            joinColumns = @JoinColumn(
                    name = "item_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "request_id", referencedColumnName = "id"
            )
    )
    private ItemRequest itemRequest;
}

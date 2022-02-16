package ecommerce.web.app.domain.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cardId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post;

    private String totalPrice;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ordered_by_user")
    private User user;

    private LocalTime time;

    private LocalDate date;

}

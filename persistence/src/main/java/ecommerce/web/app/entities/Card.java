package ecommerce.web.app.entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "card")
public class Card extends BaseEntity  {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
    private String totalPrice;
}

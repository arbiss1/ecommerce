package ecommerce.web.app.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "card")
public class Card extends BaseEntity  {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post;
    private String totalPrice;
}

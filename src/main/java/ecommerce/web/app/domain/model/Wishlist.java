package ecommerce.web.app.domain.model;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long wishlistId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post")
    private Post post ;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
}

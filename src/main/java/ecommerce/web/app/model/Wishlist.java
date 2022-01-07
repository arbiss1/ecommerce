package ecommerce.web.app.model;

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

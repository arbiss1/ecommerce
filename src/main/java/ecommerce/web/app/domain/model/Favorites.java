package ecommerce.web.app.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "favorites")
public class Favorites extends BaseEntity {


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post ;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
}

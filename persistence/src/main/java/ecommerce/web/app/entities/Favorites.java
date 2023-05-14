package ecommerce.web.app.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "favorites")
public class Favorites extends BaseEntity {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post ;
}

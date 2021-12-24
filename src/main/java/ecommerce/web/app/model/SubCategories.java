package ecommerce.web.app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "subcateogry")
public class SubCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private long subcategoryId;
//    private List<String> subcategories;
}

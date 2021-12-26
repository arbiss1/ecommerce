package ecommerce.web.app.model;


import lombok.Data;
import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "categories")
@Data
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long categoryId;

    private String categoryName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "psot_subcategory")
    private List<SubCategories> subCategories;

}

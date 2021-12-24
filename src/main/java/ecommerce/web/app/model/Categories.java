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

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "details_id")
//    private List<SubCategories> subCategories;

}

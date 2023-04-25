package ecommerce.web.app.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "subcategory")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long id;
    private String name;
}

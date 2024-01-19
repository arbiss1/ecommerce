package ecommerce.web.app.entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "subcategory")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  String id;
    private String name;
}

package ecommerce.web.app.entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
}
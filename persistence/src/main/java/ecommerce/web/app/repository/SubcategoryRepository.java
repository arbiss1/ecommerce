package ecommerce.web.app.repository;

import ecommerce.web.app.entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory,Long> {
    Optional<Subcategory> findSubcategoryByName(String name);
}

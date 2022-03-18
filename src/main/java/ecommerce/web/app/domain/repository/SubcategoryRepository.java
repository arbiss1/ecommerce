package ecommerce.web.app.domain.repository;

import ecommerce.web.app.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory,Long> {
    Optional<Subcategory> findSubcategoryByName(String name);
}

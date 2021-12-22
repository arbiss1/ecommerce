package ecommerce.web.app.repository;

import ecommerce.web.app.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories,Long> {

    Optional<Categories> findByCategories(String category);
}

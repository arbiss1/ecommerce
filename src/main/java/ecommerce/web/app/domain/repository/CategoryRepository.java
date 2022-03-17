package ecommerce.web.app.domain.repository;

import ecommerce.web.app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}

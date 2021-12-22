package ecommerce.web.app.service;

import ecommerce.web.app.model.Categories;
import ecommerce.web.app.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoriesRepository categoriesRepository;

    public Optional<Categories> findByCategory(Categories category){
        return categoriesRepository.findByCategories(category.getCategories());
    }
}

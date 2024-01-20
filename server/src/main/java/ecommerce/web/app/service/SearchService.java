package ecommerce.web.app.service;

import ecommerce.web.app.controller.model.SearchBuilderRequest;
import ecommerce.web.app.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private EntityManager entityManager;

    public List<Post> searchPosts(SearchBuilderRequest searchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root<Post> root = criteriaQuery.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        addPricePredicates(searchRequest, criteriaBuilder, root, predicates);
        addYearPredicates(searchRequest, criteriaBuilder, root, predicates);
        addColorPredicate(searchRequest, criteriaBuilder, root, predicates);

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private void addPricePredicates(SearchBuilderRequest searchRequest, CriteriaBuilder criteriaBuilder, Root<Post> root, List<Predicate> predicates) {
        if (searchRequest.getHighestPrice() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), searchRequest.getHighestPrice()));
        }

        if (searchRequest.getLowestPrice() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), searchRequest.getLowestPrice()));
        }
    }

    private void addYearPredicates(SearchBuilderRequest searchRequest, CriteriaBuilder criteriaBuilder, Root<Post> root, List<Predicate> predicates) {
        if (searchRequest.getFromYear() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("firstRegistration"), searchRequest.getFromYear()));
        }

        if (searchRequest.getToYear() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("firstRegistration"), searchRequest.getToYear()));
        }
    }

    private void addColorPredicate(SearchBuilderRequest searchRequest, CriteriaBuilder criteriaBuilder, Root<Post> root, List<Predicate> predicates) {
        if (searchRequest.getColor() != null) {
            predicates.add(criteriaBuilder.equal(root.get("color"), searchRequest.getColor()));
        }
    }
}


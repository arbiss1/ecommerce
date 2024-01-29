package ecommerce.web.app.service;

import ecommerce.web.app.controller.model.SearchBuilderRequest;
import ecommerce.web.app.entities.Post;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

@Service
@AllArgsConstructor
public class SearchService {

    private final EntityManager entityManager;

    public Page<Post> searchPosts(SearchBuilderRequest searchRequest, Integer page, Integer size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root<Post> root = criteriaQuery.from(Post.class);

        List<Predicate> predicates = buildSearchPredicates(searchRequest, criteriaBuilder, root);

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Post> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Post> result = typedQuery
                .setFirstResult(page)
                .setMaxResults(size)
                .getResultList();

        return new PageImpl<>(result, PageRequest.of(page, size), getCount());
    }

    public long getCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Post> postRoot = criteriaQuery.from(Post.class);

        criteriaQuery.select(builder.count(postRoot));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private List<Predicate> buildSearchPredicates(SearchBuilderRequest searchRequest, CriteriaBuilder criteriaBuilder, Root<Post> root) {
        List<Predicate> predicates = new ArrayList<>();

        addPredicateIfNotNull(searchRequest.getHighestPrice(), root.get("price"), criteriaBuilder::lessThanOrEqualTo, predicates);
        addPredicateIfNotNull(searchRequest.getLowestPrice(), root.get("price"), criteriaBuilder::greaterThanOrEqualTo, predicates);

        addPredicateIfNotNull(searchRequest.getFromYear(), root.get("firstRegistration"), criteriaBuilder::greaterThanOrEqualTo, predicates);
        addPredicateIfNotNull(searchRequest.getToYear(), root.get("firstRegistration"), criteriaBuilder::lessThanOrEqualTo, predicates);

        addPredicateIfNotNull(searchRequest.getColor(), root.get("color"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getType(), root.get("type"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getBrand(), root.get("brand"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getModel(), root.get("model"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getPower(), root.get("power"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getEngineSize(), root.get("engineSize"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getTransmission(), root.get("transmission"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getKilometers(), root.get("kilometers"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getFuel(), root.get("fuel"), criteriaBuilder::equal, predicates);
        addPredicateIfNotNull(searchRequest.getPostType(), root.get("postType"), criteriaBuilder::equal, predicates);

        return predicates;
    }

    private <T> void addPredicateIfNotNull(T value, Path<T> path, BiFunction<Path<T>, T, Predicate> predicateFunction, List<Predicate> predicates) {
        if (Objects.nonNull(value)) {
            predicates.add(predicateFunction.apply(path, value));
        }
    }
}


package ptit.edu.vn.bookshop.repository.specification;

import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;
import ptit.edu.vn.bookshop.domain.entity.Role;

public class GenericSpecification<T> implements Specification<T> {
    private SpecSearchCriteria criteria;

    public GenericSpecification(SpecSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NotNull final Root<T> root, @NotNull final CriteriaQuery<?> query, @NotNull final CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
        };
    }
}

package ptit.edu.vn.bookshop.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecificationBuilder<T> {
    private final List<SpecSearchCriteria> params;

    public GenericSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public GenericSpecificationBuilder<T> with(String key, String operation, String value, String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public GenericSpecificationBuilder<T> with(String orPredicate, String key, String operation, String value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));

        if (oper != null && oper.equals(SearchOperation.EQUALITY)) {
            boolean startWithAsterisk = prefix != null && prefix.contains("*");
            boolean endWithAsterisk = suffix != null && suffix.contains("*");

            if (startWithAsterisk && endWithAsterisk) {
                oper = SearchOperation.CONTAINS;
            } else if (startWithAsterisk) {
                oper = SearchOperation.ENDS_WITH;
            } else if (endWithAsterisk) {
                oper = SearchOperation.STARTS_WITH;
            }
        }

        params.add(new SpecSearchCriteria(orPredicate, key, oper, value));
        return this;
    }

    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<SpecSearchCriteria> validCriteria = params.stream()
                .filter(c -> c != null && c.getOperation() != null && c.getKey() != null)
                .toList();

        if (validCriteria.isEmpty()) {
            return null;
        }

        Specification<T> spec = new GenericSpecification<>(validCriteria.get(0));
        for (int i = 1; i < validCriteria.size(); i++) {
            SpecSearchCriteria criteria = validCriteria.get(i);
            spec = criteria.getOrPredicate()
                    ? Specification.where(spec).or(new GenericSpecification<>(criteria))
                    : Specification.where(spec).and(new GenericSpecification<>(criteria));
        }
        return spec;
    }
}

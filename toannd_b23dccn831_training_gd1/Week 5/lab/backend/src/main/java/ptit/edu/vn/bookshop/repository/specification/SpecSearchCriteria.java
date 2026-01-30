package ptit.edu.vn.bookshop.repository.specification;

import lombok.Getter;

import static ptit.edu.vn.bookshop.repository.specification.SearchOperation.OR_PREDICATE_FLAG;

@Getter
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private Boolean orPredicate;

    public SpecSearchCriteria(String key, SearchOperation operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String orPredicate, String key, SearchOperation operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
    }

    public SpecSearchCriteria(String key, String operation, String value, String prefix, String suffix) {
        SearchOperation oper = null;
        if (operation != null && operation.length() > 0) {
            oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        }
        if (oper != null && oper == SearchOperation.EQUALITY) {
            boolean startWithAsterisk = prefix != null && prefix.contains("*");
            boolean endWithAsterisk = suffix != null && suffix.contains("*");
            if (startWithAsterisk && endWithAsterisk) oper = SearchOperation.CONTAINS;
            else if (startWithAsterisk) {
                oper = SearchOperation.ENDS_WITH;
            } else if (endWithAsterisk) {
                oper = SearchOperation.STARTS_WITH;
            }
        }
        this.key = key;
        this.operation = oper;
        this.value = value;
        this.orPredicate = false;
    }
}

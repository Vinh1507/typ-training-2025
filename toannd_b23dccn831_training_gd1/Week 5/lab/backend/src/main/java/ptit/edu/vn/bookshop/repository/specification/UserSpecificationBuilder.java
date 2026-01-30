package ptit.edu.vn.bookshop.repository.specification;

import ptit.edu.vn.bookshop.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificationBuilder extends GenericSpecificationBuilder<User> {
    public UserSpecificationBuilder() {
        super();
    }
}

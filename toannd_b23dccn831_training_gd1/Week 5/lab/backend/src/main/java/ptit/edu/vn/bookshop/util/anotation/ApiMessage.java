package ptit.edu.vn.bookshop.util.anotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiMessage {
    String value();
}

package ptit.edu.vn.bookshop.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}

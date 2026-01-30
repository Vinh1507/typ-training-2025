package ptit.edu.vn.bookshop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import ptit.edu.vn.bookshop.domain.entity.Permission;
import ptit.edu.vn.bookshop.domain.entity.Role;
import ptit.edu.vn.bookshop.domain.entity.User;
import ptit.edu.vn.bookshop.exception.PermissionException;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.util.List;

@Service
public class PermissionIntercepter implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();
        // check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.getUserByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(p ->
                            p.getApiPath().equals(path) && p.getMethod().equalsIgnoreCase(httpMethod));
                    // neu khong co quyen
                    if (!isAllow) {
                        throw new PermissionException("User don't have permission to access this resource");
                    }
                }
                else {
                    throw new PermissionException("User don't have permission to access this resource");
                }
            }
        } else {
            System.out.println(">>> email is null");
        }
        return true;
    }
}

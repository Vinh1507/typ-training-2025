package ptit.edu.vn.bookshop.service.impl;

import ptit.edu.vn.bookshop.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ptit.edu.vn.bookshop.domain.entity.User user = this.userService.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
//        return new User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
    }

}

// package ptit.edu.vn.bookshop.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import ptit.edu.vn.bookshop.service.UserService;


// @Configuration
// public class PermissionIntercepterConfiguration implements WebMvcConfigurer {

//     @Bean
//     PermissionIntercepter permissionIntercepter() {
//         return new PermissionIntercepter();
//     }

//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         String[] whiteList = {
//                 "/",
//                 "/api/v1/auth/verify",
//                 "/api/v1/auth/register",
//                 "/api/v1/auth/refresh",
//                 "/api/v1/auth/account",
//                 "/api/v1/auth/login",
//                 "/api/v1/auth/logout",
//                 "/api/v1/files/**",
//                 "/api/v1/auth/forgot-password",
//                 "/api/v1/auth/verify-otp",
//                 "/api/v1/auth/reset-password",
//                 "/storage/**",
//                 "/v3/api-docs/**",
//                 "/swagger-ui/**",
//                 "/swagger-ui.html"
//         };

//         registry.addInterceptor(permissionIntercepter())
//                 .addPathPatterns("/**")          // Áp dụng cho tất cả request
//                 .excludePathPatterns(whiteList); // Bỏ qua các API public
//     }
// }


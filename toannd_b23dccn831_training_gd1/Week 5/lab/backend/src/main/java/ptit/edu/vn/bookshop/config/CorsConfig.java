package ptit.edu.vn.bookshop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;

@Configuration
public class CorsConfig {
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4173", "http://localhost:5173"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
//                "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type",
//                "Accept", "x-no-retry"));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
@Bean
public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:4173",
            "http://localhost:5173",
            "http://192.168.22.150:3000"
    ));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
}
}

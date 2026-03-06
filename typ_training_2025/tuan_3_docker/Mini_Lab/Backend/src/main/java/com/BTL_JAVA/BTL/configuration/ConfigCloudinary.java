package com.BTL_JAVA.BTL.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {

    @Bean
    public Cloudinary configKey() {
        Map<String,String> config = new HashMap<String,String>();
        config.put("cloud_name", "de8vli4dk");
        config.put("api_key", "926854398157523");
        config.put("api_secret", "h1xxhjl01jQ_zgqu6K6-BLXy-XM");
        return new Cloudinary(config);
    }
}

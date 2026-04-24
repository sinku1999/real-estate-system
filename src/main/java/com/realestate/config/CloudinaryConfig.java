package com.realestate.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ddjjfgzxs",
                "api_key", "792842596144226",
                "api_secret", "wDnSi6EkIEfKazuBL5mKPGIw9lY"
        ));
    }
}
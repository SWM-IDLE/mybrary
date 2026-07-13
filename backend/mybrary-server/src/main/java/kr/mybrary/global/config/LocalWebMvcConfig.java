package kr.mybrary.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Profile("local")
public class LocalWebMvcConfig implements WebMvcConfigurer {

    @Value("${local.storage.upload-dir:./local-uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path path = Paths.get(uploadDir).toAbsolutePath();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path + "/");
    }
}

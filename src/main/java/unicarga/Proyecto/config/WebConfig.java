package unicarga.Proyecto.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico",
                        "/error",
                        // springdoc (ya está en el pom)
                        "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**");
    }
}

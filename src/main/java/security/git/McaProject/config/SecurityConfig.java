package security.git.McaProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/webhook")  // disable CSRF for webhook
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/webhook").permitAll()  // allow without login
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
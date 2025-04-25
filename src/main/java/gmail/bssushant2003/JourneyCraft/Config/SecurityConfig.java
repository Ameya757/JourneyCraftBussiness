package gmail.bssushant2003.JourneyCraft.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/firebase-sync/all").permitAll()
                        .requestMatchers(
                                "/api/restaurants/update/**",
                                "/api/guides/update/**",
                                "/api/restaurants/register/restaurant/**",
                                "/api/guides/register/guide/**",
                                "/api/restaurants/restaurant/**",
                                "/api/users/login",
                                "/api/guides/guide/**",
                                "/api/users/register",
                                "/api/users/send-otp",
                                "/api/users/verify-otp"
                        ).permitAll()) // Allow all requests
                .build();
    }
}

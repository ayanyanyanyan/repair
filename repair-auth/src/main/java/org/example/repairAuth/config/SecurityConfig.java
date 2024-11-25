package org.example.repairAuth.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.repairAuth.service.Impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceimpl;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    /*
    获取登录表单额外参数
 */
    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable()) // 关闭csrf
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user/*").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login").permitAll());


//                        .authenticationDetailsSource(authenticationDetailsSource))
//                .logout((logout) -> logout.permitAll());
        return http.build();
    }


}

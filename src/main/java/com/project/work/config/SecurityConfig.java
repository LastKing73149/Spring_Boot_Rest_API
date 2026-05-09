package com.project.work.config;

import com.project.work.model.Role;
import com.project.work.model.User;
import com.project.work.service.RoleService;
import com.project.work.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class SecurityConfig {
    private final SuccessUserHandler successUserHandler;

    public SecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public CommandLineRunner initData(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder){
        return args -> {

            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleService.save(adminRole);
            roleService.save(userRole);

            if(userService.getAllUsers().isEmpty()) {
                User admin = new User();
                admin.setUsername("Admin");
                admin.setPassword("2222");
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(userRole);
                admin.setRoles(roles);

                admin.setName("Admin");
                admin.setLastName("Adminsk");
                admin.setAge(20);
                admin.setEmail("admin@gmail.com");
                userService.saveUser(admin);

                System.out.println("CommandLineRunner: Аккаунт администратора создан");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, SuccessUserHandler successUserHandler) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
        )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successUserHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

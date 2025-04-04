/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurante.example.demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import restaurante.example.demo.config.security.filters.JwtTokenValidator;
import restaurante.example.demo.config.security.jwt.JwtUtils;
import restaurante.example.demo.service.implementation.user.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {
      
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtils jwtUtils;
    
   
     // ejemplo de configuracion basica de securityFilterChain
  
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {    
                    // configurar los enpoints privados                         
                    http.requestMatchers(HttpMethod.GET, "/api/v1/employee/find/all").hasAnyRole("Administrador","Empleado","Super");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/employee/signup").hasAnyRole("Administrador","Super");                    
                    http.requestMatchers(HttpMethod.GET, "/api/v1/admin/find/all").hasAnyRole("Super","Administrador");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/admin/signup").hasAnyRole("Super");
                    //configurar los endpoints publicos                  
                    http.requestMatchers(HttpMethod.GET, "/api/v1/super/find/all").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/super/signup").permitAll();                    
                    http.requestMatchers(HttpMethod.GET, "/api/v1/customer/find/all").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/customer/signup").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").permitAll();
                    // configurar los enpoints no especificados
                    // 1 forma:
                    // permite todo endpoints que tenga credenciales
                    //http.anyRequest().authenticated();
                    
                    // 2 forma(reconmedada):
                    // rechaza a todo endpoints que no se especifica
                 //   http.anyRequest().denyAll();                 
                })
               // .httpBasic(Customizer.withDefaults())
                .addFilterBefore(new JwtTokenValidator(this.jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }
 
    

    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(this.passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }
        
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
     
}

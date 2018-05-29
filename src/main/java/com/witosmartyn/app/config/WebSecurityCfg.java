package com.witosmartyn.app.config;

import com.witosmartyn.app.services.UserService;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Log4j
public class WebSecurityCfg extends WebSecurityConfigurerAdapter  {

//    @Setter(onMethod = @__({@Autowired,@Qualifier("userService")}) )
    private UserService userService;

//    @Setter(onMethod = @__({@Autowired, @Qualifier("bcryptPasswordEncoder")}) )
    private PasswordEncoder passwordEncoder;

    private AuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Autowired
    @Qualifier("myAuthSuccessHandler")
    public void setCustomizeAuthenticationSuccessHandler(AuthenticationSuccessHandler customizeAuthenticationSuccessHandler) {
        log.debug("WebSecurityCfg -> set myAuthSuccessHandler");

        this.customizeAuthenticationSuccessHandler = customizeAuthenticationSuccessHandler;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        log.debug("WebSecurityCfg -> set bcryptPasswordEncoder");
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setUserService(UserService userService) {
        log.debug("WebSecurityCfg -> set UserDetailService");
        this.userService = userService;
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
                .antMatchers("/readme.txt", "/css/**","/webjars/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                .antMatchers("/search","/search/items/**").permitAll()
                .antMatchers("/files/images/**").permitAll()
                .antMatchers("/login", "/register").permitAll()
                .antMatchers("/profile/**","/logout").hasAnyAuthority("USER","ADMIN","ACTUATOR")
                .antMatchers("/control/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .successHandler(customizeAuthenticationSuccessHandler)
                    .loginPage("/login")
                    .permitAll()
            .and()
                .sessionManagement()
                    .invalidSessionUrl("/login")
            .and()
                .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder)
        ;
    }




}

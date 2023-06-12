
package com.oksanatrifonova.bookshop.security;

import com.oksanatrifonova.bookshop.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private AppUserService userService;
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();}
        @Bean
        public DaoAuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider auth=new DaoAuthenticationProvider();
            auth.setUserDetailsService(userService);
            auth.setPasswordEncoder(passwordEncoder());
            return auth;
        }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/**/*.html","/**/*.css","/**/*.jpg",
                        "/images/**","/","/books","/contacts","/book/{id:[0-9]+}","/book/{id:[0-9]+}/cart",
                        "/cart/**","/place-order")
                .permitAll()
//                .antMatchers("/users","/user/add","user")
                .antMatchers("/login/register/**")
                .permitAll()
                .antMatchers("/admin","/user/add","/user/set-role").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/users","/authors/**","/author/add/**","authors/{id:[0-9]+}/books","/book/add","/book/{id:[0-9]+}/edit",
                        "/book/{id:[0-9]+}/remove").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/manager").hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}





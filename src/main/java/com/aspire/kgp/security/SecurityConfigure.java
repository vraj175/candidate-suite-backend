package com.aspire.kgp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomDetailsService customDetailsService;

  @Autowired
  private ApiKeyAuthFilter apiKeyAuthFilter;

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  @Autowired
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customDetailsService).passwordEncoder(encoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().and()
        .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  // @Override
  // protected void configure(HttpSecurity http) throws Exception {
  // http.csrf().disable().authorizeRequests()
  // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().and()
  // .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
  // .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  //
  // }
}

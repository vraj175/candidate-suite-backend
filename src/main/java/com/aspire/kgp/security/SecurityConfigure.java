package com.aspire.kgp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

	@Autowired
	private ApiKeyAuthFilter apiKeyAuthFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.permitAll().and().addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);

	}
}

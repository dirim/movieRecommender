package com.ozge.movieRecommender.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/css/**", "/fonts/**", "/js/**", "/templates/**").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/users/register").permitAll()
				.antMatchers("/users/login").permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.loginPage("/users/login")
				.usernameParameter("username")
				.defaultSuccessUrl("/movies")
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/users/logout")
				.logoutSuccessUrl("/users/login")
				.permitAll();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
}

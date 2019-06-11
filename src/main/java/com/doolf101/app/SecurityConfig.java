package com.doolf101.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery(getUserQuery())
				.authoritiesByUsernameQuery(getAuthoritiesQuery())
				.passwordEncoder(passwordEncoder);
	}

	private String getUserQuery() {
		System.out.println("select username,password, enabled from users where username=?");
		return "select username,password,enabled from users where username=?";
	}

	private String getAuthoritiesQuery() {
		System.out.println( "select users.username, user_roles.role from user_roles INNER JOIN users ON users.userid=user_roles.userid WHERE users.username=? ");
		return "select users.username, user_roles.role from user_roles INNER JOIN users ON users.userid=user_roles.userid WHERE users.username=? ";
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.anyRequest()
				.authenticated()
				.and()
				.httpBasic();
	}
}

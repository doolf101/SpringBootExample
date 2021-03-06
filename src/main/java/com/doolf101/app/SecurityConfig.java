package com.doolf101.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

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
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/login").anonymous()
				.antMatchers("/logout").hasAnyAuthority()
				.antMatchers("/page").hasRole("ADMIN")
				.antMatchers("/main**").hasRole("ADMIN")
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.failureHandler(failureHandler())
				.successHandler(successHandle())
				.defaultSuccessUrl("/page")
				.failureForwardUrl("/login")
				.and()
				.logout()
				.invalidateHttpSession(true)
				.logoutUrl("/logout")
				.logoutSuccessHandler(logoutSuccessHandler())
				.logoutSuccessUrl("/")
				.and()
				.httpBasic();
	}

	private AuthenticationSuccessHandler successHandle() {
		return new SimpleUrlAuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				System.out.println("successHandle");
				System.out.println(authentication.getAuthorities());
				super.onAuthenticationSuccess(request, response, authentication);
			}
		};
	}

	private AuthenticationFailureHandler failureHandler() {
		return new SimpleUrlAuthenticationFailureHandler(){
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,AuthenticationException exception)throws IOException, ServletException {
				System.out.println("failureHandler");
				super.onAuthenticationFailure(request, response, exception);
			}
		};
	}

	private LogoutSuccessHandler logoutSuccessHandler() {
		return new SimpleUrlLogoutSuccessHandler() {
			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				System.out.println("logoutSuccessHandler");
				super.onLogoutSuccess(request,response,authentication);
			}
		};
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}

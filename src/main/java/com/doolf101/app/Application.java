package com.doolf101.app;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.naming.NamingException;
import javax.sql.DataSource;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.doolf101"} )
public class Application {

	public static void main(String[] args){
		SpringApplication.run(Application.class,args);
	}

	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {

			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatWebServer(tomcat);
			}

			@Override                   // create JNDI resource
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
				resource.setName("jdbc/myDatasourceName");
				resource.setType(DataSource.class.getName());
				resource.setProperty("factory", "org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory");
				resource.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
				resource.setProperty("url", "jdbc:mysql://localhost:3306/main");
				resource.setProperty("username", "root");
				resource.setProperty("password", "");
				context.getNamingResources().addResource(resource);
			}

		};
	}

	@Bean
	public DataSource jndiDataSource() throws NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:comp/env/jdbc/myDatasourceName");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.afterPropertiesSet();
		return (DataSource)bean.getObject();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public TokenStore tokenStore() throws NamingException {
		return new JdbcTokenStore(jndiDataSource());
	}

}
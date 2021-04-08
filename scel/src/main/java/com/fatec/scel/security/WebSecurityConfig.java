package com.fatec.scel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	CustomizeLogoutSuccessHandler customizeLogoutSuccessHandler;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/aluno/cadastrar").hasAnyRole("ADMIN", "BIB") //
			.antMatchers("/livros/cadastrar").hasRole("BIB") // somente login maria
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.defaultSuccessUrl("/", true)
			.and()
			// sample logout customization
				.logout().deleteCookies("remove").invalidateHttpSession(false)
				.logoutUrl("/custom-logout").logoutSuccessUrl("/logout-success");

		
		
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("jose").password(pc().encode("123")).roles("ADMIN").and()
				                     .withUser("maria").password(pc().encode("456")).roles("BIB");
	}

	@Bean
	public BCryptPasswordEncoder pc() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/h2-console/**");
	}
}

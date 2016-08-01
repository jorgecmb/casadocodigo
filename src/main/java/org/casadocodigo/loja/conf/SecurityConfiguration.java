package org.casadocodigo.loja.conf;

import org.casadocodigo.loja.daos.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioDAO usuarioDao;

	//Configuração das URLs que o Spring fará controle de acesso
	//Deve bloquear primeiro e liberar depois
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/produtos/form").hasRole("ADMIN")
			.antMatchers("/carrinho").permitAll()
			.antMatchers(HttpMethod.POST,"/produtos").hasRole("ADMIN")
			.antMatchers(HttpMethod.GET, "/produtos").hasRole("ADMIN")
			.antMatchers("/produtos/**").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
			.and().formLogin();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioDao)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
}

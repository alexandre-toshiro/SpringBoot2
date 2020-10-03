package br.com.alura.forum.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // Habilita o security
@Configuration // Diz que é de configuração
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	//configurações de autenticação (login)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

	}

	//Config de autorização (acesso a URLs, quem pode acessar)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/topicos").permitAll() 
		//permite os métodos GET da url /topicos
		.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
		//permite métodos get da url /topico/algumaCoisa
		.anyRequest().authenticated() // qualquer outra req, precisa estar autenticado
		.and().formLogin();
	}

	//Config de recursos estatiscos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {

	}
}

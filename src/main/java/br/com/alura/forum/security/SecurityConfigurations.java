package br.com.alura.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity // Habilita o security
@Configuration // Diz que é de configuração
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// Devemos criar o método que devolve, para ai sim fazer a injeção.
		return super.authenticationManager();
	}

	// configurações de autenticação (login)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// Config de autorização (acesso a URLs, quem pode acessar)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/topicos").permitAll()
				// permite os métodos GET da url /topicos
				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
				// permite métodos get da url /topico/algumaCoisa
				.antMatchers(HttpMethod.POST, "/auth").permitAll().anyRequest().authenticated() // qualquer outra req,
																								// precisa estar
																								// autenticado
				.and().csrf().disable()// csrf ataque a aplicações web desabilitado, resolvemos com jwt.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		// indica que usará stateless
				.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
				//Ordem de execução dos filtros, 1º o criado, 2º Filtro interno do spring.
	}

	// Config de recursos estatiscos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {

	}
}

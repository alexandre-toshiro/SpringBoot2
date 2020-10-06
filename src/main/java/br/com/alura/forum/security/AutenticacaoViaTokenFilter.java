package br.com.alura.forum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;// essa classe não recebe injeção de dependencia.
	private UsuarioRepository usuarioRepository;

	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		// então obrigamos que seja passado pelo construtor.
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = recuperarToken(request);// no request conseguimos recuperar o token
		boolean valido = tokenService.isTokenValido(token);
		if (valido) {
			autenticarCliente(token);
		}

		filterChain.doFilter(request, response);

	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}

	private void autenticarCliente(String token) {
		Long idUsuario = tokenService.getIdUsuario(token); // Recupera o ID do user pelo token
		Usuario usuario = usuarioRepository.findById(idUsuario).get();// Recuperando o usuário com o seu ID da base.
		UsernamePasswordAuthenticationToken authentication 
		= new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());// Criando uma autenticação de usuário já logado.
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//Setando a autenticação de usuário, que teoricamente já está logado. Seguindo o padrão Stateless

	}

}

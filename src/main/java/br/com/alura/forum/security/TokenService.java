package br.com.alura.forum.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}")// Injeta uma propriedade -> application.properties
	private String expiration;

	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();//getPrincipal recupera qual usuário que ta logado e devolve um Object, fazer cast para Usuario
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		// Data de hoje + tempo de expiração(ou seja, expira no dia seguinte)

		return Jwts.builder()
				.setIssuer("API do Fórum da Alura")// Seta a aplicação que está gerando o token.
				.setSubject(logado.getId().toString())//Qual usuário o token pertence(deve ser uma string)
				.setIssuedAt(hoje)// Data de geração do token, espera um Date(api antiga)
				.setExpiration(dataExpiracao)// Data de expiração do token
				.signWith(SignatureAlgorithm.HS256, secret)
				//passamos o algoritmos de ecriptação, e a senha da aplicação que será utilizada para fazer a assintura e gerar hash do token.
				.compact();// compacta e transforma numa string
	}

	public boolean isTokenValido(String token) {
		try {
			Jwts.parser()// Descriptografa o token
			.setSigningKey(this.secret)// Passamos o secret que tbm será usado para validar o token.
			.parseClaimsJws(token);// UM objeto que recuperamos o tokne e a suas informações
			//Se o objeto não achar o token, ele devolve uma excpetion, então faremos o tratamento.
			return true;
		} catch (Exception e) {
			return false;
		}
		
		
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}

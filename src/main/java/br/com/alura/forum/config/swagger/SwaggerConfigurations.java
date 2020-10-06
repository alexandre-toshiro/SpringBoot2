package br.com.alura.forum.config.swagger;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.forum.modelo.Usuario;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurations {
	
	@Bean
	public Docket forumApi() {
		return new Docket(DocumentationType.SWAGGER_2)// tipo da documentação
				.select().apis(RequestHandlerSelectors.basePackage("br.com.alura.forum"))
				//Diz qual o pacote base da aplicação
				.paths(PathSelectors.ant("/**"))// Quais endpoints o swagger irá análisar
				.build()
				.ignoredParameterTypes(Usuario.class)
				// Irá ignorar as URLS que trabalham com a classe do usuário, para não expor dados sensíveis como a senha.
				.globalOperationParameters(Arrays.asList( // adiciona par globais(irá aparecer em todos os endpoints)
						new ParameterBuilder() // Recebe uma lista com os parâmetros
						.name("Authorization") // nome do par
						.description("Header para Token JWT")// descricao do par
						.modelRef(new ModelRef("string")) // Tipo do par
						.parameterType("header")// tipo de par(cabeçalho)
						.required(false) // Não é obrigatório
						.build())); // Montando o parâmetro.
	}

}

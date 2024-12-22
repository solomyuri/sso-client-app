package com.solomyuri.sso_client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.logging.LogLevel;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class AppConfig implements WebFluxConfigurer {

	private final String BASE_URL;
	private final String BASE_PATH;

	public AppConfig(@Value("${keycloak.scheme}") String keycloakScheme, @Value("${keycloak.host}") String keycloakHost,
			@Value("${keycloak.port}") String keycloakPort, @Value("${spring.application.name}") String appName) {
		this.BASE_URL = String.format("%s://%s:%s", keycloakScheme, keycloakHost, keycloakPort);
		this.BASE_PATH = "/eapi/" + appName;
	}

	@Override
	public void configurePathMatching(PathMatchConfigurer configurer) {
		configurer.addPathPrefix(BASE_PATH, clazz -> true);
	}

	@Bean("WebClientAuth")
	WebClient webClientAuth(WebClient.Builder builder) {
		return builder.baseUrl(BASE_URL)
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create()
						.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG,
								AdvancedByteBufFormat.TEXTUAL)))
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs()
						.maxInMemorySize(1024 * 1024 * 10))
				.build();
	}

	@Bean("WebClient")
	WebClient webClient(WebClient.Builder builder, ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager) {

		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				auth2AuthorizedClientManager);

		oauth2Filter.setDefaultClientRegistrationId("keycloak");

		return builder.baseUrl(BASE_URL)
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create()
						.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG,
								AdvancedByteBufFormat.TEXTUAL)))
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs()
						.maxInMemorySize(1024 * 1024 * 10))
				.filter(oauth2Filter)
				.build();
	}

	@Bean
	ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager(
			ReactiveClientRegistrationRepository registrationRepository,
			ServerOAuth2AuthorizedClientRepository clientRepository) {

		ReactiveOAuth2AuthorizedClientProvider auth2AuthorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder
				.builder()
				.clientCredentials()
				.build();

		DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new DefaultReactiveOAuth2AuthorizedClientManager(
				registrationRepository, clientRepository);

		authorizedClientManager.setAuthorizedClientProvider(auth2AuthorizedClientProvider);

		return authorizedClientManager;
	}

}

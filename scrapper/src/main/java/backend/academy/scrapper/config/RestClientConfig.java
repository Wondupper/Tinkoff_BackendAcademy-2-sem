package backend.academy.scrapper.config;

import backend.academy.scrapper.client.BotRestClient;
import backend.academy.scrapper.client.GitHubRestClient;
import backend.academy.scrapper.client.StackExchangeRestClient;
import backend.academy.scrapper.client.properties.AbstractRestClientProperties;
import backend.academy.scrapper.client.properties.BotRestClientProperties;
import backend.academy.scrapper.client.properties.GitHubRestClientProperties;
import backend.academy.scrapper.client.properties.StackExchangeRestClientProperties;
import backend.academy.scrapper.exception.ClientApiException;
import backend.academy.scrapper.exception.ServerApiException;
import java.nio.charset.Charset;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties({
    BotRestClientProperties.class,
    GitHubRestClientProperties.class,
    StackExchangeRestClientProperties.class
})
public class RestClientConfig {

    @Bean
    public BotRestClient botRestClient(RestClient.Builder builder, BotRestClientProperties properties) {
        RestClient restClient = buildBaseRestClient(builder, properties).build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(BotRestClient.class);
    }

    @Bean
    public GitHubRestClient githubRestClient(RestClient.Builder builder, GitHubRestClientProperties properties) {
        RestClient restClient = buildBaseRestClient(builder, properties)
                .defaultHeaders(headers -> headers.setBearerAuth(properties.token()))
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GitHubRestClient.class);
    }

    @Bean
    public StackExchangeRestClient stackExchangeRestClient(
            RestClient.Builder builder, StackExchangeRestClientProperties properties) {
        RestClient restClient = buildBaseRestClient(builder, properties).build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(StackExchangeRestClient.class);
    }

    private RestClient.Builder buildBaseRestClient(
            RestClient.Builder builder, AbstractRestClientProperties properties) {

        return builder.baseUrl(properties.baseUrl()).defaultStatusHandler(HttpStatusCode::isError, this::handleError);
    }

    @SneakyThrows
    private void handleError(HttpRequest ignored, ClientHttpResponse response) {
        String errorBody = new String(response.getBody().readAllBytes(), Charset.defaultCharset());
        String errorMessage = "HTTP Error: %d - %s: %s"
                .formatted(response.getStatusCode().value(), response.getStatusText(), errorBody);

        if (response.getStatusCode().is4xxClientError()) {
            throw new ClientApiException(errorMessage);
        } else {
            throw new ServerApiException(errorMessage);
        }
    }
}

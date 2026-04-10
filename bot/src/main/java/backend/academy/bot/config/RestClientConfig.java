package backend.academy.bot.config;

import backend.academy.bot.client.ScrapperClient;
import backend.academy.bot.client.properties.ScrapperRestClientProperties;
import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
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
@EnableConfigurationProperties({ScrapperRestClientProperties.class})
public class RestClientConfig {
    private static final String HTTP_ERROR_MESSAGE_PATTERN = "HTTP error: %d - %s: %s";

    @Bean
    public ScrapperClient scrapperRestClient(RestClient.Builder builder, ScrapperRestClientProperties properties) {
        RestClient restClient = builder.baseUrl(properties.baseUrl())
                .defaultStatusHandler(HttpStatusCode::isError, this::handleError)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(ScrapperClient.class);
    }

    @SneakyThrows
    private void handleError(HttpRequest ignored, ClientHttpResponse response) {
        String errorBody = new String(response.getBody().readAllBytes(), Charset.defaultCharset());
        String errorMessage = HTTP_ERROR_MESSAGE_PATTERN.formatted(
                response.getStatusCode().value(), response.getStatusText(), errorBody);

        if (response.getStatusCode().is4xxClientError()) {
            throw new ClientApiException(errorMessage);
        } else {
            throw new ServerApiException(errorMessage);
        }
    }
}

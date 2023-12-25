package Bot.model;

import Bot.model.auth.AuthHeaders;
import Bot.service.BitmexClient;
import lombok.Getter;
import java.net.URI;
import java.net.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderHttpRequest {
    private static final Logger logger = LogManager.getLogger(OrderHttpRequest.class);
    @Getter
    private final HttpRequest httpRequest;

    public OrderHttpRequest(String data, String baseUrl, String endpoint, String httpMethod, AuthHeaders authHeaders) {

        HttpRequest.BodyPublisher bodyPublisher = httpMethod.equals("GET") ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(data);

        httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .method(httpMethod, bodyPublisher)
                .header("api-key", authHeaders.getApiKey())
                .header("api-signature", authHeaders.getSignature())
                .header("api-expires", Long.toString(authHeaders.getExpires()))
                .header("Content-Type", "application/json")
                .build();

        logger.info("Created HttpRequest: " + httpRequest);
    }
}



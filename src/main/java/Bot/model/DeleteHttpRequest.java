package Bot.model;

import Bot.model.auth.AuthHeaders;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpRequest;

public class DeleteHttpRequest {
    private static final Logger logger = LogManager.getLogger(DeleteHttpRequest.class);
    @Getter
    private HttpRequest httpRequest;

    public DeleteHttpRequest(String baseUrl, String endpoint, AuthHeaders authHeaders) {

        httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .DELETE()
                .header("api-key", authHeaders.getApiKey())
                .header("api-signature", authHeaders.getSignature())
                .header("api-expires", Long.toString(authHeaders.getExpires()))
                .header("Content-Type", "application/json")
                .build();

        logger.info("Created HttpRequest: " + httpRequest);
    }
}



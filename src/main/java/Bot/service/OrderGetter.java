package Bot.service;

import Bot.model.OrderHttpRequest;
import Bot.model.auth.AuthHeaders;
import Bot.model.util.Endpoints;
import Bot.model.util.Parser;
import Bot.repo.OrderRepoList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class OrderGetter {
    private static final Logger logger = LogManager.getLogger(OrderGetter.class);
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final Parser parser = new Parser();
    private final String baseUrl;
    private final SignGenerator signGenerator;

    public OrderGetter(BitmexClient bitmexClient) {
        this.baseUrl = bitmexClient.getBaseUrl();
        this.signGenerator = new SignGenerator(bitmexClient);
    }

    public void getOrders() {
        String verb = "GET";
        String data = "";
        String path = Endpoints.ORDER_ENDPOINT+Endpoints.COUNT_100_REVERSE_TRUE;
        String endpoint = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.COUNT_100_REVERSE_TRUE;
        AuthHeaders authHeaders = signGenerator.getAuthHeaders(verb, data, endpoint);

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, path, verb,authHeaders);
        try {
            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + send.statusCode());
            parser.getPrettyOrderList(send);

        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

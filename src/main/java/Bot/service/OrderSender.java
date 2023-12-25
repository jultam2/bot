package Bot.service;

import Bot.model.Order;
import Bot.model.OrderHttpRequest;
import Bot.model.OrderRequest;
import Bot.model.auth.AuthHeaders;
import Bot.model.util.Endpoints;
import Bot.model.util.Parser;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class OrderSender {
    private static final Logger logger = LogManager.getLogger(PriceGetter.class);
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final Gson gson = new Gson();
    private final Parser parser = new Parser();
    @Getter
    public String lastOrderID;
    private final String baseUrl;
    private final SignGenerator signGenerator;

    public OrderSender(BitmexClient bitmexClient) {
        this.baseUrl = bitmexClient.getBaseUrl();
        this.signGenerator = new SignGenerator(bitmexClient);
    }

    public void sendOrder(Order order) {
        OrderRequest orderRequest = OrderRequest.toRequest(order);
        String data = gson.toJson(orderRequest);

        String verb = "POST";
        String path = Endpoints.ORDER_ENDPOINT;
        String endpoint = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT;
        AuthHeaders authHeaders = signGenerator.getAuthHeaders(verb, data, endpoint);

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, path, verb, authHeaders);
        try {
            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            parser.getPrettyOrderList(send);
            lastOrderID = parser.getLastOrderID();
            logger.info("Order: " + data + "created");

        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

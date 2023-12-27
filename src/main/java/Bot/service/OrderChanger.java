package Bot.service;
import Bot.model.Order;
import Bot.model.OrderHttpRequest;
import Bot.model.OrderRequest;
import Bot.model.auth.AuthHeaders;
import Bot.model.util.Endpoints;
import Bot.model.util.Parser;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class OrderChanger {
    private static final Logger logger = LogManager.getLogger(PriceGetter.class);
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final Gson gson = new Gson();
    private final Parser parser = new Parser();

    private final String baseUrl;
    private final SignGenerator signGenerator;

    public OrderChanger(BitmexClient bitmexClient) {
        this.baseUrl = bitmexClient.getBaseUrl();
        this.signGenerator = new SignGenerator(bitmexClient);
    }

    public void changeOrder (Order order) {
        String orderId = order.getOrderId();
        Double price = order.getPrice();
        OrderRequest orderRequest = OrderRequest.toRequest(order);
        String data = gson.toJson(orderRequest);

        String verb = "PUT";
        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId+Endpoints.PRICE+price;
        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId+Endpoints.PRICE+price;
        AuthHeaders authHeaders = signGenerator.getAuthHeaders(verb, data, path);

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, endpoint, verb, authHeaders);

        try{
            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Changed order: ");
            parser.getPrettyOrderList(send);
        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());;
            throw new RuntimeException(e);
        }
        logger.info("Order: " + orderId + "changed");
    }
}

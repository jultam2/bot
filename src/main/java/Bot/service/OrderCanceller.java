package Bot.service;

import Bot.model.DeleteHttpRequest;
import Bot.model.Order;
import Bot.model.auth.AuthHeaders;
import Bot.model.util.Endpoints;
import Bot.model.util.Parser;
import Bot.repo.OrderRepoList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

public class OrderCanceller {
    private static final Logger logger = LogManager.getLogger(OrderCanceller.class);
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final OrderRepoList orderRepoList = new OrderRepoList();
    private final Parser parser = new Parser();

    private final String baseUrl;
    private final SignGenerator signGenerator;

    private final String verb = "DELETE";
    private final String data = "";

    public OrderCanceller(BitmexClient bitmexClient) {
        this.baseUrl = bitmexClient.getBaseUrl();
        this.signGenerator = new SignGenerator(bitmexClient);
    }

    public void cancelOrder(String orderId) {
        String path = Endpoints.BASE_TEST_URL_SECOND_PART + Endpoints.ORDER_ENDPOINT + Endpoints.ORDER_ID + orderId;
        String endpoint = Endpoints.ORDER_ENDPOINT + Endpoints.ORDER_ID + orderId;
        executeCancelRequest(verb, data, path, endpoint);
        if (orderId != null) {
            orderRepoList.remove(orderId);
            logger.info("Order: " + orderId + " cancelled");
        }
    }

    public void cancelOrders(String side) {
        String sideEndpoint = side.equals("SELL") ? Endpoints.SIDE_SELL : Endpoints.SIDE_BUY;
        String path = Endpoints.BASE_TEST_URL_SECOND_PART + Endpoints.ORDER_ENDPOINT + Endpoints.ORDER_ALL + sideEndpoint;
        String endpoint = Endpoints.ORDER_ENDPOINT + Endpoints.ORDER_ALL + sideEndpoint;
        executeCancelRequest(verb, data, path, endpoint);
    }

    public void cancelAllOrders() {
        String path = Endpoints.BASE_TEST_URL_SECOND_PART + Endpoints.ORDER_ENDPOINT + Endpoints.ORDER_ALL;
        String endpoint = Endpoints.ORDER_ENDPOINT + Endpoints.ORDER_ALL;
        executeCancelRequest(verb, data, path, endpoint);
    }

    private void executeCancelRequest(String verb, String data, String path, String endpoint) {
        AuthHeaders authHeaders = signGenerator.getAuthHeaders(verb, data, path);
        DeleteHttpRequest deleteHttpRequest = new DeleteHttpRequest(baseUrl, endpoint, authHeaders);

        try {
            HttpResponse<String> send = httpClient.send(deleteHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Cancelled orders: ");
            parser.getPrettyOrderList(send);
            logger.info("All orders cancelled");

        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
        }
    }

    public void chooseOrderToClose() {
        List<Order> orderList = orderRepoList.getOrdersAsList();
        for (Order openOrder : orderList) {
            System.out.println(openOrder.getOrderId());
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Choose orderId to close: ");
            String userInput = reader.readLine();
            cancelOrder(userInput);
            logger.info("Order: " + userInput + "cancelled");
        } catch (IOException e) {
            logger.error("Error during request: " + e.getMessage());
        }
    }
}

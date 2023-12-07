package Bot.service;

import Bot.model.DeleteHttpRequest;
import Bot.repo.OrderRepoList;
import Bot.model.util.Parser;
import com.google.gson.*;
import Bot.model.auth.AuthHeaders;
import Bot.model.Order;
import Bot.model.OrderHttpRequest;
import Bot.model.OrderRequest;
import Bot.model.util.Endpoints;
import Bot.model.util.Signature;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class BitmexClient {
    private static final Logger logger = LogManager.getLogger(BitmexClient.class);
    private static final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final Signature signature = new Signature();
    private final OrderRepoList orderRepoList = new OrderRepoList();
    Parser parser = new Parser();
    private final String apiSecretKey;
    private final String apiKey;
    private final String baseUrl;

    public void sendOrder(Order order) {
        OrderRequest orderRequest = OrderRequest.toRequest(order);
        String data = gson.toJson(orderRequest);

        String verb = "POST";
        String path = Endpoints.ORDER_ENDPOINT;
        String endpoint = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT;

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, path, verb, getAuthHeaders(verb, data, endpoint));
        try {
            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + send.statusCode());
            parser.getPrettyOrderList(send);
            logger.info("Order: " + data + "created");

        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void getOrders() {
        String verb = "GET";
        String data = "";
        String path = Endpoints.ORDER_ENDPOINT+Endpoints.COUNT_100_REVERSE_TRUE;
        String endpoint = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.COUNT_100_REVERSE_TRUE;

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, path, verb,getAuthHeaders(verb, data, endpoint));
        try {
            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + send.statusCode());
            parser.getPrettyOrderList(send);

        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void getOrdersList() {
        logger.info("OrderList requested");
        orderRepoList.getOpenOrdersList();
    }

    public void changeOrder (Order order) {
        String orderId = order.getOrderId();
        Double price = order.getPrice();

        OrderRequest orderRequest = OrderRequest.toRequest(order);
        String data = gson.toJson(orderRequest);

        String verb = "PUT";
        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId+Endpoints.PRICE+price;
        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId+Endpoints.PRICE+price;

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, endpoint, verb,getAuthHeaders(verb, data, path));

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

    public void cancelOrder(String orderId) {
        String verb = "DELETE";
        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId;
        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId;

        DeleteHttpRequest deleteHttpRequest = new DeleteHttpRequest(orderId, baseUrl, endpoint, verb,getAuthHeaders(verb, "", path));

        try{
            HttpResponse<String> send = httpClient.send(deleteHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Cancelled order: ");
            parser.getPrettyOrderList(send);
            orderRepoList.remove(orderId);
        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("Order: " + orderId + "cancelled");
    }

    public void cancelAllOrders() {
        String verb = "DELETE";
        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL;
        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL;

        DeleteHttpRequest deleteHttpRequest = new DeleteHttpRequest(baseUrl, endpoint, verb,getAuthHeaders(verb, "", path));

        try {
            HttpResponse<String> send = httpClient.send(deleteHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Cancelled orders: ");
            parser.getPrettyOrderList(send);
        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("All orders cancelled");
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

    private AuthHeaders getAuthHeaders(String httpMethod, String data, String path) {
        long expires = System.currentTimeMillis() / 1000 + 60;
        String message = httpMethod + path + expires + data;
        String signatureStr = signature.getSignature(apiSecretKey, message);

        return AuthHeaders.builder()
                .apiKey(apiKey)
                .signature(signatureStr)
                .expires(expires)
                .build();
    }

}

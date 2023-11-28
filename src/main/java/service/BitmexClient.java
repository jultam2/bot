package service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import model.AuthenticationHeaders;
import model.Order;
import model.OrderHttpRequest;
import model.OrderRequest;
import util.Endpoints;
import util.Signature;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
public class BitmexClient {
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final Gson gson = new Gson();
    private final Signature signature = new Signature();
    private final String apiSecretKey;
    private final String apiKey;
    private final String baseUrl;
    private boolean isReal;
    private final int EXPIRES_DELAY = 60;
    public void sendOrder(Order order) {

        String httpMethod = "POST";
        String data = "";
        if (order == null) {
            data = "";
        } else {
            OrderRequest orderRequest = OrderRequest.toRequest(order);
            data = gson.toJson(orderRequest);
        }

        System.out.println(data);
        String base = "/api/v1";

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(order, baseUrl, Endpoints.ORDER_ENDPOINT, httpMethod,getAuthenticationHeaders(httpMethod, data, base+Endpoints.ORDER_ENDPOINT));
        try {
            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + send.statusCode());
            System.out.println("Response Body: " + send.body());;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void cancelOrder(String orderId) {

    }


    private AuthenticationHeaders getAuthenticationHeaders(String httpMethod, String data, String path) {
        long expires = System.currentTimeMillis() / 1000 + EXPIRES_DELAY;
        System.out.println(data);

        String signatureStr = signature.getSignature(apiSecretKey, httpMethod + path + expires + data);

        return AuthenticationHeaders.builder()
                .apiKey(apiKey)
                .signature(signatureStr)
                .expires(Long.toString(expires))
                .build();
    }

}

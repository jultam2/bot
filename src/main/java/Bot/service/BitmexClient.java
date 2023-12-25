package Bot.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BitmexClient {
    private static BitmexClient instance;
    @Getter
    private final String apiSecretKey;
    @Getter
    private final String apiKey;
    @Getter
    private final String baseUrl;
    @Getter
    public String lastOrderID;  // Переменная для хранения последнего OrderID

//    public void getOrders() {
//        String verb = "GET";
//        String data = "";
//        String path = Endpoints.ORDER_ENDPOINT+Endpoints.COUNT_100_REVERSE_TRUE;
//        String endpoint = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.COUNT_100_REVERSE_TRUE;
//
//        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, path, verb,getAuthHeaders(verb, data, endpoint));
//        try {
//            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
//            System.out.println("Response Code: " + send.statusCode());
//            parser.getPrettyOrderList(send);
//
//        } catch (IOException | InterruptedException e) {
//            logger.error("Error during request: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }

//    public void cancelOrder(String orderId) {
//        String verb = "DELETE";
//        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId;
//        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ID+orderId;
//
//        DeleteHttpRequest deleteHttpRequest = new DeleteHttpRequest(orderId, baseUrl, endpoint, verb,getAuthHeaders(verb, "", path));
//
//        try{
//            HttpResponse<String> send = httpClient.send(deleteHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
//            System.out.println(send.body());
//            parser.getPrettyOrderList(send);
//            orderRepoList.remove(orderId);
//        } catch (IOException | InterruptedException e) {
//            logger.error("Error during request: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//        logger.info("Order: " + orderId + "cancelled");
//    }
//
//    public void cancelAllOrders() {
//        String verb = "DELETE";
//        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL;
//        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL;
//
//        DeleteHttpRequest deleteHttpRequest = new DeleteHttpRequest(baseUrl, endpoint, verb, getAuthHeaders(verb, "", path));
//
//        try {
//            HttpResponse<String> send = httpClient.send(deleteHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
//            System.out.println("Cancelled orders: ");
//            parser.getPrettyOrderList(send);
//        } catch (IOException | InterruptedException e) {
//            logger.error("Error during request: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//        logger.info("All orders cancelled");
//    }
//
//    public void cancelSellOrders() {
//        String verb = "DELETE";
//        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL+Endpoints.SIDE_SELL;
//        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL+Endpoints.SIDE_SELL;
//
//        DeleteHttpRequest deleteHttpRequest = new DeleteHttpRequest(baseUrl, endpoint, verb,getAuthHeaders(verb, "", path));
//
//        try {
//            HttpResponse<String> send = httpClient.send(deleteHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
//            System.out.println("Cancelled orders: ");
//            parser.getPrettyOrderList(send);
//        } catch (IOException | InterruptedException e) {
//            logger.error("Error during request: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//        logger.info("All orders cancelled");
//    }
//
//    public void cancelBuyOrders() {
//        String verb = "DELETE";
//        String path = Endpoints.BASE_TEST_URL_SECOND_PART+Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL+Endpoints.SIDE_BUY;
//        String endpoint = Endpoints.ORDER_ENDPOINT+Endpoints.ORDER_ALL+Endpoints.SIDE_BUY;
//
//        DeleteHttpRequest deleteHttpRequest = new DeleteHttpRequest(baseUrl, endpoint, verb,getAuthHeaders(verb, "", path));
//
//        try {
//            HttpResponse<String> send = httpClient.send(deleteHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
//            System.out.println("Cancelled orders: ");
//            parser.getPrettyOrderList(send);
//        } catch (IOException | InterruptedException e) {
//            logger.error("Error during request: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//        logger.info("All orders cancelled");
//    }

//    public void chooseOrderToClose() {
//        List<Order> orderList = orderRepoList.getOrdersAsList();
//        for (Order openOrder : orderList) {
//            System.out.println(openOrder.getOrderId());
//        }
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
//            System.out.print("Choose orderId to close: ");
//            String userInput = reader.readLine();
//            cancelOrder(userInput);
//            logger.info("Order: " + userInput + "cancelled");
//        } catch (IOException e) {
//            logger.error("Error during request: " + e.getMessage());
//        }
//    }

}

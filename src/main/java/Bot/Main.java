package Bot;


import Bot.model.Order;
import Bot.model.OrderTypeConverter;
import Bot.model.unused.Symbol;
import Bot.model.util.Endpoints;
import Bot.service.BitmexClient;
import Bot.service.BitmexWebSocketClient;

public class Main {

public static void main(String[] args) {
        Order order = Order.builder()
                .orderQty(100)
                .orderType(OrderTypeConverter.OrderType.LMT)
                .isBuy(true)
                .symbol(Symbol.XBTUSD)
                .price(30000.)
                .build();
        String apiKey = "CcFO3TMuN9Itk2MiZKo6Eq4h";
        String apiSecret = "cX0ku9shlz0Qvb1QWtypd0WRhinnlIQWhJ3ysiNDEMrSxn3I";

        BitmexClient bitmexClient = new BitmexClient(apiSecret, apiKey, Endpoints.BASE_TEST_URL);
        bitmexClient.sendOrder(order);


//    Order order = Order.builder()
//            .orderId("0716cf4c-fe7f-4250-9519-6d4e3f9409de")
//            .orderQty(200)
//            .price(44000.)
//            .build();
//    String apiKey = "CcFO3TMuN9Itk2MiZKo6Eq4h";
//    String apiSecret = "cX0ku9shlz0Qvb1QWtypd0WRhinnlIQWhJ3ysiNDEMrSxn3I";
//
//    BitmexClient bitmexClient = new BitmexClient(apiSecret, apiKey, Endpoints.BASE_TEST_URL);
//    bitmexClient.changeOrder(order);


//        BitmexWebSocketClient bitmexWebSocketClient = new Bot.service.BitmexWebSocketClient();
//        bitmexWebSocketClient.connectAndSubscribe();



//
//        String apiKey = "CcFO3TMuN9Itk2MiZKo6Eq4h";
//        String apiSecret = "cX0ku9shlz0Qvb1QWtypd0WRhinnlIQWhJ3ysiNDEMrSxn3I";
//
//        BitmexClient bitmexClient = new BitmexClient(apiSecret, apiKey, Endpoints.BASE_TEST_URL);
//        bitmexClient.getOrders();


//        String apiKey = "CcFO3TMuN9Itk2MiZKo6Eq4h";
//        String apiSecret = "cX0ku9shlz0Qvb1QWtypd0WRhinnlIQWhJ3ysiNDEMrSxn3I";
//
//        BitmexClient bitmexClient = new BitmexClient(apiSecret, apiKey, Endpoints.BASE_TEST_URL);
//        bitmexClient.cancelOrder("2abd33ae-5581-4028-80f0-866740ec0655");


    }
}
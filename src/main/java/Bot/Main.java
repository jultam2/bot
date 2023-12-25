package Bot;


import Bot.model.Order;
import Bot.model.OrderTypeConverter;
import Bot.model.unused.Symbol;
import Bot.model.util.Endpoints;
import Bot.service.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
//      так как юзер будет это все вводить, здесь должен быть UI (сделаю сервелетами)
        String apiKey = "jjRD31eZEyh48sw-f3S_tCgO";
        String apiSecret = "lTvHkKkX-4Vvxi5k7l_6wUHpUlBg0-ZmAwfU1ipqfTtue8Rf";
        double priceStep = 1000.0;
        double coefficient = 100.0;
        int numberOfOrders = 2;

        BotExecutor botExecutor = new BotExecutor(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
        botExecutor.execute();

    }
}

//public static void main(String[] args) {
//    String apiKey = "kEm8fueJxqidJO-weEN99qMA";
//    String apiSecret = "Po8iJ53HTu_aTTk6BCJXFuAGErMR2SndjrMokhxfm8z5SNxM";
//
//    BitmexClient bitmexClient = new BitmexClient(apiSecret, apiKey, Endpoints.BASE_TEST_URL);
//    bitmexClient.cancelBuyOrders();
//}
//}

//        String apiKey = "kEm8fueJxqidJO-weEN99qMA";
//        String apiSecret = "Po8iJ53HTu_aTTk6BCJXFuAGErMR2SndjrMokhxfm8z5SNxM";
//
//    BitmexWebSocketClient bitmexWebSocketClient = new Bot.service.BitmexWebSocketClient();
//    bitmexWebSocketClient.connectAndSubscribe();
//
//    double priceStep = 100.0;
//    double coefficient = 100.0;
//    int numberOfOrders = 6;
//    OrderMassOpener orderMassOpener = new OrderMassOpener(apiKey, apiSecret);
//    orderMassOpener.generateAndSendBuyOrders(priceStep, coefficient, numberOfOrders);



//    Order order = Order.builder()
//            .orderId("0716cf4c-fe7f-4250-9519-6d4e3f9409de")
//            .orderQty(200)
//            .price(44000.)
//            .build();






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



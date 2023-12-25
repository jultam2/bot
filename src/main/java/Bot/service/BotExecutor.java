package Bot.service;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiredArgsConstructor
public class BotExecutor {
    private final String apiKey;
    private final String apiSecret;
    private final double priceStep;
    private final double coefficient;
    private final int numberOfOrders;

    public void execute() {
        CompletableFuture<Void> orderTask = CompletableFuture.runAsync(() -> {
            OrderMassOpener orderMassOpener = new OrderMassOpener(apiKey, apiSecret);
            orderMassOpener.generateBuyOrders(priceStep, coefficient, numberOfOrders);
        });

        CompletableFuture<Void> webSocketTask = CompletableFuture.runAsync(() -> {
            BitmexWebSocketClient bitmexWebSocketClient = new BitmexWebSocketClient(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
            bitmexWebSocketClient.connectAndSubscribe();
        });

        try {
            // Дождитесь завершения обоих задач
            CompletableFuture.allOf(orderTask, webSocketTask).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

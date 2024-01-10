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
        String apiKey = "EFM5XM5mKRybXej7MTjJ3j-w";
        String apiSecret = "Job25yeeRVsU4INvWRkIh_gVVuyMJGp9naXSjUkGrbBaV9QL";
        double priceStep = 500.0;
        double coefficient = 100.0;
        int numberOfOrders = 3;

        BotExecutor botExecutor = new BotExecutor(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
        botExecutor.execute();

    }
}

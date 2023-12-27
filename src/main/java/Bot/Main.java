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
        double priceStep = 500.0;
        double coefficient = 100.0;
        int numberOfOrders = 1;

        BotExecutor botExecutor = new BotExecutor(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
        botExecutor.execute();

    }
}

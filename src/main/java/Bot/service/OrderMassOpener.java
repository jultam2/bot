package Bot.service;

import Bot.model.Order;
import Bot.model.OrderTypeConverter;
import Bot.model.unused.Symbol;
import Bot.model.util.Endpoints;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
public class OrderMassOpener {
    private static final Logger logger = LogManager.getLogger(OrderMassOpener.class);
    private final OrderSender orderSender;
    private final double initialPrice;

    public OrderMassOpener(String apiKey, String apiSecret) {
        BitmexClient bitmexClient = new BitmexClient(apiSecret, apiKey, Endpoints.BASE_TEST_URL);
        this.initialPrice = new PriceGetter(bitmexClient).getPrice();
//        this.initialPrice = 42000.;
                this.orderSender = new OrderSender(bitmexClient);
    }

    public void generateBuyOrders(double priceStep, double coefficient, int numberOfOrders) {
        List<Double> fibonacciNumbers = new FiboGenerator().generateFibonacciSequence(numberOfOrders);
        double startPrice = initialPrice - priceStep * fibonacciNumbers.get(0);

        for (int i = 0; i < numberOfOrders; i++) {
            double orderAmount = fibonacciNumbers.get(i) * coefficient;
            Order order = Order.builder()
                    .orderQty(orderAmount)
                    .orderType(OrderTypeConverter.OrderType.LMT)
                    .isBuy(true)
                    .symbol(Symbol.XBTUSD)
                    .price(startPrice)
                    .build();

            orderSender.sendOrder(order);
            // Пересчитываем цену для следующего ордера
            if (i + 1 < numberOfOrders) {
                startPrice -= priceStep * fibonacciNumbers.get(i + 1);
            }
            logger.info(numberOfOrders + "buy orders created");
        }
    }

    public void generateSellOrders(double priceStep, double coefficient, int numberOfOrders) {
        List<Double> fibonacciNumbers = new FiboGenerator().generateFibonacciSequence(numberOfOrders);
        double startPrice = initialPrice + 500.;

        for (int i = numberOfOrders - 1; i >= 0; i--) {
            double orderAmount = fibonacciNumbers.get(i) * coefficient;
            Order order = Order.builder()
                    .orderQty(orderAmount)
                    .orderType(OrderTypeConverter.OrderType.LMT)
                    .isBuy(false)
                    .symbol(Symbol.XBTUSD)
                    .price(startPrice)
                    .build();

            orderSender.sendOrder(order);
            // Пересчитываем цену для следующего ордера
            if (i > 0) {
                double nextStartPrice = startPrice - priceStep * fibonacciNumbers.get(i);
                if (nextStartPrice < startPrice) {
                    startPrice = nextStartPrice;
                }
                logger.info(numberOfOrders + "sell orders created");
            }
        }
    }
}

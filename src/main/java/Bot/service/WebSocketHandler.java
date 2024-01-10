package Bot.service;

import Bot.model.Order;
import Bot.model.OrderTypeConverter;
import Bot.model.unused.Symbol;
import Bot.model.util.Endpoints;
import Bot.repo.OrderQueue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class WebSocketHandler {
    private static final Logger logger = LogManager.getLogger(WebSocketHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FiboGenerator fiboGenerator = new FiboGenerator();
    private final String apiKey;
    private final String apiSecret;
    private final double coefficient;
    private final double priceStep;
    private final int numberOfOrders;
    private final OrderSender orderSender;
    private final OrderCanceller orderCanceller;
    private final OrderMassOpener orderMassOpener;
    private int ordersNumber = 0;
    private int buyCounter = 0;
    private int sellCounter = 0;
    private boolean anyCounterIncreased = false;

    private final OrderQueue<JsonNode> buyMessagesQueue = new OrderQueue<>(1);
    private double totalOrderAmount = 0.0;
    private String orderID = null;
    private double lastPrice = 0.0;

    public WebSocketHandler(String apiKey, String apiSecret, double priceStep, double coefficient, int numberOfOrders) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.priceStep = priceStep;
        this.coefficient = coefficient;
        this.numberOfOrders = numberOfOrders;
        BitmexClient bitmexClient = new BitmexClient(apiSecret, apiKey, Endpoints.BASE_TEST_URL);
        this.orderCanceller = new OrderCanceller(bitmexClient);
        this.orderSender = new OrderSender(bitmexClient);
        this.orderMassOpener = new OrderMassOpener(apiKey, apiSecret);
    }

    public void processMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            if (jsonNode.has("data") && jsonNode.get("data").isArray()) {
                JsonNode dataArray = jsonNode.get("data");
                for (JsonNode data : dataArray) {
                    if (data.has("ordStatus") && data.get("ordStatus").asText().equals("Canceled") && data.has("side") && data.get("side").asText().equals("Buy")) {
                        buyCounter++;
                        ordersNumber++;
                        buyMessagesQueue.offer(data);
                        anyCounterIncreased = true;
                        orderCanceller.cancelOrders("SELL");

                        if (ordersNumber > 0) {
                            Thread.sleep(1000);
                            orderMassOpener.generateSellOrders(priceStep, coefficient, ordersNumber);
                        }
                        logger.info("Sell orders opened");
                    }
                    if (data.has("ordStatus") && data.get("ordStatus").asText().equals("Canceled") && data.has("side") && data.get("side").asText().equals("Sell")) {
                        sellCounter++;
                        anyCounterIncreased = true;

                        if (orderID != null && !orderID.isEmpty()) {
                            orderCanceller.cancelOrder(orderID);
                        }

                        JsonNode lastJson = buyMessagesQueue.poll();
                        if (lastJson != null) {
                            lastPrice = lastJson.get("price").asDouble();
                        }

                        List<Double> fibonacciNumbers = fiboGenerator.generateFibonacciSequence(sellCounter);
                        double orderAmount = fibonacciNumbers.get(fibonacciNumbers.size() - 1) * coefficient;
                        totalOrderAmount += orderAmount;

                        Order order = Order.builder().orderQty(totalOrderAmount).orderType(OrderTypeConverter.OrderType.LMT).isBuy(true).symbol(Symbol.XBTUSD).price(lastPrice).build();

                        orderSender.sendOrder(order);
                        this.orderID = orderSender.getLastOrderID();
                    }
                    if (data.has("ordStatus") && data.get("ordStatus").asText().equals("Canceled") && data.has("orderID") && data.get("orderID").asText().equals(orderID)) {
                        double returnedAmount = 0;
                        if (data.has("orderQty")) {
                            returnedAmount = data.get("orderQty").asDouble();
                        }

                        int desiredNumber = fiboGenerator.findNumberOfElements(returnedAmount, coefficient);
                        orderMassOpener.generateBuyOrders(priceStep, coefficient, desiredNumber);
                        logger.info("Order net reopened");
                    }
                }
            }

            if (anyCounterIncreased && buyCounter == sellCounter) {
                orderCanceller.cancelAllOrders();
                buyCounter = 0;
                sellCounter = 0;
                BotExecutor botExecutor = new BotExecutor(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
                botExecutor.execute();
            }

        } catch (Exception e) {
            logger.error("Error during request: " + e.getMessage());
        }
    }
}


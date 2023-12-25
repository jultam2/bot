package Bot.service;

import Bot.model.Order;
import Bot.model.OrderTypeConverter;
import Bot.model.unused.Symbol;
import Bot.model.util.Endpoints;
import Bot.model.util.Signature;
import Bot.model.util.Parser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class BitmexWebSocketClient {
    private static final Logger logger = LogManager.getLogger(BitmexWebSocketClient.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FiboGenerator fiboGenerator = new FiboGenerator();
    private final String serverUri = Endpoints.BASE_TEST_URL_WEBSOCKET;
    private final long expires = Endpoints.EXPIRES;
    private Session session;
    private ScheduledExecutorService pingScheduler;
    private final String apiKey;
    private final String apiSecret;
    private final double coefficient;
    private final double priceStep;
    private final int numberOfOrders;
    private final OrderSender orderSender;
    private final OrderCanceller orderCanceller;
    private final OrderMassOpener orderMassOpener;

    public BitmexWebSocketClient(String apiKey, String apiSecret, double priceStep, double coefficient, int numberOfOrders) {
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

    private int ordersNumber = 0;
    private int buyCounter = 0;
    private int sellCounter = 0;
    private boolean anyCounterIncreased = false;

    private Queue<JsonNode> buyMessagesQueue = new LinkedList<>();
    double totalOrderAmount = 0.0;
    String orderID = "";


    public void connectAndSubscribe() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            session = container.connectToServer(this, URI.create(serverUri));

            // Ваша логика подписки
            Map<String, Object> subscribe = new HashMap<>();
            subscribe.put("op", "subscribe");
            subscribe.put("args", "order");
            String json = Parser.toJson(subscribe);
            session.getBasicRemote().sendText(json);

            // Поддерживаем соединение, отправляя периодические ping-сообщения
            pingScheduler = Executors.newSingleThreadScheduledExecutor();
            pingScheduler.scheduleAtFixedRate(this::sendPing, 0, 5, TimeUnit.SECONDS);

        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Signature signature = new Signature();
        String sign = signature.getSignature(apiSecret, "GET/realtime" + expires);
        try {
            Map<String, Object> args = new HashMap<>();
            args.put("op", "authKeyExpires");
            args.put("args", new Object[]{apiKey, expires, sign});
            String json = Parser.toJson(args);

            session.getBasicRemote().sendText(json);
        } catch (IOException e) {
            logger.error("Error during request: " + e.getMessage());
        }
    }
    @OnMessage
    public void onMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            if (jsonNode.has("data") && jsonNode.get("data").isArray()) {
                JsonNode dataArray = jsonNode.get("data");
                for (JsonNode data : dataArray) {
                    if (data.has("ordStatus") && data.has("side")) {
                        if (data.get("ordStatus").asText().equals("Canceled") && data.get("side").asText().equals("Buy")) {
                            buyMessagesQueue.offer(data);
                            buyCounter++;
                            anyCounterIncreased = true;

                            orderCanceller.cancelOrders("SELL");
                            ordersNumber++;

                            if (ordersNumber > 0) {
                                Thread.sleep(1000);
                                orderMassOpener.generateSellOrders(priceStep, coefficient, ordersNumber);
                            }

                        } else if (data.get("ordStatus").asText().equals("Canceled") && data.get("side").asText().equals("Sell")) {
                            sellCounter++;
                            anyCounterIncreased = true;

                            if (orderID != null && !orderID.isEmpty()) {
                                // Если ордер существует, отменяем его
                                orderCanceller.cancelOrder(orderID);
                            }

                            JsonNode lastJson = buyMessagesQueue.poll();
                            double lastPrice = lastJson.get("price").asDouble();

                            List<Double> fibonacciNumbers = fiboGenerator.generateFibonacciSequence(sellCounter);
                            double orderAmount = fibonacciNumbers.get(fibonacciNumbers.size() - 1) * coefficient;
                            totalOrderAmount += orderAmount;

                                Order order = Order.builder()
                                        .orderQty(totalOrderAmount)
                                        .orderType(OrderTypeConverter.OrderType.LMT)
                                        .isBuy(true)
                                        .symbol(Symbol.XBTUSD)
                                        .price(lastPrice)
                                        .build();

                                orderSender.sendOrder(order);
                                orderID = orderSender.getLastOrderID();

                        } else if (data.has("orderID") && data.get("orderID").asText().equals(orderID) && data.get("ordStatus").asText().equals("Cancelled")) {
                            JsonNode json = objectMapper.readTree(message);
                            double returnedAmount = 0;

                            if (json != null && json.has("orderQty")) {
                                returnedAmount = json.get("price").asDouble();
                            }

                            int desiredNumber = fiboGenerator.findNumberOfElements(returnedAmount, coefficient);
                            orderMassOpener.generateBuyOrders(priceStep, coefficient, desiredNumber);
                        }
                    }
                }

                if (anyCounterIncreased && buyCounter == sellCounter) {
                    orderCanceller.cancelAllOrders();
                    BotExecutor botExecutor = new BotExecutor(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
                    botExecutor.execute();
                }
            }
        } catch (Exception e) {
            logger.error("Error during request: " + e.getMessage());
        }
    }

    private void sendPing() {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText("ping");
            } else {
                // Если соединение закрыто, отменяем планировщик
                pingScheduler.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("Error during request: " + error.getMessage());
    }

    public void stopWebsocketThreads() {
        try {
            if (session != null && session.isOpen()) {
                session.close();
                logger.debug("Session is closed");
            }
        } catch (IOException e) {
            logger.error("Error during closing session: " + e);
        }
    }
}
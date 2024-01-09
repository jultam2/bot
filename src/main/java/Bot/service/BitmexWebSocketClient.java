package Bot.service;

import Bot.model.util.Endpoints;
import Bot.model.util.Parser;
import Bot.model.util.Signature;
import jakarta.websocket.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class BitmexWebSocketClient implements WebSocketListener {
    private static final Logger logger = LogManager.getLogger(BitmexWebSocketClient.class);
    private final String serverUri = Endpoints.BASE_TEST_URL_WEBSOCKET;
    private final long expires = Endpoints.EXPIRES;
    private Session session;
    private ScheduledExecutorService pingScheduler;
    private final String apiKey;
    private final String apiSecret;
    private final WebSocketHandler webSocketHandler;

    public BitmexWebSocketClient(String apiKey, String apiSecret, double priceStep, double coefficient, int numberOfOrders) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.webSocketHandler = new WebSocketHandler(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
    }

    public void connectAndSubscribe() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            session = container.connectToServer(this, URI.create(serverUri));

            Map<String, Object> subscribe = new HashMap<>();
            subscribe.put("op", "subscribe");
            subscribe.put("args", "order");
            String json = Parser.toJson(subscribe);
            session.getBasicRemote().sendText(json);

            pingScheduler = Executors.newSingleThreadScheduledExecutor();
            pingScheduler.scheduleAtFixedRate(this::sendPing, 0, 5, TimeUnit.SECONDS);
            logger.info("Session opened");

        } catch (DeploymentException | IOException e) {
            logger.error("Error during request: " + e.getMessage());
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
            logger.info("Authentication completed");
        } catch (IOException e) {
            logger.error("Error during request: " + e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message) {
        webSocketHandler.processMessage(message);
    }

    private void sendPing() {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText("ping");
            } else {
                pingScheduler.shutdown();
            }
        } catch (IOException e) {
            logger.error("Error during request: " + e.getMessage());
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
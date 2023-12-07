package Bot.service;

import Bot.model.util.Endpoints;
import Bot.model.util.Signature;
import Bot.model.util.Parser;
import jakarta.websocket.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ClientEndpoint
public class BitmexWebSocketClient {
    private final String serverUri = Endpoints.BASE_TEST_URL_WEBSOCKET;
    long expires = Endpoints.EXPIRES;
    private Session session;
    private String apiKey = "CcFO3TMuN9Itk2MiZKo6Eq4h";
    private String apiSecret = "cX0ku9shlz0Qvb1QWtypd0WRhinnlIQWhJ3ysiNDEMrSxn3I";
    private static final Logger logger = LogManager.getLogger(BitmexWebSocketClient.class);

    public void connect() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, URI.create(serverUri));
        } catch (DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectAndSubscribe() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            Session session = container.connectToServer(this, URI.create(serverUri));

            Map<String, Object> subscribe = new HashMap<>();
            subscribe.put("op", "subscribe");
            subscribe.put("args", "order");
            String json = Parser.toJson(subscribe);
            session.getBasicRemote().sendText(json);

            Thread.sleep(120000);

        } catch (DeploymentException | IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
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
        System.out.println((message));
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
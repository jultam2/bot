package Bot.service;

import jakarta.websocket.Session;

public interface WebSocketListener {
    void onOpen(Session session);
    void onMessage(String message);
    void onError(Session session, Throwable error);
    void stopWebsocketThreads();
}

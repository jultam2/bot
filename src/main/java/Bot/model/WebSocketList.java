package Bot.model;

import Bot.service.BitmexWebSocketClient;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class WebSocketList {
    @Getter
    private static final WebSocketList instance = new WebSocketList();
    @Getter
    private final Map<Integer, BitmexWebSocketClient> SocketStatusMap;

    private WebSocketList() {

        SocketStatusMap = new HashMap<>();
    }

    public BitmexWebSocketClient getBotByIndex(int index) {
        return SocketStatusMap.get(index);
    }

}


package Bot.model;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

public class BotList {
    @Getter
    private static final BotList instance = new BotList();
    @Getter
    private final Map<Integer, BotStatus> botStatusMap;

    private BotList() {
        botStatusMap = new HashMap<>();
    }

}

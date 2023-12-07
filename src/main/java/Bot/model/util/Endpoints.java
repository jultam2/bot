package Bot.model.util;

public class Endpoints {
    public static final String BASE_TEST_URL = "https://testnet.bitmex.com/api/v1";
    public static final String BASE_REAL_URL = "https://www.bitmex.com/api/v1";
    public static final String ORDER_ENDPOINT = "/order";
    public static final String ORDER_ALL = "/all";
    public static final String ORDER_ID = "?orderID=";
    public static final String PRICE = "&price=";
    public static final String COUNT_100_REVERSE_TRUE = "?count=100&reverse=true";
    public static final String BASE_TEST_URL_WEBSOCKET = "wss://ws.testnet.bitmex.com/realtime";
    public static final String BASE_TEST_URL_WEBSOCKET_REALTIME = "/realtime";
    public static final String BASE_TEST_URL_SECOND_PART = "/api/v1";
    public static long EXPIRES = System.currentTimeMillis() / 1000 + 60;
}

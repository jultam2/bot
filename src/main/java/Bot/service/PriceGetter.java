package Bot.service;

import Bot.model.OrderHttpRequest;
import Bot.model.auth.AuthHeaders;
import Bot.model.util.Endpoints;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class PriceGetter {
    private static final Logger logger = LogManager.getLogger(PriceGetter.class);
    private final HttpClient httpClient = HttpClient.newBuilder().build();

    private final String baseUrl;
    private final SignGenerator signGenerator;

    public PriceGetter(BitmexClient bitmexClient) {
        this.baseUrl = bitmexClient.getBaseUrl();
        this.signGenerator = new SignGenerator(bitmexClient);
    }

    public double getPrice() {
        double lastPrice = 0;
        String verb = "GET";
        String data = "";
        String path = Endpoints.INSTRUMENT;
        String endpoint = Endpoints.BASE_TEST_URL_SECOND_PART + Endpoints.INSTRUMENT;
        AuthHeaders authHeaders = signGenerator.getAuthHeaders(verb, data, endpoint);

        OrderHttpRequest orderHttpRequest = new OrderHttpRequest(data, baseUrl, path, verb, authHeaders);
        try {
            HttpResponse<String> send = httpClient.send(orderHttpRequest.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            if (send.statusCode() == 200) {
                JsonArray instrumentArray = JsonParser.parseString(send.body()).getAsJsonArray();
                for (int i = 0; i < instrumentArray.size(); i++) {
                    JsonObject instrumentObject = instrumentArray.get(i).getAsJsonObject();
                    // Проверка наличия символа и цены
                    if (instrumentObject.has("symbol") && instrumentObject.has("lastPrice")) {
                        String symbol = instrumentObject.get("symbol").getAsString();
                        // Проверка, что символ - XBTUSD
                        if ("XBTUSD".equals(symbol)) {
                            JsonElement lastPriceElement = instrumentObject.get("lastPrice");
                            lastPrice = lastPriceElement.getAsJsonPrimitive().getAsDouble();
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error during request: " + e.getMessage());
        }
        return lastPrice;
    }
}


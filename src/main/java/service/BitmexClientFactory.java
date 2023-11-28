package service;

import util.Endpoints;
import service.BitmexClient;

import java.net.http.HttpClient;

public class BitmexClientFactory {
    public static BitmexClient newTestnetBitmexClient(String apiKey, String apiSecret) {
        return new BitmexClient(apiKey, apiSecret, Endpoints.BASE_TEST_URL);
    }

    //public BitmexClient newRealBitmexClient(String apiKey) {
    //    return new BitmexClient(apiKey, Endpoints.BASE_REAL_URL, true);
    //}
}

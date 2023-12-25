package Bot.service;

import Bot.model.auth.AuthHeaders;
import Bot.model.util.Signature;

public class SignGenerator {
    private final String apiSecret;
    private final String apiKey;

    public SignGenerator(BitmexClient bitmexClient) {
        this.apiSecret = bitmexClient.getApiSecretKey();
        this.apiKey = bitmexClient.getApiKey();
    }

    public AuthHeaders getAuthHeaders(String httpMethod, String data, String path) {
        long expires = System.currentTimeMillis() / 1000 + 60;
        String message = httpMethod + path + expires + data;
        Signature signatureService = new Signature();
        String signatureStr = signatureService.getSignature(apiSecret, message);

        return AuthHeaders.builder()
                .apiKey(apiKey)
                .signature(signatureStr)
                .expires(expires)
                .build();
    }
}

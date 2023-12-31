package Bot.model.auth;

import Bot.model.util.Signature;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthHeaders {
    private long expires;
    private String apiKey;
    private String signature;
}

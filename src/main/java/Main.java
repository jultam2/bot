import model.Order;
import model.OrderRequest;
import model.OrderType;
import model.Symbol;
import service.BitmexClient;
import service.BitmexClientFactory;


public class Main {

public static void main(String[] args) {
        Order order = Order.builder()
                .orderQty(100)
                .orderType(OrderType.LMT)
                .isBuy(true)
                .symbol(Symbol.XBTUSD)
                .price(30000.)
                .build();
        String apiKey = "CcFO3TMuN9Itk2MiZKo6Eq4h";
        String apiSecret = "cX0ku9shlz0Qvb1QWtypd0WRhinnlIQWhJ3ysiNDEMrSxn3I";

        BitmexClient bitmexClient = BitmexClientFactory.newTestnetBitmexClient(apiKey, apiSecret);
        bitmexClient.sendOrder(order);


    }
}
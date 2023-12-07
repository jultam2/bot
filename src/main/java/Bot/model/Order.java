package Bot.model;

import Bot.model.unused.OrderStatus;
import Bot.model.unused.Symbol;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private String orderId;
    private Symbol symbol;
    private boolean isBuy;
    private double orderQty;
    private Double price;
    private Double stopPx;
    private OrderTypeConverter.OrderType orderType;
    private OrderStatus orderStatus;
}

package Bot.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class OrderRequest {
    private final String symbol;
    private final String side;
    private final Double orderQty;
    private final Double price;
    private final String ordType;
    private final Double stopPx;

    public static OrderRequest toRequest(Order order) {
        String symbol = order.getSymbol() != null ? order.getSymbol().toString() : null;
        String side = order.isBuy() ? "Buy" : "Sell";
        Double orderQty = order.getOrderQty();
        Double price = order.getPrice();
        String ordType = order.getOrderType() != null ? OrderTypeConverter.getType(order.getOrderType()) : null;
        Double stopPx = order.getStopPx();

        return new OrderRequest(symbol, side, orderQty, price, ordType, stopPx);
    }
}

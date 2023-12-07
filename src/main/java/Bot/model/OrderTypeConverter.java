package Bot.model;

import lombok.Getter;

public class OrderTypeConverter {
    @Getter
    public enum OrderType {
        LMT("Limit"),
        MKT("Market"),
        STP_LMT("StopLimit"),
        STP_MKT("Stop");

        private final String type;

        OrderType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        // Метод для сопоставления строки типа ордера с enum значением
        public static OrderType mapOrderType(String orderTypeString) {
            switch (orderTypeString) {
                case "Limit":
                    return OrderType.LMT;
                case "Market":
                    return OrderType.MKT;
                case "StopLimit":
                    return OrderType.STP_LMT;
                case "Stop":
                    return OrderType.STP_MKT;
                default:
                    throw new IllegalArgumentException("Unsupported order type: " + orderTypeString);
            }
        }
    }

    public static String getType(OrderType orderType) {
        return orderType != null ? orderType.getType() : null;
    }
}
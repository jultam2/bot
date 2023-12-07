package Bot.model.util;

import Bot.model.OrderTypeConverter;
import Bot.repo.OrderRepoList;
import Bot.model.unused.Symbol;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Bot.model.Order;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;

public class Parser {

    private final OrderRepoList orderRepoList = new OrderRepoList();

    public void getPrettyOrderList(HttpResponse<String> send) {
        String jsonString = send.body();

        // Проверка, является ли JSON строкой массивом
        if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

            // Обработка каждого объекта в массиве
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                processJsonObject(jsonObject, false);
            }
        } else {
            // Если это не массив, считаем, что это одиночный объект
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            processJsonObject(jsonObject, true);
        }
    }

    // Метод для обработки JSON-объекта
    private void processJsonObject(JsonObject jsonObject, boolean isSingleObject) {
        // Извлечение данных из объекта
        String orderId = jsonObject.get("orderID").getAsString();
        String symbol = jsonObject.get("symbol").getAsString();
        String side = jsonObject.get("side").getAsString();
        int orderQty = jsonObject.get("orderQty").getAsInt();
        double price = jsonObject.get("price").getAsDouble();

        // Извлечение типа ордера из объекта
        String orderTypeString = jsonObject.get("ordType").getAsString();
        OrderTypeConverter.OrderType orderType = OrderTypeConverter.OrderType.mapOrderType(orderTypeString);

        // Создание объекта Order
        Order order = Order.builder()
                .orderId(orderId)
                .symbol(Symbol.valueOf(symbol))
                .isBuy("Buy".equals(side))
                .orderQty(orderQty)
                .price(price)
                .orderType(orderType)  // Добавление параметра OrderType
                .build();

        // Добавление объекта Order в список, если это одиночный объект
        if (isSingleObject) {
            orderRepoList.add(order);
        }

        // Вывод данных объекта Order в консоль
        System.out.println("Order ID: " + orderId);
        System.out.println("Symbol: " + symbol);
        System.out.println("Side: " + side);
        System.out.println("Order Quantity: " + orderQty);
        System.out.println("Price: " + price);
        System.out.println("Order Type: " + OrderTypeConverter.getType(orderType));
        System.out.println("---------------------");
    }
    // Метод для создания JSON-объекта
    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
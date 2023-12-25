package Bot.service;

import Bot.model.Order;
import Bot.repo.OrderRepoList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SpecificOrderCanceller {
    private static final Logger logger = LogManager.getLogger(SpecificOrderCanceller.class);
    private final OrderRepoList orderRepoList = new OrderRepoList();
    private final OrderCanceller orderCanceller;

    public SpecificOrderCanceller(BitmexClient bitmexClient) {
        this.orderCanceller = new OrderCanceller(bitmexClient);
    }

    public void chooseOrderToClose() {
        List<Order> orderList = orderRepoList.getOrdersAsList();
        for (Order openOrder : orderList) {
            System.out.println(openOrder.getOrderId());
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Choose orderId to close: ");
            String userInput = reader.readLine();
            orderCanceller.cancelOrder(userInput);
            logger.info("Order: " + userInput + "cancelled");
        } catch (IOException e) {
            logger.error("Error during request: " + e.getMessage());
        }
    }
}

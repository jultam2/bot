package Bot.repo;

import Bot.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class OrderRepoList {
    private List<Order> orders = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(OrderRepoList.class);

    public void add(Order order) {
        orders.add(order);
        logger.info("Orders repository created");
    }

    public void remove(String orderId) {
        orders.removeIf(order -> order.getOrderId().equals(orderId));
        logger.info("Order: " + orderId + "removed");
    }

    public Order getOrderById(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId))
                return order;
            logger.info("Order: " + orderId + "found");
            }
        return null;
    }

    public void getOpenOrdersList() {

        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public List<Order> getOrdersAsList() {
        List<Order> openOrdersList = new ArrayList<>();

        for (Order order : orders) {
            openOrdersList.add(order);
        }

        return openOrdersList;
    }
}

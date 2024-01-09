package Bot.model;

import lombok.Data;

@Data
public class BotStatus {
    private int id;
    private String apiKey;
    private double priceStep;
    private double coefficient;
    private int numberOfOrders;
    private String status;

    public void updateStatus(String apiKey, double priceStep, double coefficient, int numberOfOrders, String status) {
        this.id = 1;
        this.apiKey = apiKey;
        this.priceStep = priceStep;
        this.coefficient = coefficient;
        this.numberOfOrders = numberOfOrders;
        this.status = status;
    }
}
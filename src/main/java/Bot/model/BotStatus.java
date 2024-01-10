package Bot.model;

import lombok.Data;

@Data
public class BotStatus {
    private int id;
    private String apiKey;
    private double priceStep;
    private double coefficient;
    private int numberOfOrders;
    private boolean status = false;
    private boolean deleted = false;


    public void updateStatus(String apiKey, double priceStep, double coefficient, int numberOfOrders, boolean status) {
        this.id = 1;
        this.apiKey = apiKey;
        this.priceStep = priceStep;
        this.coefficient = coefficient;
        this.numberOfOrders = numberOfOrders;
        this.status = status;

    }

    public void updateBoolean(boolean status) {
        this.status = status;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
package univ_smb.m1.info803.model;

import java.io.Serializable;

public class Transporter implements Serializable {
    private String company;
    private int quantity;
    private int speed;
    private int orderId;

    public Transporter(Order order, String company, int quantity, int speed) {
        this.company = company;
        this.quantity = quantity;
        this.speed = speed;
        this.orderId = order.getId();
    }

    public String getCompany() {
        return company;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSpeed() {
        return speed;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Transporter{" +
                "company='" + company + '\'' +
                ", quantity=" + quantity +
                ", speed=" + speed +
                ", orderId=" + orderId +
                '}';
    }
}

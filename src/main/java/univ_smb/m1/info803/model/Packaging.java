package univ_smb.m1.info803.model;

public class Packaging {
    private String company;
    private String color;
    private double weight;
    private double size;
    private int orderId;

    public Packaging(Order order, String company, String color, double weight, double size) {
        this.company = company;
        this.color = color;
        this.weight = weight;
        this.size = size;
        this.orderId = order.getId();
    }

    public String getCompany() {
        return company;
    }

    public String getColor() {
        return color;
    }

    public double getWeight() {
        return weight;
    }

    public double getSize() {
        return size;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Packaging{" +
                "company='" + company + '\'' +
                ", color='" + color + '\'' +
                ", weight=" + weight +
                ", size=" + size +
                '}';
    }
}

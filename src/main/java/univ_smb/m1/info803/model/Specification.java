package univ_smb.m1.info803.model;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {
    private int id;
    private List<String> requirements;
    private double cost;
    private int time;
    private int quantity;

    public Specification(List<String> requirements, double cost, int time, int quantity) {
        this.id = 0;
        this.requirements = requirements;
        this.cost = cost;
        this.time = time;
        this.quantity = quantity;
    }

    public Specification(int id, List<String> requirements, double cost, int time, int quantity) {
        this.id = id;
        this.requirements = requirements;
        this.cost = cost;
        this.time = time;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public double getCost() {
        return cost;
    }

    public int getTime() {
        return time;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "id=" + id +
                ", requirements=" + requirements +
                ", cost=" + cost +
                ", time=" + time +
                ", quantity=" + quantity +
                '}';
    }
}

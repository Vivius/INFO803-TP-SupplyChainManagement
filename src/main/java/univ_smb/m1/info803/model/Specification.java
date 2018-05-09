package univ_smb.m1.info803.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Specification implements Serializable {
    private int id;
    private List<String> requirements;
    private double cost;
    private int time;
    private int quantity;

    private List<SpecificationAlteration> alterations;

    public Specification(List<String> requirements, double cost, int time, int quantity) {
        this.id = 0;
        this.requirements = requirements;
        this.cost = cost;
        this.time = time;
        this.quantity = quantity;
        this.alterations = new ArrayList<>();
    }

    public Specification(int id, List<String> requirements, double cost, int time, int quantity) {
        this(requirements, cost, time, quantity);
        this.id = id;
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

    public void addAlteration(SpecificationAlteration alteration) {
        alterations.add(alteration);
    }

    public List<SpecificationAlteration> getAlterations() {
        return alterations;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "id=" + id +
                ", requirements=" + requirements +
                ", cost=" + cost +
                ", time=" + time +
                ", quantity=" + quantity +
                ", alterations=" + alterations +
                '}';
    }
}

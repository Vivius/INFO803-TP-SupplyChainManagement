package univ_smb.m1.info803.model;

import java.io.Serializable;
import java.util.List;

public class SpecificationAlteration implements Serializable {
    private int specificationId;
    private List<String> requirements;
    private double cost;
    private int time;

    public SpecificationAlteration(Specification specification, List<String> requirements) {
        this.requirements = requirements;
        this.specificationId = specification.getId();
        this.cost = -1;
        this.time = -1;
    }

    public SpecificationAlteration(Specification specification, List<String> requirements, double cost, int time) {
        this(specification, requirements);
        this.cost = cost;
        this.time = time;
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

    public int getSpecificationId() {
        return specificationId;
    }

    @Override
    public String toString() {
        return "SpecificationAlteration{" +
                "specificationId=" + getSpecificationId() +
                ", requirements=" + requirements +
                ", cost=" + cost +
                ", time=" + time +
                '}';
    }
}

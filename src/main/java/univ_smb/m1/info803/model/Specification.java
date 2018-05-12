package univ_smb.m1.info803.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Specification implements Serializable {
    private static int index = 1;

    private int id;
    private List<String> requirements;
    private double cost;
    private int time;
    private int quantity;
    private String company;
    private int version;

    private List<SpecificationAlteration> alterations;

    public Specification(List<String> requirements, double cost, int time, int quantity) {
        this.id = index++;
        this.requirements = requirements;
        this.cost = cost;
        this.time = time;
        this.quantity = quantity;
        this.alterations = new ArrayList<>();
        this.version = 1;
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

    public String getCompany() {
        return company;
    }

    public int getVersion() {
        return version;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void upgradeVersion() {
        alterations.clear();
        version++;
    }

    public void addAlteration(SpecificationAlteration alteration) {
        alterations.add(alteration);
    }

    public List<SpecificationAlteration> getAlterations() {
        return alterations;
    }

    public List<String> getAllRequirements() {
        List<String> result = new ArrayList<>(requirements);
        for(SpecificationAlteration alt : alterations) {
            result.addAll(alt.getRequirements());
        }
        return result;
    }

    public double getLastCost() {
        double lastCost = cost;
        for(SpecificationAlteration alt : alterations) {
            if(alt.getCost() > 0) {
                lastCost = alt.getCost();
            }
        }
        return lastCost;
    }

    public int getLastTime() {
        int lastTime = time;
        for(SpecificationAlteration alt : alterations) {
            if(alt.getTime() > 0) {
                lastTime = alt.getTime();
            }
        }
        return lastTime;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "id=" + id +
                ", requirements=" + requirements +
                ", cost=" + cost +
                ", time=" + time +
                ", quantity=" + quantity +
                ", company='" + company + '\'' +
                ", version=" + version +
                ", alterations=" + alterations +
                '}';
    }
}

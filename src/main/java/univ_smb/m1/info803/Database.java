package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton permettant de stocker les données partagées du programme.
 */
public class Database {
    private static Database instance;

    private int id;

    private List<Specification> specifications;
    private List<Runnable> world;

    private Database() {
        this.id = 0;

        this.specifications = new ArrayList<>();
        this.world = new ArrayList<>();

        instance = this;
    }

    private int getId() {
        return ++id;
    }

    public static Database getInstance() {
        if(instance != null) {
            return instance;
        } else {
            return new Database();
        }
    }

    public Specification getSpecification(int id) {
        List<Specification> result = specifications.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
        if(result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public Specification addSpecification(Specification spec) {
        Specification uniquSpec = new Specification(getId(), spec.getRequirements(), spec.getCost(), spec.getTime(), spec.getQuantity());
        specifications.add(uniquSpec);
        return uniquSpec;
    }

    public List<Runnable> getWorld() {
        return world;
    }

    public void setWorld(List<Runnable> world) {
        this.world = world;
    }
}

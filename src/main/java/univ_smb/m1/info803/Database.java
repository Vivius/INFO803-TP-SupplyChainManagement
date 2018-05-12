package univ_smb.m1.info803;

import java.util.ArrayList;
import java.util.List;
/**
 * Singleton permettant de stocker les données partagées du programme.
 */
public class Database {
    private static Database instance;

    private int id;

    private List<Runnable> world;

    private Database() {
        this.id = 0;

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

    public List<Runnable> getWorld() {
        return world;
    }

    public void setWorld(List<Runnable> world) {
        this.world = world;
    }
}

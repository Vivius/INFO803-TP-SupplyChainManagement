package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.runnable.*;
import univ_smb.m1.info803.util.Pipe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {

    public Application() throws IOException, InterruptedException {
        init();
    }

    public void init() throws IOException, InterruptedException {
        // La base de données de l'ensemble du programme
        Database db = Database.getInstance();

        // Liste des threads
        List<Thread> threads = new ArrayList<>();

        // Création des pipes (pour la communication entre threads)
        Pipe<Specification> client_logistics_pipe = new Pipe<>();
        Pipe<Specification> logistics_plant_pipe = new Pipe<>();

        // Création des runnables
        List<Runnable> runnables = new ArrayList<>();
        runnables.add(new LogisticsRunnable(client_logistics_pipe, logistics_plant_pipe));

        runnables.add(new PlantRunnable(logistics_plant_pipe));
        runnables.add(new PlantRunnable(logistics_plant_pipe));

        runnables.add(new RetailerRunnable());
        runnables.add(new SupplierRunnable());
        runnables.add(new TransporterRunnable());
        runnables.add(new WarehouseRunnable());

        // Le 'monde' correspond à l'ensemble des entités (entreprises, transporteurs...) qui communiquent ensemble
        // Dans ce programme il s'agit des runnables (threads)
        db.setWorld(runnables);

        // Démarrage des runnables
        // Création des threads et stockage de leur référence
        for(Runnable run : runnables) {
            Thread th = new Thread(run);
            th.start();
            threads.add(th);
        }

        // Ajout d'un cahier des charges en database
        // La database va lui donner un identifiant unique
        db.addSpecification(new Specification(Arrays.asList("toto", "test"), 10, 20, 30));

        // Envoi du cahier des charge au logistics runnable
        client_logistics_pipe.write(db.getSpecification(1));

        // Arrêt/Attente des threads
        for(Thread th : threads) {
            // th.interrupt();
            th.join();
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        new Application();
    }

}

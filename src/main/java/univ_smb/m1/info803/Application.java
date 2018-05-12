package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.runnable.*;
import univ_smb.m1.info803.ui.Home;
import univ_smb.m1.info803.util.Pipe;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application implements Runnable {
    // Contient certains données globales
    private Database db;

    // Liste des threads et runnables exécutés
    private List<Thread> threads;
    private List<Runnable> runnables;

    // Pipes (pour la communication entre le client et les threads)
    private Pipe<Specification> clientToLogisticsSpecificationsPipe;
    private Pipe<Specification> plantToClientSpecificationsPipe;

    // Listeners
    private List<ApplicationListener> listeners;

    public Application() throws IOException {
        this.db = Database.getInstance();

        this.threads = new ArrayList<>();
        this.runnables = new ArrayList<>();
        this.listeners = new ArrayList<>();

        this.clientToLogisticsSpecificationsPipe = new Pipe<>();
        this.plantToClientSpecificationsPipe = new Pipe<>();

        // Création des runnables
        Pipe<Specification> logisticsToPlantSpecificationsPipe = new Pipe<>();

        this.runnables.add(new LogisticsRunnable(clientToLogisticsSpecificationsPipe, logisticsToPlantSpecificationsPipe));

        this.runnables.add(new PlantRunnable("NVIDIA", logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));
        this.runnables.add(new PlantRunnable("INTEL", logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));
        this.runnables.add(new PlantRunnable("AMD", logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));

        this.runnables.add(new RetailerRunnable());
        this.runnables.add(new SupplierRunnable());
        this.runnables.add(new TransporterRunnable());
        this.runnables.add(new WarehouseRunnable());

        // Le 'monde' correspond à l'ensemble des entités (entreprises, transporteurs...) qui communiquent ensemble
        // Dans ce programme il s'agit des runnables (threads)
        this.db.setWorld(runnables);
    }

    @Override
    public void run() {

        // Démarrage des runnables
        // Création des threads et stockage de leur référence
        for(Runnable run : runnables) {
            Thread th = new Thread(run);
            th.start();
            threads.add(th);
        }

        // TEST
        // Le client envoie un cahier des charges à traiter
        try {
            sendSpecification(new Specification(Arrays.asList("toto", "test"), 10, 20, 30));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Traitements effectués par le client...
        while(true) {
            try {
                Specification spec = plantToClientSpecificationsPipe.read();

                // On indique aux listeners qu'un cahier des charge a été traité
                for(ApplicationListener listener : listeners) {
                    listener.specificationProcessed(spec);
                }

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        // Arrêt/Attente des threads
        /*
        for(Thread th : threads) {
            // th.interrupt();
            th.join();
        }
        */
    }

    public void sendSpecification(Specification spec) throws IOException {
        clientToLogisticsSpecificationsPipe.write(spec);
    }

    public void addApplicationListener(ApplicationListener listener) {
        listeners.add(listener);
    }
}

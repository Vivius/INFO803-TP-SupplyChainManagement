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

public class Application {
    // La base de données de l'ensemble du programme
    private Database db;

    // Liste des threads et runnables exécutés
    private List<Thread> threads;
    private List<Runnable> runnables;

    // Pipes (pour la communication entre le client et les threads)
    private Pipe<Specification> clientToLogisticsSpecificationsPipe;
    private Pipe<Specification> plantToClientSpecificationsPipe;

    // Listeners
    private List<ApplicationListener> listeners;

    private Application() throws IOException {
        this.db = Database.getInstance();

        this.threads = new ArrayList<>();
        this.runnables = new ArrayList<>();
        this.listeners = new ArrayList<>();

        this.clientToLogisticsSpecificationsPipe = new Pipe<>();
        this.plantToClientSpecificationsPipe = new Pipe<>();

        // Création des runnables
        Pipe<Specification> logisticsToPlantSpecificationsPipe = new Pipe<>();

        this.runnables.add(new LogisticsRunnable(clientToLogisticsSpecificationsPipe, logisticsToPlantSpecificationsPipe));

        this.runnables.add(new PlantRunnable(logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));
        this.runnables.add(new PlantRunnable(logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));

        this.runnables.add(new RetailerRunnable());
        this.runnables.add(new SupplierRunnable());
        this.runnables.add(new TransporterRunnable());
        this.runnables.add(new WarehouseRunnable());

        // Le 'monde' correspond à l'ensemble des entités (entreprises, transporteurs...) qui communiquent ensemble
        // Dans ce programme il s'agit des runnables (threads)
        this.db.setWorld(runnables);
    }

    private void run() throws IOException {

        // Démarrage des runnables
        // Création des threads et stockage de leur référence
        for(Runnable run : runnables) {
            Thread th = new Thread(run);
            th.start();
            threads.add(th);
        }

        // Le client envoie un cahier des charges à traiter

        // Traitements effectués par le client...
        while(true) {
            try {
                Specification spec = plantToClientSpecificationsPipe.read();

                // On indique aux listeners qu'un cahier des charge a été traité
                for(ApplicationListener listener : listeners) {
                    listener.specificationProcessed(spec);
                }

            } catch (ClassNotFoundException e) {
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
        // Ajout du cahier des charges en database
        // La database va lui donner un identifiant unique
        Specification newSpec = db.addSpecification(spec);

        // Envoi du cahier des charge au logistics runnable
        clientToLogisticsSpecificationsPipe.write(newSpec);
    }

    public void addApplicationListener(ApplicationListener listener) {
        listeners.add(listener);
    }

    public static void main(String args[]) throws IOException {
        final Application app = new Application();

        Thread gui = new Thread(() -> {

            JFrame frame = new JFrame("Supply Chain Management");
            frame.setContentPane(new Home(app).getWindow());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

        });

        // Démarrage de l'application et de l'interface graphique
        gui.start();
        app.run();
    }

}

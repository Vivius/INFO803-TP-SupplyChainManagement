package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Order;
import univ_smb.m1.info803.model.Packaging;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.Transporter;
import univ_smb.m1.info803.runnable.*;
import univ_smb.m1.info803.util.Pipe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Application implements Runnable {
    // Contient certains données globales
    private final Database db;

    // Liste des threads et runnables exécutés
    private List<Thread> threads;
    private List<Runnable> runnables;

    // Pipes (pour la communication entre le client et les threads)
    private final Pipe<Specification> clientToLogisticsSpecificationsPipe;
    private final Pipe<Specification> plantToClientSpecificationsPipe;
    private final Pipe<Order> clientToLogisiticsOrdersPipe;
    private final Pipe<Packaging> logisticsToClientPackagingPipe;
    private final Pipe<Transporter> logisticsToClientTransporterPipe;

    // Listeners
    private final List<ApplicationListener> listeners;

    public Application() throws IOException {
        this.db = Database.getInstance();

        this.threads = new ArrayList<>();
        this.runnables = new ArrayList<>();
        this.listeners = new ArrayList<>();

        this.clientToLogisticsSpecificationsPipe = new Pipe<>();
        this.plantToClientSpecificationsPipe = new Pipe<>();
        this.clientToLogisiticsOrdersPipe = new Pipe<>();
        this.logisticsToClientPackagingPipe = new Pipe<>();
        this.logisticsToClientTransporterPipe = new Pipe<>();

        // Création des runnables
        Pipe<Specification> logisticsToPlantSpecificationsPipe = new Pipe<>();

        Pipe<Order> logisticsToTransporterOrdersPipe = new Pipe<>();
        Pipe<Order> logisticsToSupplierOrdersPipe = new Pipe<>();

        Pipe<Transporter> transporterToLogisticsTransporterPipe = new Pipe<>();
        Pipe<Packaging> supplierToLogisticsPackagingPipe = new Pipe<>();

        this.runnables.add(new LogisticsRunnable(
                clientToLogisticsSpecificationsPipe,
                logisticsToPlantSpecificationsPipe,
                clientToLogisiticsOrdersPipe,
                logisticsToTransporterOrdersPipe,
                logisticsToSupplierOrdersPipe,
                transporterToLogisticsTransporterPipe,
                supplierToLogisticsPackagingPipe,
                logisticsToClientPackagingPipe,
                logisticsToClientTransporterPipe));

        this.runnables.add(new PlantRunnable("NVIDIA", logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));
        this.runnables.add(new PlantRunnable("INTEL", logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));
        this.runnables.add(new PlantRunnable("AMD", logisticsToPlantSpecificationsPipe, plantToClientSpecificationsPipe));

        this.runnables.add(new SupplierRunnable("EMBALEX", logisticsToSupplierOrdersPipe, supplierToLogisticsPackagingPipe));
        this.runnables.add(new SupplierRunnable("LESU", logisticsToSupplierOrdersPipe, supplierToLogisticsPackagingPipe));
        this.runnables.add(new SupplierRunnable("FLEY", logisticsToSupplierOrdersPipe, supplierToLogisticsPackagingPipe));

        this.runnables.add(new TransporterRunnable("DHL", logisticsToTransporterOrdersPipe, transporterToLogisticsTransporterPipe));
        this.runnables.add(new TransporterRunnable("TNT", logisticsToTransporterOrdersPipe, transporterToLogisticsTransporterPipe));
        this.runnables.add(new TransporterRunnable("COLLISIMO", logisticsToTransporterOrdersPipe, transporterToLogisticsTransporterPipe));

        this.runnables.add(new RetailerRunnable());
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

        // Traitements effectués par le client...
        while(true) {
            try {

                if(plantToClientSpecificationsPipe.ready()) {
                    Specification spec = plantToClientSpecificationsPipe.read();
                    for(ApplicationListener listener : listeners) {
                        listener.specificationProcessed(spec);
                    }
                }

                if(logisticsToClientPackagingPipe.ready()) {
                    Packaging packaging = logisticsToClientPackagingPipe.read();
                    for(ApplicationListener listener : listeners) {
                        listener.packagingReceived(packaging);
                    }
                }

                if(logisticsToClientTransporterPipe.ready()) {
                    Transporter transporter= logisticsToClientTransporterPipe.read();
                    for(ApplicationListener listener : listeners) {
                        listener.transporterReceived(transporter);
                    }
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

    public void sendOrder(Order order) throws IOException {
        clientToLogisiticsOrdersPipe.write(order);
    }

    public void addApplicationListener(ApplicationListener listener) {
        listeners.add(listener);
    }
}

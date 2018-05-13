package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.Database;
import univ_smb.m1.info803.model.Order;
import univ_smb.m1.info803.model.Packaging;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.Transporter;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogisticsRunnable implements Runnable {
    private final Pipe<Specification> clientToLogisticsSpecificationsPipe;
    private final Pipe<Specification> logisticsToPlantSpecificationsPipe;

    private final Pipe<Order> clientToLogisiticsOrdersPipe;
    private final Pipe<Order> logisticsToTransporterOrdersPipe;
    private final Pipe<Order> logisticsToSupplierOrdersPipe;

    private final Pipe<Packaging> supplierToLogisticsPackagingPipe;
    private final Pipe<Packaging> logisticsToClientPackagingPipe;

    private final Pipe<Transporter> transporterToLogisticsTransporterPipe;
    private final Pipe<Transporter> logisticsToClientTransporterPipe;

    private final List<Order> processedOrders;

    public LogisticsRunnable(Pipe<Specification> clientToLogisticsSpecificationsPipe,
                             Pipe<Specification> logisticsToPlantSpecificationsPipe,
                             Pipe<Order> clientToLogisiticsOrdersPipe,
                             Pipe<Order> logisticsToTransporterOrdersPipe,
                             Pipe<Order> logisticsToSupplierOrdersPipe,
                             Pipe<Transporter> transporterToLogisticsTransporterPipe,
                             Pipe<Packaging> supplierToLogisticsPackagingPipe,
                             Pipe<Packaging> logisticsToClientPackagingPipe,
                             Pipe<Transporter> logisticsToClientTransporterPipe)
    {
        this.clientToLogisticsSpecificationsPipe = clientToLogisticsSpecificationsPipe;
        this.logisticsToPlantSpecificationsPipe = logisticsToPlantSpecificationsPipe;
        this.clientToLogisiticsOrdersPipe = clientToLogisiticsOrdersPipe;
        this.logisticsToTransporterOrdersPipe = logisticsToTransporterOrdersPipe;
        this.logisticsToSupplierOrdersPipe = logisticsToSupplierOrdersPipe;
        this.supplierToLogisticsPackagingPipe = supplierToLogisticsPackagingPipe;
        this.transporterToLogisticsTransporterPipe = transporterToLogisticsTransporterPipe;
        this.logisticsToClientPackagingPipe = logisticsToClientPackagingPipe;
        this.logisticsToClientTransporterPipe = logisticsToClientTransporterPipe;

        this.processedOrders = new ArrayList<>();
    }

    @Override
    public void run() {
        while(true) {

            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            try {

                // On s'occupe d'envoyer le cahier des charges d'un client aux usines (plants).
                if(clientToLogisticsSpecificationsPipe.ready()) {

                    // Réception du cahier des charges
                    Specification spec = clientToLogisticsSpecificationsPipe.read();

                    // Envoi de la spec pour chaque fournisseur
                    // Chaque fournisseur s'occupera de contacter le client directement pour lui proposer un cahier des charges.
                    for(Runnable run : Database.getInstance().getWorld()) {
                        if(run.getClass().equals(PlantRunnable.class)) {
                            logisticsToPlantSpecificationsPipe.write(spec);
                            Thread.sleep(100);
                        }
                    }

                }

                // Réception des commandes des clients
                if(clientToLogisiticsOrdersPipe.ready()) {

                    // On reçoit une nouvelle commande d'un client.
                    // Cela implique qu'un cahier des charges (spécification) ait été validé auparavant.
                    Order order = clientToLogisiticsOrdersPipe.read();

                    // Nouvelle commande car non en cours de traitement
                    if(findProcessedOrder(order.getId()) == null) {

                        processedOrders.add(order);

                        // Envoi de la commande pour chaque supplier
                        for(Runnable run : Database.getInstance().getWorld()) {
                            if(run.getClass().equals(SupplierRunnable.class)) {
                                logisticsToSupplierOrdersPipe.write(order);
                                Thread.sleep(100);
                            }
                        }

                    } else {

                        // Envoi de la commande pour chaque transporter
                        for(Runnable run : Database.getInstance().getWorld()) {
                            if(run.getClass().equals(TransporterRunnable.class)) {
                                logisticsToTransporterOrdersPipe.write(order);
                                Thread.sleep(100);
                            }
                        }

                    }

                }

                // Réception des offres d'emballage (suppliers) et envoi au client
                if(supplierToLogisticsPackagingPipe.ready()) {
                    Packaging packaging = supplierToLogisticsPackagingPipe.read();
                    logisticsToClientPackagingPipe.write(packaging);
                }

                // Réception des offres de transport et envoi au client
                if(transporterToLogisticsTransporterPipe.ready()) {
                    Transporter transporter = transporterToLogisticsTransporterPipe.read();
                    logisticsToClientTransporterPipe.write(transporter);
                }

                Thread.sleep(100);

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }

        }
    }

    private Order findProcessedOrder(int id) {
        List<Order> result = processedOrders.stream().filter(o -> o.getId() == id).collect(Collectors.toList());
        if(result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}

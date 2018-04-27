package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.Database;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;

public class LogisticsRunnable implements Runnable {
    private Pipe<Specification> clientToLogisticsSpecificationsPipe;
    private Pipe<Specification> logisticsToPlantSpecificationsPipe;

    public LogisticsRunnable(Pipe<Specification> clientToLogisticsSpecificationsPipe, Pipe<Specification> logisticsToPlantSpecificationsPipe) {
        this.clientToLogisticsSpecificationsPipe = clientToLogisticsSpecificationsPipe;
        this.logisticsToPlantSpecificationsPipe = logisticsToPlantSpecificationsPipe;
    }

    @Override
    public void run() {
        while(true) {

            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            try {
                // Un client demande un produit
                Specification spec = clientToLogisticsSpecificationsPipe.read();

                // Envoi de la spec pour chaque fournisseur
                logisticsToPlantSpecificationsPipe.writeForEach(Database.getInstance().getWorld(), PlantRunnable.class, spec);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

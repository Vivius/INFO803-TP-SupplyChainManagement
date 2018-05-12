package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.Database;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.util.Pipe;

import javax.xml.crypto.Data;
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
                for(Runnable run : Database.getInstance().getWorld()) {
                    if(run.getClass().equals(PlantRunnable.class)) {
                        logisticsToPlantSpecificationsPipe.write(spec);
                        Thread.sleep(100);
                    }
                }

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

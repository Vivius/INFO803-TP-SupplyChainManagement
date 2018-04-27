package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.Database;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;

public class LogisticsRunnable implements Runnable {
    private Pipe<Specification> clientPipe;
    private Pipe<Specification> plantPipe;

    public LogisticsRunnable(Pipe<Specification> clientPipe, Pipe<Specification> plantPipe) {
        this.clientPipe = clientPipe;
        this.plantPipe = plantPipe;
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
                Specification spec = clientPipe.read();

                // Envoi de la spec pour chaque fournisseur
                plantPipe.writeForEach(Database.getInstance().getWorld(), PlantRunnable.class, spec);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

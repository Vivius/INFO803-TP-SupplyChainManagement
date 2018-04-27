package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;

public class WorkshopRunnable implements Runnable {
    private Pipe<Specification> plantPipe;

    public WorkshopRunnable(Pipe<Specification> plantPipe) {
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
                Specification spec = plantPipe.read();
                System.out.println("Workshop " + Thread.currentThread().getId() + " : Réception d'une cahier des charges à analyser");
                System.out.println(spec);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

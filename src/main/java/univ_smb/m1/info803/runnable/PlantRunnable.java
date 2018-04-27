package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.utils.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlantRunnable implements Runnable {
    private Pipe<Specification> logisticsPipe;
    private List<Specification> specificationsProcessed;

    public PlantRunnable(Pipe<Specification> logisticsPipe) {
        this.logisticsPipe = logisticsPipe;
        this.specificationsProcessed = new ArrayList<>();
    }

    @Override
    public void run() {
        while(true) {
            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            try {
                Specification spec = logisticsPipe.read();

                if(shouldProcessSpecification(spec)) {
                    specificationsProcessed.add(spec);
                    System.out.println(Thread.currentThread().getId() + " : Réception d'une cahier des charges");
                    System.out.println(spec);
                } else {
                    // On renvoie la spec si elle ne nous est pas destinée
                    logisticsPipe.write(spec);
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean shouldProcessSpecification(Specification receivedSpec) {
        for(Specification spec : specificationsProcessed) {
            if(receivedSpec.getId() == spec.getId()) {
                return false;
            }
        }
        return true;
    }
}

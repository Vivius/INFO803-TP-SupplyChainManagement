package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.SpecificationAlteration;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WorkshopRunnable implements Runnable {
    private Pipe<Specification> plantToWorkshopSpecificationsPipe;
    private Pipe<SpecificationAlteration> workshopToPlantSpecificationsPipe;

    public WorkshopRunnable(Pipe<Specification> plantToWorkshopSpecificationsPipe, Pipe<SpecificationAlteration> workshopToPlantSpecificationsPipe) {
        this.plantToWorkshopSpecificationsPipe = plantToWorkshopSpecificationsPipe;
        this.workshopToPlantSpecificationsPipe = workshopToPlantSpecificationsPipe;
    }

    @Override
    public void run() {
        while(true) {

            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            try {
                Specification spec = plantToWorkshopSpecificationsPipe.read();
                // System.out.println("Workshop " + Thread.currentThread().getId() + " : Réception d'une cahier des charges à analyser");
                // System.out.println(spec);

                double randCost = ThreadLocalRandom.current().nextInt(100, 10000);
                int randTime = ThreadLocalRandom.current().nextInt(10, 200);

                SpecificationAlteration alteration = new SpecificationAlteration(spec, new ArrayList<>(), randCost, randTime);

                workshopToPlantSpecificationsPipe.write(alteration);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

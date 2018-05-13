package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.SpecificationAlteration;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WorkshopRunnable implements Runnable {
    private final Pipe<Specification> plantToWorkshopSpecificationsPipe;
    private final Pipe<SpecificationAlteration> workshopToPlantSpecificationsPipe;

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
                // Récupéation d'une spec à analyser et modifier
                Specification spec = plantToWorkshopSpecificationsPipe.read();

                // Modification du coût et du temps
                double randCost = ThreadLocalRandom.current().nextDouble(100, 10000);
                int randTime = ThreadLocalRandom.current().nextInt(10, 200);

                SpecificationAlteration alteration = new SpecificationAlteration(spec, new ArrayList<>(), randCost, randTime);

                // Envoi de l'altération au l'usine (fabricant)
                workshopToPlantSpecificationsPipe.write(alteration);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}

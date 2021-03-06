package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.SpecificationAlteration;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DesignRunnable implements Runnable {
    private final Pipe<Specification> plantToDesignSpecificationsPipe;
    private final Pipe<SpecificationAlteration> designToPlantSpecificationsPipe;

    public DesignRunnable(Pipe<Specification> plantToDesignSpecificationsPipe, Pipe<SpecificationAlteration> designToPlantSpecificationsPipe) {
        this.plantToDesignSpecificationsPipe = plantToDesignSpecificationsPipe;
        this.designToPlantSpecificationsPipe = designToPlantSpecificationsPipe;
    }

    @Override
    public void run() {
        while(true) {

            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            try {
                // Récupération de la spec à traiter
                Specification spec = plantToDesignSpecificationsPipe.read();

                // Création d'une altération au niveau des requirements.
                List<String> newRequirements = new ArrayList<>();
                int startIndex = ThreadLocalRandom.current().nextInt(10, 1000);
                int nbRequirements = ThreadLocalRandom.current().nextInt(1, 10);

                for(int i=startIndex; i<startIndex + nbRequirements; ++i) {
                    newRequirements.add("REQ-" + i);
                }
                SpecificationAlteration alteration = new SpecificationAlteration(spec, newRequirements);

                // Envoi de l'alteration à l'entreprise concernée.
                designToPlantSpecificationsPipe.write(alteration);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}

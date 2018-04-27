package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.SpecificationAlteration;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlantRunnable implements Runnable {
    private Pipe<Specification> logisticsToPlantSpecificationsPipe;
    private Pipe<Specification> plantToDesignSpecificationsPipe;
    private Pipe<Specification> plantToWorkshopSpecificationsPipe;
    private Pipe<SpecificationAlteration> departmentsToPlantAlterationsPipe;

    private List<Specification> specificationsProcessed;

    private Thread workshop;
    private Thread design;

    public PlantRunnable(Pipe<Specification> logisticsToPlantSpecificationsPipe) throws IOException {
        this.logisticsToPlantSpecificationsPipe = logisticsToPlantSpecificationsPipe;
        this.specificationsProcessed = new ArrayList<>();

        this.plantToDesignSpecificationsPipe = new Pipe<>();
        this.plantToWorkshopSpecificationsPipe = new Pipe<>();
        this.departmentsToPlantAlterationsPipe = new Pipe<>();

        this.workshop = new Thread(new WorkshopRunnable(plantToWorkshopSpecificationsPipe, departmentsToPlantAlterationsPipe));
        this.design = new Thread(new DesignRunnable(plantToDesignSpecificationsPipe, departmentsToPlantAlterationsPipe));

        this.workshop.start();
        this.design.start();
    }

    @Override
    public void run() {
        while(true) {

            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            try {

                if(logisticsToPlantSpecificationsPipe.ready()) {
                    Specification spec = logisticsToPlantSpecificationsPipe.read();

                    if(shouldProcessSpecification(spec)) {
                        specificationsProcessed.add(spec);
                        System.out.println("Plant " + Thread.currentThread().getId() + " : Réception d'une cahier des charges");
                        System.out.println(spec);

                        Thread.sleep(1000);

                        // Envoi du cahier des charges à l'atelier de prototypage et à l'atelier d'étude
                        plantToDesignSpecificationsPipe.write(spec);
                        plantToWorkshopSpecificationsPipe.write(spec);

                    } else {
                        // On renvoie la spec si elle ne nous est pas destinée
                        logisticsToPlantSpecificationsPipe.write(spec);
                    }
                }

                if(departmentsToPlantAlterationsPipe.ready()) {
                    SpecificationAlteration alteration = departmentsToPlantAlterationsPipe.read();
                    System.out.println("Plant " + Thread.currentThread().getId() + " : Réception d'une contre proposition");
                    System.out.println(alteration);

                    Specification spec = findProcessedSpecification(alteration.getSpecificationId());
                    if(spec != null) {
                        spec.addAlteration(alteration);

                        if(spec.getAlterations().size() == 2) {
                            System.out.println("Les ateliers ont fait leur travail, on envoie le résultat au client");
                        }
                    } else {
                        throw new NullPointerException();
                    }
                }

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
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

    private Specification findProcessedSpecification(int id) {
        List<Specification> result = specificationsProcessed.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
        if(result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
}

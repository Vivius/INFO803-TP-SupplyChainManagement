package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.SpecificationAlteration;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlantRunnable implements Runnable {
    private String companyName;

    private Pipe<Specification> logisticsToPlantSpecificationsPipe;
    private Pipe<Specification> plantToDesignSpecificationsPipe;
    private Pipe<Specification> plantToWorkshopSpecificationsPipe;
    private Pipe<SpecificationAlteration> workshopToPLantAlterationsPipe;
    private Pipe<SpecificationAlteration> designToPlantAlterationsPipe;
    private Pipe<Specification> plantToClientSpecificationsPipe;

    private List<Specification> specificationsProcessed;
    private List<SpecificationAlteration> alterations;

    private Thread workshop;
    private Thread design;

    public PlantRunnable(String companyName, Pipe<Specification> logisticsToPlantSpecificationsPipe, Pipe<Specification> plantToClientSpecificationsPipe) throws IOException {
        this.companyName = companyName;

        this.logisticsToPlantSpecificationsPipe = logisticsToPlantSpecificationsPipe;
        this.plantToClientSpecificationsPipe = plantToClientSpecificationsPipe;

        this.specificationsProcessed = new ArrayList<>();
        this.alterations = new ArrayList<>();

        this.plantToDesignSpecificationsPipe = new Pipe<>();
        this.plantToWorkshopSpecificationsPipe = new Pipe<>();
        this.workshopToPLantAlterationsPipe = new Pipe<>();
        this.designToPlantAlterationsPipe = new Pipe<>();

        this.workshop = new Thread(new WorkshopRunnable(plantToWorkshopSpecificationsPipe, workshopToPLantAlterationsPipe));
        this.design = new Thread(new DesignRunnable(plantToDesignSpecificationsPipe, designToPlantAlterationsPipe));

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

                // On traite les cahier des charges venant de la logisitique
                if(logisticsToPlantSpecificationsPipe.ready()) {
                    Specification spec = logisticsToPlantSpecificationsPipe.read();

                    if(shouldProcessSpecification(spec)) {
                        spec.setCompany(companyName);
                        specificationsProcessed.add(spec);
                        // System.out.println("Plant " + Thread.currentThread().getId() + " : Réception d'une cahier des charges");
                        // System.out.println(spec);

                        // Envoi du cahier des charges à l'atelier de prototypage et à l'atelier d'étude
                        plantToDesignSpecificationsPipe.write(spec);
                        plantToWorkshopSpecificationsPipe.write(spec);
                    } else {
                        // On renvoie la spec si elle ne nous est pas destinée
                        logisticsToPlantSpecificationsPipe.write(spec);
                        Thread.yield();
                    }
                }

                // On récupère une altération venant d'un ateler Workshop si possible
                if(workshopToPLantAlterationsPipe.ready()) {
                    alterations.add(workshopToPLantAlterationsPipe.read());
                }

                // On récupère une altération venant d'un ateler Design si possible
                if(designToPlantAlterationsPipe.ready()) {
                    alterations.add(designToPlantAlterationsPipe.read());
                }

                // On traite les éventuelles alterations reçues pour tous les cahier des charges en cours de traitement.
                if(alterations.size() > 0) {
                    for(SpecificationAlteration alt : alterations) {
                        Specification spec = findProcessedSpecification(alt.getSpecificationId());
                        if(spec != null) {
                            spec.addAlteration(alt);

                            if(spec.getAlterations().size() > 0 && spec.getAlterations().size() % 2 == 0) {
                                plantToClientSpecificationsPipe.write(spec);
                            }

                        } else {
                            throw new NullPointerException();
                        }
                    }
                    alterations.clear();
                }

            } catch (IOException | ClassNotFoundException e) {
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

package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.SpecificationAlteration;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class PlantRunnable implements Runnable {
    private final String companyName;

    private final Pipe<Specification> logisticsToPlantSpecificationsPipe;
    private final Pipe<Specification> plantToDesignSpecificationsPipe;
    private final Pipe<Specification> plantToWorkshopSpecificationsPipe;
    private final Pipe<Specification> plantToClientSpecificationsPipe;

    private final Pipe<SpecificationAlteration> workshopToPLantAlterationsPipe;
    private final Pipe<SpecificationAlteration> designToPlantAlterationsPipe;

    private final List<Specification> specificationsProcessed;
    private final List<SpecificationAlteration> alterations;

    private final Lock mutex = new ReentrantLock(true);

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

        new Thread(new WorkshopRunnable(plantToWorkshopSpecificationsPipe, workshopToPLantAlterationsPipe)).start();
        new Thread(new DesignRunnable(plantToDesignSpecificationsPipe, designToPlantAlterationsPipe)).start();
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

                        // Envoi du cahier des charges à l'atelier de prototypage et à l'atelier d'étude pour modifications
                        plantToDesignSpecificationsPipe.write(spec);
                        plantToWorkshopSpecificationsPipe.write(spec);
                    } else {
                        // On renvoie la spec si elle ne nous est pas destinée
                        mutex.lock();
                        logisticsToPlantSpecificationsPipe.write(spec);
                        mutex.unlock();
                        Thread.yield();
                    }
                }

                // On récupère une altération venant d'un atelier Workshop si possible
                if(workshopToPLantAlterationsPipe.ready()) {
                    alterations.add(workshopToPLantAlterationsPipe.read());
                }

                // On récupère une altération venant d'un atelier Design si possible
                if(designToPlantAlterationsPipe.ready()) {
                    alterations.add(designToPlantAlterationsPipe.read());
                }

                // On traite les éventuelles alterations reçues pour tous les cahier des charges en cours de traitement.
                if(alterations.size() > 0) {
                    for(SpecificationAlteration alt : alterations) {
                        Specification spec = findProcessedSpecification(alt.getSpecificationId());
                        if(spec != null) {
                            spec.addAlteration(alt);

                            // Si le workshop et le design ont envoyé chacun une altération, on envoie le nouveau cahier des charges au client.
                            if(spec.getAlterations().size() > 0 && spec.getAlterations().size() % 2 == 0) {
                                mutex.lock();
                                plantToClientSpecificationsPipe.write(spec);
                                mutex.unlock();
                            }

                        } else {
                            throw new NullPointerException();
                        }
                    }
                    alterations.clear();
                }

                Thread.sleep(100);

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

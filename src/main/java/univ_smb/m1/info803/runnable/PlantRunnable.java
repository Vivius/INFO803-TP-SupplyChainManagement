package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlantRunnable implements Runnable {
    private Pipe<Specification> logisticsPipe;
    private Pipe<Specification> designPipe;
    private Pipe<Specification> workshopPipe;

    private List<Specification> specificationsProcessed;

    private Thread workshop;
    private Thread design;

    public PlantRunnable(Pipe<Specification> logisticsPipe) throws IOException {
        this.logisticsPipe = logisticsPipe;
        this.specificationsProcessed = new ArrayList<>();

        this.designPipe = new Pipe<>();
        this.workshopPipe = new Pipe<>();

        this.workshop = new Thread(new WorkshopRunnable(workshopPipe));
        this.design = new Thread(new DesignRunnable(designPipe));

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

                if(logisticsPipe.ready()) {
                    Specification spec = logisticsPipe.read();

                    if(shouldProcessSpecification(spec)) {
                        specificationsProcessed.add(spec);
                        System.out.println("Plant " + Thread.currentThread().getId() + " : Réception d'une cahier des charges");
                        System.out.println(spec);

                        Thread.sleep(1000);

                        // Envoi du cahier des charges à l'atelier de prototypage et à l'atelier d'étude
                        designPipe.write(spec);
                        workshopPipe.write(spec);

                    } else {
                        // On renvoie la spec si elle ne nous est pas destinée
                        logisticsPipe.write(spec);
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
}

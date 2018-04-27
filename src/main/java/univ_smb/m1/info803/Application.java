package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.runnable.*;
import univ_smb.m1.info803.utils.Pipe;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {

    public static void main(String args[]) throws IOException, InterruptedException {
        // Liste des threads
        List<Thread> threads = new ArrayList<>();

        // Création des pipes
        Pipe<Specification> client_logistics_pipe = new Pipe<>();
        Pipe<Specification> logistics_plant_pipe = new Pipe<>();

        // Création des runnables
        List<Runnable> runnables = new ArrayList<>();
        runnables.add(new LogisticsRunnable(client_logistics_pipe, logistics_plant_pipe));

        runnables.add(new PlantRunnable(logistics_plant_pipe));
        runnables.add(new PlantRunnable(logistics_plant_pipe));

        runnables.add(new RetailerRunnable());
        runnables.add(new SupplierRunnable());
        runnables.add(new TransporterRunnable());
        runnables.add(new WarehouseRunnable());

        // Le 'monde' correspond à l'ensemble des entités (entreprises...) qui communiquent ensemble
        Database.getInstance().setWorld(runnables);

        // Démarrage des runnables
        for(Runnable run : runnables) {
            Thread th = new Thread(run);
            th.start();
            threads.add(th);
        }

        // Ajout d'une sepc en database
        Database.getInstance().addSpecification(new Specification(Arrays.asList("toto", "test"), 10, 20, 30));

        // Envoi du cahier des charge au logistics runnable
        client_logistics_pipe.write(Database.getInstance().getSpecification(1));

        // Arrêt des threads
        for(Thread th : threads) {
            //th.interrupt();
            th.join();
        }

        /*
        Pipe<Specification> specPipe = new Pipe<>();

        Thread th = new Thread(new PipeRunnable(specPipe));
        th.start();

        System.out.println("En cours d'écriture");
        for(int i=0; i<10; ++i) {
            specPipe.write(new Specification(Arrays.asList("machin", "truc", "bidule"), 10, 20, 30));
        }

        Thread.sleep(1000);

        System.out.println("En cours de lecture");
        for(int i=0; i<10; ++i) {
            Specification spec = specPipe.read();
            System.out.println(spec);
        }

        th.join();
        */
    }

}

package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.runnable.*;
import univ_smb.m1.info803.utils.Pipe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {

    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException {

        // Création des threads
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new DesignRunnable()));
        threads.add(new Thread(new LogisticsRunnable()));
        threads.add(new Thread(new PlantRunnable()));
        threads.add(new Thread(new RetailerRunnable()));
        threads.add(new Thread(new SupplierRunnable()));
        threads.add(new Thread(new TransporterRunnable()));
        threads.add(new Thread(new WarehouseRunnable()));
        threads.add(new Thread(new WorkshopRunnable()));

        // Démarrage des threads
        for(Thread th : threads) {
            th.start();
        }

        Thread.sleep(1000);

        // Arrêt des threads
        for(Thread th : threads) {
            th.interrupt();
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

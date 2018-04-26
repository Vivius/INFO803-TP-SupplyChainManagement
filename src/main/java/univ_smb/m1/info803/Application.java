package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.runnable.PipeRunnable;
import univ_smb.m1.info803.utils.Transmission;

import java.io.*;
import java.util.Arrays;

public class Application {

    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
        Transmission<Specification> specTransmission = new Transmission<>();

        Thread th = new Thread(new PipeRunnable(specTransmission));
        th.start();

        System.out.println("En cours d'écriture");
        for(int i=0; i<10; ++i) {
            specTransmission.write(new Specification(Arrays.asList("machin", "truc", "bidule"), 10, 20, 30));
            Thread.sleep(200);
        }

        System.out.println("En cours de lecture");
        for(int i=0; i<10; ++i) {
            Specification spec = specTransmission.read();
            System.out.println(spec);
        }

        th.join();
    }

}

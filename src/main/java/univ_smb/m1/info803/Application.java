package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.runnable.PipeRunnable;
import univ_smb.m1.info803.utils.Pipe;

import java.io.*;
import java.util.Arrays;

public class Application {

    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
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
    }

}

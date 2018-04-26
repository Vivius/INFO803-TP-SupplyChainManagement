package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.utils.Pipe;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Arrays;

public class PipeRunnable implements Runnable {
    private Pipe<Specification> specPipe;

    public PipeRunnable(Pipe<Specification> specPipe) {
        this.specPipe = specPipe;
    }

    public void run() {
        int count = 0;

        while(true) {

            try {

                if(count < 10 ) {
                    Specification spec = specPipe.read();
                    System.out.println(spec);
                } else {
                    System.out.println("Echange des roles");

                    for(int i=0; i<10; ++i) {
                        specPipe.write(new Specification(Arrays.asList("ok", "cool"), 11, 22, 33));
                    }
                    break;
                }

                count++;

            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

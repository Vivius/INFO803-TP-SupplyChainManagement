package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.utils.Transmission;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Arrays;

public class PipeRunnable implements Runnable {
    private Transmission<Specification> specTransmission;

    public PipeRunnable(Transmission<Specification> specTransmission) {
        this.specTransmission = specTransmission;
    }

    public void run() {
        int count = 0;

        while(true) {

            try {

                if(count < 10 ) {
                    Specification spec = specTransmission.read();
                    System.out.println(spec);
                } else {
                    System.out.println("Echange des roles");

                    for(int i=0; i<10; ++i) {
                        specTransmission.write(new Specification(Arrays.asList("ok", "cool"), 11, 22, 33));
                        Thread.sleep(200);
                    }
                    break;
                }

                count++;

            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

package univ_smb.m1.info803.runnable;

public class LogisticRunnable implements Runnable {
    public void run() {
        System.out.println("Département Logisitic en fonctionnement...");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

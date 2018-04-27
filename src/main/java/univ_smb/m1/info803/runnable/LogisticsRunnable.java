package univ_smb.m1.info803.runnable;

public class LogisticsRunnable implements Runnable {

    @Override
    public void run() {
        while(true) {
            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

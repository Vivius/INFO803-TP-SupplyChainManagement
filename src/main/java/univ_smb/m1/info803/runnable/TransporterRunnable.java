package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Order;
import univ_smb.m1.info803.model.Transporter;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransporterRunnable implements Runnable {
    private String companyName;

    private Pipe<Order> logisticsToTransporterOrdersPipe;
    private Pipe<Transporter> transporterToLogisticsTransporterPipe;

    private List<Order> processedOrders;

    public TransporterRunnable(String companyName, Pipe<Order> logisticsToTransporterOrdersPipe, Pipe<Transporter> transporterToLogisticsTransporterPipe) {
        this.companyName = companyName;

        this.logisticsToTransporterOrdersPipe = logisticsToTransporterOrdersPipe;
        this.transporterToLogisticsTransporterPipe = transporterToLogisticsTransporterPipe;

        this.processedOrders = new ArrayList<>();
    }

    @Override
    public void run() {
        while(true) {

            if(Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            try {

                System.out.println(logisticsToTransporterOrdersPipe.read());

                Thread.sleep(100);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}

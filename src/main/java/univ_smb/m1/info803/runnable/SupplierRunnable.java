package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Order;
import univ_smb.m1.info803.model.Packaging;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRunnable implements Runnable {
    private String companyName;

    private Pipe<Order> logisticsToSupplierOrdersPipe;
    private Pipe<Packaging> supplierToLogisticsPackagingPipe;

    private List<Order> processedOrders;

    public SupplierRunnable(String companyName, Pipe<Order> logisticsToSupplierOrdersPipe, Pipe<Packaging> supplierToLogisticsPackagingPipe) {
        this.companyName = companyName;

        this.logisticsToSupplierOrdersPipe = logisticsToSupplierOrdersPipe;
        this.supplierToLogisticsPackagingPipe = supplierToLogisticsPackagingPipe;

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

                System.out.println(logisticsToSupplierOrdersPipe.read());

                Thread.sleep(100);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}

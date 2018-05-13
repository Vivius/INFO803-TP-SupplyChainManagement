package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Order;
import univ_smb.m1.info803.model.Packaging;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class SupplierRunnable implements Runnable {
    private final String companyName;

    private final Pipe<Order> logisticsToSupplierOrdersPipe;
    private final Pipe<Packaging> supplierToLogisticsPackagingPipe;

    private final List<Order> processedOrders;

    private final Lock mutex = new ReentrantLock(true);

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
                // Réception des commandes à analyser
                Order order = logisticsToSupplierOrdersPipe.read();

                // Si la commande n'a jamais été traitée on la prend
                if(findProcessedOrder(order.getId()) == null ) {
                    processedOrders.add(order);

                    // Création d'un packaging random...
                    int color = ThreadLocalRandom.current().nextInt(1000, 10000);
                    double weight = ThreadLocalRandom.current().nextDouble(1, 1000);
                    double size = ThreadLocalRandom.current().nextDouble(1, 100);

                    Packaging packaging = new Packaging(order, companyName, "#" + color, weight, size);

                    // Attention ! exclusion mutuelle car la pipe peut être write par plusieurs threads à ce moment là...
                    mutex.lock();
                    supplierToLogisticsPackagingPipe.write(packaging);
                    mutex.unlock();

                } else {
                    mutex.lock();
                    logisticsToSupplierOrdersPipe.write(order);
                    mutex.unlock();
                    Thread.yield();
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }

        }
    }

    private Order findProcessedOrder(int id) {
        List<Order> result = processedOrders.stream().filter(o -> o.getId() == id).collect(Collectors.toList());
        if(result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}

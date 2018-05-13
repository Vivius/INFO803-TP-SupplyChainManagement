package univ_smb.m1.info803.runnable;

import univ_smb.m1.info803.model.Order;
import univ_smb.m1.info803.model.Transporter;
import univ_smb.m1.info803.util.Pipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class TransporterRunnable implements Runnable {
    private final String companyName;

    private final Pipe<Order> logisticsToTransporterOrdersPipe;
    private final Pipe<Transporter> transporterToLogisticsTransporterPipe;

    private final List<Order> processedOrders;

    private final Lock mutex = new ReentrantLock(true);

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

                Order order = logisticsToTransporterOrdersPipe.read();

                if(get0rderById(order.getId()) == null ) {
                    processedOrders.add(order);

                    int quantity = ThreadLocalRandom.current().nextInt(10, 1000);
                    int speed = ThreadLocalRandom.current().nextInt(10, 1000);

                    Transporter transporter = new Transporter(order, companyName, quantity, speed);

                    mutex.lock();
                    transporterToLogisticsTransporterPipe.write(transporter);
                    mutex.unlock();

                } else {
                    mutex.lock();
                    logisticsToTransporterOrdersPipe.write(order);
                    mutex.unlock();
                    Thread.yield();
                }

                Thread.sleep(100);

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }

        }
    }

    private Order get0rderById(int id) {
        List<Order> result = processedOrders.stream().filter(o -> o.getId() == id).collect(Collectors.toList());
        if(result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}

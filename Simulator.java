import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Simulator extends Thread {

    private int numTellers;
    private int avgInterArrivalTime;
    private int avgServiceTime;
    private int simulationTime;
    private Tellers tellers;

    public Simulator(int numTellers, int avgInterArrivalTime, int avgServiceTime, int simulationTime) {

        this.numTellers = numTellers;
        this.avgInterArrivalTime = avgInterArrivalTime / 10;
        this.avgServiceTime = avgServiceTime / 10;
        this.simulationTime = simulationTime / 10;

        // Generate tellers for the bank
        this.tellers = getTellers();
    }

    @Override
    public void run() {

        reportInputParameters();

        // Set real world start time in the simulation environment
        Statistics.setStartTime(LocalDateTime.now());

        // Generate customers
        List<Customer> customers = generateCustomers(tellers);

        // Join customer threads with current thread so that customers still in bank can be served and global statistics are updated correctly
        joinCustomerThreads(customers);

        // Report final metrics
        reportFinalMetrics();

    }

    private Tellers getTellers() {
        return new Tellers(numTellers);
    }

    // Generate customers
    private List<Customer> generateCustomers(Tellers tellers) {

        List<Customer> customers = new ArrayList<>();

        while (Statistics.getEnvironmentTime() < simulationTime) {

            // This thread sleeps to generate new customer after some time based upon average inter arrival time
            try {
                Thread.sleep(Random_Int_Mean.random_int(avgInterArrivalTime) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If the simulation time has not ended yet, generate a new customer thread and start it
            if (Statistics.getEnvironmentTime() < simulationTime) {
                Customer customer = new Customer(tellers, avgServiceTime);
                customers.add(customer);
                customer.start();
            }
        }

        return customers;
    }

    private void joinCustomerThreads(List<Customer> customers) {

        for (Customer customer : customers) {
            try {
                // join is a blocking call. If this customer is still in bank, the main thread will wait for it to leave.
                customer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void reportInputParameters() {
        System.out.println("Mean inter-arrival time: " + avgInterArrivalTime * 10);
        System.out.println("Mean service time: " + avgServiceTime * 10);
        System.out.println("Number of tellers: " + numTellers);
        System.out.println("Length of simulation: " + simulationTime * 10);
    }

    private void reportFinalMetrics() {
        System.out.println("Simulation terminated after " + Statistics.getTotalCustomers() + " customers served");
        System.out.println("Average waiting time = " + Statistics.getAverageWaitingTime());
    }
}

public class Customer extends Thread {

    private int customerNumber = 0;
    private Tellers tellers;
    private int avgServiceTime;

    public Customer(Tellers tellers, int avgServiceTime) {
        this.tellers = tellers;
        this.avgServiceTime = avgServiceTime;
    }

    @Override
    public void run() {

        int customerArrivalTime = generateCustomer();
        int waitEndTime = waitInQueue();
        serveCustomer();
        Statistics.updateTotalWaitTime(waitEndTime - customerArrivalTime);
    }

    // Arrival of customer
    private int generateCustomer() {
        customerNumber = Statistics.getNextCustomerNumber();
        int customerArrivalTime = Statistics.getEnvironmentTime() * 10;
        System.out.println("At time " + customerArrivalTime + ", customer " + customerNumber + " arrives in line");
        return customerArrivalTime;
    }

    // Customer tries to acquire the semaphore in the Teller class
    private int waitInQueue() {

        // This call is blocking. As soon as the semaphore is acquired, customer starts being served
        tellers.getTeller();

        int waitEndTime = Statistics.getEnvironmentTime() * 10;
        System.out.println("At time " + waitEndTime + ", customer " + customerNumber + " starts being served");
        return waitEndTime;
    }

    private void serveCustomer() {

        try {
            // Service the customer based upon average service time
            Thread.sleep(Random_Int_Mean.random_int(avgServiceTime) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // After the customer is served, he releases the semaphore in Teller class and leaves the bank
        tellers.releaseTeller();

        int serviceEndTime = Statistics.getEnvironmentTime() * 10;
        System.out.println("At time " + serviceEndTime + ", customer " + customerNumber + " leaves the bank");
    }
}

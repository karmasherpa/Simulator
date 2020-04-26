import java.util.concurrent.Semaphore;

public class Tellers {

    // Tellers are implemented using Semaphore class
    private Semaphore semaphore;

    public Tellers(int numTellers) {

        // Number of permits is equal to the number of tellers and fairness setting is true in order to simulate FIFO
        this.semaphore = new Semaphore(numTellers, true);
    }

    // Get the next teller which is free.
    public void getTeller() {
        try {

            // acquire method is a blocking call to get the next teller
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // After a customer has done his job, he releases the teller for the next customer
    public void releaseTeller() {

        semaphore.release();
    }
}

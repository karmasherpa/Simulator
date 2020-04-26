import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {

    private static int nextCustomerNumber = 0;
    private static int totalWaitTime = 0;

    // This variable tracks the start of simulation so as to calculate the times in simulated environment
    private static LocalDateTime startTime;

    public static void setStartTime(LocalDateTime startTimeValue) {
        startTime = startTimeValue;
    }

    // This method though can be accessed by multiple threads concurrently, shouldn't be synchronized
    public static int getEnvironmentTime() {
        return (int) (Duration.between(startTime, LocalDateTime.now()).toMillis()) / 1000;
    }

    // This method is synchronized as it can be updated by multiple threads
    public static synchronized int getNextCustomerNumber() {
        nextCustomerNumber++;
        return nextCustomerNumber;
    }

    public static int getTotalCustomers() {
        return nextCustomerNumber;
    }

    // This method is synchronized as it can be updated by multiple threads
    public static synchronized void updateTotalWaitTime(int waitTime) {
        totalWaitTime += waitTime;
    }

    public static String getAverageWaitingTime() {
        return String.format("%.2f", (1.0 * totalWaitTime) / nextCustomerNumber);
    }
}

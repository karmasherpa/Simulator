public class Assignment2 {

    public static void main(String[] args) {

        // Reading the command line variables
        int numTellers = Integer.valueOf(args[0]);
        int avgInterArrivalTime = Integer.valueOf(args[1]);
        int avgServiceTime = Integer.valueOf(args[2]);
        int simulationTime = Integer.valueOf(args[3]);

        // Simulation thread is the main thread which generates customers and simulates the entire scenario
        Thread simulationThread = new Thread(new Simulator(numTellers, avgInterArrivalTime, avgServiceTime, simulationTime));
        simulationThread.start();
    }
}

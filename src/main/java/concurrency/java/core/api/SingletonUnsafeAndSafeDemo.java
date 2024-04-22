package concurrency.java.core.api;


/*
 * Simulates, with singleton you can create multiple instances 
 */
class UnsafeSingleton {
    //https://stackoverflow.com/questions/11072262/singleton-multithreading-in-java
    /*
    The java.lang.Runtime.availableProcessors() method returns the number of processors available to the Java virtual machine.
    This value may change during a particular invocation of the virtual machine.
     */
    private static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
    private static final Thread[] THREADS = new Thread[PROCESSOR_COUNT];
    private static int instancesCount = 0;

    private static UnsafeSingleton instance = null;

    /**
     * private constructor to prevent Creation of Object from Outside of the
     * This class.
     */
    private UnsafeSingleton() {
    }

    /**
     * return the instance only if it does not exist
     */
    public static UnsafeSingleton getInstance() {
        if (instance == null) {
            instancesCount++;
            instance = new UnsafeSingleton();
        }
        return instance;
    }


    /**
     * reset instancesCount and instance.
     */
    private static void reset() {
        instancesCount = 0;
        instance = null;
    }

    /**
     * validate system to run the test
     */
    private static void validate() {
        if (UnsafeSingleton.PROCESSOR_COUNT < 2) {
            System.out.print("PROCESSOR_COUNT Must be >= 2 to Run the test.");
            System.exit(0);
        }
    }

    public static void main(String... args) {
        validate();
        System.out.printf("Summary :: PROCESSOR_COUNT %s, Running Test with %s of Threads. %n", PROCESSOR_COUNT, PROCESSOR_COUNT);

        long currentMili = System.currentTimeMillis();
        int testCount = 0;
        do {
            reset();

            for (int i = 0; i < PROCESSOR_COUNT; i++)
                THREADS[i] = new Thread(UnsafeSingleton::getInstance);

            for (int i = 0; i < PROCESSOR_COUNT; i++)
                THREADS[i].start();

            for (int i = 0; i < PROCESSOR_COUNT; i++)
                try {
                    THREADS[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            testCount++;
        }
        while (instancesCount <= 1 && testCount < Integer.MAX_VALUE);

        System.out.printf("Singleton Pattern is broken after %d try. %nNumber of instances count is %d. %nTest duration %dms", testCount, instancesCount, System.currentTimeMillis() - currentMili);
    }


}


class SafeSingleton {

    private static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
    private static final Thread[] THREADS = new Thread[PROCESSOR_COUNT];

    public static void main(String... args) {
    	ThreadSafeSingletonFirstSolution instanceMain = ThreadSafeSingletonFirstSolution.getInstance();

        final int[] instancesCount = {1};
        System.out.printf("Summary :: PROCESSOR_COUNT %s, Running Test with %s of Threads. %n", PROCESSOR_COUNT, PROCESSOR_COUNT);

        long currentMili = System.currentTimeMillis();
        int testCount = 0;
        do {
            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                THREADS[i] = new Thread() {
                    public void run() {
                    	ThreadSafeSingletonFirstSolution instance = ThreadSafeSingletonFirstSolution.getInstance();
                        if(!instanceMain.equals(instance)) {
                            instancesCount[0]++;

                        }
                    }
                };
            }

            for (int i = 0; i < PROCESSOR_COUNT; i++)
                THREADS[i].start();

            for (int i = 0; i < PROCESSOR_COUNT; i++)
                try {
                    THREADS[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            testCount++;
        }
        while (testCount < 1000);

        System.out.printf("Singleton Pattern is broken after %d try. %nNumber of instances count is %d. %nTest duration %dms", testCount, instancesCount[0], System.currentTimeMillis() - currentMili);
    }
}

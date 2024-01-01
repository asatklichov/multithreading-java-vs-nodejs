//package concurrency.java.concurrent.api;
//
//import java.util.Arrays;
//import java.util.Random;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.RecursiveAction;
//
///**
// * Java 7 introduced a new parallel mechanism for compute intensive tasks, the
// * fork-join framework.
// *  
// *
// */
//public class ForkAndJoinDemo {
//	public static void main(String[] args) {
//        Problem test = new Problem();
//        // check the number of available processors
//        int nThreads = Runtime.getRuntime().availableProcessors();
//        System.out.println(nThreads);
//        Solver mfj = new Solver(test.getList());
//        ForkJoinExecutor pool = new ForkJoinPool(nThreads);
//        pool.invoke(mfj);
//        long result = mfj.getResult();
//        System.out.println("Done. Result: " + result);
//        long sum = 0;
//        // check if the result was ok
//        for (int i = 0; i < test.getList().length; i++) {
//            sum += test.getList()[i];
//        }
//        System.out.println("Done. Result: " + sum);
//    }
//}
//
//class Solver extends RecursiveAction {
//    private int[] list;
//    public long result;
//
//    public Solver(int[] array) {
//        this.list = array;
//    }
//
//    @Override
//    protected void compute() {
//        if (list.length == 1) {
//            result = list[0];
//        } else {
//            int midpoint = list.length / 2;
//            int[] l1 = Arrays.copyOfRange(list, 0, midpoint);
//            int[] l2 = Arrays.copyOfRange(list, midpoint, list.length);
//            Solver s1 = new Solver(l1);
//            Solver s2 = new Solver(l2);
//            forkJoin(s1, s2);
//            result = s1.result + s2.result;
//        }
//    }
//}
//
//class Problem {
//    private final int[] list = new int[2000000];
//
//    public Problem() {
//        Random generator = new Random(19580427);
//        for (int i = 0; i < list.length; i++) {
//            list[i] = generator.nextInt(500000);
//        }
//    }
//
//    public int[] getList() {
//        return list;
//    }
//
//}
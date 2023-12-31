package concurrency.java.concurrent.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import sun.misc.Unsafe;

public class AtomicCounter {

	private static class MyAtomicCounter extends AtomicInteger {
		
		private static Unsafe unsafe = null;
		
		static {
			Field unsafeField;
			try {
				unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
				unsafeField.setAccessible(true);
				unsafe = (Unsafe) unsafeField.get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private AtomicInteger countIncrement = new AtomicInteger(0);
		
		public MyAtomicCounter(int counter) {
			super(counter);
		}
	
		public int myIncrementAndGet() {

			long valueOffset = 0L;
			try {
				valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			int v;
	        do {
	            v = unsafe.getIntVolatile(this, valueOffset);
	            countIncrement.incrementAndGet();
	        } while (!unsafe.compareAndSwapInt(this, valueOffset, v, v + 1));
	        
	        return v;
		}
		
		public int getIncrements() {
			return this.countIncrement.get();
		}
	}
	
	private static MyAtomicCounter counter = new MyAtomicCounter(0);
	
	public static void main(String[] args) {

		class Incrementer implements Runnable {
			
			public void run() {
				for (int i = 0 ; i < 1_000 ; i++) {
					counter.myIncrementAndGet();
				}
			}
		}
		
		class Decrementer implements Runnable {
			
			public void run() {
				for (int i = 0 ; i < 1_000 ; i++) {
					counter.decrementAndGet();
				}
			}
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		List<Future<?>> futures = new ArrayList<>();
		
		try {
				
			for (int i = 0 ; i < 4 ; i++) {
				futures.add(executorService.submit(new Incrementer()));
			}
			for (int i = 0 ; i < 4 ; i++) {
				futures.add(executorService.submit(new Decrementer()));
			}
			
			futures.forEach(
				future -> {
					try {
						future.get();
					} catch (InterruptedException | ExecutionException e) {
						System.out.println(e.getMessage());
					}
				}
			);
			
			System.out.println("counter = " + counter);
			System.out.println("# increments = " + counter.getIncrements());
			
		} finally {
			executorService.shutdown();
		}
	}
}

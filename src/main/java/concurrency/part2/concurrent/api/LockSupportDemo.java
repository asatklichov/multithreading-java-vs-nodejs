package concurrency.part2.concurrent.api;
/*
package concurrency.java.concurrent.api;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import org.junit.jupiter.api.Test;

public class LockSupportDemo {
	*/
/**
	 * * LockSupport Is a kind of lock mechanism. LockSupport provides an
	 * alternative for some of Thread's deprecated methods: suspend() and resume().
	 * It uses a concept of permit and parking to detect if given thread should
	 * block or not. park() to block the thread waiting for a permit,
	 * unpark(thread) to add a permit to the thread
	 *
	 *//*


	@Test
	public void should_unblock_parked_thread() throws InterruptedException {
		List<Integer> iteratedNumbers = new ArrayList<>();
		Thread thread1 = new Thread(() -> {
			int i = 0;
			// park() blocks thread invoking this method
			LockSupport.park();
			while (true) {
				try {
					Thread.sleep(1_000L);
					iteratedNumbers.add(i);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		});
		thread1.start();

		Thread thread2 = new Thread(() -> {
			try {
				Thread.sleep(2_600L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
			// unpark(Thread) releases thread specified
			// in the parameter
			LockSupport.unpark(thread1);
		});
		thread2.start();

		Thread.sleep(5_000L);
		thread1.interrupt();

		assertEquals(2, iteratedNumbers);
		// assertThat(iteratedNumbers).hasSize(2);

		// Only 2 numbers are expected:
		// * thread1 blocks before starting the iteration
		// * thread2 wakes up after ~3 seconds and releases blocked thread1
		// * from 5 seconds allocated to execution, thread1 has only 2
		// seconds to execute and since the sleep between iterations
		// is 1 second, it should make only 2 iterations

		// assertThat(iteratedNumbers).containsOnly(0, 1);
		assertEquals(true, iteratedNumbers.contains(0));
		assertEquals(true, iteratedNumbers.contains(1));
	}

	@Test
	public void should_block_thread_with_deadline() throws InterruptedException {
		List<Integer> iteratedNumbers = new ArrayList<>();
		Thread thread1 = new Thread(() -> {
			int i = 0;
			// park() blocks thread invoking this method
			long lockReleaseTimestamp = System.currentTimeMillis() + 2_600L;
			LockSupport.parkUntil(lockReleaseTimestamp);
			while (true) {
				try {
					Thread.sleep(1_000L);
					iteratedNumbers.add(i);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		});
		thread1.start();

		Thread.sleep(5_000L);
		thread1.interrupt();

		// assertThat(iteratedNumbers).hasSize(2);
		assertEquals(2, iteratedNumbers);

		// Only 2 numbers are expected because lock is held during ~3 seconds:
		// thread1 blocks before starting the iteration during ~3 seconds
		// from 5 seconds allocated to execution, thread1 has only 2 seconds
		// of execution time and since the sleep between iterations is 1 second,
		// it should make only 2 iterations

		// assertThat(iteratedNumbers).containsOnly(0, 1);
		assertEquals(true, iteratedNumbers.contains(0));
		assertEquals(true, iteratedNumbers.contains(1));
	}

	@Test
	public void should_prove_that_blocker_is_not_an_exclusive_lock() throws InterruptedException {
		Object lock = new Object();
		boolean[] blocks = new boolean[2];
		Thread thread1 = new Thread(() -> {
			LockSupport.park(lock);
			blocks[0] = true;
		});
		thread1.start();

		Thread thread2 = new Thread(() -> {
			LockSupport.park(lock);
			blocks[1] = true;
		});
		thread2.start();

		Thread.sleep(2_000L);

		// Both threads stopped with the same blocker object (Object lock)
		// It shows that blocker can't work as an exclusive lock mechanism
		Object blockerThread1 = LockSupport.getBlocker(thread1);
		Object blockerThread2 = LockSupport.getBlocker(thread2);

		assertEquals(blockerThread1, lock);
		assertEquals(blockerThread2, lock);
		assertFalse(blocks[0]);
		assertFalse(blocks[1]);
		// assertThat(blocks[1]).isFalse();
	}

	@Test
	public void should_implement_locking_mechanism_with_blocker() throws InterruptedException {
		Object lock = new Object();
		Thread thread1 = new Thread(() -> {
			LockSupport.parkUntil(lock, System.currentTimeMillis() + 3_000L);
		});
		thread1.start();

		long timeBeforeLockAcquire = System.currentTimeMillis();

		// Give some guarantee to thread1 to acquire lock
		Thread.sleep(10L);

		// Try to lock current thread as long as
		// thread1 doesn't release its lock - let's suppose
		// that thread1 is making some job needed by current thread
		while (LockSupport.getBlocker(thread1) != null) {
		}
		LockSupport.parkUntil(lock, System.currentTimeMillis() + 1_000L);
		long timeAfterLockRelease = System.currentTimeMillis();
		long duration = timeAfterLockRelease - timeBeforeLockAcquire;

		// Duration should be ~4 seconds because of 3 seconds of lock
		// acquired by thread1 and 1-second blocking of current thread
		// assertThat(duration).isEqualTo(4_000L);
		assertEquals(duration, 4_000L);
	}

}
*/

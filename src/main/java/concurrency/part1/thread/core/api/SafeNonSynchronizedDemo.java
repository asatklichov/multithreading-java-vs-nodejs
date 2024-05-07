package concurrency.part1.thread.core.api;

public
/**
 * * If there is not STATE exchange between threads then it is safe to run
 * methods.
 * 
 * 
 * https://stackoverflow.com/questions/19674093/different-threads-calling-the-method-at-the-same-time
 *
 */
class SafeNonSynchronizedDemo {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runnable t1 = new Runnable() {
			public void run() {
				add(1, 2);
			}
		};

		Runnable t2 = new Runnable() {
			public void run() {
				add(3, 4);

			}
		};

		Runnable t3 = () -> add(36, -4);
		Runnable t4 = () -> add(-3, 47);

		new Thread(t1).start();
		new Thread(t2).start();
		new Thread(t3).start();
		new Thread(t4).start();

	}

	/**
	 * If there is not STATE exchange between threads then it is safe to run
	 * methods.
	 * 
	 * Each variable in method is called local-variables, and it is created
	 * separately (newly) for each THREAD CALL STACK
	 * 
	 * @param number
	 * @param num
	 * @return
	 */
	public static int add(int number, int num) {
		// when different thread call method
		// Runnable t1 call ,then "number" will be assigned 1, "num" will be assigned 2
		// number ,num will keep in thread'stack stack
		int sum = number + num;
		System.out.println(sum);
		return sum;
	}

}

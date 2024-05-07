package concurrency.part1.thread.core.api;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class ObjectSizeCalc {
	// https://www.baeldung.com/java-size-of-object#:~:text=References%20have%20a%20typical%20size,%2D50%25%20more%20heap%20space.

	public static void main(String[] arguments) {
		String emptyString = "";
		String string = "Estimating Object Size Using Instrumentation";
		String[] stringArray = { emptyString, string, "com.baeldung" };
		String[] anotherStringArray = new String[100];
		List<String> stringList = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder(100);
		int maxIntPrimitive = Integer.MAX_VALUE;
		int minIntPrimitive = Integer.MIN_VALUE;
		Integer maxInteger = Integer.MAX_VALUE;
		Integer minInteger = Integer.MIN_VALUE;
		long zeroLong = 0L;
		double zeroDouble = 0.0;
		boolean falseBoolean = false;
		Object object = new Object();

		class EmptyClass {
		}
		EmptyClass emptyClass = new EmptyClass();

		class StringClass {
			public String s;
		}
		StringClass stringClass = new StringClass();

		printObjectSize(emptyString);
		printObjectSize(string);
		printObjectSize(stringArray);
		printObjectSize(anotherStringArray);
		printObjectSize(stringList);
		printObjectSize(stringBuilder);
		printObjectSize(maxIntPrimitive);
		printObjectSize(minIntPrimitive);
		printObjectSize(maxInteger);
		printObjectSize(minInteger);
		printObjectSize(zeroLong);
		printObjectSize(zeroDouble);
		printObjectSize(falseBoolean);
		printObjectSize(Day.TUESDAY);
		printObjectSize(object);
		printObjectSize(emptyClass);
		printObjectSize(stringClass);
	}

	public enum Day {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}

	public static void printObjectSize(Object object) {
		System.out.println("Object type: " + object.getClass() + ", size: " + InstrumentationAgent.getObjectSize(object)
				+ " bytes");
	}

}

class InstrumentationAgent {
	private static volatile Instrumentation globalInstrumentation;

	public static void premain(final String agentArgs, final Instrumentation inst) {
		globalInstrumentation = inst;
	}

	public static long getObjectSize(final Object object) {
		if (globalInstrumentation == null) {
			throw new IllegalStateException("Agent not initialized.");
		}
		return globalInstrumentation.getObjectSize(object);
	}
}

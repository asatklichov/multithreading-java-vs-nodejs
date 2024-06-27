package concurrency.virtual.threads.api;

class Collar {
}

class Dog {
	Collar c; // instance variable on HEAP
	String name; // instance variable on HEAP

	// int y=10; //on HEAP

	public static void main(String[] args) { // main() placed on the STACK
		System.out.println("Where the storage lives");

		Dog d; // local variable: d (on STACK)
		d = new Dog(); // but Dog object is created on HEAP
		d.go(d); // copy of ref-var is passed

		// int z=10; //on STACK
	}

	void go(Dog dog) { // go() is placed on STACK with dog parameter as a local variable (dog is
						// created on STACK)
		c = new Collar(); // created on heap and assigned to c
		dog.setName("Aiko");
	}

	void setName(String dogName) { // setName() is placed on STACK with dogName parameter as a local variable
									// (dogName is created on STACK)
		name = dogName; // instance var. name is referencing to String Object which is referenced by
						// dogName
		// ...
	}
	// once method call end, it is removed from the stack, and all local variables
	// (e.g. dogName) is removed as well. But "name" variable will be on the HEAP
	// Just
	// as the local variable starts its life inside the method, it's also destroyed
	// when the method has completed.
	// the variable itself lives only within the scope of the method
}

class Memory {

	public static void main(String[] args) { // Line 1
		int i = 1; // Line 2
		Object obj = new Object(); // Line 3
		Memory mem = new Memory(); // Line 4
		mem.foo(obj); // Line 5
	} // Line 9

	private void foo(Object param) { // Line 6
		String str = param.toString(); //// Line 7
		System.out.println(str);
	} // Line 8

}
package concurrency.part1.thread.core.api;

import java.io.*;

public class SingletonSerializable implements Serializable {

    private static SingletonSerializable INSTANCE;
    //state is a string variable that holds the state of the class
    private String state = "State Zero";

    private SingletonSerializable() {
    }

    public static SingletonSerializable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonSerializable();
        }

        return INSTANCE;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    /**
     * The problem is, after instantiating a singleton class that implements Serializable,
     * and then serializing and deserializing the instance, we’ll end up with two instances of
     * the singleton class, which violates singleton-ness:
     *
     * @param args
     */
    public static void main(String[] args) {
        SingletonSerializable s1 = SingletonSerializable.getInstance();
        s1.setState("State One");

        try (
                FileOutputStream fos = new FileOutputStream("singleton_test.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                FileInputStream fis = new FileInputStream("singleton_test.txt");
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            // Serializing.
            oos.writeObject(s1);

            // Deserializing.
            SingletonSerializable s2 = (SingletonSerializable) ois.readObject();

            // Checking if the state is preserved.
            System.out.println(s1.getState().equals(s2.getState())); //true
            // Checking if s1 and s2 are not the same instance.
            System.out.println(s1 == s2); //false, different instances - ISSUE 
            System.out.println(s1 == s1); //false, different instances
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }

    }
}

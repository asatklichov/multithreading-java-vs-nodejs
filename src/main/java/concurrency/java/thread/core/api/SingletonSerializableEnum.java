package concurrency.java.thread.core.api;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public enum SingletonSerializableEnum {
    INSTANCE("State Zero");

    private String state;

    private SingletonSerializableEnum(String state) {
        this.state = state;
    }

    public SingletonSerializableEnum getInstance() {
        return INSTANCE;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static void main(String[] args) {
        SingletonSerializableEnum s1 = SingletonSerializableEnum.INSTANCE;
        s1.setState("State One");

        try (
                FileOutputStream fos = new FileOutputStream("singleton_enum_test.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                FileInputStream fis = new FileInputStream("singleton_enum_test.txt");
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            // Serializing.
            oos.writeObject(s1);

            // Deserializing.
            SingletonSerializableEnum s2 = (SingletonSerializableEnum) ois.readObject();

            // Checking if the state is preserved.
            System.out.println(s1.getState().equals(s2.getState())); //true
            // Checking if s1 and s2 are not the same instance.
            System.out.println(s1 == s2); //TRUE same instances - only single instance 
            System.out.println(s1 == s1); //TRUE same  instances
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }

    }
}

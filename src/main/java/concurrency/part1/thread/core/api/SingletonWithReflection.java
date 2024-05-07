package concurrency.part1.thread.core.api;

import java.lang.reflect.Constructor;

public class SingletonWithReflection {

    // public instance initialized when loading the class
    public static SingletonWithReflection instance = new SingletonWithReflection();

    // private constructor
    private SingletonWithReflection() {

    }


    public static void main(String[] args) {
        System.out.println("Reflection with classic singleton");

        /**
         * After running this class, you will see that hashCodes are different which means,
         * 2 objects of the same class are created and the singleton pattern has been destroyed.
         * <p>
         * instance1.hashCode():- 1163157884
         * <p>
         * instance2.hashCode():- 1956725890
         */
        SingletonWithReflection instance1 = SingletonWithReflection.instance;
        SingletonWithReflection instance2 = null;
        try {
            Constructor[] constructors
                    = SingletonWithReflection.class.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                // Below code will destroy the singleton
                // pattern
                constructor.setAccessible(true);
                instance2
                        = (SingletonWithReflection) constructor.newInstance();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("instance1.hashCode():- "
                + instance1.hashCode());
        System.out.println("instance2.hashCode():- "
                + instance2.hashCode());


        System.out.println();
        System.out.println("\n Reflection with ENUM");
        /**
         *
         * Overcome reflection issue: To overcome issues raised by reflection, enums are used because
         * java ensures internally that the enum value is instantiated only once.
         * Since java Enums are globally accessible,
         * they can be used for singletons. Its only drawback is that it is not flexible i.e it does not allow lazy initialization.
         */
        SingletonEnumWithReflection singletonEnum1 = SingletonEnumWithReflection.INSTANCE;
        SingletonEnumWithReflection singletonEnum2 = null;
        try {
            Constructor[] constructors
                    = SingletonEnumWithReflection.class.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                // Below code will destroy the singleton
                // pattern
                constructor.setAccessible(true);
                singletonEnum2
                        = (SingletonEnumWithReflection) constructor.newInstance();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("SingletonEnum1.hashCode():- "
                + singletonEnum1.hashCode());
        System.out.println("SingletonEnum2.hashCode():- "
                + singletonEnum2.hashCode());

    }
}


enum SingletonEnumWithReflection {
    INSTANCE;
}

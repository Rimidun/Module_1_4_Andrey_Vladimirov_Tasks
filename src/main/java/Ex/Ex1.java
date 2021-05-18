package Ex;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Ex1 {

    public static void main(String[] args) {


        Foo foo = Foo.getLimInstance();


        //Foo foo = new Foo();
        List<Integer> sourceList = Arrays.asList(1, 2, 3);
        Map<Integer, Runnable> map = new HashMap<>(3);
        map.put(1, () -> foo.first(() -> System.out.print("first")));
        map.put(2, () -> foo.second(() -> System.out.print("second")));
        map.put(3, () -> foo.third(() -> System.out.print("third")));


//        Foo f = Foo.getLimInstance();
//        map.put(1, () -> f.first(() -> System.out.print("один")));
//        map.put(2, () -> f.second(() -> System.out.print("два")));
//        map.put(3, () -> f.third(() -> System.out.print("три")));


        List<Thread> threadList = new ArrayList<>(3);
        for (int i = 0; i < sourceList.size(); i++) {
            Thread thread = new Thread(map.get(sourceList.get(i)), i == 0 ? "a" : (i == 1 ? "b" : "c"));
            thread.start();
            threadList.add(thread);
        }

//        threadList.forEach(thread -> {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
    }
}

class Foo {

    private AtomicInteger firstJobDone = new AtomicInteger(0);
    private AtomicInteger secondJobDone = new AtomicInteger(0);
    public static int objCount = 0;

    private Foo() {
        objCount++;
    }

    public void first(Runnable printFirst) {
        // выводит "first"
        printFirst.run();
        // фиксируем первый поток как выполненный + увеличив счетчик
        firstJobDone.incrementAndGet();
    }

    public void second(Runnable printSecond) {
        while (firstJobDone.get() != 1) {
            // ожидаем выполнения первого потока
        }
        // выводит "second"
        printSecond.run();
        // фиксируем второй поток как выполненный + увеличив счетчик
        secondJobDone.incrementAndGet();
    }

    public void third(Runnable printThird) {
        while (secondJobDone.get() != 1) {
            // ожидаем выполнения второго потока
        }
        // выводит "third"
        printThird.run();
    }


    public static synchronized Foo getLimInstance() {
        if (objCount < 1) {
            return new Foo();
        }
        System.out.println("Лимит экземпляров достигнут");
        System.gc();
        return null;
    }

    @Override
    public void finalize() {
        System.out.println("экзмпляр удален");
        objCount--;
    }


}



package Ex;

import java.util.concurrent.atomic.AtomicInteger;

class Main {

    public void first(Runnable printFirst) throws InterruptedException {
        throw new RuntimeException();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        throw new RuntimeException();
    }

    public void third(Runnable printThird) throws InterruptedException {
        throw new RuntimeException();
    }


    private static class Foo extends Main {

        private final AtomicInteger atomicInteger = new AtomicInteger(0);

        public Foo() {

        }

        public synchronized void first(Runnable printFirst) throws InterruptedException {

            // outputs "first"
            printFirst.run();
            atomicInteger.getAndIncrement();
            notifyAll();
        }

        public synchronized void second(Runnable printSecond) throws InterruptedException {

            // outputs "second"
            while (atomicInteger.get() != 1) {
                wait();
            }
            printSecond.run();
            atomicInteger.getAndIncrement();
            notifyAll();
        }

        public synchronized void third(Runnable printThird) throws InterruptedException {

            //outputs "third"
            while (atomicInteger.get() != 2) {
                wait();
            }
            printThird.run();
        }

    }


    private static Thread firstThread(Main printInOrder) {
        return new Thread(() -> {
            try {
                printInOrder.first(() -> System.out.print("first"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static Thread secondThread(Main printInOrder) {
        return new Thread(() -> {
            try {
                printInOrder.second(() -> System.out.print("second"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static Thread thirdThread(Main printInOrder) {
        return new Thread(() -> {
            try {
                printInOrder.third(() -> System.out.print("third"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {

        testAtomicIntegerFoo1();

    }

    private static void testAtomicIntegerFoo1() throws InterruptedException {
        Foo atomicIntegerFoo = new Foo();
        firstThread(atomicIntegerFoo).start();
        Thread.sleep(1000);
        secondThread(atomicIntegerFoo).start();
        Thread.sleep(1000);
        thirdThread(atomicIntegerFoo).start();
    }

}
package Main;

import java.util.concurrent.CountDownLatch;

public class Ex3 {


    public static class FooCountDownLatch {

        final CountDownLatch firstLatch = new CountDownLatch(1);

        final CountDownLatch secondLatch = new CountDownLatch(1);

        public FooCountDownLatch() {

        }

        public static class PrintFirst implements Runnable {
            @Override
            public void run() {
                System.out.printf("First");
            }
        }

        public static class PrintSecond implements Runnable {
            @Override
            public void run() {
                System.out.printf("Second");
            }
        }

        public static class PrintThird implements Runnable {
            @Override
            public void run() {
                System.out.printf("Third");
            }
        }

        public void first(Runnable printFirst) throws InterruptedException {
            printFirst.run();
            firstLatch.countDown();
        }

        public void second(Runnable printSecond) throws InterruptedException {
            firstLatch.await();
            printSecond.run();
            secondLatch.countDown();
        }

        public void third(Runnable printThird) throws InterruptedException {
            secondLatch.await();
            printThird.run();
        }

        public static void main(String[] args) {
            final FooCountDownLatch foo = new FooCountDownLatch();
            final PrintFirst printFirst = new PrintFirst();
            final PrintSecond printSecond = new PrintSecond();
            final PrintThird printThird = new PrintThird();

            Thread t1 = new Thread() {
                @Override
                public void run() {
                    try {
                        foo.first(printFirst);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            Thread t2 = new Thread() {
                @Override
                public void run() {
                    try {
                        foo.second(printSecond);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };

            Thread t3 = new Thread() {
                @Override
                public void run() {
                    try {
                        foo.third(printThird);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };

            t2.start();
            t3.start();
            t1.start();

        }
    }
}

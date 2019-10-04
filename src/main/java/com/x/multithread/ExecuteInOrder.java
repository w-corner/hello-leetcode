package com.x.multithread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecuteInOrder {

    public static void main(String[] args) {
        ExecuteInOrder executeInOrder = new ExecuteInOrder();

        executeInOrder.useJoin();

        executeInOrder.useSingleThreadPool();
    }

    private void useJoin() {
        Thread t1 = new Thread(new Task("1st"));
        Thread t2 = new Thread(new Task("2nd", t1));
        Thread t3 = new Thread(new Task("3rd", t2));

        t3.start();
        t2.start();
        t1.start();
    }

    private void useSingleThreadPool() {
        printStar();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Task("111"));
        executorService.submit(new Task("222"));
        executorService.submit(new Task("333"));
        executorService.shutdown();
    }

    private static void printStar() {
        System.out.println("********************************");
    }

    private static class Task implements Runnable {

        private String str;
        private Thread t;

        Task(String str) {
            this.str = str;
        }

        Task(String str, Thread t) {
            this.str = str;
            this.t = t;
        }

        @Override
        public void run() {
            if (t != null) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(str);
        }
    }
}

package com.jantony.concurrency;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.IntStream;

public class Timer {
    private BlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>();
    private Object lock = new Object();
    private ArrayList<Thread> workerThreads = new ArrayList<>();

    public Timer(int parallel) {
        IntStream.range(0, parallel).forEach(x ->
                {
                    Thread th = new WorkerThread();
                    workerThreads.add(th);
                    th.start();

                }
        );
    }

    public void schedule(Runnable runnable, long delay) {
        long timeToRun = System.currentTimeMillis() + delay;
        taskQueue.add(new Task(timeToRun, runnable));
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void shutDown() {
        for (Thread workerThread : workerThreads) {
            workerThread.interrupt();
        }
    }

    private static class Task implements Comparable<Task> {
        private long timeToRun;
        private Runnable runnable;

        Task(long timeToRun, Runnable task) {
            this.timeToRun = timeToRun;
            this.runnable = task;
        }

        @Override
        public int compareTo(Task that) {
            return Long.compare(this.timeToRun, that.timeToRun);
        }
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            try {
                while (!interrupted()) {
                    Task timerTask = taskQueue.take();
                    synchronized (lock) {
                        if (System.currentTimeMillis() < timerTask.timeToRun)
                            lock.wait(timerTask.timeToRun - System.currentTimeMillis());
                    }
                    if (System.currentTimeMillis() < timerTask.timeToRun)
                        taskQueue.add(timerTask);
                    else
                        timerTask.runnable.run();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer(5);
        IntStream.range(0, 200).forEach(x -> timer.schedule(() -> System.out.println(x), x));
        Thread.sleep(10000);
        timer.shutDown();
    }
}

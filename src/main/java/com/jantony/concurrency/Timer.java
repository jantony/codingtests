package com.jantony.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.IntStream;

public class Timer {
    private BlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>();
    private Object lock = new Object();

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
            while (true) {
                try {
                    Task timerTask = taskQueue.take();
                    synchronized (lock) {
                        if (System.currentTimeMillis() < timerTask.timeToRun)
                            lock.wait();
                    }
                    if (System.currentTimeMillis() < timerTask.timeToRun)
                        taskQueue.add(timerTask);
                    else
                        timerTask.runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public Timer(int parallel) {
        IntStream.range(0, parallel).forEach(x ->
                new WorkerThread().start()
        );
    }

    public void schedule(Runnable runnable, long delay) {
        //input validation
        long timeToRun = System.currentTimeMillis() + delay;
        taskQueue.add(new Task(timeToRun, runnable));

    }
}

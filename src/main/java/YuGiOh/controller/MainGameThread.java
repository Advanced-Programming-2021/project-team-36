package YuGiOh.controller;

import YuGiOh.controller.events.GameExceptionEvent;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.shape.Circle;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class MainGameThread extends Thread {
    @Getter
    private static MainGameThread instance;

    Queue<Runnable> tasks = new LinkedList<>();

    private boolean runQueuedTasksMode = false;

    public MainGameThread(Runnable runnable){
        super(runnable, "duel service thread");
        setDaemon(true);
        instance = this;
    }

    public synchronized void addRunnable(Runnable runnable){
        tasks.add(runnable);
    }

    public void stopRunningQueuedTasks(){
        runQueuedTasksMode = false;
    }

    public void runQueuedTasks(){
        runQueuedTasksMode = true;
        while (runQueuedTasksMode) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
            if (!tasks.isEmpty()) {
                Runnable r = tasks.poll();
                try {
                    r.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class OneWayTicket {
        private final CountDownLatch latch = new CountDownLatch(1);
        private final boolean isMainThread;
        public OneWayTicket() {
            isMainThread = Thread.currentThread() instanceof MainGameThread;
        }
        public void free() {
            if(isMainThread)
                latch.countDown();
        }
        public void runOneWay(Runnable runnable) {
            if(isMainThread) {
                runnable.run();
                try { latch.await(); } catch (InterruptedException ignored) {}
            }
        }
    }

    public static class TwoWayTicket<T> {
        private final CountDownLatch latch = new CountDownLatch(1);
        private final Callable<T> callable;
        private final boolean isMainThread;

        public TwoWayTicket(Callable<T> callable) {
            isMainThread = Thread.currentThread() instanceof MainGameThread;
            this.callable = callable;
        }
        public T runAndWait() {
            if(isMainThread) {
                AtomicReference<T> ret = new AtomicReference<>();
                Platform.runLater(() -> {
                    try {
                        ret.set(callable.call());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException ignored) {
                }
                return ret.get();
            } else {
                try {
                    return callable.call();
                } catch (Throwable e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}

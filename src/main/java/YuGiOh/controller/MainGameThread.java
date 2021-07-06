package YuGiOh.controller;

import YuGiOh.controller.events.GameExceptionEvent;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.shape.Circle;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public class MainGameThread extends Thread {
    @Getter
    private static MainGameThread instance;
    private final Object lock = new Object();

    Queue<Callable<?>> tasks = new LinkedList<>();

    private boolean runQueuedTasksMode = false;
    private int numberOfLocks = 0;

    public void lockRunningThreadIfMain(){
        numberOfLocks += 1;
        if(numberOfLocks >= 2)
            return;
        if(Thread.currentThread() instanceof MainGameThread) {
            synchronized (lock) {
                try{
                    lock.wait();
                } catch (InterruptedException e){
                }
            }
        }
    }
    public void unlockTheThreadIfMain(){
        numberOfLocks -= 1;
        if(numberOfLocks > 0)
            return;
        synchronized (lock) {
            lock.notify();
        }
    }

    public MainGameThread(Runnable runnable){
        super(new Task<Void>(){
            @Override protected Void call() {
                try {
                    runnable.run();
                } catch (Throwable e){
                    if(!(e instanceof GameExceptionEvent)) {
                        e.printStackTrace();
                    } else {
                        System.out.println("this is safe: ");
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override protected void succeeded() {
                System.out.println("task finished successfully");
            }

            @Override protected void cancelled() {
                super.cancelled();
                System.out.println("task canceled");
            }

            @Override protected void failed() {
                super.failed();
                System.out.println("task failed");
            }
        }, "duel service thread");
        setDaemon(true);
        instance = this;
    }

    public void onlyBlockRunningThreadThenDoInGui(Runnable r){
        Platform.runLater(r);
        lockRunningThreadIfMain();
    }

    public <T> T blockUnblockRunningThreadAndDoInGui(Callable<T> task){
        AtomicReference<T> ret = new AtomicReference<T>();
        Platform.runLater(()->{
            try {
                ret.set(task.call());
            } catch(Exception e){
                e.printStackTrace();
            }
            unlockTheThreadIfMain();
        });
        lockRunningThreadIfMain();
        return ret.get();
    }

    public void blockUnblockRunningThreadAndDoInGui(Runnable r){
        blockUnblockRunningThreadAndDoInGui((Callable<Void>) ()->{
            r.run();
            return null;
        });
    }

    public synchronized void addTask(Callable<?> t){
        // todo check it works or not. instead of putting in a list we just replace the last one
        // tasks.add(t);
        tasks.clear();
        tasks.add(t);
    }

    public synchronized void addRunnable(Runnable runnable){
        addTask((Callable<?>) ()->{
            runnable.run();
            return null;
        });
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
                Callable<?> r = tasks.poll();
                try {
                    r.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

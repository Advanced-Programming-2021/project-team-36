package YuGiOh.controller;

import javafx.application.Platform;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class MainGameThread extends Thread {
    @Getter
    private static MainGameThread instance;
    private final Object lock = new Object();
    Queue<Task<?>> tasks = new LinkedList<>();

    private boolean runQueuedTasksMode = false;

    public void lockRunningThreadIfMain(){
        if(Thread.currentThread() instanceof MainGameThread) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }
    public void unlockTheThreadIfMain(){
        synchronized (lock) {
            lock.notify();
        }
    }

    public MainGameThread(Runnable runnable){
        super(runnable, "duel service thread");
        setDaemon(true);
        instance = this;
    }

    public void onlyBlockRunningThreadThenDoInGui(Runnable r){
        Platform.runLater(r);
        lockRunningThreadIfMain();
    }

    public <T> T blockUnblockRunningThreadAndDoInGui(MainGameThread.Task<T> task){
        AtomicReference<T> ret = new AtomicReference<T>();
        Platform.runLater(()->{
            try {
                ret.set(task.run());
            } catch(Exception e){
                e.printStackTrace();
            }
            unlockTheThreadIfMain();
        });
        lockRunningThreadIfMain();
        return ret.get();
    }

    public void blockUnblockRunningThreadAndDoInGui(Runnable r){
        blockUnblockRunningThreadAndDoInGui((Task<Void>) ()->{
            r.run();
            return null;
        });
    }

    public synchronized void addTask(MainGameThread.Task<?> t){
        tasks.add(t);
    }

    public synchronized void addRunnable(Runnable runnable){
        addTask((MainGameThread.Task<?>) ()->{
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
            // it should be while game is not over
            if (!tasks.isEmpty()) {
                MainGameThread.Task<?> r = tasks.poll();
                r.run();
            }
        }
    }

    public interface Task<T>{
        T run();
    }
}

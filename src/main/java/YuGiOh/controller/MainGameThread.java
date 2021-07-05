package YuGiOh.controller;

import javafx.application.Platform;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class MainGameThread extends Thread {
    @Getter
    private static MainGameThread instance;
    private final Object lock = new Object();

    Queue<Task<?>> tasks = new LinkedList<>();

    private boolean runQueuedTasksMode = false;
    private int numberOfLocks = 0;

    public void lockRunningThreadIfMain(){
//        if(Thread.currentThread() instanceof MainGameThread) {
            numberOfLocks += 1;
            if(numberOfLocks >= 2)
                return;

//            synchronized (lock) {
//                try {
//                    System.out.println("LOCKED");
//                    for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
//                        System.out.println(stackTraceElement);
//                    }
//
//                    lock.wait();
//                } catch (InterruptedException e) {
//                }
//            }
//        }

        this.suspend();
    }
    public void unlockTheThreadIfMain(){
        numberOfLocks -= 1;
        if(numberOfLocks > 0)
            return;
//        synchronized (lock) {
//            System.out.println("UNLOCKED ");
//            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
//                System.out.println(stackTraceElement);
//            }
//            lock.notify();
//        }

        this.resume();
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
        // todo check it works or not. instead of putting in a list we just replace the last one
        // tasks.add(t);
        tasks.clear();
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

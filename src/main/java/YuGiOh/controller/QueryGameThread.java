package YuGiOh.controller;

import javafx.application.Platform;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class QueryGameThread extends Thread {
    @Getter
    private static QueryGameThread instance;

    Queue<Task<?>> tasks = new LinkedList<>();
    private final Object lock = new Object();

    public QueryGameThread(){
        super();
        instance = this;
    }

    public void run(){
        while (true) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            // it should be while game is not over
            if(!tasks.isEmpty()){
                Task<?> r = tasks.poll();
                r.run();
            }
        }
    }

    public synchronized void addRunnable(Runnable runnable){
        addTask((Task<?>) ()->{
            runnable.run();
            return null;
        });
    }

    public synchronized void addTask(Task<?> t){
        tasks.add(t);
    }

    private void lockRunningThread(){
        synchronized (lock){
            try { lock.wait(); } catch (InterruptedException e){}
        }
    }
    private void unlockTheThread(){
        synchronized (lock){
            lock.notify();
        }
    }

    public <T> T blockUnblockRunningThreadAndAskInGui(Task<T> task){
        AtomicReference<T> ret = new AtomicReference<T>();
        Platform.runLater(()->{
            ret.set(task.run());
            unlockTheThread();
        });
        lockRunningThread();
        return ret.get();
    }

    public interface Task<T>{
        T run();
    }
}
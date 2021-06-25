package YuGiOh.controller;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.view.gui.GuiReporter;
import YuGiOh.view.gui.event.RoundOverEvent;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class MainGameThread extends Thread {
    @Getter
    private static MainGameThread instance;
    private final Object lock = new Object();
    Queue<Task<?>> tasks = new LinkedList<>();

    private boolean runQueuedTasksMode = false;

    public void lockRunningThread(){
        synchronized (lock){
            try { lock.wait(); } catch (InterruptedException e){}
        }
    }
    public void unlockTheThread(){
        synchronized (lock){
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
        lockRunningThread();
    }

    public <T> T blockUnblockRunningThreadAndDoInGui(MainGameThread.Task<T> task){
        AtomicReference<T> ret = new AtomicReference<T>();
        Platform.runLater(()->{
            ret.set(task.run());
            unlockTheThread();
        });
        lockRunningThread();
        return ret.get();
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

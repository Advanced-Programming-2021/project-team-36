package YuGiOh.controller;

import javafx.application.Platform;
import lombok.Getter;

public class MainGameThread extends Thread {
    @Getter
    private static MainGameThread instance;
    private final Object lock = new Object();

    private void lockRunningThread(){
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
}

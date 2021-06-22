package YuGiOh.controller;

import lombok.Getter;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class MainGameThread extends Thread {
    Queue<Runnable> queuedTasks = new SynchronousQueue<>();
    private boolean isPause = false;
    private boolean isStop = false;

    @Getter
    private MainGameThread instance;

    public MainGameThread(){
        super();
        instance = this;
    }

    public void pause(){
        isPause = true;
    }
    public void play(){
        isPause = false;
    }
    public void finish(){
        isStop = true;
    }

    public void runLater(Runnable runnable){
        queuedTasks.add(runnable);
    }

    @Override
    public void run() {
        while (!isStop){
            while (!isPause){

            }
        }
    }
}

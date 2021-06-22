package YuGiOh.controller;

public class MainGameThread extends Thread {
    public MainGameThread(Runnable runnable){
        super(runnable, "duel service thread");
        setDaemon(true);
    }
}

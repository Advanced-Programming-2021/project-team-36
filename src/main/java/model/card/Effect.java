package model.card;

import controller.events.GameOver;

public interface Effect {
    void run() throws GameOver;
}

package model.card;

import controller.events.GameOverEvent;

public interface Effect {
    void run() throws GameOverEvent;
}

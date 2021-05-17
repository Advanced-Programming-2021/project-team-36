package model.card.action;

import controller.events.GameOverEvent;

public interface Effect {
    void run() throws GameOverEvent;
}

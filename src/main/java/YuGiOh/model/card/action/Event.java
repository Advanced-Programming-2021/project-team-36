package YuGiOh.model.card.action;

abstract public class Event {
    abstract public int getSpeed();
    abstract public String getActivationQuestion();
    abstract public String getDescription();
}
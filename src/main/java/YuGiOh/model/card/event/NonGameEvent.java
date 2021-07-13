package YuGiOh.model.card.event;

public class NonGameEvent extends Event {
    @Override
    public int getSpeed() {
        return 0;
    }

    // todo can we handle this better that this errors?

    @Override
    public String getActivationQuestion() {
        throw new Error("you should never see this");
    }

    @Override
    public String getDescription() {
        throw new Error("you should never see this");
    }
}

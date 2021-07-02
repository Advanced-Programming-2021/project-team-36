package YuGiOh.view.cardSelector;

public class FinishSelectingConditions {
    public static FinishSelectingCondition or(FinishSelectingCondition... conditions){
        return (cards) -> {
            for(FinishSelectingCondition condition : conditions) {
                if (condition.canFinish(cards))
                    return true;
            }
            return false;
        };
    }

    public static FinishSelectingCondition and(FinishSelectingCondition... conditions){
        return (cards) -> {
            for(FinishSelectingCondition condition : conditions) {
                if (!condition.canFinish(cards))
                    return false;
            }
            return true;
        };
    }

    public static FinishSelectingCondition getCount(int k) {
        return (cards) -> cards.size() == k;
    }
}

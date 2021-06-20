package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.Status;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;

public class Yami extends Spell {

    public Yami(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    boolean isActivated = false;

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        return ()->{
            isActivated = true;
            for(Card card : GameController.getInstance().getGame().getAllCardsOnBoard()){
                if(card instanceof Monster){
                    Monster monster = (Monster) card;
                    MonsterType type = monster.getMonsterType();
                    if(type.equals(MonsterType.FIEND) || type.equals(MonsterType.SPELLCASTER)) {
                        monster.setAttackDamage(monster.getAttackDamage() + 200);
                        monster.setDefenseRate(monster.getDefenseRate() + 200);
                    }
                    if(type.equals(MonsterType.FAIRY)){
                        // todo what if it becomes negative?
                        monster.setAttackDamage(monster.getAttackDamage() - 200);
                        monster.setDefenseRate(monster.getDefenseRate() - 200);
                    }
                }
            }
        };
    }

    @Override
    public boolean canActivateEffect() {
        return !isActivated;
    }

    @Override
    public Yami clone(){
        Yami cloned = (Yami) super.clone();
        cloned.isActivated = false;
        return cloned;
    }
}

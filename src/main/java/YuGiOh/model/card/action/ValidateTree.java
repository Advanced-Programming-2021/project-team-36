package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.enums.*;
import YuGiOh.view.cardSelector.SelectCondition;

import java.util.Optional;

public class ValidateTree {

    public static void checkFlipSummon(Card card) throws ValidateResult {
    }

    public static void checkSetMonster(Card card) throws ValidateResult {

    }

    public static void checkPhase(Phase... validPhases) throws ValidateResult {
        Game game = GameController.instance.getGame();
        for (Phase phase : validPhases)
            if (game.getPhase().equals(phase))
                return;
        throw new ValidateResult("you can't do this action it this phase");
    }

    public static void checkSpaceInZone(Player player, ZoneType zoneType) throws ValidateResult {
        if (zoneType.equals(ZoneType.MONSTER) && player.getBoard().getMonsterCardZone().size() == 5)
            throw new ValidateResult("monster zone is full");
        if (zoneType.equals(ZoneType.MAGIC) && player.getBoard().getMagicCardZone().size() == 5)
            throw new ValidateResult("magic zone is full");
    }

    public static void checkContainInZone(Player player, Card card, ZoneType zoneType) throws ValidateResult {
        if (!player.hasCardInZone(card, zoneType))
            throw new ValidateResult("the card is not in your hands");
    }

    public static void checkAlreadySummoned(Player player) throws ValidateResult {
        if (player.isSummonedInLastTurn())
            throw new ValidateResult("you already summoned or set in this turn");
    }

    public static void checkOwnerOfCard(Player player, Card card) throws ValidateResult {
        if (!card.getOwner().equals(player))
            throw new ValidateResult("this card is not yours");
    }

    public static void checkMonsterState(Monster monster, MonsterState monsterState) throws ValidateResult {
        if (monster.getMonsterState() == null || !monster.getMonsterState().equals(monsterState))
            throw new ValidateResult("monster is not faced down in field");
    }

    public static void checkNotRitual(Monster monster) throws ValidateResult {
        if (monster.getMonsterCardType().equals(MonsterCardType.RITUAL))
            throw new ValidateResult("ritual monsters can't normal summon");
    }

    public static void checkSummon(Player player, Monster monster, SummonType summonType) throws ValidateResult {
        if (summonType.equals(SummonType.NORMAL)) {
            checkPhase(Phase.MAIN_PHASE1, Phase.MAIN_PHASE2);
            checkAlreadySummoned(player);
            checkNotRitual(monster);
            checkContainInZone(player, monster, ZoneType.HAND);
            checkSpaceInZone(player, ZoneType.MONSTER);
        } else if (summonType.equals(SummonType.SPECIAL)) {
            checkPhase(Phase.MAIN_PHASE1, Phase.MAIN_PHASE2);
            checkOwnerOfCard(player, monster);
            checkNotRitual(monster);
            monster.validateSpecialSummon();
        } else if (summonType.equals(SummonType.FLIP)) {
            checkPhase(Phase.MAIN_PHASE1, Phase.MAIN_PHASE2);
            checkAlreadySummoned(player);
            checkContainInZone(player, monster, ZoneType.MONSTER);
            checkMonsterState(monster, MonsterState.DEFENSIVE_HIDDEN);
        } else {
            checkPhase(Phase.MAIN_PHASE1, Phase.MAIN_PHASE2);
            checkSpaceInZone(player, ZoneType.MONSTER);
        }
    }

    public static void checkTribute(Player player, int required, SelectCondition condition) throws ValidateResult {
        int goodCards = 0;
        for (Card card : player.getBoard().getAllCards())
            if (condition.canSelect(card))
                goodCards++;
        if (goodCards < required)
            throw new ValidateResult("there are not enough cards for tribute");
    }

    public static void checkSpecialSummon(Monster monster) throws ValidateResult {
        checkPhase(Phase.MAIN_PHASE1, Phase.MAIN_PHASE2);

    }

    public static void checkNormalSummon(Card card) throws ValidateResult {

    }

    public static void checkSetMagic(Card card) throws ValidateResult {
        if (card instanceof Monster)
            throw new ValidateResult("card is not magic");
        Player player = card.getOwner();
        Magic magic = (Magic) card;
        if (!player.hasInHand(magic))
            throw new ValidateResult("you can't set this card");
        if (!GameController.getInstance().getGame().getPhase().isMainPhase())
            throw new ValidateResult("you can't do this action in this phase");
        if (magic.getIcon().equals(Icon.FIELD))
            throw new ValidateResult("field cards can't be set");
        if (player.getBoard().getMagicCardZone().size() == 5)
            throw new ValidateResult("spell card zone is full");
    }

    public static void checkActivateMonster(Monster monster) throws ValidateResult {
        Game game = GameController.getInstance().getGame();
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new ValidateResult("you can't activate an effect on this turn");
        if (!monster.getOwner().getBoard().getMonsterCardZone().containsValue(monster) || !monster.getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
            throw new ValidateResult("only faced up monsters can activate their effect");
        if (!monster.canActivateEffect())
            throw new ValidateResult("you cannot activate this monster now");
    }

    public static void checkActivateMagic(Spell spell) throws ValidateResult {
        Player player = spell.getOwner();
        Game game = GameController.getInstance().getGame();
        if (!player.getBoard().getMagicCardZone().containsValue(spell) && !player.getBoard().getCardsOnHand().contains(spell))
            throw new ValidateResult("you can't activate this card!");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new ValidateResult("you can't activate an effect on this turn");
        if (spell.isActivated())
            throw new ValidateResult("you have already activated this card on this turn");
        if (player.hasInHand(spell) && !spell.getIcon().equals(Icon.FIELD) && player.getBoard().getMagicCardZone().size() == 5)
            throw new ValidateResult("spell card zone is full");
        if (!spell.canActivateEffect())
            throw new ValidateResult("preparations of this spell are not done yet");
        Optional<Card> optionalBadCard = GameController.getInstance().getGame().getAllCardsOnBoard().stream().filter(
                other -> {
                    if (other instanceof Magic) {
                        Magic otherMagic = (Magic) other;
                        return other.isActivated() && !otherMagic.letMagicActivate(spell);
                    }
                    return false;
                }
        ).findAny();
        if (optionalBadCard.isPresent())
            throw new ValidateResult(optionalBadCard.get().getName() + " does not let you activate this");
    }

    public static void checkAttacker(Card attacker) throws ValidateResult {
        Player player = attacker.getOwner();
        Game game = GameController.instance.getGame();
        if (attacker instanceof Magic)
            throw new ValidateResult("only a monster can attack");
        if (!player.getBoard().getMonsterCardZone().containsValue(attacker))
            throw new ValidateResult("you can’t attack with this monster");
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new ValidateResult("you can’t do this action in this phase");
        if (!((Monster) attacker).isAllowAttack())
            throw new ValidateResult("this card already attacked");
        if (!((Monster) attacker).getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
            throw new ValidateResult("monster is in defensive position");
    }

    public static void checkMonsterAttack(Card attacker, Card defender) throws ValidateResult {
        if (defender instanceof Magic)
            throw new ValidateResult("you can only attack monsters!");
        if (!defender.getOwner().getBoard().getMonsterCardZone().containsValue(defender))
            throw new ValidateResult("you can only attack monsters on field");
        checkAttacker(attacker);
    }

    public static void checkDirectAttack(Card attacker, Player defender) throws ValidateResult {
        if (defender.getBoard().getMonsterCardZone().size() > 0)
            throw new ValidateResult("you can’t attack the opponent directly");
        checkAttacker(attacker);
    }
}

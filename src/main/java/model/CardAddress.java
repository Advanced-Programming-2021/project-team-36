package model;

public class CardAddress {
    private boolean isInField;
    private boolean isInMonsterZone;
    private boolean isInSpellZone;
    private boolean isInHand;
    private boolean isOpponentAddress;
    private Integer id;

//    public CardAddress cardAddressParser(String text) {
//
//    }


    public boolean isInField() {
        return isInField;
    }

    public boolean isInHand() {
        return isInHand;
    }

    public boolean isInMonsterZone() {
        return isInMonsterZone;
    }

    public boolean isInSpellZone() {
        return isInSpellZone;
    }

    public boolean isOpponentAddress() {
        return isOpponentAddress;
    }

    public Integer getId() {
        return id;
    }
}

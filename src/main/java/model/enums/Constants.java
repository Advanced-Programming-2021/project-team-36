package model.enums;

public enum Constants {
    InitialLifePoint(8000),
    InitialMoney(1000000000),
    InitialScore(0);

    public int val;
    Constants(int val){
        this.val = val;
    }
}

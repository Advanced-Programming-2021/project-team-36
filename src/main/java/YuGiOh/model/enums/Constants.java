package YuGiOh.model.enums;

public enum Constants {
    InitialLifePoint(8000),
    InitialMoney(20000),
    InfMoney(100000000),
    InitialScore(0);

    public int val;
    Constants(int val){
        this.val = val;
    }
}

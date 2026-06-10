public class BehaviorHPBar extends BehaviorString {
    private double maxHP;

    public BehaviorHPBar(String mode, String string, double maxHP) {
        super(mode, string);
        this.maxHP = maxHP;
    }

    public double getMaxHP() {
        return maxHP;
    }
    //maxHP is immutable -> new hp bar will be created each phase
}

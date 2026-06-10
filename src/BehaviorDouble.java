public class BehaviorDouble extends Behavior {
    private double d;

    public BehaviorDouble(String mode, double d) {
        super(mode);
        this.d = d;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}

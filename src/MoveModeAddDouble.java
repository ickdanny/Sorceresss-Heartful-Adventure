public class MoveModeAddDouble extends MoveModeAdd {
    private double d;

    public MoveModeAddDouble(String mode, double d) {
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

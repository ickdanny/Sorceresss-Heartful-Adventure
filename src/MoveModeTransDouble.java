public class MoveModeTransDouble extends MoveModeTrans {
    private double d;
    public MoveModeTransDouble(String mode, Double d){
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
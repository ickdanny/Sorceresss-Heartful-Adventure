public class MoveModeAddAngleChange extends MoveModeAddVector {
    private double a, b;

    public MoveModeAddAngleChange(String mode, Vector vector, double a) {
        super(mode, vector);
        this.a = a;
    }

    public MoveModeAddAngleChange(String mode, Vector vector, double a, double b) {
        super(mode, vector);
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }
}

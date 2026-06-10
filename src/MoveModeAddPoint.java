public class MoveModeAddPoint extends MoveModeAddVector {
    private double speed;

    public MoveModeAddPoint(String mode, Vector vector, double speed) {
        super(mode, vector);
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

public class MoveModeAddHoming extends MoveModeAddAngleChange {
    private Vector target;

    public MoveModeAddHoming(String mode, Vector vector, double a, Vector target) {
        super(mode, vector, a);
        this.target = target;
    }

    public MoveModeAddHoming(String mode, Vector vector, double a, double b, Vector target) {
        super(mode, vector, a, b);
        this.target = target;
    }

    public Vector getTarget() {
        return target;
    }

    public void setTarget(Vector target) {
        this.target = target;
    }
}

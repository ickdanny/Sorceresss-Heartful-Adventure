public class BehaviorVector extends Behavior{
    private Vector v;

    public BehaviorVector(String mode, Vector v) {
        super(mode);
        this.v = v;
    }

    public Vector getV() {
        return v;
    }

    public void setV(Vector v) {
        this.v = v;
    }
}

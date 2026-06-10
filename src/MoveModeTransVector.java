public class MoveModeTransVector extends MoveModeTrans {
    private Vector vector;

    public MoveModeTransVector(String mode, Vector vector) {
        super(mode);
        this.vector = vector;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }
}

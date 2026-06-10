public class MoveModeAddVector extends MoveModeAdd {
    private Vector vector;
    public MoveModeAddVector(String mode, Vector vector){
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

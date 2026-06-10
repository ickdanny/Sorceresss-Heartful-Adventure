//the vector is in velocity, for ease of reflection
public class MoveModeAddBounce extends MoveModeAddVector {
    private int bouncesRemaining;

    public MoveModeAddBounce(String mode, Vector vector) {
        super(mode, vector);
        bouncesRemaining = 1;
    }

    public MoveModeAddBounce(String mode, Vector vector, int bouncesRemaining) {
        super(mode, vector);
        this.bouncesRemaining = bouncesRemaining;
    }

    public int getBouncesRemaining() {
        return bouncesRemaining;
    }

    public void setBouncesRemaining(int bouncesRemaining) {
        this.bouncesRemaining = bouncesRemaining;
    }
}

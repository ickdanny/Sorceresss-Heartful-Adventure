public class MoveModeAddInt extends MoveModeAdd {
    private int i;

    public MoveModeAddInt(String mode, int i) {
        super(mode);
        this.i = i;
    }

    public int getInt() {
        return i;
    }
    public void setInt(int i) {
        this.i = i;
    }
}

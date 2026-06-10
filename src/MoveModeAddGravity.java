public class MoveModeAddGravity extends MoveModeAddVector {
    //has the vector
    private double gAng;
    private double gAccel;
    //gives the deceleration due to drag (per unit of speed)
    private double dragMulti;

    public MoveModeAddGravity(String mode, Vector velocity, double gAccel, double dragMulti) {
        super(mode, velocity);
        //pointed downwards
        gAng = 0;
        this.gAccel = gAccel;
        this.dragMulti = dragMulti;
    }

    public MoveModeAddGravity(String mode, Vector velocity, double gAng, double gAccel, double dragMulti) {
        super(mode, velocity);
        this.gAng = gAng;
        this.gAccel = gAccel;
        this.dragMulti = dragMulti;
    }

    public double getGAng() {
        return gAng;
    }

    public void setGAng(double gAng) {
        this.gAng = gAng;
    }

    public double getGAccel() {
        return gAccel;
    }

    public void setGAccel(double gAccel) {
        this.gAccel = gAccel;
    }

    public double getDragMulti() {
        return dragMulti;
    }

    public void setDragMulti(double dragMulti) {
        this.dragMulti = dragMulti;
    }
}

//distance changes linearly
public class MoveModeAddOrbitVariableDist extends MoveModeAddOrbit{

    private double distChange;

    public MoveModeAddOrbitVariableDist(String mode, boolean clockwise, double speed, Vector center, double distChange) {
        super(mode, clockwise, speed, center);
        this.distChange = distChange;
    }

    public MoveModeAddOrbitVariableDist(String mode, boolean clockwise, double speed, Vector center, Vector lastPosition, double distChange) {
        super(mode, clockwise, speed, center, lastPosition);
        this.distChange = distChange;
    }

    public double getDistChange() {
        return distChange;
    }

    public void setDistChange(double distChange) {
        this.distChange = distChange;
    }
}

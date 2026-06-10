public class MoveModeAddOrbit extends MoveModeAdd {
    private boolean clockwise;
    private double speed;
    //in case the center is moving - the angle calculations would be completely wrong
    //in format distanceFromCenter, angleFROMCenter
    private Vector lastPosition;
    //if this is moving... i think it needs to be mapped to a pComp prevPos and not the live
    private Vector center;

    public MoveModeAddOrbit(String mode, boolean clockwise, double speed, Vector center) {
        super(mode);
        this.clockwise = clockwise;
        this.speed = speed;
        this.center = center;
        lastPosition = null;
    }
    public MoveModeAddOrbit(String mode, boolean clockwise, double speed, Vector center, Vector lastPosition) {
        super(mode);
        this.clockwise = clockwise;
        this.speed = speed;
        this.lastPosition = lastPosition;
        this.center = center;
    }

    public boolean isClockwise() {
        return clockwise;
    }
    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public Vector getLastPosition() {
        return lastPosition;
    }
    public void setLastPosition(Vector lastPosition) {
        this.lastPosition = lastPosition;
    }
    public Vector getCenter() {
        return center;
    }
    public void setCenter(Vector center) {
        this.center = center;
    }
}

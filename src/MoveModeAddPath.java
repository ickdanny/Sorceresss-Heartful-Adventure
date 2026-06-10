import java.util.ArrayList;

public class MoveModeAddPath extends MoveModeAdd {

    private ArrayList<Vector> points;
    private int nextPoint;
    private double speed;

    public MoveModeAddPath(String mode, ArrayList<Vector> points, double speed) {
        super(mode);
        this.points = points;
        this.nextPoint = 0;
        this.speed = speed;
    }

    public ArrayList<Vector> getPoints() {
        return points;
    }
    public int getNextPoint() {
        return nextPoint;
    }
    public double getSpeed() {
        return speed;
    }
    public void setPoints(ArrayList<Vector> points) {
        this.points = points;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public void goNextPoint(){
        if(++nextPoint >= points.size()){
            nextPoint = 0;
        }
    }
}
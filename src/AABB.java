//holds coordinates for four sides
public class AABB {
    private double xlow, xhigh, ylow, yhigh;

    public AABB(double radius){
        xlow = -radius;
        xhigh = radius;
        ylow = -radius;
        yhigh = radius;
    }

    public AABB(double xlow, double xhigh, double ylow, double yhigh) {
        this.xlow = xlow;
        this.xhigh = xhigh;
        this.ylow = ylow;
        this.yhigh = yhigh;
    }

    public double getXlow() {
        return xlow;
    }
    public void setXlow(double xlow) {
        this.xlow = xlow;
    }
    public double getXhigh() {
        return xhigh;
    }
    public void setXhigh(double xhigh) {
        this.xhigh = xhigh;
    }
    public double getYlow() {
        return ylow;
    }
    public void setYlow(double ylow) {
        this.ylow = ylow;
    }
    public double getYhigh() {
        return yhigh;
    }
    public void setYhigh(double yhigh) {
        this.yhigh = yhigh;
    }

    public double getWidth(){
        return xhigh - xlow;
    }
    public double getHeight(){
        return yhigh - ylow;
    }
    public double getArea(){
        return getWidth() * getHeight();
    }

    public static AABB calculateTruePos(Vector pos, AABB hitbox){
        return new AABB(hitbox.getXlow() + pos.getA(), hitbox.getXhigh() + pos.getA(),
                hitbox.getYlow() + pos.getB(), hitbox.getYhigh() + pos.getB());
    }
    public static AABB calculateTwoFrame(AABB a, AABB b){
        if(a == null){
            return b;
        }
        else if(b == null){
            return a;
        }
        return new AABB(Math.min(a.getXlow(), b.getXlow()), Math.max(a.getXhigh(), b.getXhigh()),
                Math.min(a.getYlow(), b.getYlow()), Math.max(a.getYhigh(), b.getYhigh()));
    }

    public AABB deepClone(){
        return new AABB(xlow, xhigh, ylow, yhigh);
    }

    public void copy(AABB toCopy){
        this.xlow = toCopy.xlow;
        this.xhigh = toCopy.xhigh;
        this.ylow = toCopy.ylow;
        this.yhigh = toCopy.yhigh;
    }
}

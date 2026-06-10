public class Vector {
    private double a;
    private double b;

    public Vector(){
        a = 0;
        b = 0;
    }
    public Vector(double x){
        a = x;
        b = x;
    }
    public Vector(double a, double b){
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }
    public void setA(double a) {
        this.a = a;
    }
    public double getB() {
        return b;
    }
    public void setB(double b) {
        this.b = b;
    }

    public Vector deepClone(){
        return new Vector(a, b);
    }
    public void copy(Vector toCopy){
        this.a = toCopy.a;
        this.b = toCopy.b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return vector.a == a && vector.b == b;
    }

    public boolean isZero(){
        return a == 0 && b == 0;
    }

    @Override
    public String toString(){
        return "<" + a + "," + b + ">";
    }

    /////////////////////////////////////////////////////////////

    public static Vector add(Vector a, Vector b){
        return new Vector(a.getA() + b.getA(), a.getB() + b.getB());
    }
    public static Vector subtract(Vector a, Vector b){
        return new Vector(a.getA() - b.getA(), a.getB() - b.getB());
    }
    public static double getLength(Vector a){
        return Math.sqrt(Math.pow(a.getA(), 2) + Math.pow(a.getB(), 2));
    }
    public static Vector scalarMultiple(Vector a, double s){
        return new Vector(a.getA() * s, a.getB() * s);
    }

    public static double getDistance(Vector a, Vector b){
        return Math.sqrt(Math.pow(a.getA() - b.getA(), 2) + Math.pow(a.getB() - b.getB(), 2));
    }
    public static double getAngle(Vector a, Vector b){
//        i honestly have no idea what this is... i need getAngle for
//        return MiscUtil.fixAngle(Math.toDegrees(Math.acos(dotProduct(normalize(a), normalize(b)))));
        return MiscUtil.fixAngle(-1 * Math.toDegrees(Math.atan2(a.getA() - b.getA(), -1 * (a.getB() - b.getB()))));
    }

    //angle from vector with angle of zero to a
    //that doesn't work
    public static double getAngle(Vector a){
//        return getAngle(new Vector(0, 1), a);
        return getAngle(new Vector(0, 0), a);
    }

    public static double dotProduct(Vector a, Vector b){
        return (a.getA() * b.getA()) + (a.getB() * b.getB());
    }

    public static Vector toPolar(Vector v){
        return new Vector(getAngle(v), getLength(v));
    }

    //sqrt(sin(x)^2 + cos(x)^2) = 1 for all x
    public static Vector toVelocity(Vector p){
        return scalarMultiple(new Vector(Math.sin(Math.toRadians(p.getA())), Math.cos(Math.toRadians(p.getA()))), p.getB());
    }

    //returns length of a such that b projects to a
    public static double scalarProject(Vector a, Vector b){
        return dotProduct(normalize(a), b);
    }
    //returns vector of same angle as a such that b projects to a
    public static Vector scalarProjectV(Vector a, Vector b){
        return scalarMultiple(normalize(a), scalarProject(a, b));
    }

    public static Vector normalize(Vector a){
        double l = getLength(a);
        return new Vector(a.getA()/l, a.getB()/l);
    }

    //from a to b, non commutative
    //d->0 = a, d->1 = b
    public static Vector interpolate(Vector a, Vector b, double d){
        return new Vector(a.getA() + ((b.getA() - a.getA()) * d), a.getB() + ((b.getB() - a.getB()) * d));
    }

    //given vector a, x, and y, return either x or y, depending on which one is closer
    //this doesn't require sqrt
    public static Vector distCompare(Vector a, Vector x, Vector y){
        double xcomp = Math.pow(a.getA() - x.getA(), 2) + Math.pow(a.getB() - x.getB(), 2);
        double ycomp = Math.pow(a.getA() - y.getA(), 2) + Math.pow(a.getB() - y.getB(), 2);
        if(xcomp > ycomp){
            return x;
        }
        return y;
    }

    public static void polarAngleAdd(Vector p, double a){
        p.setA(MiscUtil.fixAngle(p.getA() + a));
    }

    //<v,k>
    public static Vector parseVector(String toParse){
        //v,k
        String s = toParse.substring(1, toParse.length() - 1);
        int i = s.indexOf(",");
        return new Vector(Double.parseDouble(s.substring(0, i)), Double.parseDouble(s.substring(i+1)));
    }
}

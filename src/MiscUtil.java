public class MiscUtil {
    private MiscUtil(){}

    //[0, 360)
    public static double fixAngle(double a){
        if(a < 0){
            //mod will give negative number < 360, or zero
            a = (a % 360);
            if(a != 0){
                //e.g. -90 -> 270
                a += 360;
            }
        }
        else if(a >= 360){
            a %= 360;
        }
        return a;
    }

    //1 = positive direction (counter)
    //-1 = negative direction (clockwise)
    public static int turnDirection(double angleA, double angleB){
        double angleA_ = fixAngle(angleA);
        double angleB_ = fixAngle(angleB);
        double bZero = fixAngle(angleB_ - angleA_);

        if(bZero < 180){
            return 1;
        }
        return -1;
    }

    //tells degrees difference between 2 angles
    public static double angleDiff(double a, double b){
        a = fixAngle(a);
        b = fixAngle(b);
        double maxDiff;
        if(a == b){
            return 0;
        }
        if(a > b){
            maxDiff = a- b;
        }
        else{
            maxDiff = b-a;
        }
        if(maxDiff <= 180){
            return maxDiff;
        }
        else{
            return 360 - maxDiff;
        }
    }


    public static double angleToSpriteRotation(double angle){
        return -(angle - 180);
    }
    public static AABB addAABBToVector(AABB aabb, Vector v){
        double newXlow = aabb.getXlow() + v.getA();
        double newXhigh = aabb.getXhigh() + v.getA();
        double newYlow = aabb.getYlow() + v.getB();
        double newYhigh = aabb.getYhigh() + v.getB();
        return new AABB(newXlow, newXhigh, newYlow, newYhigh);
    }

    //this ought not to be here but who cares
    public static boolean vectorCollidesAABB(AABB aabb, Vector v){
        //check within bounds for all four sides
        return v.getA() >= aabb.getXlow() && v.getA() <= aabb.getXhigh() &&
                v.getB() >= aabb.getYlow() && v.getB() <= aabb.getYhigh();
    }

    //random is from 0 to 1 here
    public static double rangeRandom(double low, double high, double random){
        if(low == high){
            return low;
        }
        if(low > high){
            double t = low;
            low = high;
            high = t;
        }
        double range = high - low;
        double add = range * random;
        return low + add;
    }

    public static int trueRandomInt(int bot, int top){
        if(top == bot){
            return top;
        }
        return bot + (int)((top - bot + 1) * Math.random());
    }
}

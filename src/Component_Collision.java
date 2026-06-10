//aabb
public class Component_Collision implements Component {

    //for hitbox xlow and ylow are negative
    private AABB hitbox;
    private AABB truePos;
    private AABB twoFrame;

    private double hp, dmg;
    //by default hp = 1 shot kill and dmg = 0;

    public Component_Collision(AABB hitbox){
        this.hitbox = hitbox;
        hp = .01;
        dmg = 0;
    }

    public Component_Collision(AABB hitbox, double hp, double dmg){
        this.hitbox = hitbox;
        this.hp = hp;
        this.dmg = dmg;
    }

    public AABB getHitbox(){
        return hitbox;
    }
    public AABB getTruePos() {
        return truePos;
    }
    public AABB getTwoFrame() {
        return twoFrame;
    }
    public void setHitbox(AABB hitbox) {
        this.hitbox = hitbox;
    }
    public void setTruePos(AABB truePos) {
        this.truePos = truePos;
    }
    public void setTwoFrame(AABB twoFrame) {
        this.twoFrame = twoFrame;
    }

    public double getHp() {
        return hp;
    }
    public void setHp(double hp) {
        this.hp = hp;
    }
    public double getDmg() {
        return dmg;
    }
    public void setDmg(double dmg) {
        this.dmg = dmg;
    }

    @Override
    public String getComponentType(){
        return "collision";
    }
}
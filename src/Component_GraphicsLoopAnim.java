import java.util.ArrayList;

public class Component_GraphicsLoopAnim extends Component_Graphics {
    private ArrayList<SImage> currentAnim;
    //0 to maxTick-1
    private int tick, maxTick, index;

    public Component_GraphicsLoopAnim(ArrayList<SImage> currentAnim, int maxTick){
        super(currentAnim.get(0));
        this.currentAnim = currentAnim;
        tick = 0;
        this.maxTick = maxTick;
        index = 0;
    }

    public ArrayList<SImage> getCurrentAnim() {
        return currentAnim;
    }
    public void setCurrentAnim(ArrayList<SImage> currentAnim) {
        this.currentAnim = currentAnim;
    }
    public int getMaxTick() {
        return maxTick;
    }
    public int getTick() {
        return tick;
    }
    public void setTick(int tick) {
        this.tick = tick;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
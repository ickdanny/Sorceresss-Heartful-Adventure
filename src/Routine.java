import java.util.ArrayList;

public class Routine extends Behavior {
    private ArrayList<Behavior> behaviors;
    private boolean looping;
    private int index;

    public Routine(String mode, ArrayList<Behavior> behaviors, boolean looping) {
        super(mode);
        this.behaviors = behaviors;
        this.looping = looping;
        index = 0;
    }

    public ArrayList<Behavior> getBehaviors() {
        return behaviors;
    }
    public boolean isLooping() {
        return looping;
    }
    public int getIndex() {
        return index;
    }
    public void setLooping(boolean looping) {
        this.looping = looping;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
import java.util.ArrayList;
import java.util.Iterator;

public class Component_Behavior implements Component{

    private ArrayList<Behavior> behaviors;

    public Component_Behavior(){
        behaviors = new ArrayList<>();
    }
    public Component_Behavior(Behavior b){
        behaviors = new ArrayList<>();
        behaviors.add(b);
    }
    public Component_Behavior(ArrayList<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    public ArrayList<Behavior> getBehaviors() {
        return behaviors;
    }

    public void addBehavior(Behavior b){
        behaviors.add(b);
    }
    public void removeBehavior(Behavior b){
        behaviors.remove(b);
    }
    //top level only
    public void removeBehavior(String mode){
        Iterator<Behavior> iter = behaviors.iterator();
        while(iter.hasNext()){
            if(iter.next().getMode().equals(mode)){
                iter.remove();
            }
        }
    }
    public void clear(){
        behaviors.clear();
    }

    @Override
    public String getComponentType() {
        return "behavior";
    }
}
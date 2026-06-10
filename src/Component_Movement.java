import java.util.ArrayList;
import java.util.Iterator;

public class Component_Movement implements Component {
    private ArrayList<MoveModeAdd> addModes;
    private ArrayList<MoveModeTrans> transModes;

    public Component_Movement(){
        addModes = new ArrayList<>();
        transModes = new ArrayList<>();
    }
    public Component_Movement(MoveModeAdd mode){
        addModes = new ArrayList<>();
        addModes.add(mode);
        transModes = new ArrayList<>();
    }
    public Component_Movement(MoveModeAdd aMode, MoveModeTrans tmode){
        addModes = new ArrayList<>();
        addModes.add(aMode);
        transModes = new ArrayList<>();
        transModes.add(tmode);
    }
    public Component_Movement(ArrayList<MoveModeAdd> addModes){
        this.addModes = addModes;
        transModes = new ArrayList<>();
    }
    public Component_Movement(ArrayList<MoveModeAdd> addModes, ArrayList<MoveModeTrans> transModes){
        this.addModes = addModes;
        this.transModes = transModes;
    }

    public ArrayList<MoveModeAdd> getAddModes() {
        return addModes;
    }
    public ArrayList<MoveModeTrans> getTransModes() {
        return transModes;
    }

    public void addMode(MoveMode mode){
        if(mode instanceof MoveModeAdd){
            addModes.add((MoveModeAdd)mode);
        }
        else if(mode instanceof MoveModeTrans){
            transModes.add((MoveModeTrans)mode);
        }
    }
    public void removeMode(MoveMode mode){
        if(mode instanceof MoveModeAdd){
            addModes.remove(mode);
        }
        else if(mode instanceof MoveModeTrans){
            transModes.remove(mode);
        }
    }
    public void removeMode(String mode){
        Iterator iter = addModes.iterator();
        while(iter.hasNext()){
            if(((MoveModeAdd)iter.next()).getMode().equals(mode)){
                iter.remove();
            }
        }
        iter = transModes.iterator();
        while(iter.hasNext()){
            if(((MoveModeTrans)iter.next()).getMode().equals(mode)){
                iter.remove();
            }
        }
    }
    public void clear(){
        addModes.clear();
        transModes.clear();
    }

    @Override
    public String getComponentType(){
        return "movement";
    }
}
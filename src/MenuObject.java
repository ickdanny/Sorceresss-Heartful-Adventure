import java.util.ArrayList;

//base class for menu objects. Includes menus, the pause screen, credits screen, game, etc etc.
//must have a reference to an inputTable
//must have update method
//must have a getImage method that returns an image.
//must have a position vector.
//must have a boolean drawPrevious

//the different menu objects are diverse enough to just use inheritance.
public abstract class MenuObject {

    protected InputTable inputTable;
    //this is a reference to the mainBoard in MenuManager
    protected Blackboard mainBoard;
    //absolute position from 0,0
    protected Vector position;

    protected boolean drawPrevious;
    protected boolean inputTransparent;

    public abstract void update();
    //arraylist of stringimage for some graphics class to draw
    public abstract ArrayList<DrawableElement> getImage(double dt);
    public Vector getPosition(){
        return position;
    }
    public boolean isDrawPrevious(){
        return drawPrevious;
    }
    public boolean isInputTransparent(){
        return inputTransparent;
    }

    @Override
    public String toString(){
        return this.getClass().getName();
    }
}
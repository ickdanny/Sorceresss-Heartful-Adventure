import java.util.ArrayList;

public class LoadScreen extends MenuObject {

    private int timer;
    private BoardMessage toSend;
    private ArrayList<DrawableElement> drawableElements;
    public LoadScreen(Blackboard mainBoard){
        position = new Vector(0, 0);
        drawPrevious = false;

        //no input table

        this.mainBoard = mainBoard;

        SImage black = new SImage("background_black", position.deepClone());
        drawableElements = new ArrayList<>();
        drawableElements.add(black);

        inputTransparent = false;
    }

    @Override
    public void update(){
        if(timer > 0) {
            if (--timer == 0) {
                mainBoard.write(toSend);
                toSend = null;
            }
        }
    }

    @Override
    public ArrayList<DrawableElement> getImage(double dt){
        return drawableElements;
    }

    public void startTimer(int timer, BoardMessage toSend){
        this.timer = timer;
        this.toSend = toSend;
    }
}

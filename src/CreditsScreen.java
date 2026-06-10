import java.util.ArrayList;

public class CreditsScreen extends MenuObject {
    private static final int BASE_TIME = 31 * 60;
    private int timer;
    private ArrayList<DrawableElement> drawableElements;

    public CreditsScreen(Blackboard mainBoard){
        position = new Vector(0, 0);
        drawPrevious = false;

        this.mainBoard = mainBoard;
        SImage credits = new SImage("background_credits", position.deepClone());
        drawableElements = new ArrayList<>();
        drawableElements.add(credits);
        inputTransparent = false;
        timer = BASE_TIME;
    }

    @Override
    public void update(){
        if(timer > 0) {
            if (--timer == 0) {
                mainBoard.write(new BoardMessage("command_back_to_main_menu", this));
            }
        }
    }

    @Override
    public ArrayList<DrawableElement> getImage(double dt){
        return drawableElements;
    }

    public void resetTimer(){
        timer = BASE_TIME;
    }
}

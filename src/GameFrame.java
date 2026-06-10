import java.util.ArrayList;
import java.util.Random;

//menu object that interfaces with GameDriver
public class GameFrame extends MenuObject {

    private ArrayList<DrawableElement> previousImage;

    public GameFrame(InputTable inputTable, Blackboard mainBoard, Random random){
        this.inputTable = inputTable;
        this.mainBoard = mainBoard;
        previousImage = new ArrayList<>();
        GameDriver.setCurrentInputTable(inputTable);
        GameDriver.setRandom(random);
    }
    //without setting the random -> random is static in GameDriver; for campaign
    public GameFrame(InputTable inputTable, Blackboard mainBoard){
        this.inputTable = inputTable;
        this.mainBoard = mainBoard;
        previousImage = new ArrayList<>();
        GameDriver.setCurrentInputTable(inputTable);
        GameDriver.reseedRandom();
    }

    @Override
    public ArrayList<DrawableElement> getImage(double dt) {
        //if paused return the last image
        if (dt == -1) {
            return previousImage;
        }
        previousImage = GameDriver.getImage(dt);
//        previousImage = GameDriver.getImage(0);
        return previousImage;
    }

    @Override
    public void update(){
        readInputs();
        GameDriver.update();
    }

    private void readInputs(){
        String[] inputs = inputTable.getInputs();
        //escape for pause
        if(inputs[7].equals("just pressed")){
            mainBoard.write(new BoardMessage("command_add_menu pause", this));
        }
    }
}
import java.util.ArrayList;

public class DialogueScreen extends MenuObject {
    private String[][] dialogue;
    private int dialoguePos;
    private SImage currentBox;
    private SText currentText;
    public DialogueScreen(String key, InputTable it, Blackboard mainBoard){
        position = Vector.add(GameDriver.getGameWindowOffset().deepClone(), new Vector(0, GameDriver.getGameHeight() - 190));
        drawPrevious = true;
        inputTable = it;
        this.mainBoard = mainBoard;
        dialogue = ResourceManager.getDialogue(key);
        inputTransparent = true;
        dialoguePos = 0;
        currentBox = new SImage("", position, new Vector());
        currentText = new SText("", Vector.add(position, new Vector(160, 40)), 30, 27);
        //load the initial box and text and stuff
        readDialogue();
    }

    //lock parts of the IT for the game below
    @Override
    public void update(){
        if(dialoguePos >= dialogue.length){
            mainBoard.write(new BoardMessage("command_back", 1, this));
            mainBoard.write(new BoardMessage("dialogue_over", 2, this));
            return;
        }

        String[] inputs = inputTable.getInputs();
        //lock input table for below
        inputTable.lockKeys(new boolean[]{false, true, true, false, false, false, false, true});
        if(inputs[1].equals("just pressed")) {
            readDialogue();
        }
    }

    private void readDialogue(){
        while (readNextLine()) {
            if (++dialoguePos >= dialogue.length) {
                mainBoard.write(new BoardMessage("command_back", 1, this));
                mainBoard.write(new BoardMessage("dialogue_over", 2, this));
                return;
            }
        }
        if (++dialoguePos >= dialogue.length) {
            mainBoard.write(new BoardMessage("command_back", 1, this));
            mainBoard.write(new BoardMessage("dialogue_over", 2, this));
        }
    }

    //true = reads the next line -> advance dialoguePos
    private boolean readNextLine(){
        String cmd = dialogue[dialoguePos][0];
        String data = dialogue[dialoguePos][1];

        switch(cmd){
            case "set_box":
                currentBox.setImage(data);
                return true;
            case "set_text":
                currentText.setToDraw(data);
                return true;
            case "set_track":
                //not gonna bother doublechecking what data is, i can probably guess
                MusicDriver.playTrack(data, true);
                return true;
            case "stop":
                return false;
            //not convinced i need an end command -> i can just let dialoguePos run over the length of the array
//            case "end":
//                mainBoard.write(new BoardMessage("command_back", 1, this));
//                return false;
            case "continue":
            case "continue_game":
                continueGame();
                return false;
            default:
                return false;
        }
    }

    @Override
    public ArrayList<DrawableElement> getImage(double dt){
        ArrayList<DrawableElement> toRet = new ArrayList<>();
        if(currentBox != null){
            toRet.add(currentBox);
        }
        if(currentText != null){
            toRet.add(currentText);
        }
        return toRet;
    }

    private void continueGame(){
        if(GameDriver.getCurrentCampaign()){
            int lvl = GameDriver.getCurrentStage() + 1;
            int diff = GameDriver.getCurrentDifficulty();
            int shot = GameDriver.getCurrentShot();
            //wonder how this works. copy pasted from gamedriver.
            Component_Player playComp = (Component_Player)(ComponentManager.getComponent("player",
                    (SystemManager.getSortedEntities().get(System_Player.getCompFilter())).get(0).getEntityID()));
            String args = "continue_" + lvl + "_" + diff + "_shot" + shot;
            mainBoard.write(new BoardMessage("command_continue_game " + args, 1, this, new Object[]{playComp}));
        }
        else{
            mainBoard.write(new BoardMessage("command_back_to_menu", 1, this));
        }
    }
}

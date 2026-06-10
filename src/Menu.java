import java.util.ArrayList;

//each menu holds the background image as well as an arraylist for buttons, and an int for selected button
public class Menu extends MenuObject {
    private MenuButton[] buttons;
    private int selectedButton;

    private SImage backgroundStringImage;

    //mainBoard here being the mainboard of driver
    //position = top left corner
    public Menu(Vector position, boolean drawPrevious, String backgroundImage, InputTable inputTable, MenuButton[] buttons, Blackboard mainBoard){
        this.position = position;
        this.drawPrevious = drawPrevious;
        this.inputTable = inputTable;
        this.buttons = buttons;
        this.mainBoard = mainBoard;
        backgroundStringImage = new SImage(backgroundImage, position);
        selectedButton = 0;
    }

    //update based upon inputs
    @Override
    public void update(){
        String[] inputs = inputTable.getInputs();
        //escape and xfirst for back
        if(inputs[7].equals("just pressed") || inputs[2].equals("just pressed")){
            mainBoard.write(new BoardMessage("command_back", this));
        }
        //then select
        else if(inputs[1].equals("just pressed")){
            mainBoard.write(new BoardMessage(buttons[selectedButton].getCommand(), this));
        }
        //up and down | up = go down the buttons, down = go up the buttons //left right too
        else if((inputs[6].equals("just pressed") || inputs[4].equals("just pressed")) &&
                (!inputs[5].equals("just pressed") || !inputs[3].equals("just pressed"))){
            int nextSelectedButton = selectedButton + 1;
            //skip unavailable buttons
            while(nextSelectedButton < buttons.length && !buttons[nextSelectedButton].isAvailable()){
                nextSelectedButton++;
            }
            //as long as not 1 over i.e. there exists a next button
            if(nextSelectedButton != buttons.length){
                selectedButton = nextSelectedButton;
            }
        }
        else if((inputs[5].equals("just pressed") || inputs[3].equals("just pressed")) &&
                (!inputs[6].equals("just pressed") || !inputs[4].equals("just pressed"))){
            int nextSelectedButton = selectedButton - 1;
            //skip unavailable buttons
            while(nextSelectedButton >= 0 && !buttons[nextSelectedButton].isAvailable()){
                nextSelectedButton--;
            }
            //as long as not 1 under i.e. there exists a previous button
            if(nextSelectedButton != -1){
                selectedButton = nextSelectedButton;
            }
        }
    }

    //lower index = first drawn
    @Override
    public ArrayList<DrawableElement> getImage(double dt){
        ArrayList<DrawableElement> toRet = new ArrayList<>();
        toRet.add(backgroundStringImage);
        for(int i = 0; i < buttons.length; i++){
            if(!buttons[i].isAvailable()){
                toRet.add(buttons[i].getUnavailable());
            }
            else{
                if(i != selectedButton){
                    toRet.add(buttons[i].getUnselected());
                }
                else{
                    toRet.add(buttons[i].getSelected());
                }
            }
        }
        return toRet;
    }

    public MenuButton[] getButtons() {
        return buttons;
    }

    //assume the first button of any menu is available
    public void reset(){
        selectedButton = 0;
    }
}


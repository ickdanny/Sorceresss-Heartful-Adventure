//holds complete arrow of current and last tick button presses.
//buttons = shift, z, x, four arrows = 7 buttons
public class InputTable{
    //holds raw inputs
    //0 = shift
    //1 = z
    //2 = x
    //3 = left
    //4 = right
    //5 = up
    //6 = down
    //7 = esc

    //0 = this turn
    //1 = last turn
    //2 = is locked
    private boolean[][] table;
    public InputTable(){
        table = new boolean[8][3];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 3; j++){
                table[i][j] = false;
            }
        }
    }

    //whenever a key is pressed or released
    public void keyAction(String keyID){
        table[keyPosition(keyID)][0] = keyValue(keyID);
    }

    //turn string keyID into int position on table
    private int keyPosition(String keyID){
        switch(keyID){
            case "ShiftKey":
            case "ReleaseShiftKey":
                return 0;
            case "ZKey":
            case "ReleaseZKey":
                return 1;
            case "XKey":
            case "ReleaseXKey":
                return 2;
            case "LeftArrow":
            case "ReleaseLeftArrow":
                return 3;
            case "RightArrow":
            case "ReleaseRightArrow":
                return 4;
            case "UpArrow":
            case "ReleaseUpArrow":
                return 5;
            case "DownArrow":
            case "ReleaseDownArrow":
                return 6;
            case "EscapeKey":
            case "ReleaseEscapeKey":
                return 7;
            //instant crash
            default:
                return -1;
        }
    }

    //parse the value of keyID
    private boolean keyValue(String keyID){
        return !keyID.contains("Release");
    }

    //locks the rows listed in keys
    //if keys[i] is true then that position on the table will be true/locked
    public void lockKeys(boolean[] keys){
        for(int i = 0; i < 8; i++) {
            table[i][2] = keys[i];
        }
    }

    //assume no key info change until a released or press signal is sent
    public void newTurn(){
        for(int i = 0; i < 8; i++){
            table[i][1] = table[i][0];
            table[i][2] = false;
        }
    }

    public String[] getInputs(){
        String[] toRet = new String[8];
        for(int i = 0; i < 8; i++){
            //[i][2] true = islocked, false = can be read
            if(table[i][2]){
                toRet[i] = "locked";
            }
            else{
                if(table[i][1]){
                    if(table[i][0]){
                        toRet[i] = "still pressed";
                    }
                    else{
                        toRet[i] = "just released";
                    }
                }
                else{
                    if(table[i][0]){
                        toRet[i] = "just pressed";
                    }
                    else{
                        toRet[i] = "still released";
                    }
                }
            }
        }
        return toRet;
    }

    @Override
    public String toString() {
        String toRet = "";
        String[] inputs = getInputs();
        for(int i = 0; i < 8; i++){
            String s = inputs[i];
            toRet = toRet.concat(table[i][1] + " " + table[i][0] + " ");
            toRet = toRet.concat(s);
            toRet = toRet.concat(System.lineSeparator());
        }
        return toRet;
    }
}

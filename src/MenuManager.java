import java.lang.System;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class MenuManager {

    private static Stack<MenuObject> menuObjectStack;

    //this IS the main board. Other top level managers don't need one.
    private static Blackboard mainBoard;

    //store all menus at all times
    private static HashMap<String, Menu> menus;
    private static LoadScreen loadScreen;
    private static CreditsScreen creditsScreen;

    public static void init(){
        menuObjectStack = new Stack<>();
        mainBoard = new Blackboard();
        menus = new HashMap<>();

        initMenu_Main();
        initMenu_Difficulty();
        initMenu_LevelSelect();
        initMenu_ShotSelect();
        initMenu_Option();
        initMenu_Music();
        initMenu_Pause();

        loadScreen = new LoadScreen(mainBoard);
        creditsScreen = new CreditsScreen(mainBoard);

        menuObjectStack.push(menus.get("main"));
    }

    public static void update(){
        //read first
        readBlackboard();
        //update the stack
        int toUpdate = menuObjectStack.size() - 1;
        do{
            menuObjectStack.get(toUpdate).update();
            toUpdate--;
        }while(toUpdate >= 0 && menuObjectStack.get(toUpdate + 1).isInputTransparent());
    }

    public static ArrayList<DrawableElement> getImages(double dt){
        //we need to check because of inputTransparency
        boolean gameFrameIsUpdating = false;
        int lowestDrawnMenuObject = menuObjectStack.size() - 1;

        //if gameFrame is top of stack it is updating
        if(menuObjectStack.get(lowestDrawnMenuObject) instanceof GameFrame){
            gameFrameIsUpdating = true;
        }

        while(lowestDrawnMenuObject > 0 && menuObjectStack.get(lowestDrawnMenuObject).isDrawPrevious()){
            lowestDrawnMenuObject--;

            //check for game frame underneath inputTransparent (i.e. dialogue boxes)
            if(!gameFrameIsUpdating){
                if(menuObjectStack.get(lowestDrawnMenuObject) instanceof GameFrame &&
                        menuObjectStack.get(lowestDrawnMenuObject + 1).isInputTransparent()){
                    gameFrameIsUpdating = true;
                }
            }
        }

        //combine StringImages
        ArrayList<DrawableElement> toRet = new ArrayList<>();
        for(int i = lowestDrawnMenuObject; i < menuObjectStack.size(); i++){
            if(!(menuObjectStack.get(i) instanceof GameFrame) || gameFrameIsUpdating) {
                toRet.addAll(menuObjectStack.get(i).getImage(dt));
            }
            //this tells GameFrame to display the last image it sent
            else{
                toRet.addAll(menuObjectStack.get(i).getImage(-1));
            }
        }
        return toRet;
    }

    //hard coding in the menus - not really worth the time to store in files ATM
    private static void initMenu_Main(){
        Vector beginning = new Vector(1005, 583);
        Vector down_offset = new Vector(-23, 40);
        Vector sel_offset = new Vector(15, 2);
        Vector[] sel = new Vector[6];
        Vector[] unsel = new Vector[6];
        for(int i = 0; i < 6; i++){
            unsel[i] = Vector.add(beginning, Vector.scalarMultiple(down_offset, i));
            sel[i] = Vector.add(unsel[i], sel_offset);
        }

        MenuButton start = new MenuButton(unsel[0], sel[0], true, "menu_button_main_start_sel",
                "menu_button_main_start_unsel", "menu_button_main_start_unsel", "command_add_menu difficulty_campaign");
        MenuButton extra = new MenuButton(unsel[1], sel[1], ResourceManager.isExtraUnlocked(), "menu_button_main_extra_sel",
                "menu_button_main_extra_unsel", "menu_button_main_extra_unavail", "command_add_menu difficulty_extra");
        MenuButton practice = new MenuButton(unsel[2], sel[2], true, "menu_button_main_practice_sel",
                "menu_button_main_practice_unsel", "menu_button_main_practice_unsel", "command_add_menu difficulty_practice");
        MenuButton option = new MenuButton(unsel[3], sel[3], true, "menu_button_main_option_sel",
                "menu_button_main_option_unsel", "menu_button_main_option_unsel", "command_add_menu option");
        MenuButton music = new MenuButton(unsel[4], sel[4],  true, "menu_button_main_music_sel",
                "menu_button_main_music_unsel", "menu_button_main_music_unsel", "command_add_menu music");
        MenuButton quit = new MenuButton(unsel[5], sel[5], true, "menu_button_main_quit_sel",
                "menu_button_main_quit_unsel", "menu_button_main_quit_unsel", "command_exit");
        MenuButton[] buttons = new MenuButton[]{start, extra, practice, option, music, quit};
        Menu main = new Menu(new Vector(0, 0), false, "background_main",
                InputManager.getLiveInputTable(), buttons, mainBoard);
        menus.put("main", main);
    }
    private static void initMenu_Difficulty(){

        Vector beginning = new Vector(39, 206);
        Vector down_offset = new Vector(300, 0);
        Vector sel_offset = new Vector(-9, -6);
        Vector[] sel = new Vector[4];
        Vector[] unsel = new Vector[4];
        for(int i = 0; i < 4; i++){
            unsel[i] = Vector.add(beginning, Vector.scalarMultiple(down_offset, i));
            sel[i] = Vector.add(unsel[i], sel_offset);
        }

        //campaign
        {
            MenuButton diff1 = new MenuButton(unsel[0], sel[0], true, "menu_button_difficulty_diff1_sel",
                    "menu_button_difficulty_diff1_unsel", "menu_button_difficulty_diff1_unsel", "command_add_menu shot_select_campaign_1");
            MenuButton diff2 = new MenuButton(unsel[1], sel[1], true, "menu_button_difficulty_diff2_sel",
                    "menu_button_difficulty_diff2_unsel", "menu_button_difficulty_diff2_unsel", "command_add_menu shot_select_campaign_2");
            MenuButton diff3 = new MenuButton(unsel[2], sel[2], true, "menu_button_difficulty_diff3_sel",
                    "menu_button_difficulty_diff3_unsel", "menu_button_difficulty_diff3_unsel", "command_add_menu shot_select_campaign_3");
            MenuButton diff4 = new MenuButton(unsel[3], sel[3], true, "menu_button_difficulty_diff4_sel",
                    "menu_button_difficulty_diff4_unsel", "menu_button_difficulty_diff4_unsel", "command_add_menu shot_select_campaign_4");
            MenuButton[] buttons = new MenuButton[]{diff1, diff2, diff3, diff4};
            Menu difficulty_campaign = new Menu(new Vector(0, 0), false, "background_difficulty",
                    InputManager.getLiveInputTable(), buttons, mainBoard);
            menus.put("difficulty_campaign", difficulty_campaign);
        }
        //practice
        {
            MenuButton diff1 = new MenuButton(unsel[0], sel[0], true, "menu_button_difficulty_diff1_sel",
                    "menu_button_difficulty_diff1_unsel", "menu_button_difficulty_diff1_unsel", "command_add_menu level_select_1");
            MenuButton diff2 = new MenuButton(unsel[1], sel[1], true, "menu_button_difficulty_diff2_sel",
                    "menu_button_difficulty_diff2_unsel", "menu_button_difficulty_diff2_unsel", "command_add_menu level_select_2");
            MenuButton diff3 = new MenuButton(unsel[2], sel[2], true, "menu_button_difficulty_diff3_sel",
                    "menu_button_difficulty_diff3_unsel", "menu_button_difficulty_diff3_unsel", "command_add_menu level_select_3");
            MenuButton diff4 = new MenuButton(unsel[3], sel[3], true, "menu_button_difficulty_diff4_sel",
                    "menu_button_difficulty_diff4_unsel", "menu_button_difficulty_diff4_unsel", "command_add_menu level_select_4");
            MenuButton[] buttons = new MenuButton[]{diff1, diff2, diff3, diff4};
            Menu difficulty_practice = new Menu(new Vector(0, 0), false, "background_difficulty",
                    InputManager.getLiveInputTable(), buttons, mainBoard);
            menus.put("difficulty_practice", difficulty_practice);
        }
        //extra
        {
            MenuButton diffEX = new MenuButton(new Vector(483, 200), true, "menu_button_difficulty_extra",
                    "menu_button_difficulty_extra", "menu_button_difficulty_extra", "command_add_menu shot_select_extra");
            MenuButton[] buttons = new MenuButton[]{diffEX};
            Menu difficulty_extra = new Menu(new Vector(0, 0), false, "background_difficulty_extra",
                    InputManager.getLiveInputTable(), buttons, mainBoard);
            menus.put("difficulty_extra", difficulty_extra);
        }
    }
    private static void initMenu_LevelSelect(){
        Vector beginning = new Vector(312, 175);
        Vector down_offset = new Vector(0, 100);
        Vector sel_offset = new Vector(-120, -6);
        Vector[] sel = new Vector[6];
        Vector[] unsel = new Vector[6];
        for(int i = 0; i < 6; i++){
            unsel[i] = Vector.add(beginning, Vector.scalarMultiple(down_offset, i));
            sel[i] = Vector.add(unsel[i], sel_offset);
        }

        //for each of the difficulties
        for(int diff = 1; diff <= 4; diff++){
            MenuButton lvl1 = new MenuButton(unsel[0], sel[0], true, "menu_button_level_level1_sel",
                    //format here is shot_select_practice_(difficulty)_(level)
                    "menu_button_level_level1_unsel", "menu_button_level_level1_unsel", "command_add_menu shot_select_practice_" + diff + "_1");
            MenuButton lvl2 = new MenuButton(unsel[1], sel[1], true, "menu_button_level_level2_sel",
                    "menu_button_level_level2_unsel", "menu_button_level_level2_unsel", "command_add_menu shot_select_practice_" + diff + "_2");
            MenuButton lvl3 = new MenuButton(unsel[2], sel[2], true, "menu_button_level_level3_sel",
                    "menu_button_level_level3_unsel", "menu_button_level_level3_unsel", "command_add_menu shot_select_practice_" + diff + "_3");
            MenuButton lvl4 = new MenuButton(unsel[3], sel[3], true, "menu_button_level_level4_sel",
                    "menu_button_level_level4_unsel", "menu_button_level_level4_unsel", "command_add_menu shot_select_practice_" + diff + "_4");
            MenuButton lvl5 = new MenuButton(unsel[4], sel[4], true, "menu_button_level_level5_sel",
                    "menu_button_level_level5_unsel", "menu_button_level_level5_unsel", "command_add_menu shot_select_practice_" + diff + "_5");
            MenuButton lvl6 = new MenuButton(unsel[5], sel[5], true, "menu_button_level_level6_sel",
                    "menu_button_level_level6_unsel", "menu_button_level_level6_unsel", "command_add_menu shot_select_practice_" + diff + "_6");
            MenuButton[] buttons = new MenuButton[]{lvl1, lvl2, lvl3, lvl4, lvl5, lvl6};
            Menu level_select = new Menu(new Vector(0, 0), false, "background_level_select",
                    InputManager.getLiveInputTable(), buttons, mainBoard);
            menus.put("level_select_" + diff, level_select);
        }
//        MenuButton lvlEX = new MenuButton(new Vector(50, 100), true, "menu_button_levelEX_sel",
//                "menu_button_levelEX_unsel", "menu_button_levelEX_unsel", "command_add_menu shot_select_extra");
//        MenuButton[] buttons = new MenuButton[]{lvlEX};
//        Menu level_select_extra = new Menu(new Vector(0, 0), false, "background_level_select",
//                InputManager.getLiveInputTable(), buttons, mainBoard);
//        menus.put("level_select_extra", level_select_extra);
    }
    private static void initMenu_ShotSelect(){

        Vector middle = new Vector(600, 450);
        Vector down_offset = new Vector(300, 210);
        Vector sel_offset = new Vector(0, -10);
        Vector[] sel = new Vector[3];
        Vector[] unsel = new Vector[3];

        unsel[0] = Vector.subtract(middle, down_offset);
        unsel[1] = middle.deepClone();
        unsel[2] = Vector.add(middle, down_offset);

        Vector image_offset = new Vector(-300, -99);

        for(int i = 0; i < 3; i++){
            unsel[i] = Vector.add(unsel[i], image_offset);
            sel[i] = Vector.add(unsel[i], sel_offset);
        }

        //for each of the difficulties
        for(int diff = 1; diff <= 4; diff++){
            //for the campaign
            MenuButton shot1 = new MenuButton(unsel[0], sel[0], true, "menu_button_shot_shot1_sel",
                    "menu_button_shot_shot1_unsel", "menu_button_shot_shot1_unsel", "command_start_game campaign_" + diff + "_shot1");
            MenuButton shot2 = new MenuButton(unsel[1], sel[1], true, "menu_button_shot_shot2_sel",
                    "menu_button_shot_shot2_unsel", "menu_button_shot_shot2_unsel", "command_start_game campaign_" + diff + "_shot2");
            MenuButton shot3 = new MenuButton(unsel[2], sel[2], true, "menu_button_shot_shot3_sel",
                    "menu_button_shot_shot3_unsel", "menu_button_shot_shot3_unsel", "command_start_game campaign_" + diff + "_shot3");
            MenuButton[] buttons = new MenuButton[]{shot1, shot2, shot3};
            Menu shot_select_campaign = new Menu(new Vector(0, 0), false, "background_shot_select",
                    InputManager.getLiveInputTable(), buttons, mainBoard);
            menus.put("shot_select_campaign_" + diff, shot_select_campaign);
            //for each of the practice levels
            for(int lvl = 1; lvl <= 6; lvl++){
                shot1 = new MenuButton(unsel[0], sel[0], true, "menu_button_shot_shot1_sel",
                        "menu_button_shot_shot1_unsel", "menu_button_shot_shot1_unsel", "command_start_game practice_" + lvl + "_" + diff + "_shot1");
                shot2 = new MenuButton(unsel[1], sel[1], true, "menu_button_shot_shot2_sel",
                        "menu_button_shot_shot2_unsel", "menu_button_shot_shot2_unsel", "command_start_game practice_" + lvl + "_" + diff + "_shot2");
                shot3 = new MenuButton(unsel[2], sel[2], true, "menu_button_shot_shot3_sel",
                        "menu_button_shot_shot3_unsel", "menu_button_shot_shot3_unsel", "command_start_game practice_" + lvl + "_" + diff + "_shot3");
                buttons = new MenuButton[]{shot1, shot2, shot3};
                Menu shot_select_practice = new Menu(new Vector(0, 0), false, "background_shot_select",
                        InputManager.getLiveInputTable(), buttons, mainBoard);
                menus.put("shot_select_practice_" + diff + "_" + lvl, shot_select_practice);
            }
        }
        //for extra level
        MenuButton shot1 = new MenuButton(unsel[0], sel[0], true, "menu_button_shot_shot1_sel",
                "menu_button_shot_shot1_unsel", "menu_button_shot_shot1_unsel", "command_start_game extra_shot1");
        MenuButton shot2 = new MenuButton(unsel[1], sel[1], true, "menu_button_shot_shot2_sel",
                "menu_button_shot_shot2_unsel", "menu_button_shot_shot2_unsel", "command_start_game extra_shot2");
        MenuButton shot3 = new MenuButton(unsel[2], sel[2], true, "menu_button_shot_shot3_sel",
                "menu_button_shot_shot3_unsel", "menu_button_shot_shot3_unsel", "command_start_game extra_shot3");
        MenuButton[] buttons = new MenuButton[]{shot1, shot2, shot3};
        Menu shot_select_extra = new Menu(new Vector(0, 0), false, "background_shot_select_extra",
                InputManager.getLiveInputTable(), buttons, mainBoard);
        menus.put("shot_select_extra", shot_select_extra);
    }
    private static void initMenu_Option(){
        Vector beginning = new Vector(537, 380);
        Vector down_offset = new Vector(12, 100);
        Vector[] positions = new Vector[2];
        for(int i = 0; i < 2; i++){
            positions[i] = Vector.add(beginning, Vector.scalarMultiple(down_offset, i));
        }

        MenuButton sound = new MenuButton(positions[0], positions[0], true, "menu_button_option_sound_sel",
                "menu_button_option_sound_unsel", "menu_button_option_sound_unsel", "command_toggle_sound");
        MenuButton quit = new MenuButton(positions[1], positions[1], true, "menu_button_main_quit_sel",
                "menu_button_main_quit_unsel", "menu_button_main_quit_unsel", "command_back");
        MenuButton[] buttons = new MenuButton[]{sound, quit};
        Menu option = new Menu(new Vector(0, 0), false, "background_option",
                InputManager.getLiveInputTable(), buttons, mainBoard);
        menus.put("option", option);
    }
    private static void initMenu_Music(){
        double xStart = 170, yStart = 220;
        double yOff = 150;
        double xOff = 260;
        MenuButton[] buttons = new MenuButton[16];
        for(int i = 0; i < 4; i++){
            double x = xStart + (xOff * i);
            for(int j = 0; j < 4; j++){
                double y = yStart + (yOff * j);
                Vector pos = new Vector(x, y);
                int num = (i * 4) + j + 1;
                String id;
                if(num < 10){
                    id = "0" + num;
                }else{
                    id = "" + num;
                }

                buttons[num - 1] = new MenuButton(pos, true, id + "_sel",
                        id + "_unsel", id + "_unsel", "command_set_track " + num);
            }
        }

        Menu music = new Menu(new Vector(0, 0), false, "background_music",
                InputManager.getLiveInputTable(), buttons, mainBoard);
        menus.put("music", music);
    }
    private static void initMenu_Pause(){
        MenuButton back = new MenuButton(new Vector(297, 450), true, "menu_button_pause_back_sel",
                "menu_button_pause_back_unsel", "menu_button_pause_back_unsel", "command_back");
        MenuButton quit = new MenuButton(new Vector(420, 450), true, "menu_button_pause_quit_sel",
                "menu_button_pause_quit_unsel", "menu_button_pause_quit_unsel", "command_back_to_main_menu");
//        MenuButton exit = new MenuButton(new Vector(600, 400), true, "menu_button_pause_exit_sel",
//                "menu_button_pause_exit_unsel", "menu_button_pause_exit_unsel", "command_exit");
        MenuButton[] buttons = new MenuButton[]{back, quit};
        Menu pause = new Menu(GameDriver.getGameWindowOffset().deepClone(), true, "background_pause",
                InputManager.getLiveInputTable(), buttons, mainBoard);
        menus.put("pause", pause);
    }

    private static void readBlackboard(){
        for(BoardMessage b : mainBoard.getMessages()){
            String msg = b.getMessage();
            String cmd;
            String args;
            if(msg.contains(" ")){
                //does not contain character at end index
                cmd = msg.substring(0, msg.indexOf(' '));
                args = msg.substring(msg.indexOf(' ') + 1);
            }
            else{
                cmd = msg;
                args = "";
            }
            switch(cmd){
                case "command_back":
                    command_back();
                    break;
                case "command_back_to_menu":
                    command_back_to_menu();
                    break;
                case "command_back_to_main_menu":
                    command_back_to_main_menu();
                    break;
                case "command_back_to_load_screen":
                    command_back_to_load_screen();
                    break;
                case "command_add_menu":
                    command_add_menu(args);
                    break;
                case "command_start_game":
                    command_start_game(args);
                    break;
                case "command_add_game":
                    command_add_game(args);
                    break;
                case "command_continue_game":
                    command_continue_game(args, b.getData());
                    break;
                case "command_add_continue_game":
                    command_add_continue_game(args, b.getData());
                    break;
                case "command_add_dialogue":
                    command_add_dialogue(args);
                    break;
                case "command_add_credits":
                    command_add_credits();
                    break;
                case "command_set_track":
                    command_set_track(args);
                    break;
                case "command_unlock_extra":
                    command_unlock_extra();
                    break;
                case "command_toggle_sound":
                    command_toggle_sound();
                    break;
                case "command_exit":
                    command_exit();
                    break;
            }
        }
        mainBoard.update();
    }

    //pops 1 off the stack
    private static void command_back(){
        if(menuObjectStack.size() > 1){
            menuObjectStack.pop();
        }
        //when its a menu but not the pause play the menu music
        if(menuObjectStack.peek() instanceof Menu && !(menuObjectStack.peek() == menus.get("pause"))){
            playMenuTrack();
        }
    }
    //goes back to first non-pause menu
    private static void command_back_to_menu(){
        //while it's not a menu or if the menu is pause i.e. the pause gets popped
        while(!(menuObjectStack.peek() instanceof Menu) || (menuObjectStack.peek() == menus.get("pause"))){
            menuObjectStack.pop();
        }
        playMenuTrack();
    }
    //goes to the lowest menu
    private static void command_back_to_main_menu(){
        while(menuObjectStack.size() > 1){
            menuObjectStack.pop();
        }
        playMenuTrack();
    }
    //goes to loadScreen or first non-pause menu
    private static void command_back_to_load_screen(){
        //either goes to loadScreen or menu; if there is no loadscreen underneath it stops at the first non-pause menu
        while(!(menuObjectStack.peek() instanceof Menu) || (menuObjectStack.peek() == menus.get("pause"))){
            if(menuObjectStack.peek() != loadScreen) {
                menuObjectStack.pop();
            }
            else{
                break;
            }
        }
        MusicDriver.stopTrack();
        if(menuObjectStack.peek() instanceof Menu && !(menuObjectStack.peek() == menus.get("pause"))){
            playMenuTrack();
        }
    }
    private static void command_add_menu(String key){
        Menu toAdd = menus.get(key);
        //reset the pause menu every time
        if(key.equals("pause")){
            toAdd.reset();
        }
        menuObjectStack.push(toAdd);
    }
    //adds loadScreen and starts the start game timer
    private static void command_start_game(String key){
        menuObjectStack.push(loadScreen);
        loadScreen.startTimer(40, new BoardMessage("command_add_game " + key, loadScreen));
    }
    //starts game based on key
    private static void command_add_game(String key){
        //add_game command should be impossible to call if a game is running, so not gonna add code to remove existing gameframes

        //level, difficulty, shot, campaign
        if(key.contains("extra")){
            int shot = Integer.parseInt("" + key.charAt(key.length() - 1));
            //campaign to true
            GameDriver.initGame(7, -1, shot, true);
        }
        else if(key.contains("campaign")) {
            int shot = Integer.parseInt("" + key.charAt(key.length() - 1));
            int difficulty = Integer.parseInt("" + key.charAt(key.indexOf('_') + 1));
            GameDriver.initGame(1, difficulty, shot, true);
        }
        else if(key.contains("practice")){
            int shot = Integer.parseInt("" + key.charAt(key.length() - 1));
            int level = Integer.parseInt("" + key.charAt(key.indexOf('_') + 1));
            key = key.substring(key.indexOf('_') + 1);
            int difficulty = Integer.parseInt("" + key.charAt(key.indexOf('_') + 1));
            GameDriver.initGame(level, difficulty, shot, false);
        }

        //gameframe interfaces between the gameDriver and the menu stack
        menuObjectStack.push(new GameFrame(InputManager.getLiveInputTable(), mainBoard, new Random()));
    }
    private static void command_continue_game(String key, Object[] data){
        //go back to loadscreen
        command_back_to_load_screen();
        //have loadscreen ready up a new game
        loadScreen.startTimer(120, new BoardMessage("command_add_continue_game " + key, loadScreen, data));
    }
    private static void command_add_continue_game(String key, Object[] data){
        Component_Player playComp = (Component_Player)data[0];
        int shot = Integer.parseInt("" + key.charAt(key.length() - 1));
        int level = Integer.parseInt("" + key.charAt(key.indexOf('_') + 1));
        key = key.substring(key.indexOf('_') + 1);
        int difficulty = Integer.parseInt("" + key.charAt(key.indexOf('_') + 1));
        GameDriver.initGame(level, difficulty, shot, playComp);

        menuObjectStack.push(new GameFrame(InputManager.getLiveInputTable(), mainBoard));
    }
    private static void command_add_dialogue(String key){
        menuObjectStack.push(new DialogueScreen(key, InputManager.getLiveInputTable(), mainBoard));
    }
    private static void command_add_credits(){
        menuObjectStack.push(creditsScreen);
        creditsScreen.resetTimer();
        MusicDriver.playTrack("credits", false);
    }
    private static void command_set_track(String args){
        int num = Integer.parseInt(args);
        String t;
        switch(num){
            case 1:
                t = "menu";
                break;
            case 2:
                t = "stage1";
                break;
            case 3:
                t = "boss1";
                break;
            case 4:
                t = "stage2";
                break;
            case 5:
                t = "boss2";
                break;
            case 6:
                t = "stage3";
                break;
            case 7:
                t = "boss3";
                break;
            case 8:
                t = "stage4";
                break;
            case 9:
                t = "boss4";
                break;
            case 10:
                t = "stage5";
                break;
            case 11:
                t = "boss5";
                break;
            case 12:
                t = "stage6";
                break;
            case 13:
                t = "boss6";
                break;
            case 14:
                t = "stage7";
                break;
            case 15:
                t = "boss7";
                break;
            case 16:
            default:
                t = "credits";
                break;
        }
        MusicDriver.playTrack(t, false);
    }
    //same function as back_to_main_menu, maybe delete?
    private static void command_clear(){
        //presume main menu is the base
        while(menuObjectStack.size() > 1){
            menuObjectStack.pop();
        }
    }
    private static void command_unlock_extra(){
        //checks to see if extra is currently unlocked
        if(ResourceManager.isExtraUnlocked()){
            return;
        }
        //if not, tell ResourceManager to swap the boolean
        //and unlock the button on the main menu
        ResourceManager.unlockExtra();
        menus.get("main").getButtons()[1].setAvailable(true);
    }
    private static void command_toggle_sound(){
        ResourceManager.toggleSound();
        //routed to sound driver by resource manager
    }
    //quits game
    private static void command_exit(){
        System.exit(0);
    }

    private static void playMenuTrack(){
        MusicDriver.playTrack("menu", true);
    }

    //didn't want to use this but beggars can't be choosers
    public static Blackboard getMainBoard(){
        return mainBoard;
    }
}
import java.util.ArrayList;

public class System_Player implements ECSSystem {

    private static final ComponentFilter COMPFILTER = new ComponentFilter(new String[]{"player"});
    private static final int NO_INPUT_TIME_BASE = 60, NO_HIT_TIME_BASE = 60 * 4;
    private static final double TRANSPARENCY = .8;

    public System_Player(){
    }

    @Override
    public void update(ArrayList<Entity> entities){
        for(Entity e : entities){
            Component_Player pComp = (Component_Player)ComponentManager.getComponent("player", e.getEntityID());
            //ticking noHit
            if(pComp.getNoHitTimer() > 0){
                pComp.setNoHitTimer(pComp.getNoHitTimer() - 1);
            }

            //ticking noInput
            if(pComp.getNoInputTimer() > 0){
                pComp.setNoInputTimer(pComp.getNoInputTimer() - 1);
            }


            //read blackboard
            for(BoardMessage m : (ArrayList<BoardMessage>)SystemManager.getECSBoard().getMessages().clone()){
                if(m.getSender().equals("gameplay")){
                    switch(m.getMessage()){
                        case "bombPress":
                            bombPress(pComp);
                            break;
                        case "playerDeath":
                            playerDeath(pComp, e);
                            break;
                        case "playerRespawn":
                            if(m.getLifetime() == 1) {
                                playerRespawn(e);
                            }
                            break;
                        case "playerReset":
                            if(m.getLifetime() == 1) {
                                playerReset(pComp, e);
                            }
                            break;
                        case "gameLoss":
                            if(m.getLifetime() == 1){
                                gameLoss();
                            }
                    }
                }
            }
        }
    }

    private void bombPress(Component_Player pComp){
        if (pComp.getBombs() > 0) {
            pComp.setBombs(pComp.getBombs() - 1);
        }
    }
    private void playerDeath(Component_Player pComp, Entity e){

        //if we press the bomb just this tick then don't die
        //the bombPress message would just come after

        //this prevents the case where the bomb is pressed on the same tick as playerDeath gets parsed
        //since spawning system comes before player - the bomb IS just pressed under those circumstances
        for(BoardMessage m : (ArrayList<BoardMessage>)SystemManager.getECSBoard().getMessages().clone()){
            if(m.getSender().equals("gameplay")){
                if(m.getMessage().equals("bombPress")){
                    return;
                }
            }
        }

        //this should prevent double death on the same tick
//        if(pComp.getNoInputTimer() == NO_INPUT_TIME_BASE){
//            return;
//        }
        if(!pComp.canInput() || !pComp.canBeHit()){
            return;
        }


        pComp.setNoInputTimer(NO_INPUT_TIME_BASE);
        pComp.setNoHitTimer(NO_INPUT_TIME_BASE + NO_HIT_TIME_BASE);

        //kill attack
        Component_Spawning spawnComp = (Component_Spawning)ComponentManager.getComponent("spawning", e.getEntityID());
        spawnComp.clear();

        //transparent graphics
        Component_Graphics oldGComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
        SImage frame = (SImage)oldGComp.getCurrentSImage();
//        frame.setTransparency(TRANSPARENCY);
        ComponentManager.setComponent(new Component_Graphics(frame), e.getEntityID());

        //reduce and spawn in power
        int pow = pComp.getPower();
        int loss = powerLoss(pow);
        pComp.setPower(pow - loss);
        int remain = loss/2;

        ArrayList<SpawningInstruction> si = new ArrayList<>();
        while(remain > 0){
            if(remain >= 8){
                si.add(new SISingleSpawn("pickup_player_large"));
                remain -= 8;
            }
            else{
                si.add(new SISingleSpawn("pickup_player_small"));
                --remain;
            }
        }
        SpawnUnit su = new SpawnUnit(1, false, si);
        spawnComp.addUnit(su);


        if(pComp.getLives() > 0){
            pComp.setLives(pComp.getLives() - 1);
            SystemManager.getECSBoard().write(new BoardMessage("playerRespawn", 30, "gameplay"));
            frame.setTransparency(TRANSPARENCY);
        }
        else{
            //continue screen instead of playerReset (the yes option would call playerReset)
            //no time to implement continues -> instead we'll just end the game
//            SystemManager.getECSBoard().write(new BoardMessage("playerReset", 30, "gameplay"));
            SystemManager.getECSBoard().write(new BoardMessage("gameLoss", 140, "gameplay"));
            frame.setTransparency(0);

            // prevent player from shooting while game over lmao
            pComp.setNoInputTimer(1000);
            pComp.setNoHitTimer(1000);
        }
        //internalmessage comes right after - so lifetime 1
        //this tells internalmessage to clear playerDeath messges to prevent double death
        SystemManager.getECSBoard().write(new BoardMessage("playerDeathLastTick", 1, "gameplay"));
    }
    private void playerRespawn(Entity e){
        //reset position
        Component_Position posComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        //reset to have homing shit go on the old one, new homing shit goes on new one
        posComp.resetPosition(GameDriver.getInitPlayerPosition());
        GameDriver.setCurrentPlayerPosition(posComp.getPosition());
        //reset graphics
        ComponentManager.setComponent(newGraphicsComp(), e.getEntityID());
    }
    private void playerReset(Component_Player pComp, Entity e){
        //reset position
        Component_Position posComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        posComp.resetPosition(GameDriver.getInitPlayerPosition());
        GameDriver.setCurrentPlayerPosition(posComp.getPosition());
        //reset l/b
        pComp.setLives(GameDriver.getInitLives());
        pComp.setBombs(GameDriver.getInitBombs());
        //reset graphics
        ComponentManager.setComponent(newGraphicsComp(), e.getEntityID());
    }
    //this doesn't really belong in this class but, this is the end of development
    private void gameLoss(){
        if(GameDriver.getCurrentCampaign()) {
            MenuManager.getMainBoard().write(new BoardMessage("command_back_to_main_menu", this));
        }else{
            MenuManager.getMainBoard().write(new BoardMessage("command_back_to_menu", this));
        }
    }

    private int powerLoss(int initPower){
        int minLoss = 4;
        int maxLoss = 19;
        if(initPower <= minLoss){
            return initPower;
        }

        int loss = (initPower / 4);
        if (loss < minLoss) {
            loss = minLoss;
        } else if (loss > maxLoss) {
            loss = maxLoss;
        }
        if (GameDriver.randomDouble() > .5) {
            ++loss;
        }
        return loss;
    }

    private Component_GraphicsMultiSprite newGraphicsComp(){
        ArrayList<SImage> sImages = new ArrayList<>();
        Vector offset = new Vector(-39, -40);

        sImages.add(new SImage("p_left_2", new Vector(), offset.deepClone()));
        sImages.add(new SImage("p_left_1", new Vector(), offset.deepClone()));
        sImages.add(new SImage("p_idle", new Vector(), offset.deepClone()));
        sImages.add(new SImage("p_right_1", new Vector(), offset.deepClone()));
        sImages.add(new SImage("p_right_2", new Vector(), offset.deepClone()));
        return new Component_GraphicsMultiSprite(sImages, 2);
    }

    @Override
    public ComponentFilter getCompfilter(){
        return COMPFILTER;
    }

    public static ComponentFilter getCompFilter(){
        return COMPFILTER;
    }
}

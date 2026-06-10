import java.util.ArrayList;
import java.util.Iterator;

public class System_EntityBehavior implements ECSSystem {

    private static final ComponentFilter COMPFILTER = new ComponentFilter(new String[]{"behavior"});

    private GameplayMessageTable gmTable;
    public System_EntityBehavior(){
        gmTable = new GameplayMessageTable(SystemManager.getECSBoard());
    }

    @Override
    public void update(ArrayList<Entity> entities) {
        //read the ECS blackboard for removed behaviors
        ArrayList<Entity> toRemove = new ArrayList<>();
        for(BoardMessage bm : SystemManager.getECSBoard().getMessages()){
            if(bm.getMessage().equals("remove_behaviors")){
                toRemove.add((Entity)(bm.getData()[0]));
            }
        }
        if(!toRemove.isEmpty()) {
            entities.removeAll(toRemove);
            for(Entity e : toRemove){
                ComponentManager.deleteComponent("behavior", e.getEntityID());
            }
        }

        gmTable.reset();
        gmTable.parseMessages(SystemManager.getECSBoard());

        for(Entity e : entities){
            Component_Behavior bComp = (Component_Behavior)ComponentManager.getComponent("behavior", e.getEntityID());
            Iterator<Behavior> iter = bComp.getBehaviors().iterator();

            //run through behaviors for this entity
            while(iter.hasNext()){
                //remove if act returns true
                if(act(iter.next(), e)){
                    iter.remove();
                }
            }
        }
    }

    //true = behavior finished
    //false = behavior continuing
    private boolean act(Behavior b, Entity e){
        switch (b.getMode()) {
            case "routine":
                return routine((Routine) b, e);
            case "boss_routine":
                return bossRoutine((Routine)b, e);
            case "timer":
                return timer((BehaviorVector) b);
            case "deactivate":
                return deactivate(e);
            case "delete_component":
                return deleteComponent((BehaviorString)b, e);
            case "write_message":
                return writeMessage((BehaviorBM)b);
            case "write_game_message":
                return writeGameMessage((BehaviorInt)b);
            case "end_game":
                return endGame();

            //boundary detects whether or not you are past
//            case "boundary":
//                return boundary((BehaviorDouble)b, e);
            case "boundary_top":
                return boundary((BehaviorDouble)b, e, 0);
            case "boundary_bot":
            case "boundary_bottom":
                return boundary((BehaviorDouble)b, e, 1);
            case "boundary_left":
                return boundary((BehaviorDouble)b, e, 2);
            case "boundary_right":
                return boundary((BehaviorDouble)b, e, 3);

            //movement
            case "add_MM":
            case "add_mm":
                return addMM((BehaviorMM)b, e);
            case "set_MM":
            case "set_mm":
                return setMM((BehaviorMM)b, e);
            case "boss_point":
                return bossPoint((BehaviorDouble)b, e);
            case "boss_point_limited":
                return bossPointLimited((BehaviorDouble)b, e);
            case "set_velocity":
                return set_velocity((BehaviorVector)b, e);
            case "set_polar":
                return set_polar((BehaviorVector)b, e);
            case "set_polar_to_player":
                return set_polar_to_player((BehaviorDouble) b, e);
            case "clear_movement":
                return clearMovement(e);
            case "continue_trajectory":
                return continueTrajectory(e);
            case "continue_trajectory_bounce":
                return continueTrajectoryBounce(e);
            case "continue_trajectory_ignore_orbit":
                return continueTrajectoryIgnoreOrbit((BehaviorString)b, e);
            case "continue_trajectory_gravity"://gives a movemode gravity, all we do is set the vector
                return continueTrajectoryGravity((BehaviorMM)b, e);
            case "set_speed":
                return setSpeed((BehaviorDouble) b, e);

            //spawning
            case "add_spawn_unit":
            case "add_su":
            case "add_SU":
                return addSpawnUnit((BehaviorSU)b, e);
            case "set_spawn_unit":
            case "set_su":
            case "set_SU":
                return setSpawnUnit((BehaviorSU)b, e);
            case "single_spawn":
                return singleSpawn((BehaviorString)b, e);
            case "clear_spawning":
                return clearSpawning(e);

            //dspawn
            case "set_death_spawn":
            case "set_dspawn":
                return setDeathSpawn((BehaviorString)b, e);

            //collision
            case "set_hp":
            case "set_HP":
                return setHP((BehaviorDouble)b, e);
            case "subtract_health":
            case "subtract_hp":
            case "subtract_HP":
                return subtractHealth((BehaviorDouble)b, e);

            case "wait_until_death":
                return waitUntilDeath((BehaviorString)b);
            case "wait_near_position":
            case "wait_near_pos":
                return waitNearPosition((BehaviorVector)b, e);
            case "wait_near_angle":
                return waitNearAngle((BehaviorDouble)b, e);
            case "wait_dialogue_over":
            case "wait_dialogue_end":
                return waitDialogueOver();
            case "wait_collision":
                return waitCollision((BehaviorString)b, e);
            case "wait_game_message":
                return waitGameMessage((BehaviorInt)b);
            case "kill_movement_modes_near_angle":
                return killMovementModesNearAngle((BehaviorDouble) b, e);

            //specialized gameplay
            case "wasp_homing_behavior":
                return waspHomingBehavior((BehaviorString)b, e);


            //graphics
            case "rotate_sprite_with_angle":
                return rotateSpriteWithAngle(e);
            case "rotate_sprite":
                return rotateSprite((BehaviorDouble)b, e);

            case "background_scroll":
                return backgroundScroll((BehaviorVector)b, e);
            case "player_graphics":
                return playerGraphics((BehaviorInt)b, e);
            case "boss_graphics_triple_sprite":
                return bossGraphicsTripleSprite(e);
            case "boss_graphics_triple_anim":
                return bossGraphicsTripleAnim((BehaviorInt)b, e);
            case "hp_bar_graphics":
                return hpBarGraphics((BehaviorHPBar) b, e);

        }
        return true;
    }

    private boolean routine(Routine r, Entity e){
        //act on the current index
        ArrayList<Behavior> behaviors = r.getBehaviors();

        if(r.getIndex() >= behaviors.size()){
            return true;
        }

        //lets limit the max number of iterations
        int iterations = 0;
        while(act(behaviors.get(r.getIndex()), e)){
            if(iterations > 50){
                break;
            }
            //any execution inside this while loop means that particular behavior has finished
            int newIndex = r.getIndex() + 1;
            if(newIndex >= behaviors.size()){
                if(r.isLooping()){
                    newIndex = 0;
                }
                else{
                    //testing this out here
                    r.setIndex(newIndex);
                    return true;
                }
            }
            r.setIndex(newIndex);
            iterations++;
        }
        return false;
    }
    private boolean bossRoutine(Routine r, Entity e){
        Component_Collision cComp = (Component_Collision)ComponentManager.getComponent("collision", e.getEntityID());
        //test for hp
        if(cComp != null){
            double hp = cComp.getHp();
            if(hp <= 0){
                SystemManager.getECSBoard().write(new BoardMessage("boss_phase_over", 2, "gameplay"));
                return true;
            }
        }
        //the only case where bossRoutine returns true is if hp <= 0;
        routine(r, e);
        return false;
    }

    private boolean timer(BehaviorVector b){
        Vector v = b.getV();
        int time = (int)v.getA();
        if(time <= 0){
            v.setA(v.getB());
            return true;
        }
        v.setA(--time);
        return false;
    }
    private boolean deactivate(Entity e){
        e.deactivate();
        return true;
    }
    private boolean deleteComponent(BehaviorString b, Entity e){
        ComponentManager.deleteComponent(b.getString(), e.getEntityID());
        return true;
    }
    private boolean writeMessage(BehaviorBM b){
        BoardMessage toWrite = b.getBm();
        if(toWrite != null){
            SystemManager.getECSBoard().write(toWrite);
        }
        return true;
    }
    private boolean writeGameMessage(BehaviorInt b){
        int i = b.getInt();
        if(i < 0 || i >= 8){
            return true;
        }
        SystemManager.getECSBoard().write(new BoardMessage("game_message", 2, "gameplay", new Object[]{i}));
        return true;
    }
    //if campaign enter credits screen - else go back to menu
    private boolean endGame(){
        if(GameDriver.getCurrentCampaign()){
            MenuManager.getMainBoard().write(new BoardMessage("command_add_credits", this));
            //extra stage unlock if difficulty >= normal
            if(GameDriver.getCurrentDifficulty() >= 1) {
                MenuManager.getMainBoard().write(new BoardMessage("command_unlock_extra", this));
            }
        }else{
            MenuManager.getMainBoard().write(new BoardMessage("command_back_to_menu", this));
        }
        // uncomment to test if credits works using practice :3
        // MenuManager.getMainBoard().write(new BoardMessage("command_add_credits", this));
        return true;
    }
    //doesn't work - need to give double (bound large needs - bound) -> will just workaround fuckin hell
//    private boolean boundary(BehaviorDouble b, Entity e){
//        return boundary(b, e, 0) || boundary(b, e, 1) || boundary(b, e, 2) || boundary(b, e, 3);
//    }
    private boolean boundary(BehaviorDouble b, Entity e, int dir){
        //0 = top, 1 = bot, 2 = left, 3 = right
        Component_Position pComp= (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        double coord;
        if(dir == 0 || dir == 1){
            coord = pComp.getPrevPosition().getB();
        }
        else{
            coord = pComp.getPrevPosition().getA();
        }

        double bound = b.getD();

        //top and left = smaller
        if(dir == 0 || dir == 2){
//            if(coord <= bound){
//            }
            return coord <= bound;
        }
        //bot and right = larger
        else{
//            if(coord >= bound){
//            }
            return coord >= bound;
        }
    }
    //will create a new mComp if necessary
    private boolean addMM(BehaviorMM b, Entity e){
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        mComp.addMode(b.getMM());
        return true;
    }
    //will create a new mComp if necessary
    private boolean setMM(BehaviorMM b, Entity e){
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else {
            mComp.clear();
        }
        mComp.addMode(b.getMM());
        return true;
    }
    //will create a new mComp if necessary
    //the double value is for time in ticks
    private boolean bossPoint(BehaviorDouble b, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        Vector pos = pComp.getPrevPosition();
        double time = b.getD();
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        double distOut = 100;
        AABB near = new AABB(pos.getA() - distOut, pos.getA() + distOut, pos.getB() - distOut, pos.getB() + distOut);
        AABB bounds = new AABB(100, GameDriver.getGameWidth()-100, 75, 250);
        Vector dest = pos.deepClone();
        while(MiscUtil.vectorCollidesAABB(near, dest)){
            dest = new Vector(MiscUtil.rangeRandom(bounds.getXlow(), bounds.getXhigh(), GameDriver.randomDouble()),
                    MiscUtil.rangeRandom(bounds.getYlow(), bounds.getYhigh(), GameDriver.randomDouble()));
        }
        double dist = Vector.getDistance(pos, dest);
        double speed = dist/time;
        mComp.addMode(new MoveModeAddPoint("point", dest, speed));
        return true;
    }
    //limited version of this -> more space between boss and walls
    private boolean bossPointLimited(BehaviorDouble b, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        Vector pos = pComp.getPrevPosition();
        double time = b.getD();
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        double distOut = 100;
        AABB near = new AABB(pos.getA() - distOut, pos.getA() + distOut, pos.getB() - distOut, pos.getB() + distOut);
        AABB bounds = new AABB(130, GameDriver.getGameWidth()-130, 130, 250);
        Vector dest = pos.deepClone();
        while(MiscUtil.vectorCollidesAABB(near, dest)){
            dest = new Vector(MiscUtil.rangeRandom(bounds.getXlow(), bounds.getXhigh(), GameDriver.randomDouble()),
                    MiscUtil.rangeRandom(bounds.getYlow(), bounds.getYhigh(), GameDriver.randomDouble()));
        }
        double dist = Vector.getDistance(pos, dest);
        double speed = dist/time;
        mComp.addMode(new MoveModeAddPoint("point", dest, speed));
        return true;
    }
    //will create a new mComp if necessary
    private boolean set_velocity(BehaviorVector b, Entity e){
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else {
            mComp.clear();
        }
        mComp.addMode(new MoveModeAddVector("velocity", b.getV()));
        return true;
    }
    //will create a new mComp if necessary
    private boolean set_polar(BehaviorVector b, Entity e){
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else {
            mComp.clear();
        }
        mComp.addMode(new MoveModeAddVector("polar", b.getV()));
        return true;
    }
    //will create a new mComp if necessary
    //double = speed
    private boolean set_polar_to_player(BehaviorDouble b, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else {
            mComp.clear();
        }
        Vector pos = pComp.getPrevPosition();
        mComp.addMode(new MoveModeAddVector("polar", new Vector(Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition()), b.getD())));
        return true;
    }
    private boolean clearMovement(Entity e){
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp != null){
            mComp.clear();
        }
        return true;
    }
    //will create a new mComp if necessary
    private boolean continueTrajectory(Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else{
            mComp.clear();
        }
        mComp.addMode(new MoveModeAddVector("velocity", Vector.subtract(pComp.getPosition(), pComp.getPrevPosition())));
        return true;
    }
    private boolean continueTrajectoryBounce(Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else{
            mComp.clear();
        }
        Vector vel = Vector.subtract(pComp.getPosition(), pComp.getPrevPosition());
        mComp.addMode(new MoveModeAddBounce("bounce", vel, 1));
        return true;
    }
    private boolean continueTrajectoryIgnoreOrbit(BehaviorString id, Entity e){
        Component_Position idComp = (Component_Position)ComponentManager.getComponent("position", id.getString());
        if(idComp == null){
            return continueTrajectory(e);
        }

        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else{
            mComp.clear();
        }

        Vector ourVelocity = Vector.subtract(pComp.getPosition(), pComp.getPrevPosition());
        Vector centerVelocity = Vector.subtract(idComp.getPosition(), idComp.getPrevPosition());
        Vector newVelocity = Vector.subtract(ourVelocity, centerVelocity);

        mComp.addMode(new MoveModeAddVector("velocity", newVelocity));
        return true;
    }
    private boolean continueTrajectoryGravity(BehaviorMM b, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        MoveModeAddGravity mm;
        if(b.getMM() instanceof MoveModeAddGravity){
            mm = (MoveModeAddGravity)b.getMM();
        }else{
            return true;
        }
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp == null){
            mComp = new Component_Movement();
            ComponentManager.setComponent(mComp, e.getEntityID());
        }
        else{
            mComp.clear();
        }
        Vector vel = Vector.subtract(pComp.getPosition(), pComp.getPrevPosition());
        mm.setVector(vel);
        mComp.addMode(mm);
        return true;
    }
    private boolean setSpeed(BehaviorDouble b, Entity e){
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        if(mComp != null){
            mComp.addMode(new MoveModeTransDouble("speed", b.getD()));
        }
        return true;
    }

    private boolean addSpawnUnit(BehaviorSU b, Entity e){
        Component_Spawning sComp = (Component_Spawning)ComponentManager.getComponent("spawning", e.getEntityID());
        if(sComp == null){
            sComp = new Component_Spawning();
            ComponentManager.setComponent(sComp, e.getEntityID());
        }
        sComp.addUnit(b.getSU());
        return true;
    }
    private boolean setSpawnUnit(BehaviorSU b, Entity e){
        Component_Spawning sComp = (Component_Spawning)ComponentManager.getComponent("spawning", e.getEntityID());
        if(sComp == null){
            sComp = new Component_Spawning();
            ComponentManager.setComponent(sComp, e.getEntityID());
        }
        sComp.clear();
        sComp.addUnit(b.getSU());
        return true;
    }
    //end of dev - otherwise would have set and add variants
    private boolean singleSpawn(BehaviorString b, Entity e){
        Component_Spawning sComp = (Component_Spawning)ComponentManager.getComponent("spawning", e.getEntityID());
        if(sComp == null){
            sComp = new Component_Spawning();
            ComponentManager.setComponent(sComp, e.getEntityID());
        }
        SpawnUnit su = new SpawnUnit(1, false, new SISingleSpawn(b.getString()));
        sComp.addUnit(su);
        return true;
    }
    private boolean clearSpawning(Entity e){
        Component_Spawning sComp = (Component_Spawning)ComponentManager.getComponent("spawning", e.getEntityID());
        if(sComp != null){
            sComp.clear();
        }
        return true;
    }

    private boolean setDeathSpawn(BehaviorString b, Entity e){
        ComponentManager.setComponent(new Component_DeathSpawn(b.getString()), e.getEntityID());
        return true;
    }

    private boolean setHP(BehaviorDouble b, Entity e){
        Component_Collision cComp = (Component_Collision)ComponentManager.getComponent("collision", e.getEntityID());
        if(cComp == null){
            return true;
        }
        cComp.setHp(b.getD());
        return true;
    }
    //used b5
    private boolean subtractHealth(BehaviorDouble b, Entity e){
        Component_Collision cComp = (Component_Collision)ComponentManager.getComponent("collision", e.getEntityID());
        if(cComp == null){
            return false;
        }
        double oldHP = cComp.getHp();
        cComp.setHp(oldHP - b.getD());
        return false;
    }

    private boolean waitUntilDeath(BehaviorString b){
        String testEntityID = b.getString();
        //if the entity is  null returns true (which means behavior is over)
        return EntityManager.getEntity(testEntityID) == null;
    }
    private boolean waitNearPosition(BehaviorVector b, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return true;
        }
        Vector pos = pComp.getPrevPosition();
        Vector check = b.getV();
        return Vector.getDistance(pos, check) < .1;
    }
    private boolean waitNearAngle(BehaviorDouble b, Entity e){
        double margin = 5;
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        //under these circumstances since there is no move modes to kill i will just end the behavior
        if(pComp == null){
            return true;
        }

        //calculate the angle with the prevPos and truePos of the pcomp
        Vector prevPos = pComp.getPrevPosition();
        Vector truePos = pComp.getPosition();

        double angle = Vector.getAngle(prevPos, truePos);
        double diff = Math.abs(angle - b.getD());
        return diff < margin;
    }
    private boolean waitDialogueOver(){
        for(BoardMessage bm : MenuManager.getMainBoard().getMessages()){
            if(bm.getSender() instanceof DialogueScreen){
                if(bm.getMessage().equals("dialogue_over")){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean waitCollision(BehaviorString b, Entity e){
        String type = b.getString();
        type = "collidedLastTick" + type;
        String id = e.getEntityID();
        for(BoardMessage bm : SystemManager.getECSBoard().getMessages()){
            //checks for the correct type
            if(bm.getMessage().equalsIgnoreCase(type)){
                Object[] ids = bm.getData();
                String id1 = (String)ids[0];
                String id2 = (String)ids[1];
                //if either of the ids match is what we're looking for
                if(id1.equals(id)){
                    return true;
                }
                if(id2.equals(id)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean waitGameMessage(BehaviorInt b){
        int i = b.getInt();
        if(i < 0 || i >= 8){
            return true;
        }
        return isMessagePresent(i);
    }
    private boolean killMovementModesNearAngle(BehaviorDouble b, Entity e){
        double margin = 5;
        Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        //under these circumstances since there is no move modes to kill i will just end the behavior
        if(mComp == null || pComp == null){
            return true;
        }

        //calculate the angle with the prevPos and truePos of the pcomp
        Vector prevPos = pComp.getPrevPosition();
        Vector truePos = pComp.getPosition();

        double angle = Vector.getAngle(prevPos, truePos);
        double diff = Math.abs(angle - b.getD());
        if(diff < margin){
            double distance = Vector.getDistance(prevPos, truePos);
            //remove all modes from mComp
            mComp.clear();
            //add normal addMode
            mComp.addMode(new MoveModeAddVector("polar", new Vector(angle, distance)));
            return true;
        }
        return false;
    }
    //reads for a collision between big and enemy/boss, and then sets movementmode
    //parses blackboard but theoretically only once since the preceding behavior is a waitUntilDeath
    //and big cannot die before small unless it's collided (theoretically... but if it does this will just not fire)
    private boolean waspHomingBehavior(BehaviorString b, Entity e){
        Component_Position myPos = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(myPos == null){
            return true;
        }

        String bigID = b.getString();
        String collidedID = null;
        for(BoardMessage bm : SystemManager.getECSBoard().getMessages()) {
            if (bm.getSender().equals("gameplay")) {
                if (bm.getMessage().equals("collidedLastTickPlayerPEnemy") || bm.getMessage().equals("collidedLastTickPlayerPBoss")) {
                    //the data in collidedLastTick messages are in format [id1, id2] in accordance with the order of the types
                    if (bm.getData()[0].equals(bigID)) {
                        collidedID = (String) bm.getData()[1];
                        break;
                    }
                }
            }
        }
        if(collidedID != null){
            Entity collided = EntityManager.getEntity(collidedID);
            if(collided != null){
                Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", collidedID);
                if(pComp != null){
                    double angle = Vector.getAngle(myPos.getPrevPosition(), pComp.getPrevPosition());
                    //hardcoding speed for wasps
                    double speed = 20;
                    //this is what setComponent is for haha
                    ComponentManager.setComponent(new Component_Movement(new MoveModeAddVector("polar", new Vector(angle, speed))), e.getEntityID());
                }
            }
        }
        return true;
    }

    private boolean rotateSpriteWithAngle(Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());

        if(pComp == null || gComp == null){
            return true;
        }

        double angle = Vector.getAngle(pComp.getPrevPosition(), pComp.getPosition());
        gComp.getCurrentSImage().setRotation(MiscUtil.angleToSpriteRotation(angle));
        return false;
    }
    private boolean rotateSprite(BehaviorDouble b, Entity e){
        Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
        if(gComp == null){
            return true;
        }
        double angle = gComp.getCurrentSImage().getRotation();
        angle += b.getD();
        gComp.getCurrentSImage().setRotation(angle);
        return false;
    }
    private boolean playerGraphics(BehaviorInt b, Entity e){
        //positions
        //0 = far left
        //4 = far right

        Component_GraphicsMultiSprite gComp;
        //the reason for this try/catch block is to implement transparenting the player upon death - we swap the gComp
        try {
             gComp = (Component_GraphicsMultiSprite) ComponentManager.getComponent("graphics", e.getEntityID());
        }catch(Exception ex){
            return false;
        }
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(gComp == null || pComp == null){
            //i have no idea when this will happen but suffice to say - if it ever comes back it should at least work
            return false;
        }

        //figure out what direction we're headed
        double xChange = Vector.subtract(pComp.getPrevPosition(), pComp.getOldPosition()).getA();
        int tendsTowards;
        if(Math.abs(xChange) <= .5){
            tendsTowards = 2;
        }
        else if(xChange < 0){
            tendsTowards = 0;
        }
        else if (xChange > 0) {
            tendsTowards = 4;
        }
        else{
            tendsTowards = 2;
        }

        int pos = b.getInt();

        //if we are in the correct position don't change anything
        if(pos == tendsTowards){
            return false;
        }

        //new pos
        if(pos < tendsTowards){
            ++pos;
            b.setInt(pos);
        }
        else{
            --pos;
            b.setInt(pos);
        }
        //send new sprite over
        gComp.setCurrentSImage(pos);
        //will start a timer
        return true;
    }
    private boolean bossGraphicsTripleSprite(Entity e){
        //positions
        //0 = idle
        //1 = left
        //2 = right

        Component_GraphicsMultiSprite gComp;
        try {
            gComp = (Component_GraphicsMultiSprite) ComponentManager.getComponent("graphics", e.getEntityID());
        }catch(Exception ex){
            return false;
        }
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(gComp == null || pComp == null){
            return false;
        }
        double xChange = Vector.subtract(pComp.getPrevPosition(), pComp.getOldPosition()).getA();
        int newSpritePos;
        //buffer for idle
        if(Math.abs(xChange) <= 1){
            newSpritePos = 0;
        }
        else if(xChange < 0){
            newSpritePos = 1;
        }
        else {
            newSpritePos = 2;
        }

        //set the new animation
        gComp.setCurrentSImage(newSpritePos);
        return false;
    }
    private boolean bossGraphicsTripleAnim(BehaviorInt b, Entity e){
        //positions
        //0 = idle
        //1 = left
        //2 = right

        Component_GraphicsMultiAnim gComp;
        try {
            gComp = (Component_GraphicsMultiAnim) ComponentManager.getComponent("graphics", e.getEntityID());
        }catch(Exception ex){
            return false;
        }
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(gComp == null || pComp == null){
            return false;
        }
        double xChange = Vector.subtract(pComp.getPrevPosition(), pComp.getOldPosition()).getA();
        int newSpritePos;
        //buffer for idle
        if(Math.abs(xChange) <= 1){
            newSpritePos = 0;
        }
        else if(xChange < 0){
            newSpritePos = 1;
        }
        else {
            newSpritePos = 2;
        }

        int oldSpritePos = b.getInt();
        if(oldSpritePos == newSpritePos){
            return false;
        }

        //reset animation
        gComp.setTick(0);
        gComp.setIndex(0);

        //set the new animation
        b.setInt(newSpritePos);
        gComp.setCurrentAnim(newSpritePos);
        gComp.setCurrentSImage(gComp.getCurrentAnim().get(0));
        return false;
    }
    private boolean backgroundScroll(BehaviorVector b, Entity e){
        Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
        if(gComp != null) {
            SImageSubImage s = (SImageSubImage)gComp.getCurrentSImage();
            Vector currentSubPos = s.getSubPos();
            //if we're not at the top we're done
            if(currentSubPos.equals(new Vector(0, 0))) {
                return true;
            }
            double speed = b.getV().getA();
            double pastIdealPos = b.getV().getB();
            //stores the past ideal position (between pixels)
            double newIdealPos = Math.max(0, pastIdealPos - speed);
            b.getV().setB(newIdealPos);

            //bottoms out at zero
            currentSubPos.setB((int)newIdealPos);
            //when hits zero ends behavior atnext tick
        }
        return false;
    }
    private boolean hpBarGraphics(BehaviorHPBar b, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());

        if(pComp == null || gComp == null){
            return true;
        }

        SImageSubImage s = (SImageSubImage)gComp.getCurrentSImage();

        String bossID = b.getString();
        Component_Collision bossComp = (Component_Collision)ComponentManager.getComponent("collision", bossID);
        if(bossComp == null){
            return true;
        }

        double bossHP = bossComp.getHp();
        if(bossHP <= 0){
            return true;
        }
        double maxHP = b.getMaxHP();
        double ratio = bossHP/maxHP;
        if(ratio <= 0 || ratio > 1){
            return true;
        }
        double length = ratio * 600;
        if(length >= 1) {
            s.setWh(new Vector(length, 3));
        }
        return false;
    }

    @Override
    public ComponentFilter getCompfilter(){
        return COMPFILTER;
    }

    private boolean isMessagePresent(int i){
        return gmTable.isMessagePresent(i);
    }
    public static ComponentFilter getCompFilter(){
        return COMPFILTER;
    }

    //taking out playerDeath and bomb and using this for gameMessages that bosses (or others) want to throw out
    //to only parse once
    private class GameplayMessageTable{
        private boolean[] gameMessages;

        private GameplayMessageTable(Blackboard board){
            gameMessages = new boolean[8];
            reset();
            parseMessages(board);
        }
        private void parseMessages(Blackboard board){
            for(BoardMessage bm : board.getMessages()){
                if(bm.getMessage().equals("gameMessage") || bm.getMessage().equals("game_message")){
                    try {
                        int i = (int) bm.getData()[0];
                        gameMessages[i] = true;
                    }catch(Exception e){
                        return;
                    }
                }
            }
        }
        private void reset(){
            for(int i = 0; i < 8; i++){
                gameMessages[i] = false;
            }
        }
        private boolean isMessagePresent(int i){
            if(i >= 0 && i < 8){
                return gameMessages[i];
            }
            return false;
        }
    }
}
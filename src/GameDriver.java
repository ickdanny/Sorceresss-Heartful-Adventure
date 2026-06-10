import java.util.ArrayList;
import java.util.Random;

//acts upon the ECS as an outside force, initializes and displays the game
//takes campaign + difficulty + shot, or practice + level + difficulty + shot.
//enters parameters into ECS (and initial entities).
//in campaign will handle transfering to next level (moving player and resetting)
public class GameDriver{

    private static InputTable currentInputTable;
    private static Random random;
    private static long seed;
    //6 to 7 ratio
    private final static int GAME_WIDTH = 666;
    private final static int GAME_HEIGHT = 777;

    private final static Vector GAME_WINDOW_OFFSET = new Vector(80, 80);

    private final static int INIT_POWER = 0, INIT_LIVES = 3, INIT_BOMBS = 3;
    private final static int MAX_POWER = 128, MAX_LIVES = 6, MAX_BOMBS = 6;

    private final static Vector INIT_PLAYER_POSITION = new Vector(GAME_WIDTH/2, GAME_HEIGHT - (GAME_HEIGHT/8));

    //storing for now, we'll see how it goes
    private static int currentStage, currentDifficulty, currentShot;
    private static boolean currentCampaign;

    private static Vector currentPlayerPosition;

    private static int tick;

    private final static SImage UI_OVERLAY = new SImage("ui_overlay", new Vector(0, 0));

    private static SImage[] lives;
    private static SImage[] bombs;

    private static SImage pow_under;
    private static SImage pow_over;

    static{
        int initX = 925;
        int incrX = 40;
        lives = new SImage[MAX_LIVES];
        for(int i = 0; i < lives.length; i++){
            lives[i] = new SImage("ui_player", new Vector(initX + (incrX * i), 80));
        }
        bombs = new SImage[MAX_BOMBS];
        for(int i = 0; i < bombs.length; i++){
            bombs[i] = new SImage("ui_bomb", new Vector(initX + (incrX * i), 168));
        }

        pow_under = new SImage("ui_power_under", new Vector(initX, 262));
        pow_over = new SImage("ui_power_over", new Vector(initX, 262));
    }


    //all logic is in sy temM
    public static void update(){
        SystemManager.update();
        ++tick;
    }

    public static ArrayList<DrawableElement> getImage(double dt){
        ArrayList<DrawableElement> toRet = new ArrayList<>();
        //draw graphicsComponents
        //in order from bot to top
        ArrayList<Entity> noType = new ArrayList<>();
        ArrayList<Entity> pp = new ArrayList<>();
        ArrayList<Entity> bomb = new ArrayList<>();
        ArrayList<Entity> ep = new ArrayList<>();
        ArrayList<Entity> enemy = new ArrayList<>();
        ArrayList<Entity> boss = new ArrayList<>();
        ArrayList<Entity> pickup = new ArrayList<>();
        ArrayList<Entity> player = new ArrayList<>();
        for(Entity e : SystemManager.getSortedEntities().get(System_EntityGraphics.getCompFilter())){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Type tComp = (Component_Type)ComponentManager.getComponent("type", e.getEntityID());
            //bug-deleted components first and then try to draw = nullPointer
            //so add in a test... may need structural changes later, we'll see
            if(gComp == null){
                continue;
            }

            //sort entities by type first and then add them
            //entities with no type drawn bottom

            //could have used a map i guess...
            if (tComp == null) {
                noType.add(e);
            } else if (tComp.getType() == EntityType.PLAYER_P) {
                pp.add(e);
            } else if (tComp.getType() == EntityType.PLAYER_B) {
                bomb.add(e);
            } else if (tComp.getType() == EntityType.ENEMY_P) {
                ep.add(e);
            } else if (tComp.getType() == EntityType.ENEMY) {
                enemy.add(e);
            } else if (tComp.getType() == EntityType.BOSS) {
                boss.add(e);
            } else if (tComp.getType() == EntityType.PICKUP) {
                pickup.add(e);
            } else if (tComp.getType() == EntityType.PLAYER) {
                player.add(e);
            }
        }
        for(Entity e : noType){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }
        //bomb under pp
        for(Entity e : bomb){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }
        for(Entity e : pp){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }
        //moving pickups under ep
        for(Entity e : pickup){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }
        for(Entity e : ep){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }
        for(Entity e : enemy){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }
        for(Entity e : boss){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }
        for(Entity e : player){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            drawHelper(toRet, gComp, pComp, dt);
        }

        //draw ui
        toRet.add(UI_OVERLAY);
        Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player",
                (SystemManager.getSortedEntities().get(System_Player.getCompFilter())).get(0).getEntityID()));
        if(pComp != null) {
            int lives = pComp.getLives();
            int bombs = pComp.getBombs();
            int power = pComp.getPower();

            for (int i = 0; i < lives; i++) {
                toRet.add(GameDriver.lives[i]);
            }
            for (int i = 0; i < bombs; i++) {
                toRet.add(GameDriver.bombs[i]);
            }

            //power
            toRet.add(GameDriver.pow_under);
            toRet.add(GameDriver.pow_over);
            GameDriver.pow_over.setPosition(new Vector(925 + (power * 2), 262));
        }

        return toRet;
    }

    public static void drawHelper(ArrayList<DrawableElement> list, Component_Graphics gComp, Component_Position pComp, double dt){
        SImage toDraw = gComp.getCurrentSImage();
        //deltaTime
        if (pComp != null) {
            Vector interpolatedPosition = Vector.interpolate(pComp.getPrevPosition(), pComp.getPosition(), dt);
            Vector drawPos = Vector.add(interpolatedPosition, GAME_WINDOW_OFFSET);
            toDraw.setPosition(drawPos);
        }
        list.add(toDraw);
    }

    //init game is directly called from menuManager's add_game command
    //takes arguments for the level, difficulty, shot, and whether or not it is in campaign or not
    //loading screen below
    //since the player entity will be the same
    public static void initGame(int level, int difficulty, int shot, boolean campaign){
        init();
        currentStage = level;
        currentDifficulty = difficulty;
        currentShot = shot;
        currentCampaign = campaign;

        tick = 0;

        Blackboard ECSBoard = SystemManager.getECSBoard();
        ECSBoard.write(new BoardMessage("level", -1, new Object[]{level}));
        ECSBoard.write(new BoardMessage("difficulty", -1, new Object[]{difficulty}));
        ECSBoard.write(new BoardMessage("shot", -1, new Object[]{shot}));
        ECSBoard.write(new BoardMessage("campaign", -1, new Object[]{campaign}));

        //init code goes here since after this method returns GameFrame will be sending updates
        GameInitiator.initGame(level, difficulty, shot, campaign);
    }
    public static void initGame(int level, int difficulty, int shot, Component_Player playComp){
        init();
        currentStage = level;
        currentDifficulty = difficulty;
        currentShot = shot;
        currentCampaign = true;

        tick = 0;

        Blackboard ECSBoard = SystemManager.getECSBoard();
        ECSBoard.write(new BoardMessage("level", -1, new Object[]{level}));
        ECSBoard.write(new BoardMessage("difficulty", -1, new Object[]{difficulty}));
        ECSBoard.write(new BoardMessage("shot", -1, new Object[]{shot}));
        ECSBoard.write(new BoardMessage("campaign", -1, new Object[]{true}));

        //init code goes here since after this method returns GameFrame will be sending updates
        GameInitiator.initGame(level, difficulty, shot, playComp);
    }

    public static InputTable getCurrentInputTable() {
        return currentInputTable;
    }
    public static void setCurrentInputTable(InputTable currentInputTable) {
        GameDriver.currentInputTable = currentInputTable;
    }

    public static void setRandom(Random random) {
        GameDriver.random = random;
        seed = random.nextLong();
        random.setSeed(seed);
    }
    public static void reseedRandom(){
        if(random != null){
            seed = random.nextLong();
            random.setSeed(seed);
        }
    }

    //delegate methods
    public static int randomInt() {
        return random.nextInt();
    }
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }
    public static double randomDouble() {
        return random.nextDouble();
    }
    public static long getSeed() {
        return seed;
    }

    public static int getGameWidth() {
        return GAME_WIDTH;
    }
    public static int getGameHeight() {
        return GAME_HEIGHT;
    }
    public static Vector getGameWindowOffset() {
        return GAME_WINDOW_OFFSET;
    }
    public static int getInitPower() {
        return INIT_POWER;
    }
    public static int getInitLives() {
        return INIT_LIVES;
    }
    public static int getInitBombs() {
        return INIT_BOMBS;
    }
    public static int getMaxPower() {
        return MAX_POWER;
    }
    public static int getMaxLives() {
        return MAX_LIVES;
    }
    public static int getMaxBombs() {
        return MAX_BOMBS;
    }

    public static int getTick() {
        return tick;
    }

    public static int getCurrentStage() {
        return currentStage;
    }
    public static int getCurrentDifficulty() {
        return currentDifficulty;
    }
    public static int getCurrentShot() {
        return currentShot;
    }
    public static boolean getCurrentCampaign() {
        return currentCampaign;
    }
    public static Vector getCurrentPlayerPosition() {
        if(currentPlayerPosition != null) {
            return currentPlayerPosition;
        }
        else{
            return INIT_PLAYER_POSITION.deepClone();
        }
    }
    public static void setCurrentPlayerPosition(Vector currentPlayerPosition) {
        GameDriver.currentPlayerPosition = currentPlayerPosition;
    }

    public static Vector getInitPlayerPosition() {
        return INIT_PLAYER_POSITION.deepClone();
    }

    //called only when starting a new campaign (as opposed to every level)
    private static void init(){
        EntityManager.init();
        ComponentManager.init();
        SystemManager.init();
    }

    private static class GameInitiator {

        private static void initGame(int level, int difficulty, int shot, boolean campaign){
            PlayerInitiator.initPlayer(level, shot, campaign);
            LevelInitiator.initLevel(level, difficulty, campaign);
        }
        private static void initGame(int level, int difficulty, int shot, Component_Player playComp){
            PlayerInitiator.initPlayer(shot, playComp);
            LevelInitiator.initLevel(level, difficulty, true);
        }

        private static class PlayerInitiator{
            private static void initPlayer(int level, int shot, boolean campaign){
                int spriteTime = 5;

                Vector playerPos = INIT_PLAYER_POSITION.deepClone();
                currentPlayerPosition = playerPos;
                Component_Position posComp = new Component_Position(playerPos);
                Component_Movement mComp = new Component_Movement(new MoveModeAdd("player_movement"), new MoveModeTrans("player_bounds"));
                Component_Collision cComp = new Component_Collision(new AABB(4), 1, 0);
                Component_Player playComp;
                Component_Spawning sComp;
                Component_Type tComp = new Component_Type(EntityType.PLAYER);
                Component_Graphics gComp;
                Component_Behavior bComp;
                if(campaign){
                    playComp = new Component_Player(INIT_LIVES, INIT_BOMBS, 0);
                }
                else{
                    int pow;
                    switch(level){
                        case 1:
                            pow = 0;
                            playComp = new Component_Player(MAX_LIVES, MAX_BOMBS, pow);
                            break;
                        case 2:
                            pow = 64;
                            playComp = new Component_Player(MAX_LIVES, MAX_BOMBS, pow);
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            pow = 128;
                            playComp = new Component_Player(MAX_LIVES, MAX_BOMBS, pow);
                            break;
                        case 7:
                        default:
                            pow = 128;
                            playComp = new Component_Player(INIT_LIVES, INIT_BOMBS, pow);
                            break;
                    }

                }
                sComp = new Component_SpawningPlayer(shot);

                ArrayList<SImage> sImages = new ArrayList<>();
                Vector offset = new Vector(-39, -40);

                sImages.add(new SImage("p_left_2", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_left_1", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_idle", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_right_1", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_right_2", new Vector(), offset.deepClone()));
                gComp = new Component_GraphicsMultiSprite(sImages, 2);

                ArrayList<Behavior> inRoutine = new ArrayList<>();
                inRoutine.add(new BehaviorInt("player_graphics", 2));
                inRoutine.add(new BehaviorVector("timer", new Vector(spriteTime)));
                bComp = new Component_Behavior(new Routine("routine", inRoutine, true));

                String playID = EntityManager.addEntity();
                ComponentManager.setComponent(posComp, playID);
                ComponentManager.setComponent(mComp, playID);
                ComponentManager.setComponent(cComp, playID);
                ComponentManager.setComponent(playComp, playID);
                ComponentManager.setComponent(sComp, playID);
                ComponentManager.setComponent(tComp, playID);
                ComponentManager.setComponent(gComp, playID);
                ComponentManager.setComponent(bComp, playID);
            }
            private static void initPlayer(int shot, Component_Player playComp){
                int spriteTime = 5;

                Vector playerPos = INIT_PLAYER_POSITION.deepClone();
                currentPlayerPosition = playerPos;
                Component_Position posComp = new Component_Position(playerPos);
                Component_Movement mComp = new Component_Movement(new MoveModeAdd("player_movement"), new MoveModeTrans("player_bounds"));
                Component_Collision cComp = new Component_Collision(new AABB(4), 1, 0);
                Component_Spawning sComp;
                Component_Type tComp = new Component_Type(EntityType.PLAYER);
                Component_Graphics gComp;
                Component_Behavior bComp;

                sComp = new Component_SpawningPlayer(shot);

                ArrayList<SImage> sImages = new ArrayList<>();
                Vector offset = new Vector(-39, -40);

                sImages.add(new SImage("p_left_2", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_left_1", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_idle", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_right_1", new Vector(), offset.deepClone()));
                sImages.add(new SImage("p_right_2", new Vector(), offset.deepClone()));
                gComp = new Component_GraphicsMultiSprite(sImages, 2);

                ArrayList<Behavior> inRoutine = new ArrayList<>();
                inRoutine.add(new BehaviorInt("player_graphics", 2));
                inRoutine.add(new BehaviorVector("timer", new Vector(spriteTime)));
                bComp = new Component_Behavior(new Routine("routine", inRoutine, true));

                String playID = EntityManager.addEntity();
                ComponentManager.setComponent(posComp, playID);
                ComponentManager.setComponent(mComp, playID);
                ComponentManager.setComponent(cComp, playID);
                ComponentManager.setComponent(playComp, playID);
                ComponentManager.setComponent(sComp, playID);
                ComponentManager.setComponent(tComp, playID);
                ComponentManager.setComponent(gComp, playID);
                ComponentManager.setComponent(bComp, playID);
            }
        }
        private static class LevelInitiator{
            //distributor method
            private static void initLevel(int level, int difficulty, boolean campaign){
                switch(level){
                    case 1:
                        initlevel1(difficulty);
                        break;
                    case 2:
                        initlevel2(difficulty);
                        break;
                    case 3:
                        initlevel3(difficulty);
                        break;
                    case 4:
                        initlevel4(difficulty);
                        break;
                    case 5:
                        initlevel5(difficulty);
                        break;
                    case 6:
                        initlevel6(difficulty);
                        break;
                    case 7:
                        initLevelEX();
                        break;
                }
            }

            private static void initlevel1(int difficulty){
                int levelLength = (140 * 60) + 1;
                ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
                SpawnUnit unit = new SpawnUnit(levelLength, false, spawnList);

//                spawnList.add(new SISingleSpawn("dummy"));
                spawnList.add(new SISingleSpawn("background_1"));

                spawnList.add(new SISingleTick(60, "spawner1_1"));
                spawnList.add(new SISingleTick((int)(60 * 2.5), "spawner1_2"));
                spawnList.add(new SISingleTick(60 * 6, "spawner1_3"));
                spawnList.add(new SISingleTick(60 * 8, "spawner1_4"));
                spawnList.add(new SISingleTick((int)(60 * 32.5), "spawner1_5"));
                spawnList.add(new SISingleTick(60 * 61, "spawner1_6"));
                spawnList.add(new SISingleTick(60 * 98, "spawner1_7"));
                spawnList.add(new SISingleTick(60 * 126, "spawner1_8"));
                spawnList.add(new SISingleTick(60 * 140, "boss_1"));//140

                String spawnerID = EntityManager.addEntity();
                ComponentManager.setComponent(new Component_Spawning(unit), spawnerID);
                ComponentManager.setComponent(lifetime(levelLength + 1), spawnerID);
            }
            private static void initlevel2(int difficulty){
                int levelLength = (150 * 60) + 1;
                ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
                SpawnUnit unit = new SpawnUnit(levelLength, false, spawnList);

                spawnList.add(new SISingleSpawn("background_2"));

                spawnList.add(new SISingleTick(120, "spawner2_1"));
                spawnList.add(new SISingleTick(60 * 20, "spawner2_2"));
                spawnList.add(new SISingleTick(60 * 36, "spawner2_1"));
                spawnList.add(new SISingleTick(60 * 55, "spawner2_3"));//55
                spawnList.add(new SISingleTick(60 * 71, "spawner2_1"));
                spawnList.add(new SISingleTick(60 * 89, "spawner2_4"));//89
                spawnList.add(new SISingleTick(60 * 105, "spawner2_1"));
                spawnList.add(new SISingleTick(60 * 125, "spawner2_5"));//125
                spawnList.add(new SISingleTick(60 * 133, "spawner2_6"));
                spawnList.add(new SISingleTick(60 * 150, "boss_2"));

                String spawnerID = EntityManager.addEntity();
                ComponentManager.setComponent(new Component_Spawning(unit), spawnerID);
                ComponentManager.setComponent(lifetime(levelLength + 1), spawnerID);
            }
            private static void initlevel3(int difficulty){
                int levelLength = (110 * 60) + 1;
                ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
                SpawnUnit unit = new SpawnUnit(levelLength, false, spawnList);

                spawnList.add(new SISingleSpawn("background_3"));

                spawnList.add(new SISingleTick(60 * 3, "spawner3_1"));
                spawnList.add(new SISingleTick((int)(60 * 13.5), "spawner3_2"));
                spawnList.add(new SISingleTick(60 * 27, "spawner3_3"));//27
                spawnList.add(new SISingleTick((int)(60 * 58.5), "spawner3_4"));//58.5
                spawnList.add(new SISingleTick(60 * 95, "spawner3_5"));//95
                spawnList.add(new SISingleTick(60 * 110, "boss_3"));

                String spawnerID = EntityManager.addEntity();
                ComponentManager.setComponent(new Component_Spawning(unit), spawnerID);
                ComponentManager.setComponent(lifetime(levelLength + 1), spawnerID);
            }
            private static void initlevel4(int difficulty){
                int levelLength = (160 * 60) + 1;
                ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
                SpawnUnit unit = new SpawnUnit(levelLength, false, spawnList);

                spawnList.add(new SISingleSpawn("background_4"));

                spawnList.add(new SISingleTick(200, "spawner4_1"));
                spawnList.add(new SISingleTick(60 * 29, "spawner4_2"));//29
                spawnList.add(new SISingleTick(60 * 42, "spawner4_3"));//42
                spawnList.add(new SISingleTick(60 * 58, "spawner4_4"));//58
                spawnList.add(new SISingleTick(60 * 71, "spawner4_5"));//71
                spawnList.add(new SISingleTick(60 * 92, "spawner4_6"));//92
                spawnList.add(new SISingleTick(60 * 117, "spawner4_7"));//117
                spawnList.add(new SISingleTick(60 * 130, "spawner4_8"));//130
                spawnList.add(new SISingleTick(60 * 160, "boss_4"));

                String spawnerID = EntityManager.addEntity();
                ComponentManager.setComponent(new Component_Spawning(unit), spawnerID);
                ComponentManager.setComponent(lifetime(levelLength + 1), spawnerID);
            }
            private static void initlevel5(int difficulty){
                int levelLength = (130 * 60) + 1;
                ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
                SpawnUnit unit = new SpawnUnit(levelLength, false, spawnList);

                spawnList.add(new SISingleSpawn("background_5"));

                spawnList.add(new SISingleTick(170, "spawner5_1"));
                spawnList.add(new SISingleTick(60 * 21, "spawner5_2"));//21
                spawnList.add(new SISingleTick((60 * 21 * 2) - 170, "spawner5_3"));//(60 * 21 * 2) - 170
                spawnList.add(new SISingleTick((60 * 21 * 2) - 170 + (11 * 30), "spawner5_4"));//~6 seconds after
                spawnList.add(new SISingleTick(60 * 52, "spawner5_5"));//52
                spawnList.add(new SISingleTick(60 * 56, "spawner5_6"));//+4
                spawnList.add(new SISingleTick(60 * 63, "spawner5_7"));//63
                spawnList.add(new SISingleTick(60 * 74, "spawner5_8"));//+11 = 74
                spawnList.add(new SISingleTick(60 * 78, "spawner5_8"));//+4 = 78
                spawnList.add(new SISingleTick(60 * 88, "spawner5_9"));//+14 = 88
                spawnList.add(new SISingleTick(60 * 97, "spawner5_10"));//+9 = 97
                spawnList.add(new SISingleTick(60 * 101, "spawner5_10"));//+4 = 101
                spawnList.add(new SISingleTick((int)(60 * 110.5), "spawner5_11"));//111

                spawnList.add(new SISingleTick(60 * 130, "boss_5"));

//                spawnList.add(new SISingleTick(60 * 1, "boss_5"));
                String spawnerID = EntityManager.addEntity();
                ComponentManager.setComponent(new Component_Spawning(unit), spawnerID);
                ComponentManager.setComponent(lifetime(levelLength + 1), spawnerID);
            }
            private static void initlevel6(int difficulty){
                int levelLength = (60 * 60) + 1;
                ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
                SpawnUnit unit = new SpawnUnit(levelLength, false, spawnList);

                spawnList.add(new SISingleSpawn("background_6"));

                spawnList.add(new SISingleTick(33, "spawner6_1"));
                spawnList.add(new SISingleTick(1586, "spawner6_2"));//61 * 26 //1586
                spawnList.add(new SISingleTick(1696, "spawner6_3"));//+110       //1696
                spawnList.add(new SISingleTick(1825, "spawner6_4"));//+129       //1825
                spawnList.add(new SISingleTick(1935, "spawner6_5"));//+110       //1935
                spawnList.add(new SISingleTick(2246, "spawner6_6"));//11 sec after 6_2   //2246
                spawnList.add(new SISingleTick(2456, "spawner6_7"));//+3.5 sec//2456
                spawnList.add(new SISingleTick(2666, "spawner6_8"));//+7 sec from 6_6//2666 //~44 sec
                spawnList.add(new SISingleTick(60 * 48, "spawner6_9"));//48 sec

                spawnList.add(new SISingleTick(60 * 60, "boss_6"));

                String spawnerID = EntityManager.addEntity();
                ComponentManager.setComponent(new Component_Spawning(unit), spawnerID);
                ComponentManager.setComponent(lifetime(levelLength + 1), spawnerID);
            }
            private static void initLevelEX(){
                int levelLength = (197 * 60) + 1;
                ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
                SpawnUnit unit = new SpawnUnit(levelLength, false, spawnList);

                spawnList.add(new SISingleSpawn("background_7"));

                spawnList.add(new SISingleTick(60 * 3, "spawner7_1"));//3
                spawnList.add(new SISingleTick(60 * 23, "spawner7_2"));//23
                spawnList.add(new SISingleTick((60 * 23) + (157 * 4), "spawner7_3"));//+157*4
                spawnList.add(new SISingleTick(60 * 45, "spawner7_4"));//45
                spawnList.add(new SISingleTick((60 * 54) + 30, "spawner7_5"));//+ 9.5 = 54.5
                spawnList.add(new SISingleTick(60 * 62, "spawner7_6"));// + 7.5 = 62
                spawnList.add(new SISingleTick(60 * 72, "spawner7_7"));//+10 = 72
                spawnList.add(new SISingleTick(60 * 79, "spawner7_8"));//81
                spawnList.add(new SISingleTick(60 * 101, "spawner7_9"));//+23 = 104
                spawnList.add(new SISingleTick(60 * 116, "spawner7_10"));//+14 = 118
                spawnList.add(new SISingleTick(60 * 139, "spawner7_11"));//+26 = 141
                spawnList.add(new SISingleTick((60 * 139) + (157 * 4), "spawner7_12"));//+157*4
                spawnList.add(new SISingleTick(60 * 164, "spawner7_13"));//141 + 23 -> 163
                spawnList.add(new SISingleTick((60 * 177) + 30, "spawner7_14"));// + 13.5 -> 176.5

                spawnList.add(new SISingleTick(60 * 195, "boss_7"));//195

                String spawnerID = EntityManager.addEntity();
                ComponentManager.setComponent(new Component_Spawning(unit), spawnerID);
                ComponentManager.setComponent(lifetime(levelLength + 1), spawnerID);
            }

            //need this for deactivating the spawner
            private static Component_Behavior lifetime(int time){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorVector("timer", new Vector(time)));
                al.add(new Behavior("deactivate"));
                return new Component_Behavior(new Routine("routine", al, false));
            }
        }
    }

}
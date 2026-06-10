import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class System_EntitySpawning implements ECSSystem {

    private static final ComponentFilter COMPFILTER = new ComponentFilter(new String[]{"spawning"});

    public System_EntitySpawning(){
    }

    @Override
    public void update(ArrayList<Entity> entities) {

        for(Entity e : entities){
            Component_Spawning sComp = (Component_Spawning)ComponentManager.getComponent("spawning", e.getEntityID());
            parseComponent(sComp, e);
            //each timer
            Iterator<SpawnUnit> iter = sComp.getSpawnUnits().iterator();
            while(iter.hasNext()){
                SpawnUnit unit = iter.next();
                if(parseUnit(unit, e)){
                    iter.remove();
                }
            }
        }
    }

    //true = remove unit
    private boolean parseUnit(SpawnUnit unit, Entity e){
        int currentTick = unit.getTick();

        //filter by si type
        for(SpawningInstruction si : unit.getSpawningInstructions()){
            //filter by si type
            //single spawn = when tick = 0
            if (si instanceof SISingleSpawn) {
                if(currentTick == 0) {
                    spawn(si.getSpawnID(), e);
                }
            }
            //single tick = when tick = tick
            else if (si instanceof SISingleTick) {
                if(currentTick == ((SISingleTick) si).getTick()){
                    spawn(si.getSpawnID(), e);
                }
            }
            //multiple tick = when tick is multiple of tick (with bounds)
            else if (si instanceof SIMultipleTick) {
                int lower = ((SIMultipleTick)si).getLowerBound();
                int upper = ((SIMultipleTick)si).getUpperBound();
                if((lower == -1 && upper == -1) || (currentTick >= lower && currentTick <= upper)){
                    if(currentTick % ((SIMultipleTick) si).getMultiple() == 0){
                        spawn(si.getSpawnID(), e);
                    }
                }
            }
            //array tick = when tick is part of the listed ticks
            else if (si instanceof SIArrayTick) {
                for(int test : ((SIArrayTick) si).getTicks()){
                    if(currentTick == test){
                        spawn(si.getSpawnID(), e);
                    }
                }
            }
            //otherwise the system will handle it
            else{
                spawn(si.getSpawnID(), e);
            }
        }

        //tick
        unit.setTick(++currentTick);
        if(unit.getTick() >= unit.getMaxTick()){
            if(!unit.isLoop()){
                //reset so we can use the same SU over again
                unit.setTick(0);
                return true;
            }
            unit.setTick(0);
        }
        return false;
    }

    //e being the spawning entity (can be null)
    private void spawn(String spawnID, Entity e){
        //parse spawnID
        switch(spawnID){
///////////////////////PLAYER RELATED///////////////////////////
            //these are the shared bullet type
            case "player1":
                PlayerSpawns.spawnAttack1(e);
                break;
            case "player2":
                PlayerSpawns.spawnAttack2(e);
                break;
            case "player3":
                PlayerSpawns.spawnAttack3(e);
                break;
            //these are the special shot bullets
            case "player1_shot":
                PlayerSpawns.spawnShot1(e);
                break;
            case "player2_shot":
                PlayerSpawns.spawnShot2(e);
                break;
            case "player3_shot":
                PlayerSpawns.spawnShot3(e);
                break;
            case "shot2_decay_small":
                PlayerSpawns.spawnShot2Decay(e, false);
                break;
            case "shot2_decay_large":
                PlayerSpawns.spawnShot2Decay(e, true);
                break;
            //bomb related
            case "bomb1":
                PlayerSpawns.spawnBomb1(e);
                break;
            case "bomb2":
                PlayerSpawns.spawnBomb2(e);
                break;
            case "bomb3":
                PlayerSpawns.spawnBomb3(e);
                break;
            case "bomb1_decay":
                PlayerSpawns.spawnBomb1Decay(e);
                break;

//////////////////////////PICKUPS/////////////////////////
            case "pickup_life":
                PickupSpawns.spawnLife(e);
                break;
            case "pickup_bomb":
                PickupSpawns.spawnBomb(e);
                break;
            case "pickup_power_large":
                PickupSpawns.spawnPowerLarge(e, false);
                break;
            case "pickup_power_small":
                PickupSpawns.spawnPowerSmall(e, false);
                break;

            case "pickup_player_large":
                PickupSpawns.spawnPowerLarge(e, true);
                break;
            case "pickup_player_small":
                PickupSpawns.spawnPowerSmall(e, true);
                break;

            case "pickup_power_large_rng":
                PickupSpawns.spawnPowerLargeRNG(e);
                break;
            case "pickup_power_small_rng_1_2":
                PickupSpawns.spawnPowerSmallRNG(e, 1d/2);
                break;
            case "pickup_power_small_rng_1_3":
                PickupSpawns.spawnPowerSmallRNG(e, 1d/3);
                break;
            case "pickup_power_small_rng_1_4":
                PickupSpawns.spawnPowerSmallRNG(e, 1d/4);
                break;
            case "pickup_power_small_rng_1_5":
                PickupSpawns.spawnPowerSmallRNG(e, 1d/5);
                break;
            case "pickup_power_small_rng_1_6":
                PickupSpawns.spawnPowerSmallRNG(e, 1d/6);
                break;

////////////////////////////ENEMY SPAWNS////////////////////////////////
            //stage 1
            case "1_1_1":
                EnemySpawns.spawn1_1_1();
                break;
            case "1_2_1":
                EnemySpawns.spawn1_2_1();
                break;
            case "1_3_1":
                EnemySpawns.spawn1_3_1();
                break;
            case "1_4_1":
                EnemySpawns.spawn1_4_1();
                break;
            case "1_4_2":
                EnemySpawns.spawn1_4_2();
                break;
            case "1_4_3":
                EnemySpawns.spawn1_4_3();
                break;
            case "1_4_4":
                EnemySpawns.spawn1_4_4();
                break;
            case "1_5_1":
                EnemySpawns.spawn1_5_1();
                break;
            case "1_5_2":
                EnemySpawns.spawn1_5_2();
                break;
            case "1_6_1":
                EnemySpawns.spawn1_6_1();
                break;
            case "1_6_2":
                EnemySpawns.spawn1_6_2();
                break;
            case "1_6_3":
                EnemySpawns.spawn1_6_3();
                break;
            case "1_6_4":
                EnemySpawns.spawn1_6_4();
                break;
            case "1_7_1":
                EnemySpawns.spawn1_7_1();
                break;
            case "1_8_1":
                EnemySpawns.spawn1_8_1();
                break;
            case "boss_1":
                EnemySpawns.spawnBoss_1();
                break;

            //stage 2
            case "2_1_1":
                EnemySpawns.spawn2_1_1();
                break;
            case "2_2_1":
                EnemySpawns.spawn2_2_1();
                break;
            case "2_2_2":
                EnemySpawns.spawn2_2_2();
                break;
            case "2_2_3":
                EnemySpawns.spawn2_2_3();
                break;
            case "2_2_4":
                EnemySpawns.spawn2_2_4();
                break;
            case "2_2_5":
                EnemySpawns.spawn2_2_5();
                break;
            case "2_2_6":
                EnemySpawns.spawn2_2_6();
                break;
            case "2_3_1":
                EnemySpawns.spawn2_3_1();
                break;
            case "2_3_2":
                EnemySpawns.spawn2_3_2();
                break;
            case "2_3_3":
                EnemySpawns.spawn2_3_3();
                break;
            case "2_3_4":
                EnemySpawns.spawn2_3_4();
                break;
            case "2_4_1":
                EnemySpawns.spawn2_4_1();
                break;
            case "2_4_2":
                EnemySpawns.spawn2_4_2();
                break;
            case "2_4_3":
                EnemySpawns.spawn2_4_3();
                break;
            case "2_4_4":
                EnemySpawns.spawn2_4_4();
                break;
            case "2_4_5":
                EnemySpawns.spawn2_4_5();
                break;
            case "2_4_6":
                EnemySpawns.spawn2_4_6();
                break;
            case "2_5_1":
                EnemySpawns.spawn2_5_1();
                break;
            case "boss_2":
                EnemySpawns.spawnBoss_2();
                break;

            //stage 3
            case "3_1_1":
                EnemySpawns.spawn3_1_1();
                break;
            case "3_1_2":
                EnemySpawns.spawn3_1_2();
                break;
            case "3_2_1":
                EnemySpawns.spawn3_2_1();
                break;
            case "3_2_2":
                EnemySpawns.spawn3_2_2();
                break;
            case "3_3_1":
                EnemySpawns.spawn3_3_1();
                break;
            case "3_3_2":
                EnemySpawns.spawn3_3_2();
                break;
            case "3_3_3":
                EnemySpawns.spawn3_3_3();
                break;
            case "3_3_4":
                EnemySpawns.spawn3_3_4();
                break;
            case "3_4_1":
                EnemySpawns.spawn3_4_1();
                break;
            case "3_4_2":
                EnemySpawns.spawn3_4_2();
                break;
            case "3_4_3":
                EnemySpawns.spawn3_4_3();
                break;
            case "3_5_1":
                EnemySpawns.spawn3_5_1();
                break;
            case "boss_3":
                EnemySpawns.spawnBoss_3();
                break;

            //stage 4
            case "4_1_1":
                EnemySpawns.spawn4_1_1();
                break;
            case "4_1_2":
                EnemySpawns.spawn4_1_2();
                break;
            case "4_1_3":
                EnemySpawns.spawn4_1_3();
                break;
            case "4_2":
                EnemySpawns.spawn4_2();
                break;
            //4_3 ^
            case "4_4":
                EnemySpawns.spawn4_4();
                break;
            //4_4 ^
            case "4_6":
                EnemySpawns.spawn4_6();
                break;
            case "4_7":
                EnemySpawns.spawn4_7();
                break;
            case "4_8_1":
                EnemySpawns.spawn4_8_1();
                break;
            case "4_8_2":
                EnemySpawns.spawn4_8_2();
                break;
            case "4_8_3":
                EnemySpawns.spawn4_8_3();
                break;
            case "boss_4":
                EnemySpawns.spawnBoss_4();
                break;

            //stage 5
            case "5_1_1":
                EnemySpawns.spawn5_1_1();
                break;
            case "5_1_2":
                EnemySpawns.spawn5_1_2();
                break;
            case "5_2_1":
                EnemySpawns.spawn5_2_1();
                break;
            case "5_2_2":
                EnemySpawns.spawn5_2_2();
                break;
            case "5_3":
                EnemySpawns.spawn5_3();
                break;
            case "5_4":
                EnemySpawns.spawn5_4();
                break;
            case "5_5":
                EnemySpawns.spawn5_5();
                break;
            case "5_6":
                EnemySpawns.spawn5_6();
                break;
            case "5_7_1":
                EnemySpawns.spawn5_7(1);
                break;
            case "5_7_2":
                EnemySpawns.spawn5_7(2);
                break;
            case "5_7_3":
                EnemySpawns.spawn5_7(3);
                break;
            case "5_7_4":
                EnemySpawns.spawn5_7(4);
                break;
            case "5_7_5":
                EnemySpawns.spawn5_7(5);
                break;
            case "5_8_1":
                EnemySpawns.spawn5_8(1);
                break;
            case "5_8_2":
                EnemySpawns.spawn5_8(2);
                break;
            case "5_8_3":
                EnemySpawns.spawn5_8(3);
                break;
            case "5_8_4":
                EnemySpawns.spawn5_8(4);
                break;
            case "5_8_5":
                EnemySpawns.spawn5_8(5);
                break;
            case "5_8_6":
                EnemySpawns.spawn5_8(6);
                break;
            case "5_8_7":
                EnemySpawns.spawn5_8(7);
                break;
            case "5_9_1":
                EnemySpawns.spawn5_9(1);
                break;
            case "5_9_2":
                EnemySpawns.spawn5_9(2);
                break;
            case "5_9_3":
                EnemySpawns.spawn5_9(3);
                break;
            case "5_9_4":
                EnemySpawns.spawn5_9(4);
                break;
            case "5_9_5":
                EnemySpawns.spawn5_9(5);
                break;
            case "5_10_1":
                EnemySpawns.spawn5_10(1);
                break;
            case "5_10_2":
                EnemySpawns.spawn5_10(2);
                break;
            case "5_10_3":
                EnemySpawns.spawn5_10(3);
                break;
            case "5_10_4":
                EnemySpawns.spawn5_10(4);
                break;
            case "5_10_5":
                EnemySpawns.spawn5_10(5);
                break;
            case "5_10_6":
                EnemySpawns.spawn5_10(6);
                break;
            case "5_10_7":
                EnemySpawns.spawn5_10(7);
                break;
            case "5_11":
                EnemySpawns.spawn5_11();
                break;
            case "boss_5":
                EnemySpawns.spawnBoss_5();
                break;

            //stage 6
            case "6_1":
                EnemySpawns.spawn6_1();
                break;
            case "6_2":
                EnemySpawns.spawn6_2();
                break;
            case "6_3":
                EnemySpawns.spawn6_3();
                break;
            case "6_4":
                EnemySpawns.spawn6_4();
                break;
            case "6_5":
                EnemySpawns.spawn6_5();
                break;
            case "6_6":
                EnemySpawns.spawn6_6();
                break;
            case "6_7":
                EnemySpawns.spawn6_7();
                break;
            case "6_8":
                EnemySpawns.spawn6_8();
                break;
            case "6_9":
                EnemySpawns.spawn6_9();
                break;
            case "boss_6":
                EnemySpawns.spawnBoss_6();
                break;

            //extra
            case "7_1":
                EnemySpawns.spawn7_1();
                break;
            case "7_2_1":
                EnemySpawns.spawn7_2(true);
                break;
            case "7_2_2":
                EnemySpawns.spawn7_2(false);
                break;
            case "7_3_1":
                EnemySpawns.spawn7_3(false);
                break;
            case "7_3_2":
                EnemySpawns.spawn7_3(true);
                break;
            case "7_4":
                EnemySpawns.spawn7_4();
                break;
            case "7_5":
                EnemySpawns.spawn7_5();
                break;
            case "7_6":
                EnemySpawns.spawn7_6();
                break;
            case "7_7":
                EnemySpawns.spawn7_7();
                break;
            case "7_8":
                EnemySpawns.spawn7_8();
                break;
            case "7_9_1":
                EnemySpawns.spawn7_9(1);
                break;
            case "7_9_2":
                EnemySpawns.spawn7_9(2);
                break;
            case "7_9_3":
                EnemySpawns.spawn7_9(3);
                break;
            case "7_9_4":
                EnemySpawns.spawn7_9(4);
                break;
            case "7_9_5":
                EnemySpawns.spawn7_9(5);
                break;
            case "7_10":
                EnemySpawns.spawn7_10();
                break;
            case "7_11_1":
                EnemySpawns.spawn7_11(true);
                break;
            case "7_11_2":
                EnemySpawns.spawn7_11(false);
                break;
            case "7_12_1":
                EnemySpawns.spawn7_12(false);
                break;
            case "7_12_2":
                EnemySpawns.spawn7_12(true);
                break;
            case "7_13_1":
                EnemySpawns.spawn7_13(1);
                break;
            case "7_13_2":
                EnemySpawns.spawn7_13(2);
                break;
            case "7_13_3":
                EnemySpawns.spawn7_13(3);
                break;
            case "7_14_1":
                EnemySpawns.spawn7_14(1);
                break;
            case "7_14_2":
                EnemySpawns.spawn7_14(2);
                break;
            case "7_14_3":
                EnemySpawns.spawn7_14(3);
                break;
            case "boss_7":
                EnemySpawns.spawnBoss_7();
                break;


            //spawners
            //stage 1
            case "spawner1_1":
                EnemySpawns.spawnSpawner(1, 1);
                break;
            case "spawner1_2":
                EnemySpawns.spawnSpawner(1, 2);
                break;
            case "spawner1_3":
                EnemySpawns.spawnSpawner(1, 3);
                break;
            case "spawner1_4":
                EnemySpawns.spawnSpawner(1, 4);
                break;
            case "spawner1_5":
                EnemySpawns.spawnSpawner(1, 5);
                break;
            case "spawner1_6":
                EnemySpawns.spawnSpawner(1, 6);
                break;
            case "spawner1_7":
                EnemySpawns.spawnSpawner(1, 7);
                break;
            case "spawner1_8":
                EnemySpawns.spawnSpawner(1, 8);
                break;

            //stage 2
            case "spawner2_1":
                EnemySpawns.spawnSpawner(2, 1);
                break;
            case "spawner2_2":
                EnemySpawns.spawnSpawner(2, 2);
                break;
            case "spawner2_3":
                EnemySpawns.spawnSpawner(2, 3);
                break;
            case "spawner2_4":
                EnemySpawns.spawnSpawner(2, 4);
                break;
            case "spawner2_5":
                EnemySpawns.spawnSpawner(2, 5);
                break;
            case "spawner2_6":
                EnemySpawns.spawnSpawner(2, 6);
                break;

            //stage 3
            case "spawner3_1":
                EnemySpawns.spawnSpawner(3, 1);
                break;
            case "spawner3_2":
                EnemySpawns.spawnSpawner(3, 2);
                break;
            case "spawner3_3":
                EnemySpawns.spawnSpawner(3, 3);
                break;
            case "spawner3_4":
                EnemySpawns.spawnSpawner(3, 4);
                break;
            case "spawner3_5":
                EnemySpawns.spawnSpawner(3, 5);
                break;

            //stage 4
            case "spawner4_1":
                EnemySpawns.spawnSpawner(4, 1);
                break;
            case "spawner4_2":
                EnemySpawns.spawnSpawner(4, 2);
                break;
            case "spawner4_3":
                EnemySpawns.spawnSpawner(4, 3);
                break;
            case "spawner4_4":
                EnemySpawns.spawnSpawner(4, 4);
                break;
            case "spawner4_5":
                EnemySpawns.spawnSpawner(4, 5);
                break;
            case "spawner4_6":
                EnemySpawns.spawnSpawner(4, 6);
                break;
            case "spawner4_7":
                EnemySpawns.spawnSpawner(4, 7);
                break;
            case "spawner4_8":
                EnemySpawns.spawnSpawner(4, 8);
                break;

            //stage 5
            case "spawner5_1":
                EnemySpawns.spawnSpawner(5, 1);
                break;
            case "spawner5_2":
                EnemySpawns.spawnSpawner(5, 2);
                break;
            case "spawner5_3":
                EnemySpawns.spawnSpawner(5, 3);
                break;
            case "spawner5_4":
                EnemySpawns.spawnSpawner(5, 4);
                break;
            case "spawner5_5":
                EnemySpawns.spawnSpawner(5, 5);
                break;
            case "spawner5_6":
                EnemySpawns.spawnSpawner(5, 6);
                break;
            case "spawner5_7":
                EnemySpawns.spawnSpawner(5, 7);
                break;
            case "spawner5_8":
                EnemySpawns.spawnSpawner(5, 8);
                break;
            case "spawner5_9":
                EnemySpawns.spawnSpawner(5, 9);
                break;
            case "spawner5_10":
                EnemySpawns.spawnSpawner(5, 10);
                break;
            case "spawner5_11":
                EnemySpawns.spawnSpawner(5, 11);
                break;

            //stage 6
            case "spawner6_1":
                EnemySpawns.spawnSpawner(6, 1);
                break;
            case "spawner6_2":
                EnemySpawns.spawnSpawner(6, 2);
                break;
            case "spawner6_3":
                EnemySpawns.spawnSpawner(6, 3);
                break;
            case "spawner6_4":
                EnemySpawns.spawnSpawner(6, 4);
                break;
            case "spawner6_5":
                EnemySpawns.spawnSpawner(6, 5);
                break;
            case "spawner6_6":
                EnemySpawns.spawnSpawner(6, 6);
                break;
            case "spawner6_7":
                EnemySpawns.spawnSpawner(6, 7);
                break;
            case "spawner6_8":
                EnemySpawns.spawnSpawner(6, 8);
                break;
            case "spawner6_9":
                EnemySpawns.spawnSpawner(6, 9);
                break;

            case "spawner7_1":
                EnemySpawns.spawnSpawner(7, 1);
                break;
            case "spawner7_2":
                EnemySpawns.spawnSpawner(7, 2);
                break;
            case "spawner7_3":
                EnemySpawns.spawnSpawner(7, 3);
                break;
            case "spawner7_4":
                EnemySpawns.spawnSpawner(7, 4);
                break;
            case "spawner7_5":
                EnemySpawns.spawnSpawner(7, 5);
                break;
            case "spawner7_6":
                EnemySpawns.spawnSpawner(7, 6);
                break;
            case "spawner7_7":
                EnemySpawns.spawnSpawner(7, 7);
                break;
            case "spawner7_8":
                EnemySpawns.spawnSpawner(7, 8);
                break;
            case "spawner7_9":
                EnemySpawns.spawnSpawner(7, 9);
                break;
            case "spawner7_10":
                EnemySpawns.spawnSpawner(7, 10);
                break;
            case "spawner7_11":
                EnemySpawns.spawnSpawner(7, 11);
                break;
            case "spawner7_12":
                EnemySpawns.spawnSpawner(7, 12);
                break;
            case "spawner7_13":
                EnemySpawns.spawnSpawner(7, 13);
                break;
            case "spawner7_14":
                EnemySpawns.spawnSpawner(7, 14);
                break;

////////////////////////////DANMAKU//////////////////////////////////
            case "fairy_1":
                DanmakuSpawns.spawnFairy_1(e);
                break;
            case "fairy_2":
                DanmakuSpawns.spawnFairy_2(e);
                break;
            case "fairy_3":
                DanmakuSpawns.spawnFairy_3(e);
                break;
            case "fairy_4":
                DanmakuSpawns.spawnFairy_4(e);
                break;

            case "wisp_1":
                DanmakuSpawns.spawnWisp_1(e);
                break;
            case "wisp_2":
                DanmakuSpawns.spawnWisp_2(e);
                break;
            case "wisp_3":
                DanmakuSpawns.spawnWisp_3(e);
                break;
            case "wisp_4":
                DanmakuSpawns.spawnWisp_4(e);
                break;

            case "wisp_death_1":
                DanmakuSpawns.spawnWispDeath_1(e);
                PickupSpawns.spawnPowerSmallRNG(e, 1d/10);
                break;
            case "wisp_death_2":
                DanmakuSpawns.spawnWispDeath_2(e);
                PickupSpawns.spawnPowerSmallRNG(e, 1d/10);
                break;
            case "wisp_death_3":
                DanmakuSpawns.spawnWispDeath_3(e);
                PickupSpawns.spawnPowerSmallRNG(e, 1d/10);
                break;
            case "wisp_death_4":
                DanmakuSpawns.spawnWispDeath_4(e);
                PickupSpawns.spawnPowerSmall(e, false);
                break;
            case "wisp_death_5":
                DanmakuSpawns.spawnWispDeath_5(e);
                PickupSpawns.spawnPowerSmallRNG(e, 1d/6);
                break;
            case "wisp_death_6":
                DanmakuSpawns.spawnWispDeath_6(e);
                PickupSpawns.spawnLife(e);
                break;

            case "bird_1":
                DanmakuSpawns.spawnBird_1(e);
                break;
            case "bird_2":
                DanmakuSpawns.spawnBird_2(e);
                break;
            case "bird_3":
                DanmakuSpawns.spawnBird_3(e);
                break;
            case "bird_4":
                DanmakuSpawns.spawnBird_4(e);
                break;
            case "bird_5":
                DanmakuSpawns.spawnBird_5(e);
                break;
            case "bird_6":
                DanmakuSpawns.spawnBird_6(e);
                break;
            case "bird_7_1":
                DanmakuSpawns.spawnBird_7(e, true);
                break;
            case "bird_7_2":
                DanmakuSpawns.spawnBird_7(e, false);
                break;

            case "auto_1":
                DanmakuSpawns.spawnAuto_1(e);
                break;
            case "auto_2":
                DanmakuSpawns.spawnAuto_2(e);
                break;
            case "auto_3":
                DanmakuSpawns.spawnAuto_3(e);
                break;
            case "auto_3_sub":
                DanmakuSpawns.spawnAuto_3_sub(e);
                break;
            case "auto_4":
                DanmakuSpawns.spawnAuto_4(e);
                break;
            case "auto_5":
                DanmakuSpawns.spawnAuto_5(e);
                break;
            case "auto_6":
                DanmakuSpawns.spawnAuto_6(e);
                break;

            case "crystal_1":
                DanmakuSpawns.spawnCrystal_1(e);
                break;
            case "crystal_2":
                DanmakuSpawns.spawnCrystal_2(e);
                break;
            case "crystal_3":
                DanmakuSpawns.spawnCrystal_3(e);
                break;
            case "crystal_3_sub":
                DanmakuSpawns.spawnCrystal_3_sub(e);
                break;

            case "crystal_death_1":
                DanmakuSpawns.spawnCrystalDeath_1(e);
                break;
            case "crystal_death_2":
                PickupSpawns.spawnLife(e);
                DanmakuSpawns.spawnCrystalDeath_2(e);
                break;

            case "cloud_1":
                DanmakuSpawns.spawnCloud_1(e);
                break;
            case "cloud_2":
                DanmakuSpawns.spawnCloud_2(e);
                break;

            case "cloud_death_1":
                DanmakuSpawns.spawnCloudDeath_1(e);
                PickupSpawns.spawnPowerSmallRNG(e, 1d/2);
                break;

            case "water_1":
                DanmakuSpawns.spawnWater_1(e);
                break;
            case "water_2":
                DanmakuSpawns.spawnWater_2(e);
                break;

            case "water_death_1":
                DanmakuSpawns.spawnWaterDeath_1(e);
                break;
            case "water_death_2":
                PickupSpawns.spawnPowerSmallRNG(e, 1d/2);
                DanmakuSpawns.spawnWaterDeath_2(e, true);
                break;
            case "water_death_3":
                DanmakuSpawns.spawnWaterDeath_2(e, false);
                break;

            case "ember_1":
                DanmakuSpawns.spawnEmber_1(e);
                break;
            case "ember_2":
                DanmakuSpawns.spawnEmber_2(e);
                break;
            case "ember_3":
                DanmakuSpawns.spawnEmber_3(e);
                break;

            case "trap_spawn_1":
                EnemySpawns.spawnTrap(e, 1);
                break;
            case "trap_spawn_2":
                EnemySpawns.spawnTrap(e, 2);
                break;
            case "trap_spawn_3":
                EnemySpawns.spawnTrap(e, 3);
                break;

            case "trap_1":
                DanmakuSpawns.spawnTrap_1(e);
                break;
            case "trap_2":
                DanmakuSpawns.spawnTrap_2(e);
                break;
            case "trap_3":
                DanmakuSpawns.spawnTrap_3(e);
                break;
            case "trap_4":
                DanmakuSpawns.spawnTrap_4(e, 0);
                break;
            case "trap_4_sub_1":
                DanmakuSpawns.spawnTrap_4(e, 1);
                break;
            case "trap_4_sub_2":
                DanmakuSpawns.spawnTrap_4(e, 2);
                break;

            case "swisp_1":
                DanmakuSpawns.spawnSWisp_1(e);
                break;
            case "swisp_2":
                DanmakuSpawns.spawnSWisp_2(e);
                break;
            case "swisp_3":
                DanmakuSpawns.spawnSWisp_3(e);
                break;
            case "swisp_4":
                DanmakuSpawns.spawnSWisp_4(e);
                break;
            case "swisp_5":
                DanmakuSpawns.spawnSWisp_5(e);
                break;
            case "swisp_6":
                DanmakuSpawns.spawnSWisp_6(e);
                break;

            case "scloud_1":
                DanmakuSpawns.spawnSCloud_1(e, true);
                break;
            case "scloud_2":
                DanmakuSpawns.spawnSCloud_1(e, false);
                break;
            case "scloud_3":
                DanmakuSpawns.spawnSCloud_3(e, true);
                break;
            case "scloud_4":
                DanmakuSpawns.spawnSCloud_3(e, false);
                break;

//            case "scloud_death_1":
//                DanmakuSpawns.spawnSCloudDeath_1(e);
//                PickupSpawns.spawnPowerLarge(e, false);
//                break;

            case "shumanoid_1":
                DanmakuSpawns.spawnSHumanoid_1(e);
                break;
            case "shumanoid_2":
                DanmakuSpawns.spawnSHumanoid_2(e);
                break;
            case "shumanoid_3":
                DanmakuSpawns.spawnSHumanoid_3(e);
                break;
            case "shumanoid_3_sub_1":
                DanmakuSpawns.spawnSHumanoid_3_sub_1(e);
                break;
            case "shumanoid_3_sub_2":
                DanmakuSpawns.spawnSHumanoid_3_sub_2(e);
                break;
            case "shumanoid_4":
                DanmakuSpawns.spawnSHumanoid_4(e);
                break;




            //bosses
            case "b1_1_1":
                DanmakuSpawns.spawnB1_1_1(e);
                break;
            case "b1_1_2":
                DanmakuSpawns.spawnB1_1_2(e);
                break;
            case "b1_1_3":
                DanmakuSpawns.spawnB1_1_3(e);
                break;
            case "b1_2":
                DanmakuSpawns.spawnB1_2();
                break;
            case "b1_3":
                DanmakuSpawns.spawnB1_3(e);
                break;
            case "b1_3_sub":
                DanmakuSpawns.spawnB1_3_sub(e);
                break;

            case "b2_1":
                DanmakuSpawns.spawnB2_1(e);
                break;
            case "b2_2_1":
                DanmakuSpawns.spawnB2_2(e, true);
                break;
            case "b2_2_2":
                DanmakuSpawns.spawnB2_2(e, false);
                break;
            case "b2_2_1_sub":
                DanmakuSpawns.spawnB2_2_sub(e, true);
                break;
            case "b2_2_2_sub":
                DanmakuSpawns.spawnB2_2_sub(e, false);
                break;
            case "b2_3":
                DanmakuSpawns.spawnB2_3(e);
                break;
            case "b2_4":
                DanmakuSpawns.spawnB2_4(e);
                break;
            case "b2_4_sub":
                DanmakuSpawns.spawnB2_4_sub(e);
                break;

            case "b3_1_1":
                DanmakuSpawns.spawnB3_1_1(e);
                break;
            case "b3_1_2":
                DanmakuSpawns.spawnB3_1_2(e);
                break;
            case "b3_1_3":
                DanmakuSpawns.spawnB3_1_3(e);
                break;
            case "b3_1_4":
                DanmakuSpawns.spawnB3_1_4(e);
                break;
            case "b3_1_5":
                DanmakuSpawns.spawnB3_1_5(e);
                break;
            case "b3_1_6":
                DanmakuSpawns.spawnB3_1_6(e);
                break;
            case "b3_2":
                DanmakuSpawns.spawnB3_2(e);
                break;
            case "b3_3_1":
                DanmakuSpawns.spawnB3_3_1(e);
                break;
            case "b3_3_2":
                DanmakuSpawns.spawnB3_3_2(e);
                break;
            case "b3_3_3":
                DanmakuSpawns.spawnB3_3_3(e);
                break;
            case "b3_3_4":
                DanmakuSpawns.spawnB3_3_4(e);
                break;
            case "b3_3_5":
                DanmakuSpawns.spawnB3_3_5(e);
                break;
            case "b3_3_6":
                DanmakuSpawns.spawnB3_3_6(e);
                break;
            case "b3_3_7":
                DanmakuSpawns.spawnB3_3_7(e);
                break;
            case "b3_3_1_sub":
                DanmakuSpawns.spawnB3_3_sub(e, 1);
                break;
            case "b3_3_2_sub":
                DanmakuSpawns.spawnB3_3_sub(e, 2);
                break;
            case "b3_3_3_sub":
                DanmakuSpawns.spawnB3_3_sub(e, 3);
                break;
            case "b3_3_4_sub":
                DanmakuSpawns.spawnB3_3_sub(e, 4);
                break;
            case "b3_3_5_sub":
                DanmakuSpawns.spawnB3_3_sub(e, 5);
                break;
            case "b3_3_6_sub":
                DanmakuSpawns.spawnB3_3_sub(e, 6);
                break;
            case "b3_4_1":
                DanmakuSpawns.spawnB3_4_1(e);
                break;
            case "b3_4_2":
                DanmakuSpawns.spawnB3_4_2(e);
                break;
            case "b3_4_3":
                DanmakuSpawns.spawnB3_4_3(e);
                break;
            case "b3_4_3_sub":
                DanmakuSpawns.spawnB3_4_3_sub(e);
                break;
            case "b3_5_1":
                DanmakuSpawns.spawnB3_5_1(e);
                break;
            case "b3_5_2":
                DanmakuSpawns.spawnB3_5_2(e);
                break;
            case "b3_5_3":
                DanmakuSpawns.spawnB3_5_3(e);
                break;
            case "b3_5_4":
                DanmakuSpawns.spawnB3_5_4(e);
                break;
            case "b3_5_5":
                DanmakuSpawns.spawnB3_5_5(e);
                break;
            case "b3_5_6":
                DanmakuSpawns.spawnB3_5_6(e);
                break;
            case "b3_5_1_sub":
                DanmakuSpawns.spawnB3_5_sub(e, 1);
                break;
            case "b3_5_2_sub":
                DanmakuSpawns.spawnB3_5_sub(e, 2);
                break;
            case "b3_5_3_sub":
                DanmakuSpawns.spawnB3_5_sub(e, 3);
                break;
            case "b3_5_4_sub":
                DanmakuSpawns.spawnB3_5_sub(e, 4);
                break;
            case "b3_5_5_sub":
                DanmakuSpawns.spawnB3_5_sub(e, 5);
                break;
            case "b3_5_6_sub":
                DanmakuSpawns.spawnB3_5_sub(e, 6);
                break;

            case "b4_1_1":
                DanmakuSpawns.spawnB4_1_1(e);
                break;
            case "b4_1_2":
                DanmakuSpawns.spawnB4_1_2(e);
                break;
            case "b4_2_1":
                DanmakuSpawns.spawnB4_2(true);
                break;
            case "b4_2_2":
                DanmakuSpawns.spawnB4_2(false);
                break;
            case "b4_3_1":
                DanmakuSpawns.spawnB4_3_1(e);
                break;
            case "b4_3_2":
                DanmakuSpawns.spawnB4_3_2(e);
                break;
            case "b4_4":
                DanmakuSpawns.spawnB4_4(e);
                break;
            case "b4_4_sub_1":
                DanmakuSpawns.spawnB4_4_sub_1(e, true);
                break;
            case "b4_4_sub_2":
                DanmakuSpawns.spawnB4_4_sub_1(e, false);
                break;
            case "b4_4_sub_3":
                DanmakuSpawns.spawnB4_4_sub_2(e, true);
                break;
            case "b4_4_sub_4":
                DanmakuSpawns.spawnB4_4_sub_2(e, false);
                break;
            case "b4_5_1":
                DanmakuSpawns.spawnB4_5_1(e);
                break;
            case "b4_5_2":
                DanmakuSpawns.spawnB4_5_2(e);
                break;
            case "b4_6_1":
                DanmakuSpawns.spawnB4_6_1();
                break;
            case "b4_6_2":
                DanmakuSpawns.spawnB4_6_2(e);
                break;
            case "b4_6_2_sub_1":
                DanmakuSpawns.spawnB4_6_2_sub_1(e);
                break;
            case "b4_6_2_sub_2":
                DanmakuSpawns.spawnB4_6_2_sub_2(e);
                break;
            case "b4_6_2_sub_3":
                DanmakuSpawns.spawnB4_6_2_sub_3(e);
                break;
            case "b4_6_2_sub_4":
                DanmakuSpawns.spawnB4_6_2_sub_4(e);
                break;

            case "b5_1":
                DanmakuSpawns.spawnB5_1(e);
                break;
            case "b5_1_sub":
                DanmakuSpawns.spawnB5_1_sub(e);
                break;
            case "b5_2":
                DanmakuSpawns.spawnB5_2(e);
                break;
            case "b5_4":
                DanmakuSpawns.spawnB5_4(e);
                break;
            case "b5_5":
                DanmakuSpawns.spawnB5_5(e);
                break;

            case "b6_1_1":
                DanmakuSpawns.spawnB6_1_1(e);
                break;
            case "b6_1_2":
                DanmakuSpawns.spawnB6_1_2(e);
                break;
            case "b6_2_1":
                DanmakuSpawns.spawnB6_2(1);
                break;
            case "b6_2_2":
                DanmakuSpawns.spawnB6_2(2);
                break;
            case "b6_2_3":
                DanmakuSpawns.spawnB6_2(3);
                break;
            case "b6_2_4":
                DanmakuSpawns.spawnB6_2(4);
                break;
            case "b6_2_sub_1":
                EnemySpawns.spawnTrap(e, 101);
                break;
            case "b6_2_sub_2":
                EnemySpawns.spawnTrap(e, 102);
                break;
            case "b6_2_sub_3":
                EnemySpawns.spawnTrap(e, 103);
                break;
            case "b6_2_sub_4":
                EnemySpawns.spawnTrap(e, 104);
                break;
            case "trap_101":
                DanmakuSpawns.spawnB6_2_sub(e, 1);
                break;
            case "trap_102":
                DanmakuSpawns.spawnB6_2_sub(e, 2);
                break;
            case "trap_103":
                DanmakuSpawns.spawnB6_2_sub(e, 3);
                break;
            case "trap_104":
                DanmakuSpawns.spawnB6_2_sub(e, 4);
                break;
            case "b6_3_1":
                DanmakuSpawns.spawnB6_3_1(e);
                break;
            case "b6_3_2":
                DanmakuSpawns.spawnB6_3_2(e);
                break;
            case "b6_3_2_sub":
                DanmakuSpawns.spawnB6_3_2_sub(e);
                break;
            case "b6_4":
                DanmakuSpawns.spawnB6_4(e);
                break;
            case "b6_4_sub":
                EnemySpawns.spawnTrap(e, 105);
                break;
            case "trap_105":
                DanmakuSpawns.spawnB6_4_sub(e);
                break;
            case "b6_5":
                DanmakuSpawns.spawnB6_5(e);
                break;
            case "b6_5_sub":
                EnemySpawns.spawnTrap(e, 106);
                break;
            case "trap_106":
                DanmakuSpawns.spawnB6_5_sub(e);
                break;
            case "b6_6_1":
                DanmakuSpawns.spawnB6_6(e, 1);
                break;
            case "b6_6_2":
                DanmakuSpawns.spawnB6_6(e, 2);
                break;
            case "b6_6_3":
                DanmakuSpawns.spawnB6_6(e, 3);
                break;
            case "b6_6_sub":
                DanmakuSpawns.spawnB6_6_sub(e, false);
                break;
            case "b6_7":
                DanmakuSpawns.spawnB6_7(e);
                break;
            case "b6_8_1":
                DanmakuSpawns.spawnB6_8_1();
                break;
            case "b6_8_1_sub_1":
                DanmakuSpawns.spawnB6_8_1_sub(e, true);
                break;
            case "b6_8_1_sub_2":
                DanmakuSpawns.spawnB6_8_1_sub(e, false);
                break;
            case "b6_8_2":
                DanmakuSpawns.spawnB6_8_2(e);
                break;

            case "b7_1":
                DanmakuSpawns.spawnB7_1(e);
                break;
            case "b7_2_1":
                DanmakuSpawns.spawnB7_2_1(e);
                break;
            case "b7_2_2":
                DanmakuSpawns.spawnB7_2_2(e);
                break;
            case "b7_3_1":
                DanmakuSpawns.spawnB7_3_1(e);
                break;
            case "b7_3_2":
                DanmakuSpawns.spawnB7_3_2(e);
                break;
            case "b7_4":
                DanmakuSpawns.spawnB7_4(e);
                break;
            case "b7_4_sub_1":
                DanmakuSpawns.spawnB7_4_sub(e, true);
                break;
            case "b7_4_sub_2":
                DanmakuSpawns.spawnB7_4_sub(e, false);
                break;
            case "b7_5":
                DanmakuSpawns.spawnB7_5(e);
                break;
            case "b7_6_1":
                DanmakuSpawns.spawnB7_6_1(e);
                break;
            case "b7_6_2":
                DanmakuSpawns.spawnB7_6_2(e);
                break;
            case "b7_7":
                DanmakuSpawns.spawnB7_7(e);
                break;
            case "b7_8_1":
                EnemySpawns.spawnB7_8_1();
                break;
            case "b7_8_1_sub":
                DanmakuSpawns.spawnB7_8_1_sub(e);
                break;
            case "b7_8_2":
                DanmakuSpawns.spawnB7_8_2(e);
                break;
            case "b7_9":
                DanmakuSpawns.spawnB7_9(e);
                break;
            case "b7_10":
                DanmakuSpawns.spawnB7_10(e);
                break;
            case "b7_11":
                DanmakuSpawns.spawnB7_11(e);
                break;
            case "b7_12_1":
                DanmakuSpawns.spawnB7_12_1();
                break;
            case "b7_12_1_sub":
                DanmakuSpawns.spawnB7_12_1_sub(e);
                break;
            case "b7_12_2":
                DanmakuSpawns.spawnB7_12_2(e);
                break;
            case "b7_13":
                DanmakuSpawns.spawnB7_13(e);
                break;
            case "b7_13_sub":
                DanmakuSpawns.spawnB7_13_sub(e);
                break;
            case "b7_14":
                DanmakuSpawns.spawnB7_14(e);
                break;
            case "b7_14_sub_1":
                DanmakuSpawns.spawnB7_14_sub_1(e, true);
                break;
            case "b7_14_sub_2":
                DanmakuSpawns.spawnB7_14_sub_1(e, false);
                break;
            case "b7_14_sub_3":
                DanmakuSpawns.spawnB7_14_sub_3(e, true);
                break;
            case "b7_14_sub_4":
                DanmakuSpawns.spawnB7_14_sub_3(e, false);
                break;
            case "b7_15":
                DanmakuSpawns.spawnB7_15(e);
                break;


//////////////////////GRAPHIC ONLY/////////////////////
            case "background_1":
                BackgroundSpawns.spawnBackground(1);
                break;
            case "background_2":
                BackgroundSpawns.spawnBackground(2);
                break;
            case "background_3":
                BackgroundSpawns.spawnBackground(3);
                break;
            case "background_4":
                BackgroundSpawns.spawnBackground(4);
                break;
            case "background_5":
                BackgroundSpawns.spawnBackground(5);
                break;
            case "background_6":
                BackgroundSpawns.spawnBackground(6);
                break;
            case "background_7":
                BackgroundSpawns.spawnBackground(7);
                break;

            case "sword_swipe":
                MiscSpawns.spawnSwordSwipe(e);
                break;

            case "hp_bar":
                MiscSpawns.spawnHPBar();
                break;


///////////////////////DIALOGUE/////////////////////////
            case "dialogue_pre":
                MenuManager.getMainBoard().write(new BoardMessage("command_add_dialogue pre_" + GameDriver.getCurrentStage(), 1, this));
                break;
            case "dialogue_post":
                MenuManager.getMainBoard().write(new BoardMessage("command_add_dialogue post_" + GameDriver.getCurrentStage(), 1, this));
                break;
            case "game_ender":
                MiscSpawns.spawnGameEnder();
                break;

/////////////////////MISC/////////////////////
            case "dummy":
                EnemySpawns.spawnDummy();
                break;
        }
    }

    private void parseComponent(Component_Spawning sComp, Entity e){
        if(sComp instanceof Component_SpawningPlayer){
            Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player", e.getEntityID()));
            if(pComp.canInput()) {
                int shotType = ((Component_SpawningPlayer) sComp).getShotType();
                InputTable it = GameDriver.getCurrentInputTable();
                String[] inputs = it.getInputs();
                //z button
                int attackMaxTick = 40;
                //if there's no spawnUnit (no attack currently going on)
                if (sComp.getSpawnUnits().isEmpty() ||
                        //or if there's only 1 spawnUnit and it's not an attack
                        (sComp.getSpawnUnits().size() == 1 && sComp.getSpawnUnits().get(0).getMaxTick() != attackMaxTick)) {

                    if (inputs[1].equals("just pressed") || inputs[1].equals("still pressed")) {
                        int power = pComp.getPower();
                        int plvl = PlayerSpawns.powerLevel(power);
                        //normal bullets
                        sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                new SIMultipleTick(10, "player" + shotType)));
                        //special bullets
                        switch(shotType) {
                            case 1:
                                if(plvl <= 1) {
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(40, "player" + shotType + "_shot")));
                                }
                                else{
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(20, "player" + shotType + "_shot")));
                                }
                                break;
                            case 2:
                                if(plvl <= 2) {
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(40, "player" + shotType + "_shot")));
                                }
                                else if(plvl == 3){
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(20, "player" + shotType + "_shot")));
                                }
                                else if(plvl == 4){
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(10, "player" + shotType + "_shot")));
                                }
                                else if(plvl == 5){
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(8, "player" + shotType + "_shot")));
                                }
                                break;
                            case 3:
                                if(plvl == 1 || plvl == 2) {
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(40, "player" + shotType + "_shot")));
                                }
                                else if(plvl == 3){
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(20, "player" + shotType + "_shot")));
                                }
                                else if(plvl == 4){
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(10, "player" + shotType + "_shot")));
                                }
                                else if(plvl == 5){
                                    sComp.addUnit(new SpawnUnit(attackMaxTick, false,
                                            new SIMultipleTick(8, "player" + shotType + "_shot")));
                                }
                                break;
                        }
                    }
                }
                //x button
                //bombMaxTick is the timer, as well as the regular cooldown timer.
                //after-death unbombability is pComp.canInput()

                if (inputs[2].equals("just pressed")) {
                    if(pComp.getBombs() > 0) {
                        int bombMaxTick = 0;
                        switch (shotType) {
                            case 1:
                                bombMaxTick = 6 * 60;
                                break;
                            case 2:
                                bombMaxTick = 5 * 60;
                                break;
                            case 3:
                                bombMaxTick = 3 * 60;
                                break;
                        }
                        //if there's no spawnUnit (no attack currently going on)
                        if (sComp.getSpawnUnits().isEmpty() ||
                                //or if there's only 1 spawnUnit and it's not a bomb
                                !hasActiveBombs(sComp.getSpawnUnits(), bombMaxTick)) {
                            //invulnerability handled here (collisions right after)
                            pComp.setNoHitTimer(bombMaxTick + (60 * 2));
                            //removing player death messages to internal msg
                            SystemManager.getECSBoard().write(new BoardMessage("bomb", bombMaxTick, "gameplay"));
                            SystemManager.getECSBoard().write(new BoardMessage("bombPress", 1, "gameplay"));
                            switch (shotType) {
                                case 1:
                                    sComp.addUnit(new SpawnUnit(bombMaxTick, false,
                                            new SISingleSpawn("bomb" + shotType)));
                                    break;
                                case 2:
                                    sComp.addUnit(new SpawnUnit(bombMaxTick, false,
                                            new SISingleSpawn("bomb" + shotType)));
                                    break;
                                case 3:
                                    sComp.addUnit(new SpawnUnit(bombMaxTick, false,
                                            new SISingleSpawn("bomb" + shotType)));
//                                    SystemManager.getECSBoard().write(new BoardMessage("clear", 1, "gameplay"));
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean hasActiveBombs(ArrayList<SpawnUnit> spawnUnits, int bombMaxTick) {
        //if in method already guaranteed SU is not empty
        for(SpawnUnit su : spawnUnits) {
            if(su.getMaxTick() == bombMaxTick) {
                return true;
            }
        }
        return false;
    }


    private static class PlayerSpawns {
        //attack = normal bullets - shot = special shot bullets. Necessary for different frequencies
        private static void spawnAttack1(Entity player){
            Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player", player.getEntityID()));
            Vector playerPos = getPos(player);
            InputTable it = GameDriver.getCurrentInputTable();
            String[] inputs = it.getInputs();
            boolean focused = inputs[0].equals("just pressed") || inputs[0].equals("still pressed");
            int power = pComp.getPower();
            int plvl = powerLevel(power);
            double speed = 40;
            switch(plvl){
                case 0:
                case 1:
                    constructAProjectile(playerPos, new Vector(180, speed));
                    break;
                case 2:
                case 3:
                    Vector offset = new Vector(17, 0);
                    constructAProjectile(Vector.add(playerPos, offset), new Vector(180, speed));
                    constructAProjectile(Vector.subtract(playerPos, offset), new Vector(180, speed));
                    break;
                case 4:
                    constructAProjectile(playerPos, new Vector(180, speed));
                    double angleOff;
                    if(!focused){
                        angleOff = 7;
                    }
                    else{
                        angleOff = 4;
                    }
                    constructAProjectile(playerPos, new Vector(180 + angleOff, speed));
                    constructAProjectile(playerPos, new Vector(180 - angleOff, speed));
                    break;
                case 5:
                    if(!focused){
                        angleOff = 7;
                    }
                    else{
                        angleOff = 4;
                    }
                    constructAProjectile(playerPos, new Vector(180, speed));
                    constructAProjectile(playerPos, new Vector(180 + angleOff, speed));
                    constructAProjectile(playerPos, new Vector(180 - angleOff, speed));
                    constructAProjectile(playerPos, new Vector(180 + (2 * angleOff), speed));
                    constructAProjectile(playerPos, new Vector(180 - (2 * angleOff), speed));
                    break;
            }
        }
        //2 and 3 angleOff is 5 and 3
        private static void spawnAttack2(Entity player){
            Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player", player.getEntityID()));
            Vector playerPos = getPos(player);
            InputTable it = GameDriver.getCurrentInputTable();
            String[] inputs = it.getInputs();
            boolean focused = inputs[0].equals("just pressed") || inputs[0].equals("still pressed");
            int power = pComp.getPower();
            int plvl = powerLevel(power);
            double speed = 40;
            switch(plvl){
                case 0:
                case 1:
                    constructAProjectile(playerPos, new Vector(180, speed));
                    break;
                case 2:
                case 3:
                    Vector offset = new Vector(17, 0);
                    constructAProjectile(Vector.add(playerPos, offset), new Vector(180, speed));
                    constructAProjectile(Vector.subtract(playerPos, offset), new Vector(180, speed));
                    break;
                case 4:
                    constructAProjectile(playerPos, new Vector(180, speed));
                    double angleOff;
                    if(!focused){
                        angleOff = 5;
                    }
                    else{
                        angleOff = 3;
                    }
                    constructAProjectile(playerPos, new Vector(180 + angleOff, speed));
                    constructAProjectile(playerPos, new Vector(180 - angleOff, speed));
                    break;
                case 5:
                    offset = new Vector(17, 0);
                    if(!focused){
                        angleOff = 5;
                    }
                    else{
                        angleOff = 3;
                    }
                    constructAProjectile(Vector.add(playerPos, offset), new Vector(180, speed));
                    constructAProjectile(Vector.subtract(playerPos, offset), new Vector(180, speed));
                    constructAProjectile(Vector.add(playerPos, offset), new Vector(180 - angleOff, speed));
                    constructAProjectile(Vector.subtract(playerPos, offset), new Vector(180 + angleOff, speed));
                    break;
            }
        }
        private static void spawnAttack3(Entity player){
            //i'm just gonna go with these are the same for now, can change later though
            spawnAttack2(player);
        }

        private static void spawnShot1(Entity player){
            Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player", player.getEntityID()));
            Vector playerPos = getPos(player);
            InputTable it = GameDriver.getCurrentInputTable();
            String[] inputs = it.getInputs();
            boolean focused = inputs[0].equals("just pressed") || inputs[0].equals("still pressed");
            int power = pComp.getPower();
            int plvl = powerLevel(power);
            double speed;
            if(!focused){
                speed = 20;
            }
            else{
                speed = 23;
            }

            switch(plvl){
                case 1:
                case 2:
                    constructSProjectile1(playerPos, new Vector(180, speed), focused);
                    break;
                case 3:
                case 4:
                    constructSProjectile1(playerPos, new Vector(180, speed), focused);
                    constructSProjectile1(playerPos, new Vector(180, speed), focused);
                    break;
                case 5:
                    for(int i = 0; i < 4; i++){
                        constructSProjectile1(playerPos, new Vector(180, speed), focused);
                    }
            }
        }
        private static void spawnShot2(Entity player){
            Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player", player.getEntityID()));
            Vector playerPos = getPos(player);
            InputTable it = GameDriver.getCurrentInputTable();
            String[] inputs = it.getInputs();
            boolean focused = inputs[0].equals("just pressed") || inputs[0].equals("still pressed");
            int power = pComp.getPower();
            int plvl = powerLevel(power);
            if(plvl >= 1) {
                if (!focused) {
                    constructSProjectile2_small_1(playerPos, new Vector(180, 30), new Vector(25, 0));
                } else {
                    constructSProjectile2_large_1(playerPos, new Vector(180, 30));
                }
            }
        }
        private static void spawnShot3(Entity player){
            Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player", player.getEntityID()));
            Vector playerPos = getRealPos(player);
            InputTable it = GameDriver.getCurrentInputTable();
            String[] inputs = it.getInputs();
            boolean focused = inputs[0].equals("just pressed") || inputs[0].equals("still pressed");
            int power = pComp.getPower();
            int plvl = powerLevel(power);

            Vector offsetA = new Vector(30, 10);
            Vector offsetB = new Vector(-30, 10);

            //no power scaling?
            if(!focused) {
                double angle = 186.25;
                constructSProjectile3(Vector.add(playerPos, offsetA), player, false, angle);
                constructSProjectile3(Vector.add(playerPos, offsetB), player, true, -angle);
            }
            else{
                double angle = 184;
                constructSProjectile3(Vector.add(playerPos, offsetA), player, false, angle);
                constructSProjectile3(Vector.add(playerPos, offsetB), player, true, -angle);
            }
        }

        private static void spawnBomb1(Entity player){
            Vector playerPos = getPos(player);
            double angleAdd = MiscUtil.rangeRandom(0, 30, GameDriver.randomDouble());
            for(int i = 0; i < 12; i++) {
                double angle = MiscUtil.fixAngle(angleAdd + (i * (360/12)));
                Vector spawnPos = Vector.add(playerPos, Vector.toVelocity(new Vector(angle, .01)));
                constructBombProjectile1(spawnPos, playerPos, false, 4.5);
                constructBombProjectile1(spawnPos, playerPos, true, 3.5);
                constructBombProjectile1(spawnPos, playerPos, false, 2.5);
            }
        }
        private static void spawnBomb2(Entity player){
            Vector playerPos = getPos(player);
            double xPos = playerPos.getA();
            constructBombProjectile2(xPos);
        }
        private static void spawnBomb3(Entity player){
            constructBombProjectile3(player);
        }


        private static void spawnBomb1Decay(Entity p){
            Vector pos = getPos(p);
            for(int i = 0; i < 4; i++){
                double angle = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                constructSProjectile1(pos, new Vector(angle, .5), false);
            }
        }

        private static void spawnShot2Decay(Entity p, boolean large){
            Vector pos = getPos(p);
            double spawnDist;
            if(!large){
                spawnDist = 10;
            }
            else{
                spawnDist = 15;
            }
            double angle = 30;
            double speed = 30;

            for(int i = -1; i <= 1; i+= 2){
                double angle_ = angle * i;
                Vector spawnPos = Vector.add(pos, Vector.toVelocity(new Vector(angle_, spawnDist)));
                if(!large) {
                    constructSProjectile2_small_2(spawnPos, new Vector(angle_, speed));
                }else{
                    constructSProjectile2_large_2(spawnPos, new Vector(angle_, speed));
                }
            }
        }

        private static void constructAProjectile(Vector spawnPos, Vector polar){
            double dmg = 1;
            double radius = 20;
            constructEntity(new Component[]{Comps.pos(spawnPos), Comps.tpp(), Comps.movePol(polar), Comps.ccSquareProj(radius, dmg),
            Comps.ppSpriteRotate("pp_default", new Vector(-16, -30), MiscUtil.angleToSpriteRotation(polar.getA()))});
        }

        private static void constructSProjectile1(Vector spawnPos, Vector polar, boolean focused){

            double dmg = 1.1;
            double radius = 20;
            double bigRadius = 150;
            double angleRand;
            if(!focused){
                angleRand = 10;
            }
            else{
                angleRand = 2;
            }

            double speedRand;
            if(!focused){
                speedRand = 4;
            }
            else{
                speedRand = 1.5;
            }
            Vector randomedPolar = new Vector(MiscUtil.rangeRandom(polar.getA() - angleRand, polar.getA() + angleRand, GameDriver.randomDouble()),
                    MiscUtil.rangeRandom(polar.getB() - speedRand, polar.getB() + speedRand, GameDriver.randomDouble()));
            Component_Movement smallMoveComp = Comps.movePol(randomedPolar);
            smallMoveComp.addMode(new MoveModeTransVector("speed_compounding", new Vector(1, .965)));
            ArrayList<Behavior> smallBehaviors = new ArrayList<>();
            Component_Behavior smallBehaviorComp = new Component_Behavior(smallBehaviors);
            double lifetime = MiscUtil.rangeRandom(180, 240, GameDriver.randomDouble());

            String smallID = constructEntity(new Component[]{Comps.pos(spawnPos), Comps.tpp(), smallMoveComp, Comps.ccSquareProj(radius, dmg),
                    Comps.ppSprite("pp_shot1", new Vector(-10, -10)), smallBehaviorComp});

            String largeID = constructEntity(new Component[]{Comps.pos(spawnPos), Comps.tpp(), new Component_Movement(new MoveModeAddString("follow_entity", smallID)),
                    Comps.ccSquareProj(bigRadius, 0), Comps.lifetime((int)lifetime + 1)});

            //since both entities need to know the ID of the other, I construct the entities first and *then* program in the behaviors


            BehaviorVector timer = new BehaviorVector("timer", new Vector((int)lifetime));
            Behavior deactivate = new Behavior("deactivate");
            ArrayList<Behavior> timerList = new ArrayList<>();
            timerList.add(timer);
            timerList.add(deactivate);
            Routine timeOut = new Routine("routine", timerList, false);

            BehaviorString waitDeath = new BehaviorString("wait_until_death", largeID);
            Behavior homing = new BehaviorString("wasp_homing_behavior", largeID);
            ArrayList<Behavior> homingList = new ArrayList<>();
            homingList.add(waitDeath);
            homingList.add(homing);
            Routine homingRoutine = new Routine("routine", homingList, false);

            smallBehaviors.add(timeOut);
            smallBehaviors.add(homingRoutine);
        }

        //unfocused - small projectiles
        private static void constructSProjectile2_small_1(Vector spawnPos, Vector polar, Vector offset){
            double dmg = 1;
            double x = 15;
            double y = 20;
            //since these behaviors have no state i'm just using them for both projectiles -> with different routines that is
            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorDouble("boundary_top", 0));
            al.add(new Behavior("deactivate"));

            constructEntity(new Component[]{Comps.pos(Vector.add(spawnPos, offset)), Comps.tpp(), Comps.movePol(polar), Comps.ccXYProj(x, y, dmg),
                    Comps.ppSprite("pp_shot2_small1", new Vector(-15, -24)), Comps.dSpawn("shot2_decay_small"),
                    new Component_Behavior(new Routine("routine", al, false))});
            constructEntity(new Component[]{Comps.pos(Vector.subtract(spawnPos, offset)), Comps.tpp(), Comps.movePol(polar), Comps.ccXYProj(x, y, dmg),
                    Comps.ppSprite("pp_shot2_small1", new Vector(-15, -24)), Comps.dSpawn("shot2_decay_small"),
                    new Component_Behavior(new Routine("routine", al, false))});
        }
        //focused - large projectiles
        private static void constructSProjectile2_large_1(Vector spawnPos, Vector polar){
            double dmg = 2.3;
            double x = 22;
            double y = 30;
            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorDouble("boundary_top", 0));
            al.add(new Behavior("deactivate"));

            constructEntity(new Component[]{Comps.pos(spawnPos), Comps.tpp(), Comps.movePol(polar), Comps.ccXYProj(x, y, dmg),
                    Comps.ppSprite("pp_shot2_large1", new Vector(-45d/2, -36)), Comps.dSpawn("shot2_decay_large"),
                    new Component_Behavior(new Routine("routine", al, false))});
        }
        private static void constructSProjectile2_small_2(Vector spawnPos, Vector polar){
            double dmg = .5;
            double radius = 10;
            Vector offset = new Vector(0, 10);
            constructEntity(new Component[]{Comps.pos(Vector.add(spawnPos, offset)), Comps.tpp(), Comps.movePol(polar), Comps.ccSquareProj(radius, dmg),
                    Comps.ppSpriteRotate("pp_shot2_small2", new Vector(-7, -17), -(polar.getA() - 180))});
        }
        private static void constructSProjectile2_large_2(Vector spawnPos, Vector polar){
            double dmg = 1.15;
            double radius = 20;
            Vector offset = new Vector(0, 15);
            constructEntity(new Component[]{Comps.pos(Vector.add(spawnPos, offset)), Comps.tpp(), Comps.movePol(polar), Comps.ccSquareProj(radius, dmg),
                    Comps.ppSpriteRotate("pp_shot2_large2", new Vector(-10.5, -25.5), -(polar.getA() - 180))});
        }
        private static void constructSProjectile3(Vector spawnPos, Entity center, boolean clockwise, double angle){
            double dmg = 2;
            double radius = 19;

            int time = 21;
            double speed = 18;
            double distChange = 1.03;
//            int ignoreTime = 20;
//            double shootAngle = angle;
//            if(clockwise){
//                shootAngle = MiscUtil.fixAngle(shootAngle * -1);
//            }
            double shootSpeed = 40;

            Vector realPos = getRealPos(center);
            Vector initPos = new Vector(Vector.getDistance(realPos, spawnPos), Vector.getAngle(realPos, spawnPos));
            Component_Movement mComp = new Component_Movement(new MoveModeAddOrbitVariableDist("orbit_angular_velocity_variable_dist", clockwise, speed, realPos,
                    initPos, distChange));
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
//            bList.add(new BehaviorString("continue_trajectory_ignore_orbit", center.getEntityID()));
//            bList.add(new BehaviorDouble("set_speed", shootSpeed));
            bList.add(new BehaviorVector("set_polar", new Vector(angle, shootSpeed)));
            Routine r = new Routine("routine", bList, false);
            Behavior spriteRotate = new Behavior("rotate_sprite_with_angle");

            ArrayList<Behavior> topList = new ArrayList<>();
            topList.add(r);
            topList.add(spriteRotate);
            Component_Behavior bComp = new Component_Behavior(topList);

            constructEntity(new Component[]{Comps.pos(spawnPos), Comps.tpp(), mComp, bComp,
                    Comps.ccSquareProj(radius, dmg), Comps.sprite("pp_shot3", new Vector(-7, -30))});
        }

        private static void constructBombProjectile1(Vector spawnPos, Vector center, boolean clockwise, double distChange){
            double dmg = .2;
            double radius = 50;

            Component_Movement mComp = new Component_Movement(new MoveModeAddOrbitVariableDist("orbit_recalc_variable_dist", clockwise, 15, center.deepClone(), distChange),
                    new MoveModeTransVector("speed_compounding", new Vector(1, .99)));
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(300)));
            bList.add(new Behavior("deactivate"));
            Component_Behavior bComp = new Component_Behavior(new Routine("routine", bList,false));


            constructEntity(new Component[]{Comps.pos(spawnPos), Comps.tpb(), mComp, bComp, Comps.dSpawn("bomb1_decay"),
                    Comps.ccSquareProj(radius, dmg), Comps.sprite("b1", new Vector(-51, -51))});
        }
        private static void constructBombProjectile2(double xPos){
            double dmg = .4;
            constructEntity(new Component[]{Comps.pos(new Vector(xPos, 0)), Comps.tpb(), Comps.lifetime(240),
                    new Component_Collision(new AABB(-170, 170, 0, 777), 1, dmg),
                    Comps.loopAnim("b2", 4, new Vector(-332/2, 0), 1)});
        }
        private static void constructBombProjectile3(Entity player){
            //no damage
            //follows player
            double radius = 62;
            Vector pos = getPos(player);
            Component_Movement follow = new Component_Movement(new MoveModeAddString("follow_entity_closely", player.getEntityID()));
            constructEntity(new Component[]{Comps.pos(pos), Comps.tpb(), Comps.lifetime(180), follow,
                    Comps.ccSquareProj(radius, 0), Comps.sprite("b3", new Vector(-80, -80))});
        }

        private static int powerLevel(int power){
            //0 = 0-7
            //1 = 8-15
            //2 = 16-31
            //3 = 32-63
            //4 = 64-127
            //5 = 128
            if(power >= 64){
                if(power == 128){
                    return 5;
                }
                else{
                    return 4;
                }
            }
            if(power >= 16){
                if(power >= 32){
                    return 3;
                }
                else{
                    return 2;
                }
            }
            if(power >= 8){
                return 1;
            }
            return 0;
        }
    }
    private static class EnemySpawns{
        private static void spawn1_1_1(){
            construct1_pairFairies(false, 150, 100, 50, 1.85);
        }
        private static void spawn1_2_1(){
            construct1_pairFairies(true, 600, 120, 50, 2);
        }
        private static void spawn1_3_1(){
            double speed = 2.4;
            double xGap = GameDriver.getGameWidth()/8;
            double crossTop = 100;
            double crossGap = 25;

            double yMidBase = 30;
            double yMidGap = 15;

            int delayGap = 12;
            for(int i = 1; i <= 7; i++){
                double x = xGap * i;
                double yCross = crossTop += crossGap;
                int delay = delayGap * i;
                construct1_fairy1SingleAttack(x, yMidBase += yMidGap, yCross, speed, delay);
            }
        }
        private static void spawn1_4_1(){
            double y = 110;
            double angle = 77;
            double speed = 2.8;
            spawn1_4_helper(y, angle, speed, true);
        }
        private static void spawn1_4_2(){
            double y = 85;
            double angle = -72;
            double speed = 2.9;
            spawn1_4_helper(y, angle, speed, false);
        }
        private static void spawn1_4_3(){
            double y = 98;
            double angle = 88;
            double speed = 3;
            spawn1_4_helper(y, angle, speed, true);
        }
        private static void spawn1_4_4(){
            double y = 42;
            double angle = -70;
            double speed = 3.1;
            spawn1_4_helper(y, angle, speed, false);
        }
        private static void spawn1_4_helper(double y, double angle, double speed, boolean left){
            Vector spawn;
            if(left) {
                spawn = spawnVLeft(y);
            }else{
                spawn = spawnVRight(y);
            }
            Vector polar = new Vector(angle, speed);
            construct1_fairyCrossAttacks(spawn, polar);
        }
        private static void spawn1_5_1(){
            //top down
            Random r = new Random((long)GameDriver.getTick());
            int lowBound = 50;
            int highBound = (GameDriver.getGameWidth() - 50);

            double x1 = MiscUtil.rangeRandom(lowBound, highBound, r.nextDouble());
            double x2 = MiscUtil.rangeRandom(lowBound, highBound, r.nextDouble());
            if(Math.abs(x2 - x1) <= 25){
                if(x2 < x1){
                    x2 -= 25;
                }
                else{
                    x2 += 25;
                }
            }
            construct1_fairyDown(x1);
            construct1_fairyDown(x2);
        }
        private static void spawn1_5_2(){
            //fire shit
            //just finding a number that works lmao
            Random r = new Random((long)GameDriver.getTick() * 1245);
            int lowBound = 50;
            int highBound = (GameDriver.getGameWidth() - 50);

            double x = MiscUtil.rangeRandom(lowBound, highBound, r.nextDouble());
            construct1_fairyTopShooting(x);
        }
        private static void spawn1_6_1(){
            spawn1_6_straight_helper(110, true, 11);
        }
        private static void spawn1_6_2(){
            spawn1_6_straight_helper(200, false, 11);
        }
        private static void spawn1_6_3(){
            spawn1_6_curved_helper(165, true, 11, 45);
        }
        private static void spawn1_6_4(){
            spawn1_6_curved_helper(185, false, 11, -135);
        }
        private static void spawn1_6_straight_helper(double y, boolean left, double speed){
            Vector pos;
            double angle;
            if(left){
                pos = spawnVLeft(y);
                angle = 90;
            }
            else{
                pos = spawnVRight(y);
                angle = -90;
            }
            Vector polar = new Vector(angle, speed);
            construct1_wispStraight(pos, polar);
        }
        private static void spawn1_6_curved_helper(double y, boolean left, double speed, double angle){
            Vector pos;
            if(left){
                pos = spawnVLeft(y);
            }
            else{
                pos = spawnVRight(y);
            }
            Vector polar = new Vector(angle, speed);
            construct1_wispCurved(pos, polar);
        }
        private static void spawn1_7_1(){
            double speed = 1.9;
            double low = 50;
            double high = 200;

            Random r = new Random((long)GameDriver.getTick());
            double y1 = MiscUtil.rangeRandom(low, high, r.nextDouble());
            double y2 = MiscUtil.rangeRandom(low, high, r.nextDouble());
            Vector p1 = spawnVLeft(y1);
            Vector p2 = spawnVRight(y2);
            Vector pol1 = new Vector(90, speed);
            Vector pol2 = new Vector(-90, speed);
            construct1_fairyCrossShooting(p1, pol1);
            construct1_fairyCrossShooting(p2, pol2);
        }
        private static void spawn1_8_1(){
            double speed1 = 4;
            double speed2 = 2;
            double hp = 138;
            double x = ((double)GameDriver.getGameWidth())/2;
            Vector spawnPos = spawnVTop(x);
            Vector midPos = new Vector(x, 150);
            Vector fPolar = new Vector(0, speed2);

            SpawnUnit su = new SpawnUnit((4 * 60) + 1, false, new SIMultipleTick(30, "fairy_4"));
            constructEntity(new Component[]{Comps.pos(spawnPos), Comps.te(), Comps.singleAttack(midPos, speed1, 15, 34 + (4 * 60) + 1, fPolar, su),
                    Comps.mobSprite("fairy"), Comps.ccMob("fairy", hp), Comps.dSpawn("pickup_bomb")});

        }

        private static void spawn2_1_1(){
            Random r = new Random((long)GameDriver.getTick());
            for(int i = 0; i < 5; i++){
                double x = MiscUtil.rangeRandom(0, GameDriver.getGameWidth(), r.nextDouble());
                construct2_wispHoming(x);
            }
        }
        private static void spawn2_2_1(){
            construct2_wispSpiral1(140, 120);
        }
        private static void spawn2_2_2(){
            construct2_wispSpiral1(340, 107);
        }
        private static void spawn2_2_3(){
            construct2_wispSpiral1(500, 82);
        }
        private static void spawn2_2_4(){
            construct2_wispSpiral1(520, 170);
        }
        private static void spawn2_2_5(){
            construct2_wispSpiral1(330, 110);
        }
        private static void spawn2_2_6(){
            construct2_wispSpiral1(120, 65);
        }
        private static void spawn2_3_1(){
            construct2_birdCrossing(134, true, 3, "bird_3");
        }
        private static void spawn2_3_2(){
            construct2_birdCrossing(67, false, 4.5, "bird_1");
        }
        private static void spawn2_3_3(){
            construct2_birdCrossing(92, true, 3.7, "bird_2");
        }
        private static void spawn2_3_4(){
            construct2_birdCrossing(115, false, 3.4, "bird_4");
        }
        private static void spawn2_4_1(){
            construct2_wispSpiral2(120, 65);
        }
        private static void spawn2_4_2(){
            construct2_wispSpiral2(330, 110);
        }
        private static void spawn2_4_3(){
            construct2_wispSpiral2(520, 170);
        }
        private static void spawn2_4_4(){
            construct2_wispSpiral2(500, 82);
        }
        private static void spawn2_4_5(){
            construct2_wispSpiral2(340, 107);
        }
        private static void spawn2_4_6(){
            construct2_wispSpiral2(140, 120);
        }
        private static void spawn2_5_1(){
            double speed1 = 3;
            double speed2 = 1.5;
            double endHP = 40;
            double x = ((double)GameDriver.getGameWidth())/2;
            Vector spawnPos = spawnVTop(x);
            Vector midPos = new Vector(x, 180);
            Vector fPolar = new Vector(0, speed2);

            int multi;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    multi = 15;
                    break;
                case 2:
                    multi = 9;
                    break;
                case 3:
                    multi = 6;
                    break;
                case 4:
                    multi = 4;
                    break;
                default:
                    multi = 25;
                    break;
            }
            SpawnUnit su = new SpawnUnit(60 * 10, false, new SIMultipleTick(multi, "wisp_3"));

            Component_Behavior b = Comps.singleAttack(midPos, speed1, 15, 60 * 11, fPolar, su);
            ((Routine)b.getBehaviors().get(0)).getBehaviors().add(new BehaviorDouble("set_hp", endHP));
            b.addBehavior(new BehaviorDouble("rotate_sprite", 8));
            constructEntity(new Component[]{Comps.pos(spawnPos), Comps.te(), b,
                    Comps.mobSprite("wisp"), Comps.ccMob("wisp", 100000000), Comps.dSpawn("pickup_life")});
        }

        private static void spawn3_1_1(){
            double y = 130;
            double x = 80;
            construct3_autoHexWall(x, y, true);
        }
        private static void spawn3_1_2(){
            Random r = new Random((long)GameDriver.getTick() + 21235);
            r.nextDouble();
            double x = MiscUtil.rangeRandom(GameDriver.getGameWidth() - 170, GameDriver.getGameWidth() - 50, r.nextDouble());
            double y = MiscUtil.rangeRandom(50, 300, r.nextDouble());
            construct3_autoSideShot(x, y, false);
        }
        private static void spawn3_2_1(){
            double y = 130;
            double x = GameDriver.getGameWidth() - 80;
            construct3_autoHexWall(x, y, false);
        }
        private static void spawn3_2_2(){
            Random r = new Random((long)GameDriver.getTick() + 21235);
            r.nextDouble();
            double x = MiscUtil.rangeRandom(50, 170, r.nextDouble());
            double y = MiscUtil.rangeRandom(50, 300, r.nextDouble());
            construct3_autoSideShot(x, y, true);
        }
        private static void spawn3_3_1(){
            Vector pos = spawnVLeft(98);
            double angle = 80;
            double speed = 2;
            Vector polar = new Vector(angle, speed);
            construct3_autoCrossing(pos, polar, "auto_3");
        }
        private static void spawn3_3_2(){
            Vector pos = spawnVRight(150);
            double angle = -100;
            double speed = 2;
            Vector polar = new Vector(angle, speed);
            construct3_autoCrossing(pos, polar, "auto_4");
        }
        private static void spawn3_3_3(){
            Vector pos = spawnVLeft(150);
            Vector polar = new Vector(100, 2);
            construct3_autoCrossing(pos, polar, "auto_5");
        }
        private static void spawn3_3_4(){
            Vector pos = spawnVRight(98);
            Vector polar = new Vector(-80, 2);
            construct3_autoCrossing(pos, polar, "auto_6");
        }
        private static void spawn3_4_1(){
            spawn3_4_side(false);
        }
        private static void spawn3_4_2(){
            spawn3_4_side(true);
        }
        private static void spawn3_4_side(boolean left){
            int time = 12 * 60;
            int lifetime = time + (int)(60 * 2.5);
            double x = 78;
            double y = 95;
            double speed = 2;
            int delay1 = 50;
            int freq = 46;
            double hp = 130;
            String attack;
            if(left){
                attack = "crystal_1";
            }
            else{
                x = GameDriver.getGameWidth() - x;
                attack = "crystal_2";
            }

            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);

            SpawnUnit su = new SpawnUnit(time, false, new SIMultipleTick(freq, attack));

            ArrayList<Behavior> al1 = new ArrayList<>();
            al1.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", mid, speed)));
            al1.add(new BehaviorVector("wait_near_position", mid));
            al1.add(new BehaviorDouble("set_hp", hp));
            al1.add(new BehaviorVector("timer", new Vector(delay1)));
            al1.add(new BehaviorSU("add_spawn_unit", su));
            al1.add(new BehaviorVector("timer", new Vector(lifetime)));
            al1.add(new BehaviorString("set_death_spawn", "crystal_death_1"));
            al1.add(new Behavior("deactivate"));
            Routine r1 = new Routine("routine", al1, false);
            Component_Behavior bComp = new Component_Behavior(r1);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("crystal", 100000), Comps.mobSprite("crystal_yellow"),
                    Comps.dSpawn("pickup_bomb"), bComp});
        }
        private static void spawn3_4_3(){
            double x = GameDriver.getGameWidth()/2;
            double y = 95;
            double speed = 2;
            int waitTime = 15 * 60;
            int time = 12 * 60;
            int lifetime = time + (int)(60 * 2.5);
            int freq = 0;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 40;
                    break;
                case 2:
                case 3:
                    freq = 30;
                    break;
                case 4:
                    freq = 20;
                    break;
            }
            int hp = 300;
            String attack = "crystal_3";

            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);

            SpawnUnit su = new SpawnUnit(time, false, new SIMultipleTick(freq, attack));

            ArrayList<Behavior> al1 = new ArrayList<>();
            al1.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", mid, speed)));
            al1.add(new BehaviorVector("wait_near_position", mid));
            al1.add(new BehaviorVector("timer", new Vector(waitTime)));
            al1.add(new BehaviorDouble("set_hp", hp));
            al1.add(new BehaviorSU("add_spawn_unit", su));
            al1.add(new BehaviorVector("timer", new Vector(lifetime)));
            al1.add(new BehaviorString("set_death_spawn", "crystal_death_1"));
            al1.add(new Behavior("deactivate"));
            Routine r1 = new Routine("routine", al1, false);
            Component_Behavior bComp = new Component_Behavior(r1);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("crystal", 100000), Comps.mobSprite("crystal_red"),
                    Comps.dSpawn("crystal_death_2"), bComp});
        }
        private static void spawn3_5_1(){
            Vector posL = spawnVLeft(123);
            Vector posR = spawnVRight(72);
            Vector polarL = new Vector(90, 2);
            Vector polarR = new Vector(-90, 2);
            construct3_autoCrossing(posL, polarL, "auto_4");
            construct3_autoCrossing(posR, polarR, "auto_6");
        }
        
        private static void spawn4_1_1(){
            double x = 120;
            double speed = 11 * .8;
            spawn4_1_helper(x, true, speed, false);
        }
        private static void spawn4_1_2(){
            double x = 120;
            double speed = 11 * .8;
            spawn4_1_helper(x, false, speed, false);
        }
        private static void spawn4_1_3(){
            double x = 120;
            double speed = 11 * .8;
            spawn4_1_helper(x, false, speed, true);
        }
        private static void spawn4_1_helper(double x, boolean left, double speed, boolean lifeSpawn){
            if(!left){
                x = GameDriver.getGameWidth() - x;
            }
            Vector pos = spawnVTop(x);
            Vector polar = new Vector(0, speed);
            construct4_wispStraights(pos, polar, lifeSpawn, true);
        }
        private static void spawn4_2(){
            construct4_cloudTop();
        }
        private static void spawn4_4(){
            construct4_cloudAimed();
        }
        private static void spawn4_6(){
            if(GameDriver.getTick() % 2 == 0){
                construct4_waterCrossing(true);
            }
            else{
                construct4_waterCrossing(false);
            }
        }
        private static void spawn4_7(){
            double x = GameDriver.getGameWidth()/2;
            double y = 140;
            double speed = 2;
            int time = 8 * 60;
            int hp = 150;
            String attack = "water_2";
            int freq = 70;

            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);
            Vector fPol = new Vector(180, speed);

            SpawnUnit su = new SpawnUnit(time, false, new SIMultipleTick(freq, attack));

            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", mid, speed)));
            al.add(new BehaviorVector("wait_near_position", mid));
            al.add(new BehaviorDouble("set_hp", hp));
            al.add(new BehaviorVector("timer", new Vector(30)));
            al.add(new BehaviorSU("add_spawn_unit", su));
            al.add(new BehaviorVector("timer", new Vector(time)));
            al.add(new BehaviorDouble("set_hp", 10));
            al.add(new BehaviorVector("timer", new Vector(60)));
            al.add(new BehaviorVector("set_polar", fPol));
            Component_Behavior bComp = new Component_Behavior(new Routine("routine", al, false));

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("water_big", 1000000), Comps.mobSprite("water_big"),
                    Comps.dSpawn("pickup_bomb"), bComp});
        }
        private static void spawn4_8_1(){
            double y = 180;
            double speed = 11 * .8;
            spawn4_8_helper(y, true, speed, false);
        }
        private static void spawn4_8_2(){
            double y = 100;
            double speed = 11 * .8;
            spawn4_8_helper(y, false, speed, false);
        }
        private static void spawn4_8_3(){
            double y = 100;
            double speed = 11 * .8;
            spawn4_8_helper(y, false, speed, true);
        }
        private static void spawn4_8_helper(double y, boolean left, double speed, boolean lifeSpawn){
            Vector pos;
            double ang;
            if(left){
                pos = spawnVLeft(y);
                ang = 90;
            }else{
                pos = spawnVRight(y);
                ang = -90;
            }
            Vector polar = new Vector(ang, speed);
            construct4_wispStraights(pos, polar, lifeSpawn, false);
        }

        private static void spawn5_1_1(){
            construct5_birdCrossing(80, false, 78.5, 12, 7, "bird_5");
        }
        private static void spawn5_1_2(){
            construct5_birdCrossing(80, true, 78.5, 12, 7, "bird_5");
        }
        private static void spawn5_2_1(){
            construct5_birdCrossing(80, false, 78.5, 12, 7, "bird_6");
        }
        private static void spawn5_2_2(){
            construct5_birdCrossing(80, true, 78.5, 12, 7, "bird_6");
        }
        private static void spawn5_3(){
            construct5_birdCurveLongSide(40, true, 45, 12, 1.4, 8, "bird_7_1");
            construct5_birdCurveLongSide(40, false, 45, 12, 1.4, 8, "bird_7_2");
        }
        private static void spawn5_4(){
            construct5_birdCurveLongSide(GameDriver.getGameHeight() - 40, true, 135, 12, -1.4, 8, "bird_7_2");
            construct5_birdCurveLongSide(GameDriver.getGameHeight() - 40, false, 135, 12, -1.4, 8, "bird_7_1");
        }
        private static void spawn5_5(){
            construct5_birdCurveShortSide(25, true, 45, 12, 1.58, 8, "bird_7_1");
            construct5_birdCurveShortSide(GameDriver.getGameWidth() - 25, false, 180 + 45, 12, 1.58, 8, "bird_7_2");
        }
        private static void spawn5_6(){
            construct5_birdCurveShortSide(GameDriver.getGameWidth() - 25, true, -45, 12, -1.58, 8, "bird_7_2");
            construct5_birdCurveShortSide(25, false, -(180 + 45), 12, -1.58, 8, "bird_7_1");
        }
        private static void spawn5_7(int n){
            int x1 = 111;
            int x2 = 222;
            int x;
            boolean big = false;
            switch(n){
                case 1:
                    x = x1;
                    break;
                case 2:
                    x = GameDriver.getGameWidth() - x1;
                    break;
                case 3:
                    x = x2;
                    break;
                case 4:
                    x = GameDriver.getGameWidth() - x2;
                    break;
                case 5:
                default:
                    x = GameDriver.getGameWidth()/2;
                    //you get the joke
                    big = true;
                    break;
            }
            construct5_emberSpikeSlow(x, big);
        }
        private static void spawn5_8(int n){
            double x = n * (GameDriver.getGameWidth()/8);
            boolean big = n==7;
            construct5_emberSpikeFast(x, big);
        }
        private static void spawn5_9(int n){
            int x1 = 111;
            int x2 = 222;
            int x;
            boolean big = false;
            switch(n){
                case 1:
                    x = x1;
                    break;
                case 2:
                    x = GameDriver.getGameWidth() - x1;
                    break;
                case 3:
                    x = x2;
                    break;
                case 4:
                    x = GameDriver.getGameWidth() - x2;
                    break;
                case 5:
                default:
                    x = GameDriver.getGameWidth()/2;
                    //you get the joke
                    big = true;
                    break;
            }
            x = GameDriver.getGameWidth() - x;
            construct5_emberShotgunSlow(x, big);
        }
        private static void spawn5_10(int n){
            double x = n * (GameDriver.getGameWidth()/8);
            x = GameDriver.getGameWidth() - x;
            boolean big = n==7;
            construct5_emberShotgunFast(x, big);
        }
        private static void spawn5_11(){
            double x = GameDriver.getGameWidth()/2;
            double y = GameDriver.getGameHeight()/2;
            double iSpeed = 4;
            //after arriving
            int lifeTime = (int)(60 * 10.25);
            int freq = 15;

            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);

            int delay1 = 10;

            SpawnUnit su = new SpawnUnit(lifeTime - 50, false, new SIMultipleTick(1, lifeTime, freq, "ember_3"));

            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", mid, iSpeed)));
            al.add(new BehaviorVector("wait_near_position", mid));
            al.add(new BehaviorVector("timer", new Vector(delay1)));
            al.add(new BehaviorSU("add_spawn_unit", su));
            al.add(new BehaviorVector("timer", new Vector(lifeTime)));
            al.add(new Behavior("deactivate"));
            Component_Behavior bComp = new Component_Behavior(new Routine("routine", al, false));

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("ember", 100000), Comps.mobSprite("ember"),
                    Comps.dSpawn("pickup_life"), bComp});
        }

        private static void spawn6_1(){
            //wisps
            Random r = new Random(GameDriver.getTick() + 198074982);
            r.nextDouble();
            double ylow = 40, yhigh = 178;
            double y = MiscUtil.rangeRandom(ylow, yhigh, r.nextDouble());
            Vector pos;
            double ang = 90;
            if(GameDriver.getTick() % 2 == 0){
                pos = spawnVLeft(y);
            }else{
                pos = spawnVRight(y);
                ang = -ang;
            }
            double speed = 8;
            Vector pol = new Vector(ang, speed);

            //frequency is not random
            int freqlow = 23, freqhigh = 46;
            int freq = (int)MiscUtil.rangeRandom(freqlow, freqhigh, r.nextDouble());
            //but the tick is
            int tick = (int)MiscUtil.rangeRandom(0, freq, r.nextDouble());
            //precaution
            if(tick >= freq){
                tick = freq-1;
            }else if(tick < 0){
                tick = 0;
            }

            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, "wisp_4"));

            construct6_wispCross(pos, pol, su);
        }
        private static void spawn6_2(){
            int freq = 10;
            double x = 600, y = 50;
            double ang = 0, speed = 8;
            Vector pos = new Vector(x, y);
            Vector pol = new Vector(ang, speed);
            constructTrapSpawnerStraight(pos, pol, freq, "trap_spawn_1");
        }
        private static void spawn6_3(){
            int freq = 10;
            double x = 66, y = GameDriver.getGameHeight() - 50;
            double ang = 180, speed = 8;
            Vector pos = new Vector(x, y);
            Vector pol = new Vector(ang, speed);
            constructTrapSpawnerStraight(pos, pol, freq, "trap_spawn_1");
        }
        private static void spawn6_4(){
            int freq = 10;
            double x = 600, y = 50;
            double ang = 0, speed = 8;
            Vector pos = new Vector(x, y);
            Vector pol = new Vector(ang, speed);
            constructTrapSpawnerStraight(pos, pol, freq, "trap_spawn_2");
        }
        private static void spawn6_5(){
            int freq = 10;
            double x = 66, y = GameDriver.getGameHeight() - 50;
            double ang = 180, speed = 8;
            Vector pos = new Vector(x, y);
            Vector pol = new Vector(ang, speed);
            constructTrapSpawnerStraight(pos, pol, freq, "trap_spawn_2");
        }
        private static void spawn6_6(){
            int freq = 15;
            double c = 50;
            double x1 = c, y1 = c;
            double x2 = GameDriver.getGameWidth() - c, y2 = GameDriver.getGameHeight() - c;
            double ang1 = 90, ang2 = 270, speed = 8;
            Vector pos1 = new Vector(x1, y1);
            Vector pos2 = new Vector(x2, y2);
            Vector pol1 = new Vector(ang1, speed);
            Vector pol2 = new Vector(ang2, speed);
            constructTrapSpawnerStraight(pos1, pol1, freq, "trap_spawn_3");
            constructTrapSpawnerStraight(pos2, pol2, freq, "trap_spawn_3");
        }
        private static void spawn6_7(){
            int freq = 15;
            double c = 50;
            double x1 = c, y1 = c;
            double x2 = GameDriver.getGameWidth() - c, y2 = GameDriver.getGameHeight() - c;
            double ang1 = 90, ang2 = 270, speed = 8;
            Vector pos1 = new Vector(x1, y1);
            Vector pos2 = new Vector(x2, y2);
            Vector pol1 = new Vector(ang1, speed);
            Vector pol2 = new Vector(ang2, speed);
            double ac = -.5;
            constructTrapSpawnerCurved(pos1, pol1, ac, freq, "trap_spawn_3");
            constructTrapSpawnerCurved(pos2, pol2, ac, freq, "trap_spawn_3");
        }
        private static void spawn6_8(){
            int freq = 15;
            double c = 50;
            double x1 = c, y1 = c;
            double x2 = GameDriver.getGameWidth() - c, y2 = GameDriver.getGameHeight() - c;
            double ang1 = 90, ang2 = 270, speed = 8;
            Vector pos1 = new Vector(x1, y1);
            Vector pos2 = new Vector(x2, y2);
            Vector pol1 = new Vector(ang1, speed);
            Vector pol2 = new Vector(ang2, speed);
            double ac = -1;
            constructTrapSpawnerCurved(pos1, pol1, ac, freq, "trap_spawn_3");
            constructTrapSpawnerCurved(pos2, pol2, ac, freq, "trap_spawn_3");
        }
        private static void spawn6_9(){
            double x = GameDriver.getGameWidth()/2;
            double y = 130;
            constructTrap(new Vector(x, y), "trap_4_sub_2");
        }

        private static void spawn7_1(){
            double out = 100;
            construct7_wispDown(out);
            construct7_wispDown(GameDriver.getGameWidth() - out);
        }
        private static void spawn7_2(boolean side){
            double buffer = 80;
            double mid = GameDriver.getGameWidth()/2;
            construct7_cloudDiagonal(mid, side, "scloud_1", false);
            construct7_cloudDiagonal(mid + buffer, side, "scloud_1", false);
            construct7_cloudDiagonal(mid + buffer + buffer, side, "scloud_1", false);
            construct7_cloudDiagonal(mid - buffer, side, "scloud_1", false);
            construct7_cloudDiagonal(mid - buffer - buffer, side, "scloud_1", false);
        }
        private static void spawn7_3(boolean side){
            double buffer = 80;
            double mid = GameDriver.getGameWidth()/2;
            construct7_cloudDiagonal(mid, side, "scloud_2", false);
            construct7_cloudDiagonal(mid + buffer, side, "scloud_2", false);
            construct7_cloudDiagonal(mid + buffer + buffer, side, "scloud_2", false);
            construct7_cloudDiagonal(mid - buffer, side, "scloud_2", false);
            construct7_cloudDiagonal(mid - buffer - buffer, side, "scloud_2", false);
        }
        private static void spawn7_4(){
            double out = 68;
            construct7_wispCurve(out, true, "swisp_2");
            construct7_wispCurve(GameDriver.getGameWidth() - out, false, "swisp_2");
        }
        private static void spawn7_5(){
            double out = 68;
            construct7_wispCurve(out, true, "swisp_3");
            construct7_wispCurve(GameDriver.getGameWidth() - out, false, "swisp_3");
        }
        private static void spawn7_6(){
            double out = 68;
            construct7_wispCurve(out, true, "swisp_4");
            construct7_wispCurve(GameDriver.getGameWidth() - out, false, "swisp_4");
        }
        private static void spawn7_7(){
            double out = 68;
            construct7_wispCurve(out, true, "swisp_5");
            construct7_wispCurve(GameDriver.getGameWidth() - out, false, "swisp_5");
        }
        private static void spawn7_8(){
            double x = GameDriver.getGameWidth()/2;
            double y = 170;
            double iSpeed = 1.5;
            //after arriving
            int lifeTime = 60 * 15;
            int freq = 9;

            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);

            int finalHP = 33;
            double finalSpeed = 1.5;

            int delay1 = 10;

            SpawnUnit su = new SpawnUnit(lifeTime - 50, false, new SIMultipleTick(1, lifeTime, freq, "shumanoid_1"));

            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", mid, iSpeed)));
            al.add(new BehaviorVector("wait_near_position", mid));
            al.add(new BehaviorVector("timer", new Vector(delay1)));
            al.add(new BehaviorSU("add_spawn_unit", su));
            al.add(new BehaviorVector("timer", new Vector(lifeTime)));
            al.add(new BehaviorDouble("set_hp", finalHP));
            al.add(new BehaviorVector("set_polar", new Vector(0, finalSpeed)));
            Component_Behavior bComp = new Component_Behavior(new Routine("routine", al, false));

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("shadow_humanoid", 100000), Comps.mobSprite("shadow_humanoid"),
                    Comps.dSpawn("pickup_life"), bComp});
        }
        private static void spawn7_9(int i){
            double startY = 80;
            double yVar = 75;
            double y = startY + ((i - 1) * yVar);
            Vector pos;
            double ang;
            if(i % 2 == 1){
                //right side
                pos = spawnVRight(y);
                ang = 270;
            }else{
                //left side
                pos = spawnVLeft(y);
                ang = 90;
            }
            double speed = 12;
            Vector pol = new Vector(ang, speed);
            construct7_humanoidCross(pos, pol, "shumanoid_2");
        }
        private static void spawn7_10(){
            double xOut = 30;
            double mid = GameDriver.getGameWidth()/2;
            construct7_humanoidDown(mid - xOut, true);
            construct7_humanoidDown(mid + xOut, false);
        }
        private static void spawn7_11(boolean side){
            double buffer = 80;
            double mid = GameDriver.getGameWidth()/2;
            construct7_cloudDiagonal(mid, side, "scloud_3", true);
            construct7_cloudDiagonal(mid + buffer, side, "scloud_3", true);
            construct7_cloudDiagonal(mid + buffer + buffer, side, "scloud_3", true);
            construct7_cloudDiagonal(mid - buffer, side, "scloud_3", true);
            construct7_cloudDiagonal(mid - buffer - buffer, side, "scloud_3", true);
        }
        private static void spawn7_12(boolean side){
            double buffer = 80;
            double mid = GameDriver.getGameWidth()/2;
            construct7_cloudDiagonal(mid, side, "scloud_4", true);
            construct7_cloudDiagonal(mid + buffer, side, "scloud_4", true);
            construct7_cloudDiagonal(mid + buffer + buffer, side, "scloud_4", true);
            construct7_cloudDiagonal(mid - buffer, side, "scloud_4", true);
            construct7_cloudDiagonal(mid - buffer - buffer, side, "scloud_4", true);
        }
        private static void spawn7_13(int i){
            double startY = 80 + 75;
            double yVar = 75;
            double y = startY + ((i - 1) * yVar);
            Vector pos;
            double ang;
            if(i % 2 == 0){
                //right side
                pos = spawnVRight(y);
                ang = 270;
            }else{
                //left side
                pos = spawnVLeft(y);
                ang = 90;
            }
            double speed = 12;
            Vector pol = new Vector(ang, speed);
            construct7_humanoidCross(pos, pol, "shumanoid_4");
        }
        private static void spawn7_14(int i){
            boolean left = i == 1;
            boolean healthy = i == 3;
            String dID;
            if(i != 3){
                dID = "pickup_power_small";
            }else{
                dID = "pickup_life";
            }
            double out = 170;
            if(left){
                construct7_wispDownEnd(out, dID, healthy);
            }else{
                construct7_wispDownEnd(GameDriver.getGameWidth() - out, dID, healthy);
            }
        }

        private static void spawnBoss_1(){
            String id = EntityManager.addEntity();
            ArrayList<Behavior> masterList = new ArrayList<>();
            masterList.add(BossHelper.tripleSprite());

            //attack 1
            ArrayList<SpawningInstruction> si1 = new ArrayList<>();
            si1.add(new SISingleSpawn("b1_1_1"));
            si1.add(new SISingleTick(72, "b1_1_2"));
            si1.add(new SISingleTick(145, "b1_1_3"));
            SpawnUnit su1 = new SpawnUnit(73 * 4, true, si1);
            //attack 2
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 5;
                    break;
                case 2:
                    freq = 4;
                    break;
                case 3:
                    freq = 3;
                    break;
                case 4:
                    freq = 2;
                    break;
                default:
                    freq = 5;
                    break;
            }
            SpawnUnit su2 = new SpawnUnit(60 * 5, true, new SIMultipleTick(0, 60 * 4, freq, "b1_2"));
            //attack 3
            ArrayList<Behavior> b3 = BossHelper.attackPhaseInit(160, id);
            double side = 100;
            double y = 90;
            Vector left = new Vector(side, y);
            Vector right = new Vector(GameDriver.getGameWidth() - side, y);
            int time = 100;
            int coolTime = 230;
            double dist = Vector.getDistance(left, right);
            double speed = dist/(time * .80);
            SpawnUnit su3 = new SpawnUnit(time, false, new SIMultipleTick(20, "b1_3"));
            //assume we start at the left
            ArrayList<Behavior> b3Loop = new ArrayList<>();
            b3Loop.add(new BehaviorSU("add_su", su3));
            b3Loop.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", right, speed)));
            b3Loop.add(new BehaviorVector("timer", new Vector(coolTime)));
            b3Loop.add(new BehaviorSU("add_su", su3));
            b3Loop.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", left, speed)));
            b3Loop.add(new BehaviorVector("timer", new Vector(coolTime)));
            b3.add(new Routine("routine", b3Loop, true));

            ArrayList<Behavior> mainList = new ArrayList<>();
            //init
            mainList.add(BossHelper.bossInit());
            //phase 1
            mainList.add(BossHelper.moveBetweenAttacks(su1, 180, 112, 180, id));
            mainList.add(BossHelper.endPhase(8, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 2
            mainList.add(BossHelper.attackInPlace(su2, 230, id));
            mainList.add(BossHelper.endPhase(10, false));
            mainList.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", left, 3)));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 3
            mainList.add(new Routine("boss_routine", b3, false));
            mainList.add(BossHelper.endPhase(0, false));
            mainList.add(new BehaviorVector("timer", new Vector(30)));
            mainList.add(BossHelper.endBoss(true));

            masterList.add(new Routine("routine", mainList, false));
            constructEntity(new Component[]{Comps.pos(BossHelper.spawn()), Comps.tb(), Comps.ccBoss(), Comps.bossSprite(1),
                    new Component_Behavior(masterList)}, id);
        }
        private static void spawnBoss_2(){
            String id = EntityManager.addEntity();
            ArrayList<Behavior> masterList = new ArrayList<>();
            //standing anim only

            //attack 1
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 14;
                    break;
                case 2:
                    freq = 12;
                    break;
                case 3:
                    freq = 10;
                    break;
                case 4:
                    freq = 8;
                    break;
                default:
                    freq = 20;
                    break;
            }
            SpawnUnit su1 = new SpawnUnit(629, true, new SIMultipleTick(0, 540, freq, "b2_1"));
            //attack 2
            ArrayList<SpawningInstruction> al2 = new ArrayList<>();
            al2.add(new SISingleSpawn("b2_2_1"));
            al2.add(new SISingleTick(6 * 60, "b2_2_2"));
            SpawnUnit su2 = new SpawnUnit(12 * 60, true, al2);
            //attack 3
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 13;
                    break;
                case 2:
                    freq = 9;
                    break;
                case 3:
                    freq = 7;
                    break;
                case 4:
                    freq = 5;
                    break;
                default:
                    freq = 20;
                    break;
            }
            SpawnUnit su3 = new SpawnUnit(629, true, new SIMultipleTick(0, 590, freq, "b2_3"));
            //attack 4
            SpawnUnit su4 = new SpawnUnit(480, true, new SISingleSpawn("b2_4"));

            ArrayList<Behavior> mainList = new ArrayList<>();
            //init
            mainList.add(BossHelper.bossInit());
            //phase 1
            mainList.add(BossHelper.attackInPlace(su1, 395, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 2
            mainList.add(BossHelper.moveBetweenAttacksLimited(su2, 60 * 2, 60 * 4, 460, id));
            mainList.add(BossHelper.endPhase(12, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 3
            mainList.add(BossHelper.attackInPlace(su3, 715, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(160)));
            //phase 4
            mainList.add(BossHelper.moveBetweenAttacksLimited(su4, 280, 480-280, 440, id));
            mainList.add(BossHelper.endPhase(0, false));
            mainList.add(new BehaviorVector("timer", new Vector(30)));

            mainList.add(BossHelper.endBoss(true));

            masterList.add(new Routine("routine", mainList, false));
            constructEntity(new Component[]{Comps.pos(BossHelper.spawn()), Comps.tb(), Comps.ccBoss(), Comps.bossSprite(2),
                    new Component_Behavior(masterList)}, id);
        }
        private static void spawnBoss_3(){
            String id = EntityManager.addEntity();
            ArrayList<Behavior> masterList = new ArrayList<>();
            masterList.add(BossHelper.tripleSprite());

            //attack 1
            int maxTick1 = 441;
            int[] freqs;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freqs = new int[]{10, 11, 9, 19, 15, 10};
                    break;
                case 2:
                    freqs = new int[]{9, 10, 8, 18, 14, 9};
                    break;
                case 3:
                    freqs = new int[]{8, 10, 7, 17, 13, 7};
                    break;
                case 4:
                default:
                    freqs = new int[]{8, 10, 6, 17, 11, 7};
                    break;
            }
            ArrayList<SpawningInstruction> si1 = new ArrayList<>();
            si1.add(new SIMultipleTick(0, 140, freqs[0], "b3_1_1"));
            si1.add(new SIMultipleTick(140, 210, freqs[1], "b3_1_2"));
            si1.add(new SIMultipleTick(190, 280, freqs[2], "b3_1_3"));
            si1.add(new SIMultipleTick(220, 300, freqs[3], "b3_1_4"));
            si1.add(new SIMultipleTick(270, 320, freqs[4], "b3_1_5"));
            si1.add(new SIMultipleTick(290, maxTick1, freqs[5], "b3_1_6"));
            SpawnUnit su1 = new SpawnUnit(maxTick1, true, si1);
            //attack 2
            SpawnUnit su2 = new SpawnUnit(22, true, new SISingleSpawn("b3_2"));
            //attack 3
            int delay3 = 85;
            int freq3End;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq3End = 3;
                    break;
                case 2:
                case 3:
                    freq3End = 2;
                    break;
                case 4:
                default:
                    freq3End = 1;
                    break;
            }
            int maxTick3 = delay3 * 10;

            ArrayList<SpawningInstruction> si3 = new ArrayList<>();
            si3.add(new SISingleSpawn("b3_3_1"));
            si3.add(new SISingleTick(delay3-1, "b3_3_2"));
            si3.add(new SISingleTick((delay3 * 2)-1, "b3_3_3"));
            si3.add(new SISingleTick((delay3 * 3)-1, "b3_3_4"));
            si3.add(new SISingleTick((delay3 * 4)-1, "b3_3_5"));
            si3.add(new SISingleTick((delay3 * 5)-1, "b3_3_6"));
            si3.add(new SIMultipleTick(delay3 * 6, maxTick3 - 10, freq3End, "b3_3_7"));
            SpawnUnit su3 = new SpawnUnit(maxTick3, true, si3);
            //attack 4
            int freq4_1;
            int freq4_2 = 35;
            int freq4_3 = 140;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq4_1 = 14;
                    break;
                case 2:
                    freq4_1 = 12;
                    break;
                case 3:
                    freq4_1 = 11;
                    break;
                case 4:
                default:
                    freq4_1 = 10;
                    break;
            }
            SpawnUnit su4_1 = new SpawnUnit(freq4_1, true, new SISingleSpawn("b3_4_1"));
            SpawnUnit su4_2 = new SpawnUnit(freq4_2, true, new SISingleSpawn("b3_4_2"));
            SpawnUnit su4_3 = new SpawnUnit(freq4_3, true, new SISingleTick(freq4_3/2, "b3_4_3"));
            //attack 5
            int delay5 = 45;
            int move5 = delay5 * 3;
            ArrayList<SpawningInstruction> si5 = new ArrayList<>();
            si5.add(new SISingleSpawn("b3_5_1"));
            si5.add(new SISingleTick(delay5-1, "b3_5_2"));
            si5.add(new SISingleTick((delay5 * 2) -1, "b3_5_3"));
            si5.add(new SISingleTick((delay5 * 3)-1, "b3_5_4"));
            si5.add(new SISingleTick((delay5 * 4)-1, "b3_5_5"));
            si5.add(new SISingleTick((delay5 * 5)-1, "b3_5_6"));
            SpawnUnit su5 = new SpawnUnit(delay5 * 6, true, si5);

            //constructing
            ArrayList<Behavior> mainList = new ArrayList<>();
            //init
            mainList.add(BossHelper.bossInit());
            //phase 1
            mainList.add(BossHelper.attackInPlace(su1, 445, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 2
            mainList.add(BossHelper.attackInPlace(su2, 700, id));
            mainList.add(BossHelper.endPhase(12, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 3
            mainList.add(BossHelper.attackInPlace(su3, 550, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 4
            mainList.add(BossHelper.moveBetweenAttacks(new SpawnUnit[]{su4_1, su4_2, su4_3}, 10, freq4_3 - 10, 285, id));
            mainList.add(BossHelper.endPhase(12, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 5
            mainList.add(BossHelper.moveBetweenAttacksLimited(su5, 10, move5-10, 415, id));
            mainList.add(BossHelper.endPhase(0, false));
            //end
            mainList.add(new BehaviorVector("timer", new Vector(30)));
            mainList.add(BossHelper.endBoss(true));

            masterList.add(new Routine("routine", mainList, false));
            constructEntity(new Component[]{Comps.pos(BossHelper.spawn()), Comps.tb(), Comps.ccBoss(), Comps.bossSprite(3),
                    new Component_Behavior(masterList)}, id);
        }
        private static void spawnBoss_4(){
            String id = EntityManager.addEntity();
            ArrayList<Behavior> masterList = new ArrayList<>();
            masterList.add(BossHelper.tripleSprite());

            //attack 1
            int maxTick1 = 12 * 60;
            int freq1;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq1 = 10;
                    break;
                case 2:
                    freq1 = 8;
                    break;
                case 3:
                    freq1 = 7;
                    break;
                case 4:
                default:
                    freq1 = 6;
                    break;
            }
            SpawnUnit su1 = new SpawnUnit(maxTick1, true, new SIMultipleTick(freq1, "b4_1_1"));
            if(GameDriver.getCurrentDifficulty() >= 3){
                su1.getSpawningInstructions().add(new SIMultipleTick(1, maxTick1, freq1 * 20, "b4_1_2"));
            }
            //attack 2
            int freq2;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq2 = 110;
                    break;
                case 2:
                case 3:
                    freq2 = 100;
                    break;
                case 4:
                default:
                    freq2 = 90;
                    break;
            }
            int maxTick2 = freq2 * 2;
            ArrayList<SpawningInstruction> si2 = new ArrayList<>();
            si2.add(new SISingleSpawn("b4_2_1"));
            si2.add(new SISingleTick(freq2 - 1, "b4_2_2"));
            SpawnUnit su2 = new SpawnUnit(maxTick2, true, si2);
            //attack 3
            SpawnUnit su3 = new SpawnUnit(maxTick1, true, new SIMultipleTick(freq1, "b4_3_1"));
            if(GameDriver.getCurrentDifficulty() >= 3){
                su3.getSpawningInstructions().add(new SIMultipleTick(1, maxTick1, freq1 * 20, "b4_3_2"));
            }
            //attack 4
            int freq4 = 9 * 60;
            SpawnUnit su4 = new SpawnUnit(freq4, true, new SISingleSpawn("b4_4"));
            su4.getSpawningInstructions().add(new SISingleTick(50, "b4_4"));
            ArrayList<Behavior> bList4 = new ArrayList<>();
            int delay4 = 120;
            bList4.add(new BehaviorVector("timer", new Vector(freq4 - delay4)));
            bList4.add(new BehaviorInt("write_game_message", 0));
            bList4.add(new BehaviorVector("timer", new Vector(delay4)));
            Routine r4 = new Routine("routine", bList4, true);
            //attack 5
            SpawnUnit su5 = new SpawnUnit(maxTick1, true, new SIMultipleTick(freq1 + 2, "b4_5_1"));
            if(GameDriver.getCurrentDifficulty() >= 3){
                su5.getSpawningInstructions().add(new SIMultipleTick(1, maxTick1, freq1 * 20, "b4_5_2"));
            }
            //attack 6
            int freq6;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq6 = 125;
                    break;
                case 2:
                    freq6 = 120;
                    break;
                case 3:
                    freq6 = 115;
                    break;
                case 4:
                default:
                    freq6 = 110;
                    break;
            }
            ArrayList<SpawningInstruction> si6 = new ArrayList<>();
            //rain
            si6.add(new SIMultipleTick(1, "b4_6_1"));
            //flowers
            si6.add(new SISingleTick(freq6/2, "b4_6_2"));
            SpawnUnit su6 = new SpawnUnit(freq6, true, si6);

            //constructing
            ArrayList<Behavior> mainList = new ArrayList<>();
            //init
            mainList.add(BossHelper.bossInit());
            //phase 1
            mainList.add(BossHelper.moveBetweenAttacksLimited(su1, (int)((((double)maxTick1/2)) * (3d/5)), (int)((((double)maxTick1)/4) * 2d/5), 340, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 2
            mainList.add(BossHelper.attackInPlace(su2,710, id));
            mainList.add(BossHelper.endPhase(14, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 3
            mainList.add(BossHelper.moveBetweenAttacksLimited(su3, (int)((((double)maxTick1/2)) * (3d/5)), (int)((((double)maxTick1)/4) * 2d/5), 300, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 4
            Routine temp = BossHelper.attackInPlace(su4,750, id);
            temp.getBehaviors().add(r4);
            mainList.add(temp);
            mainList.add(BossHelper.endPhase(14, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 5
            mainList.add(BossHelper.moveBetweenAttacksLimited(su5, (int)((((double)maxTick1/2)) * (3d/5)), (int)((((double)maxTick1)/4) * 2d/5), 320, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 6
            mainList.add(BossHelper.moveBetweenAttacksLimited(su6, (freq6 * 3)+(freq6/2), (freq6 * 2) - (freq6/2), 450, id));
            mainList.add(BossHelper.endPhase(0, false));

            //end
            mainList.add(new BehaviorVector("timer", new Vector(30)));
            mainList.add(BossHelper.endBoss(true));

            masterList.add(new Routine("routine", mainList, false));
            constructEntity(new Component[]{Comps.pos(BossHelper.spawn()), Comps.tb(), Comps.ccBoss(), Comps.bossSprite(4),
                    new Component_Behavior(masterList)}, id);
        }
        private static void spawnBoss_5(){
            String id = EntityManager.addEntity();
            ArrayList<Behavior> masterList = new ArrayList<>();
            //standing anim only
            //sword behavior thingy
            int delayBetween = 5;
            ArrayList<Behavior> sBList = new ArrayList<>();
            sBList.add(new BehaviorString("wait_collision", "PlayerPBoss"));
            sBList.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("sword_swipe"))));
            sBList.add(new BehaviorVector("timer", new Vector(delayBetween)));
            Routine r = new Routine("routine", sBList, true);
            masterList.add(r);
            //sword behavior thingy part 2
            sBList = new ArrayList<>();
            sBList.add(new BehaviorString("wait_collision", "PlayerBBoss"));
            sBList.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("sword_swipe"))));
            sBList.add(new BehaviorVector("timer", new Vector(delayBetween)));
            r = new Routine("routine", sBList, true);
            masterList.add(r);
            //sword behavior thingy part 3
            sBList = new ArrayList<>();
            sBList.add(new BehaviorString("wait_collision", "PlayerEnemy"));
            sBList.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("sword_swipe"))));
            sBList.add(new BehaviorVector("timer", new Vector(delayBetween)));
            r = new Routine("routine", sBList, true);
            masterList.add(r);

            double maxHP = Double.MAX_VALUE;
            //in ticks
            int bossLength = (126 * 60) + 44;
            double hpLossPerTick = maxHP/bossLength;
            masterList.add(new BehaviorDouble("subtract_hp", hpLossPerTick));

            //attacks
            ArrayList<SpawningInstruction> siList = new ArrayList<>();
            //attack 1
            int maxTick1 = 27 * 62;
            int freq1;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                    freq1 = 95;
                    break;
                case 3:
                case 4:
                default:
                    freq1 = 75;
                    break;
            }
            siList.add(new SIMultipleTick(0, maxTick1, freq1, "b5_1"));
            //attack 2
            int baseTick2 = maxTick1 + (60 * 6);
            int maxTick2 = baseTick2 + (60 * 15);
            int freq2;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq2 = 65;
                    break;
                case 2:
                    freq2 = 55;
                    break;
                case 3:
                    freq2 = 45;
                    break;
                case 4:
                default:
                    freq2 = 35;
                    break;
            }
            siList.add(new SIMultipleTick(baseTick2, maxTick2, freq2, "b5_2"));
            //attack 3
            int baseTick3 = 60 * 53;
            int maxTick3 = baseTick3 + (60 * 16);
            int freq3 = freq2 - 13;
            siList.add(new SIMultipleTick(baseTick3, maxTick3, freq3, "b5_2"));
            //attack 4
            int baseTick4 = maxTick3 + 144 + 112;
            int maxTick4 = baseTick4 + (60 * 21);
            siList.add(new SIMultipleTick(baseTick4, maxTick4, freq1 + 45, "b5_4"));
            //attack 5
            int baseTick5 = maxTick4 + 271;
            int freq5 = (int)(freq2 * 1.15);
            siList.add(new SIMultipleTick(baseTick5, bossLength - 360, freq5, "b5_5"));


            SpawnUnit su = new SpawnUnit(bossLength, false, siList);


            //constructing
            ArrayList<Behavior> mainList = new ArrayList<>();
            //init
            mainList.add(BossHelper.bossInitMid());
            //start
            //need this first so that the boss routine will function properly
            mainList.add(new BehaviorDouble("set_hp", maxHP));
            mainList.add(BossHelper.attackInPlace(su, maxHP, id));
            mainList.add(BossHelper.endPhase(0, false));

            //end
            mainList.add(new BehaviorVector("timer", new Vector(30)));
            mainList.add(BossHelper.endBoss(false));

            masterList.add(new Routine("routine", mainList, false));
            constructEntity(new Component[]{Comps.pos(BossHelper.spawn()), Comps.tb(), Comps.ccBoss(), Comps.bossSprite(5),
                    new Component_Behavior(masterList)}, id);
        }
        private static void spawnBoss_6(){
            String id = EntityManager.addEntity();
            ArrayList<Behavior> masterList = new ArrayList<>();
            masterList.add(BossHelper.tripleAnim());

            //attack 1
            SpawnUnit su1_1 = new SpawnUnit(1, false, new SISingleSpawn("b6_1_1"));
            int freq1 = 1;
            SpawnUnit su1_2 = new SpawnUnit(freq1, true, new SISingleSpawn("b6_1_2"));
            double hp1 = 273;//needs to be double - apparently cannot cast integer to double
            int delay1 = 60;
            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorDouble("set_hp", hp1));
            al.add(new BehaviorSU("add_su", su1_1));
            //su to spawn hp
            al.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("hp_bar"))));
            //write boardmessage behavior
            al.add(new BehaviorBM("write_message", new BoardMessage("boss_hp", 2, "gameplay", new Object[]{hp1, id})));
            al.add(new BehaviorVector("timer", new Vector(delay1)));
            al.add(new BehaviorSU("set_su", su1_2));
            Routine r1 = new Routine("boss_routine", al, false);
            //attack 2
            int freq2 = 370;
            int delay2 = 120;
            ArrayList<SpawningInstruction> si2 = new ArrayList<>();
            si2.add(new SISingleSpawn("b6_2_1"));
            si2.add(new SISingleTick(freq2 - 1, "b6_2_2"));
            si2.add(new SISingleTick((freq2 * 2) - 1, "b6_2_3"));
            si2.add(new SISingleTick((freq2 * 3) - 1, "b6_2_4"));
            si2.add(new SISingleTick((freq2 * 4) - 1, "b6_2_2"));
            si2.add(new SISingleTick((freq2 * 5) - 1, "b6_2_1"));
            si2.add(new SISingleTick((freq2 * 6) - 1, "b6_2_4"));
            si2.add(new SISingleTick((freq2 * 7) - 1, "b6_2_3"));
            SpawnUnit su2 = new SpawnUnit((freq2 * 8) + delay2, true, si2);
            //attack 3
            int freq3 = 25;
            int maxTick3 = freq3 * 6;
            ArrayList<SpawningInstruction> si3 = new ArrayList<>();
            si3.add(new SIMultipleTick(freq3, "b6_3_1"));
            si3.add(new SISingleTick(maxTick3 - 1, "b6_3_2"));
            SpawnUnit su3 = new SpawnUnit(maxTick3, true, si3);
            //attack 4
            int freq4 = 120;
            SpawnUnit su4 = new SpawnUnit(freq4, true, new SISingleSpawn("b6_4"));
            //attack 5
            int freq5 = 120;
            SpawnUnit su5 = new SpawnUnit(freq5, true, new SISingleSpawn("b6_5"));
            //attack 6
            int freq6 = 25;
            int delay6 = 110;
            ArrayList<SpawningInstruction> si6 = new ArrayList<>();
            si6.add(new SISingleSpawn("b6_6_1"));
            si6.add(new SISingleTick(freq6 - 1, "b6_6_2"));
            si6.add(new SISingleTick((freq6 * 2) - 1, "b6_6_3"));
            si6.add(new SISingleTick(delay6, "b6_6_3"));
            si6.add(new SISingleTick(delay6 + (freq6 - 1), "b6_6_2"));
            si6.add(new SISingleTick(delay6 + ((freq6 * 2)-1), "b6_6_1"));
            int maxTick6 = (delay6*2) + ((freq6*2)-1);
            SpawnUnit su6 = new SpawnUnit(maxTick6, true, si6);
            //attack 7
            int freq7;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq7 = 25;
                    break;
                case 2:
                    freq7 = 22;
                    break;
                case 3:
                    freq7 = 19;
                    break;
                case 4:
                default:
                    freq7 = 18;
                    break;
            }
            SpawnUnit su7 = new SpawnUnit(freq7, true, new SISingleSpawn("b6_7"));
            //attack 8
            int freq8;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq8 = 20;
                    break;
                case 2:
                    freq8 = 19;
                    break;
                case 3:
                    freq8 = 18;
                    break;
                case 4:
                default:
                    freq8 = 17;
                    break;
            }
            int maxTick8 = freq8 * 4;
            ArrayList<SpawningInstruction> si8 = new ArrayList<>();
            si8.add(new SISingleSpawn("b6_8_2"));
            si8.add(new SIMultipleTick(freq8, "b6_8_1"));
            SpawnUnit su8 = new SpawnUnit(maxTick8, true, si8);



            //constructing
            ArrayList<Behavior> mainList = new ArrayList<>();
            //init
            mainList.add(BossHelper.bossInit());
            //phase 1 - meek
            mainList.add(r1);
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", Vector.add(BossHelper.mid(), new Vector(0, -100)), 1)));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 2 - wall traps
            mainList.add(BossHelper.attackInPlace(su2, 573, id));
            mainList.add(BossHelper.endPhase(16, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 3 - sharp micro purple
            mainList.add(BossHelper.moveBetweenAttacksLimited(su3, (int)(maxTick3 * 2.5), (int)(maxTick3 * 1.5), 348, id));
            mainList.add(BossHelper.endPhase(0, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 4 - homing trap green/red
            mainList.add(BossHelper.moveBetweenAttacks(su4, freq4 * 2, freq4, 490, id));
            mainList.add(BossHelper.endPhase(16, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 5 - sides red
            mainList.add(BossHelper.moveBetweenAttacksLimited(su5, freq5 * 2, freq5, 510, id));
            mainList.add(BossHelper.endPhase(0, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 6 - red/blue radials
            mainList.add(BossHelper.moveBetweenAttacksLimited(su6, freq6 * 8, freq6 * 4, 525, id));
            mainList.add(BossHelper.endPhase(16, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 7 - big
            mainList.add(BossHelper.attackInPlace(su7, 330, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 8 - red walls
            mainList.add(BossHelper.moveBetweenAttacksLimited(su8, maxTick8 * 2, maxTick8, 900, id));
            mainList.add(BossHelper.endPhase(0, false));

            //end
            mainList.add(new BehaviorVector("timer", new Vector(30)));
            mainList.add(BossHelper.endCampaign());

            masterList.add(new Routine("routine", mainList, false));
            constructEntity(new Component[]{Comps.pos(BossHelper.spawn()), Comps.tb(), Comps.ccBoss(), Comps.bossSprite(6),
                    new Component_Behavior(masterList)}, id);
        }
        private static void spawnBoss_7(){
            String id = EntityManager.addEntity();
            ArrayList<Behavior> masterList = new ArrayList<>();

            //attack 1
            int freq1 = 5;
            SpawnUnit su1 = new SpawnUnit(freq1, true, new SISingleSpawn("b7_1"));
            //attack 2
            int maxTick2 = 80;
            int freq2 = 10;
            ArrayList<SpawningInstruction> si2 = new ArrayList<>();
            si2.add(new SIMultipleTick(freq2, "b7_2_1"));
            si2.add(new SISingleTick(maxTick2 - 1, "b7_2_2"));
            SpawnUnit su2 = new SpawnUnit(maxTick2, true, si2);
            //attack 3
            int freq3 = 9;
            ArrayList<SpawningInstruction> si3 = new ArrayList<>();
            si3.add(new SISingleSpawn("b7_3_1"));
            si3.add(new SISingleTick(freq3/2, "b7_3_2"));
            SpawnUnit su3 = new SpawnUnit(freq3, true, si3);
            //attack 4
            int maxTick4 = 260;
            SpawnUnit su4 = new SpawnUnit(maxTick4, true, new SISingleSpawn("b7_4"));
            //attack 5
            int freq5 = 53;
            SpawnUnit su5 = new SpawnUnit(freq5, true, new SISingleSpawn("b7_5"));
            //attack 6
            int maxTick6 = 120;
            int cooldown6 = 40;
            int freq6 = 31; //31 doesn't fit 120
            ArrayList<SpawningInstruction> si6 = new ArrayList<>();
            si6.add(new SIMultipleTick(0, maxTick6, freq6, "b7_6_1"));
            si6.add(new SISingleTick(maxTick6, "b7_6_2"));
            SpawnUnit su6 = new SpawnUnit(maxTick6 + cooldown6, true, si6);
            //attack 7
            int freq7 = 31;//make sure is odd
            SpawnUnit su7 = new SpawnUnit(freq7, true, new SISingleSpawn("b7_7"));
            //attack 8
            int freq8_sh = 3;//mob spawns across the top
            int maxTick8 = 120;
            ArrayList<SpawningInstruction> si8 = new ArrayList<>();
            si8.add(new SIMultipleTick(freq8_sh, "b7_8_1"));
            si8.add(new SISingleTick(maxTick8 - 1, "b7_8_2"));
            SpawnUnit su8 = new SpawnUnit(maxTick8, true, si8);
            //attack 9
            int freq9 = 63;
            SpawnUnit su9 = new SpawnUnit(freq9, true, new SISingleSpawn("b7_9"));
            //attack 10
            int freq10 = 70;
            int maxTick10 = freq10 * 6;
            int cooldown10 = freq10 * 2;
            SpawnUnit su10 = new SpawnUnit(maxTick10 + cooldown10, true, new SIMultipleTick(0, maxTick10, freq10, "b7_10"));
            //attack 11
            int freq11 = 23;
            SpawnUnit su11 = new SpawnUnit(freq11, true, new SISingleSpawn("b7_11"));
            //attack 12
            int maxTick12 = 186;
            int lower12 = 130;
            int freq12 = 13;
            ArrayList<SpawningInstruction> si12 = new ArrayList<>();
            si12.add(new SISingleSpawn("b7_12_1"));
            si12.add(new SIMultipleTick(lower12, maxTick12, freq12, "b7_12_2"));
            SpawnUnit su12 = new SpawnUnit(maxTick12, true, si12);
            //attack 13
            ArrayList<SpawningInstruction> si13 = new ArrayList<>();
            int freq13 = 120;
            SpawnUnit su13 = new SpawnUnit(freq13, true, new SISingleSpawn("b7_13"));
            //attack 14
            int freq14 = 230;
            SpawnUnit su14 = new SpawnUnit(freq14, true, new SISingleSpawn("b7_14"));
            //attack 15
            int freq15 = 404;
            SpawnUnit su15 = new SpawnUnit(freq15, true, new SISingleSpawn("b7_15"));





            //constructing
            ArrayList<Behavior> mainList = new ArrayList<>();
            //init
            mainList.add(BossHelper.bossInit());

            //phase 1 // spiral
            mainList.add(BossHelper.moveBetweenAttacksLimited(su1, 180, 180, 330, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 2 // line spirals + aim
            mainList.add(BossHelper.attackInPlace(su2, 600, id));
            mainList.add(BossHelper.endPhase(16, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 3 // spiral + counter medium
            mainList.add(BossHelper.moveBetweenAttacksLimited(su3, 180, 180, 410, id));
            mainList.add(BossHelper.endPhase(0, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 4 // bouncy house
            mainList.add(BossHelper.moveBetweenAttacksLimited(su4, maxTick4 + 60, maxTick4 + (maxTick4 - 60), 590, id));
            mainList.add(BossHelper.endPhase(16, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 5 // rings
            mainList.add(BossHelper.moveBetweenAttacksLimited(su5, 180, 180, 410, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 6 // side aim
            mainList.add(BossHelper.attackInPlace(su6, 480, id));
            mainList.add(BossHelper.endPhase(16, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 7 // spiky fast rings
            mainList.add(BossHelper.moveBetweenAttacksLimited(su7, 180, 180, 290, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 8 // puppet diagonal
            mainList.add(BossHelper.attackInPlace(su8, 600, id));
            mainList.add(BossHelper.endPhase(16, true));
            mainList.add(new BehaviorInt("write_game_message", 0));//tells puppets to deactivate
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 9 // low sym high spawns - walls
            mainList.add(BossHelper.moveBetweenAttacksLimited(su9, 180, 180, 410, id));
            mainList.add(BossHelper.endPhase(0, true));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 10 // mercury waltz
            mainList.add(BossHelper.moveBetweenAttacksLimited(su10, maxTick10, cooldown10, 510, id));
            mainList.add(BossHelper.endPhase(16, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 11 // fast walls spiral
            mainList.add(BossHelper.moveBetweenAttacksLimited(su11, 90, 150, 380, id));
            mainList.add(BossHelper.endPhase(0, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 12 // side walls spawn + big //basically remilia
            mainList.add(BossHelper.moveBetweenAttacksLimited(su12, maxTick12/2, maxTick12 - (maxTick12/2), 470, id));
            mainList.add(BossHelper.endPhase(16, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 13 // bounce balls -> exploding radially
            mainList.add(BossHelper.moveBetweenAttacksLimited(su13, 60, 180, 380, id));
            mainList.add(BossHelper.endPhase(0, false));
            mainList.add(new BehaviorVector("timer", new Vector(120)));
            //phase 14 //bouncy house electric boogaloo
            mainList.add(BossHelper.moveBetweenAttacksLimited(su14, 180, 90, 630, id));
            mainList.add(BossHelper.endPhase(16, false));
            mainList.add(new BehaviorVector("timer", new Vector(180)));
            //phase 15 //possessed by flandre
            mainList.add(BossHelper.moveBetweenAttacksLimited(su15, freq15/2, freq15 - (freq15/2), 1150, id));
            mainList.add(BossHelper.endPhase(0, false));

            //end
            mainList.add(new BehaviorVector("timer", new Vector(30)));
            mainList.add(BossHelper.endBoss(false));

            masterList.add(new Routine("routine", mainList, false));
            constructEntity(new Component[]{Comps.pos(BossHelper.spawn()), Comps.tb(), Comps.ccBoss(), Comps.bossSprite(7),
                    new Component_Behavior(masterList)}, id);
        }
        private static void spawnB7_8_1(){
            double speed = 12;
            double ang = 270;
            Vector pol = new Vector(ang, speed);
            double y = 60;
            Vector pos = spawnVRight(y);
            String sID = "b7_8_1_sub";

            double hp = 10;
            int freq = 28;
            double tickMulti = .3;
            int mod = 16;
            int tick = (int)(((GameDriver.getTick() * tickMulti)+((GameDriver.getTick() / mod) * mod)) % freq);
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, sID));

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorInt("wait_game_message", 0));
            bList.add(new Behavior("deactivate"));
            Routine r = new Routine("routine", bList, false);
            Component bComp = new Component_Behavior(r);
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("shadow_humanoid", hp), Comps.mobSprite("shadow_humanoid"),
                    Comps.movePol(pol), new Component_Spawning(su), bComp});
        }

        private static void spawnDummy(){
//            Component_Spawning sComp = new Component_Spawning();
            Component_Spawning sComp = new Component_Spawning(new SpawnUnit(20, true, new SISingleSpawn("crystal_3")));
            constructEntity(new Component[]{Comps.pos(new Vector(GameDriver.getGameWidth()/2, 40)), Comps.te(), Comps.ccBoss(),
                    Comps.bossSprite(4), sComp});
        }

        private static void construct1_pairFairies(boolean leftTurn, double xMiddle, double yMid, double endAngle, double speed){
            double hp = .5;
            double xOffset = 25;
            double xL = xMiddle - xOffset;
            double xR = xMiddle + xOffset;
            Vector posL = spawnVTop(xL);
            Vector posR = spawnVTop(xR);

            Vector midL = new Vector(posL.getA(), yMid);
            Vector midR = new Vector(posR.getA(), yMid);

            Vector endPolar;
            if(leftTurn){
                endPolar = new Vector(-endAngle, speed);
            }
            else{
                endPolar = new Vector(endAngle, speed);
            }

            ArrayList<Vector> pointsL = new ArrayList<>();
            ArrayList<Vector> pointsR = new ArrayList<>();
            pointsL.add(midL);
            pointsL.add(endPolar);
            pointsR.add(midR);
            pointsR.add(endPolar);

            constructEntity(new Component[]{Comps.pos(posL), Comps.te(), Comps.movePathFinalPolar(pointsL, speed), Comps.mobSprite("fairy"),
                Comps.ccMob("fairy", hp), Comps.dSpawn("pickup_power_small_rng_1_5")});
            constructEntity(new Component[]{Comps.pos(posR), Comps.te(), Comps.movePathFinalPolar(pointsR, speed), Comps.mobSprite("fairy"),
                    Comps.ccMob("fairy", hp), Comps.dSpawn("pickup_power_small_rng_1_5")});
        }
        private static void construct1_fairy1SingleAttack(double x, double yMid, double yCross, double speed, int delay2){
            double hp = .5;
            Vector sPos = spawnVTop(x);
            Vector mPos = new Vector(x, yMid);

            Vector crossPos = new Vector(GameDriver.getGameWidth()/2, yCross);
            Vector fPolar = new Vector(Vector.getAngle(mPos, crossPos), speed);

            SpawnUnit su = new SpawnUnit(1, false, new SISingleSpawn("fairy_1"));

            constructEntity(new Component[]{Comps.pos(sPos), Comps.te(), Comps.singleAttack(mPos, speed, 0, delay2, fPolar, su),
                Comps.mobSprite("fairy"), Comps.ccMob("fairy", hp), Comps.dSpawn("pickup_power_small_rng_1_5")});
        }
        private static void construct1_fairyCrossAttacks(Vector spawn, Vector polar){
            constructEntity(new Component[]{Comps.pos(spawn), Comps.te(), Comps.mobSprite("fairy"), Comps.ccMob("fairy", .5),
                new Component_Spawning(new SpawnUnit(110, true,
                        new SISingleTick((int)MiscUtil.rangeRandom(30, 110, GameDriver.randomDouble()), "fairy_2"))),
                Comps.dSpawn("pickup_power_small_rng_1_5"), Comps.movePol(polar)});
        }
        private static void construct1_fairyDown(double x){
            double speed = 4.5;
            double hp = 1;
            Vector pos = spawnVTop(x);
            Vector polar = new Vector(0, speed);
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("fairy"), Comps.ccMob("fairy", hp),
                Comps.movePol(polar), Comps.dSpawn("pickup_power_small_rng_1_4")});
        }
        private static void construct1_fairyTopShooting(double x){
            double hp = 3;
            double speed = 2.4;
            double yMid = 50;
            Vector sPos = spawnVTop(x);
            Vector mPos = new Vector(x, yMid);
            Vector fPolar = new Vector(0, speed);

            SpawnUnit su = new SpawnUnit(160, false, new SIMultipleTick(20, "fairy_3"));

            constructEntity(new Component[]{Comps.pos(sPos), Comps.te(), Comps.singleAttack(mPos, speed, 10, 180, fPolar, su),
                Comps.mobSprite("fairy"), Comps.ccMob("fairy", hp), Comps.dSpawn("pickup_power_small")});
        }
        private static void construct1_wispStraight(Vector pos, Vector polar){
            double hp = 1;
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("wisp"), Comps.ccMob("wisp", hp),
                Comps.wispSpinning(), Comps.movePol(polar), Comps.dSpawn("wisp_death_1")});
        }
        private static void construct1_wispCurved(Vector pos, Vector polar){
            //angle increases for both
            double hp = 1;
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("wisp"), Comps.ccMob("wisp", hp),
                    Comps.wispSpinning(), Comps.dSpawn("wisp_death_2"),
                    new Component_Movement(new MoveModeAddAngleChange("angle_change", polar, 1.18))});
        }
        private static void construct1_fairyCrossShooting(Vector spawn, Vector polar){
            constructEntity(new Component[]{Comps.pos(spawn), Comps.te(), Comps.mobSprite("fairy"), Comps.ccMob("fairy", .5),
                    new Component_Spawning(new SpawnUnit(80, true,
                            new SISingleTick((int)MiscUtil.rangeRandom(30, 80, GameDriver.randomDouble()), "fairy_1"))),
                    Comps.dSpawn("pickup_power_small_rng_1_5"), Comps.movePol(polar)});
        }

        private static void construct2_wispHoming(double x){
            double hp = .5;
            double speed = 7;
            double ac = .5;
            double compound = .99;
            Vector pos = spawnVTop(x);
            Vector player = GameDriver.getCurrentPlayerPosition();
            double initAngle = Vector.getAngle(pos, player);
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("wisp"), Comps.ccMob("wisp", hp),
                Comps.moveHomingCompounding(new Vector(initAngle, speed), player, ac, compound), Comps.dSpawn("wisp_death_3"),
                Comps.wispSpinning()});
        }
        private static void construct2_wispSpiral1(double x, double y){
            double hp = 20;
            double speed = 2.3;
            double endSpeed = 3;
            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);
            Vector fPolar = new Vector(0, endSpeed);

            SpawnUnit su = new SpawnUnit(210, false, new SIMultipleTick(15, "wisp_1"));

            Component_Behavior bComp = Comps.singleAttack(mid, speed, 10, 230, fPolar, su);
            bComp.addBehavior(new BehaviorDouble("rotate_sprite", 8));

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), bComp,
                    Comps.mobSprite("wisp"), Comps.ccMob("wisp", hp), Comps.dSpawn("wisp_death_4")});
        }
        private static void construct2_birdCrossing(double y, boolean left, double speed, String spawnID){
            Vector pos;
            double angle;
            if(left){
                pos = spawnVLeft(y);
                angle = 80;
            }
            else{
                pos = spawnVRight(y);
                angle = -80;
            }
            Vector polar = new Vector(angle, speed);
            SpawnUnit su = new SpawnUnit(120, true, new SIMultipleTick(
                    (int)MiscUtil.rangeRandom(45, 119, GameDriver.randomDouble()), spawnID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("bird"), Comps.ccMob("bird", 2),
                    new Component_Spawning(su), Comps.movePol(polar), Comps.dSpawn("pickup_power_small_rng_1_5")});
        }
        private static void construct2_wispSpiral2(double x, double y){
            double hp = 20;
            double speed = 2.3;
            double endSpeed = 3;
            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);
            Vector fPolar = new Vector(0, endSpeed);

            SpawnUnit su = new SpawnUnit(210, false, new SIMultipleTick(15, "wisp_2"));

            Component_Behavior bComp = Comps.singleAttack(mid, speed, 10, 230, fPolar, su);
            bComp.addBehavior(new BehaviorDouble("rotate_sprite", 8));

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), bComp,
                    Comps.mobSprite("wisp"), Comps.ccMob("wisp", hp), Comps.dSpawn("wisp_death_4")});
        }

        private static void construct3_autoHexWall(double x, double y, boolean left){
            double hp = 80;
            double initSpeed = 2;
            double endSpeed = 1.5;
            Vector pos;
            if(left){
                pos = spawnVLeft(y);
            }
            else{
                pos = spawnVRight(y);
            }
            Vector mid = new Vector(x, y);
            Vector fPolar = new Vector(0, endSpeed);

            ArrayList<SpawningInstruction> al = new ArrayList<>();
            al.add(new SIMultipleTick(0, 39, 8, "auto_1"));
            al.add(new SIMultipleTick(80, 119, 8, "auto_1"));
            al.add(new SIMultipleTick(160, 199, 8, "auto_1"));
            SpawnUnit su = new SpawnUnit(200, false, al);
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("auto"), Comps.ccMob("auto", hp),
                    Comps.singleAttack(mid, initSpeed, 10, 220, fPolar, su), Comps.dSpawn("pickup_power_large")});
        }
        private static void construct3_autoSideShot(double x, double y, boolean left){
            double hp = 5;
            double initSpeed = 4;
            double endSpeed = 3;
            Vector pos;
            if(left){
                pos = spawnVLeft(y);
            }
            else{
                pos = spawnVRight(y);
            }
            Vector mid = new Vector(x, y);
            Vector fPolar = new Vector(Vector.getAngle(mid, GameDriver.getInitPlayerPosition()), endSpeed);

            SpawnUnit su = new SpawnUnit(81, false, new SIMultipleTick(80, "auto_2"));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("auto"), Comps.ccMob("auto", hp),
                    Comps.singleAttack(mid, initSpeed, 10, 220, fPolar, su), Comps.dSpawn("pickup_power_small_rng_1_4")});
        }
        private static void construct3_autoCrossing(Vector pos, Vector polar, String attack){
            double hp = 11.8;
            //auto 3 - 5
            int freq;
            switch(attack){
                //single to hex
                case "auto_3":
                    freq = 190 - ((GameDriver.getCurrentDifficulty() - 1) * 17);
                    break;
                //walls of 3
                case "auto_4":
                case "auto_6":
                    freq = 120 - ((GameDriver.getCurrentDifficulty() - 1) * 10);
                    break;
                //expanding hex
                case "auto_5":
                    freq = 160 - ((GameDriver.getCurrentDifficulty() - 1) * 12);
                    break;
                default:
                    freq = 0;
                    break;
            }
            int aTick = (int)(MiscUtil.rangeRandom(0, freq, GameDriver.randomDouble()));
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(aTick, attack));

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("auto"), Comps.ccMob("auto", hp),
                    Comps.movePol(polar), new Component_Spawning(su), Comps.dSpawn("pickup_power_small_rng_1_3")});
        }
        
        private static void construct4_wispStraights(Vector pos, Vector polar, boolean lifeSpawn, boolean healthy){
            double hp = 2;
            if(lifeSpawn && healthy){
                hp = 13;
            }
            else if(lifeSpawn){
                hp = 3.5;
            }
            int i;
            if(!lifeSpawn){
                i = 5;
            }
            else{
                i = 6;
            }
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("wisp"), Comps.ccMob("wisp", hp),
                Comps.wispSpinning(), Comps.movePol(polar), Comps.dSpawn("wisp_death_" + i)});
        }
        private static void construct4_cloudTop(){
            double hp = 25;

            double midRange = 90;
            double midylow = 210;
            double midyhigh = 80;

            double angRange =75;
            int d1 = 10;
            Random r = new Random(GameDriver.getTick() + 1201);
            r.nextDouble();
            double px = MiscUtil.rangeRandom(midRange, GameDriver.getGameWidth() - midRange, r.nextDouble());
            Vector pos = spawnVTop(px);
            double mx = MiscUtil.rangeRandom(px - 50, px + 50, r.nextDouble());
            double my = MiscUtil.rangeRandom(midyhigh, midylow, r.nextDouble());
            Vector mid = new Vector(mx, my);
            double fAng = MiscUtil.rangeRandom(180 - angRange, 180 + angRange, r.nextDouble());

            double speed = MiscUtil.rangeRandom(2, 3, r.nextDouble());
            double fSpeed = MiscUtil.rangeRandom(2, 4, r.nextDouble());

            Vector fPolar = new Vector(fAng, fSpeed);

            int maxTick = (60 * 4) + 1;
            int d2 = maxTick + 60;

            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 23;
                    break;
                case 2:
                    freq = 19;
                    break;
                case 3:
                    freq = 15;
                    break;
                case 4:
                default:
                    freq = 12;
                    break;
            }
            SpawnUnit su = new SpawnUnit(maxTick, false, new SIMultipleTick(freq, "cloud_1"));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("cloud"), Comps.ccMob("cloud", hp),
                Comps.singleAttack(mid, speed, d1, d2, fPolar, su), Comps.dSpawn("cloud_death_1")});
        }
        private static void construct4_cloudAimed(){
            double hp = 25;

            double midRange = 90;
            double midylow = 210;
            double midyhigh = 80;

            double angRange =75;
            int d1 = 10;
            Random r = new Random(GameDriver.getTick() + 101);
            r.nextDouble();
            double px = MiscUtil.rangeRandom(midRange, GameDriver.getGameWidth() - midRange, r.nextDouble());
            Vector pos = spawnVTop(px);
            double mx = MiscUtil.rangeRandom(px - 50, px + 50, r.nextDouble());
            double my = MiscUtil.rangeRandom(midyhigh, midylow, r.nextDouble());
            Vector mid = new Vector(mx, my);
            double fAng = MiscUtil.rangeRandom(180 - angRange, 180 + angRange, r.nextDouble());

            double speed = MiscUtil.rangeRandom(2, 3, r.nextDouble());
            double fSpeed = MiscUtil.rangeRandom(2, 4, r.nextDouble());

            Vector fPolar = new Vector(fAng, fSpeed);

            int maxTick = (60 * 4) + 1;
            int d2 = maxTick + 60;

            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 23;
                    break;
                case 2:
                    freq = 20;
                    break;
                case 3:
                    freq = 18;
                    break;
                case 4:
                default:
                    freq = 15;
                    break;
            }
            SpawnUnit su = new SpawnUnit(maxTick, false, new SIMultipleTick(freq, "cloud_2"));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("cloud"), Comps.ccMob("cloud", hp),
                    Comps.singleAttack(mid, speed, d1, d2, fPolar, su), Comps.dSpawn("cloud_death_1")});
        }
        private static void construct4_waterCrossing(boolean left){
            double hp = 20;

            double yHigh = 50, yLow = 300;
            double angRange = 3;
            double bSpeed = 2;
            double speedRange = .15;
            double bFireTick = ((5.55/2) * 60) + 60;
            double fireTickRange = 19;

            Random r = new Random(GameDriver.getTick() + 1212);
            r.nextDouble();
            double y = MiscUtil.rangeRandom(yHigh, yLow, r.nextDouble());

            double ang = MiscUtil.rangeRandom(90-angRange, 90+angRange, r.nextDouble());
            double speed = MiscUtil.rangeRandom(bSpeed - speedRange, bSpeed + speedRange, r.nextDouble());
            Vector pos;
            if(left){
                pos = spawnVLeft(y);
            }else{
                pos = spawnVRight(y);
                ang = -ang;
            }
            Vector pol = new Vector(ang, speed);
            int fireTick = (int)MiscUtil.rangeRandom(bFireTick - fireTickRange, bFireTick + fireTickRange, GameDriver.randomDouble());

            SpawnUnit su = new SpawnUnit(fireTick + 1, false, new SISingleTick(fireTick, "water_1"));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("water_big"), Comps.ccMob("water_big", hp),
                    Comps.movePol(pol), new Component_Spawning(su), Comps.dSpawn("water_death_1")});
        }
        private static void construct4_waterSub(Vector pos, Vector iPol, double gAccel, double drag){
            int hp = 10;
            int lifeLow = 120;
            int lifeHigh = 180;
            int lifeTime = (int)(MiscUtil.rangeRandom(lifeLow, lifeHigh, GameDriver.randomDouble()));

            Component_Movement mComp = Comps.moveGravity(iPol, 0, gAccel, drag);
            //without power - stronger
            Component_Behavior bComp = Comps.lifetimeDspawn(lifeTime, "water_death_3");
            //with power
            Component_DeathSpawn dComp = new Component_DeathSpawn("water_death_2");
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("water_small"), Comps.ccMob("water_small", hp),
                mComp, dComp, bComp});
        }

        private static void construct5_birdCrossing(double y, boolean left, double angle, double speed, int attackTick, String spawnID){
            double hp = 2.5;
            Vector pos;
            if(left){
                pos = spawnVLeft(y);
            }else{
                pos = spawnVRight(y);
                angle = -angle;
            }
            Vector polar = new Vector(angle, speed);

            SpawnUnit su = new SpawnUnit(attackTick + 1, false, new SISingleTick(attackTick, spawnID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("bird"), Comps.ccMob("bird", hp),
                    new Component_Spawning(su), Comps.movePol(polar), Comps.dSpawn("pickup_power_small_rng_1_3")});
        }
        private static void construct5_birdCurveLongSide(double y, boolean left, double angle, double speed, double ac, int attackTick, String spawnID){
            double hp = 2.5;
            Vector pos;
            if(left){
                pos = spawnVLeft(y);
                ac = -ac;
            }else{
                pos = spawnVRight(y);
                angle = -angle;
            }
            Vector iPol = new Vector(angle, speed);

            SpawnUnit su = new SpawnUnit(attackTick + 1, false, new SISingleTick(attackTick, spawnID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("bird"), Comps.ccMob("bird", hp),
                    new Component_Spawning(su), Comps.moveAngleChange(iPol, ac), Comps.dSpawn("pickup_power_small_rng_1_3")});
        }
        private static void construct5_birdCurveShortSide(double x, boolean top, double angle, double speed, double ac, int attackTick, String spawnID){
            double hp = 2.5;
            Vector pos;
            if(top){
                pos = spawnVTop(x);
            }else{
                pos = spawnVBot(x);
            }
            Vector iPol = new Vector(angle, speed);

            SpawnUnit su = new SpawnUnit(attackTick + 1, false, new SISingleTick(attackTick, spawnID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.mobSprite("bird"), Comps.ccMob("bird", hp),
                    new Component_Spawning(su), Comps.moveAngleChange(iPol, ac), Comps.dSpawn("pickup_power_small_rng_1_3")});
        }
        private static void construct5_emberSpikeSlow(double x, boolean big){
            double hp = 14;
            double y = 70;
            double speed = 2.1;
            double endSpeed = 1.5;
            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);
            Vector fPol = new Vector(180, endSpeed);
            String dID;
            if(!big){
                dID = "pickup_power_small";
            }else{
                dID = "pickup_power_large";
            }

            SpawnUnit su = new SpawnUnit(1, false, new SISingleSpawn("ember_1"));

            Component_Behavior bComp = Comps.singleAttack(mid, speed, 10, 80, fPol, su);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), bComp,
                    Comps.mobSprite("ember"), Comps.ccMob("ember", hp), Comps.dSpawn(dID)});
        }
        private static void construct5_emberSpikeFast(double x, boolean big){
            double hp = 12;
            double y = 70;
            double speed = 4.5;
            double endSpeed = 2.5;
            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);
            Vector fPol = new Vector(180, endSpeed);
            String dID;
            if(!big){
                dID = "pickup_power_small";
            }else{
                dID = "pickup_power_large";
            }

            SpawnUnit su = new SpawnUnit(1, false, new SISingleSpawn("ember_1"));

            Component_Behavior bComp = Comps.singleAttack(mid, speed, 1, 50, fPol, su);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), bComp,
                    Comps.mobSprite("ember"), Comps.ccMob("ember", hp), Comps.dSpawn(dID)});
        }
        private static void construct5_emberShotgunSlow(double x, boolean big){
            double hp = 14;
            double y = 70;
            double speed = 2.1;
            double endSpeed = 1.5;
            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);
            Vector fPol = new Vector(180, endSpeed);
            String dID;
            if(!big){
                dID = "pickup_power_small";
            }else{
                dID = "pickup_power_large";
            }

            SpawnUnit su = new SpawnUnit(1, false, new SISingleSpawn("ember_2"));

            Component_Behavior bComp = Comps.singleAttack(mid, speed, 10, 80, fPol, su);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), bComp,
                    Comps.mobSprite("ember"), Comps.ccMob("ember", hp), Comps.dSpawn(dID)});
        }
        private static void construct5_emberShotgunFast(double x, boolean big){
            double hp = 12;
            double y = 70;
            double speed = 4.5;
            double endSpeed = 2.5;
            Vector pos = spawnVTop(x);
            Vector mid = new Vector(x, y);
            Vector fPol = new Vector(180, endSpeed);
            String dID;
            if(!big){
                dID = "pickup_power_small";
            }else{
                dID = "pickup_power_large";
            }

            SpawnUnit su = new SpawnUnit(1, false, new SISingleSpawn("ember_2"));

            Component_Behavior bComp = Comps.singleAttack(mid, speed, 1, 50, fPol, su);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), bComp,
                    Comps.mobSprite("ember"), Comps.ccMob("ember", hp), Comps.dSpawn(dID)});
        }

        private static void construct6_wispCross(Vector pos, Vector pol, SpawnUnit su){
            double hp = 3;
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("wisp", hp), Comps.mobSprite("wisp"),
                Comps.wispSpinning(), new Component_Spawning(su), Comps.movePol(pol), Comps.dSpawn("pickup_power_small_rng_1_2")});
        }

        private static void construct7_wispDown(double x){
            Vector pos = spawnVTop(x);
            double hp = 1;
            double speed = 9.3;
            Vector pol = new Vector(0, speed);
            String attack = "swisp_1";
            int freq = 53;
            int tick = (int)(MiscUtil.rangeRandom(0, freq, GameDriver.randomDouble()));
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, attack));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("shadow_wisp", hp), Comps.mobSprite("shadow_wisp"),
                    Comps.movePol(pol), new Component_Spawning(su), Comps.wispSpinning(), Comps.dSpawn("pickup_power_small")});
        }
        private static void construct7_cloudDiagonal(double x, boolean dir, String aID, boolean healthy){
            double hp;
            if(!healthy){
                hp = 7;
            }else{
                hp = 11;
            }
            Vector pos = spawnVTop(x);
            double xOff = 130;
            double yTarget = 180;
            if(dir){
                xOff = - xOff;
            }
            Vector mid = new Vector(x + xOff, yTarget);
            double speed = 4;
            Vector fPol = new Vector(-Vector.getAngle(pos, mid), speed * .6);
            String dID = "pickup_power_large_rng";
            int freq = 7;
            SpawnUnit su = new SpawnUnit(freq * 4, false, new SIMultipleTick(freq, aID));

            Component_Behavior bComp = Comps.singleAttack(mid, speed, 20, 50, fPol, su);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), bComp,
                    Comps.mobSprite("shadow_cloud"), Comps.ccMob("cloud", hp), Comps.dSpawn(dID)});
        }
        private static void construct7_wispCurve(double x, boolean dir, String attack){
            Vector pos = spawnVTop(x);
            double hp = 2;
            double speed = 9.3;
            Vector iPol = new Vector(0, speed);
            double ac = 1.3;
            if(!dir){
                ac = - ac;
            }

            int freq = 95;
            int tick = (int)(MiscUtil.rangeRandom(0, freq, GameDriver.randomDouble()));
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, attack));
            Component_Movement mComp = Comps.moveAngleChange(iPol, ac);
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("shadow_wisp", hp), Comps.mobSprite("shadow_wisp"),
                    mComp, new Component_Spawning(su), Comps.wispSpinning(), Comps.dSpawn("pickup_power_small")});
        }
        private static void construct7_humanoidCross(Vector pos, Vector pol, String sID){
            double hp = .5;
            int freq = 160;
            int tick = (int)(MiscUtil.rangeRandom(0, freq, GameDriver.randomDouble()));
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, sID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("shadow_humanoid", hp), Comps.mobSprite("shadow_humanoid"),
                    Comps.movePol(pol), new Component_Spawning(su), Comps.dSpawn("pickup_power_small_rng_1_4")});
        }
        private static void construct7_humanoidDown(double x, boolean dir){
            double hp = 17;
            Vector pos = spawnVTop(x);
            double ang = 0;
            double speed = 3;
            Vector iPol = new Vector(ang, speed);
            String attack = "shumanoid_3";
            int freq = 120;
            int tick = (int)(MiscUtil.rangeRandom(0, freq, GameDriver.randomDouble()));
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, attack));

            int time = 115, time2 = 84;
            double ac = 1.0843373494;
            if(dir){
                ac = - ac;
            }
            MoveModeAddAngleChange secondaryMoveMode = new MoveModeAddAngleChange("angle_change", iPol.deepClone(), ac);
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
            bList.add(new BehaviorMM("set_mm", secondaryMoveMode));
            bList.add(new Behavior("clear_spawning"));
            bList.add(new BehaviorVector("timer", new Vector(time2)));
            bList.add(new Behavior("continue_trajectory"));

            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);

            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("shadow_humanoid", hp), Comps.mobSprite("shadow_humanoid"),
                    Comps.movePol(iPol), new Component_Spawning(su), Comps.dSpawn("pickup_power_small"), bComp});
        }
        private static void construct7_wispDownEnd(double x, String dID, boolean healthy){
            Vector pos = spawnVTop(x);
            double hp;
            if(!healthy){
                hp = 3;
            }else{
                hp = 6;
            }
            double speed = 7.3;
            Vector pol = new Vector(0, speed);
            String attack = "swisp_6";
            int freq = 30;
            int tick = (int)(MiscUtil.rangeRandom(0, freq, GameDriver.randomDouble()));
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, attack));
            constructEntity(new Component[]{Comps.pos(pos), Comps.te(), Comps.ccMob("shadow_wisp", hp), Comps.mobSprite("shadow_wisp"),
                    Comps.movePol(pol), new Component_Spawning(su), Comps.wispSpinning(), Comps.dSpawn(dID)});
        }

        //type as te for despawner//actually type as tep for clear to kill them
        private static void constructTrapSpawnerStraight(Vector pos, Vector pol, int freq, String trapID){
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn(trapID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.movePol(pol), new Component_Spawning(su), Comps.tep()});
        }
        private static void constructTrapSpawnerCurved(Vector pos, Vector pol, double ac, int freq, String trapID){
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn(trapID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.moveAngleChange(pol, ac), new Component_Spawning(su), Comps.tep()});
        }
        private static void constructTrapSpawnerHoming(Vector pos, Vector pol, double ac, int freq, String trapID){
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn(trapID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.moveHoming(pol, GameDriver.getCurrentPlayerPosition(), ac), new Component_Spawning(su), Comps.tep()});
        }
        private static void constructTrapSpawnerStraightLifetime(Vector pos, Vector pol, int freq, int lifetime, String trapID){
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn(trapID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.movePol(pol), new Component_Spawning(su), Comps.tep(), Comps.lifetime(lifetime)});
        }
        private static void constructTrapSpawnerCurvedLifetime(Vector pos, Vector pol, double ac, int freq, int lifetime, String trapID){
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn(trapID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.moveAngleChange(pol, ac), new Component_Spawning(su), Comps.tep(), Comps.lifetime(lifetime)});
        }
        private static void constructTrapSpawnerHomingLifetime(Vector pos, Vector pol, double ac, int freq, int lifetime, String trapID){
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn(trapID));
            constructEntity(new Component[]{Comps.pos(pos), Comps.moveHoming(pol,
                    GameDriver.getCurrentPlayerPosition(), ac), new Component_Spawning(su), Comps.tep(), Comps.lifetime(lifetime)});
        }

        private static void spawnTrap(Entity e, int id){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            //this is so fucking - i cannot care any longer
            if(!DanmakuSpawns.isInBounds(pos, 50)){
                return;
            }
            constructTrap(pos, "trap_" + id);
        }
        private static void constructTrap(Vector pos, String attackID){
            constructEntity(new Component[]{Comps.pos(pos), Comps.mobSprite("trap"), Comps.trapLifetime(), Comps.dSpawn(attackID), Comps.tep()});
        }

        private static void spawnSpawner(int stage, int wave){
            int waveLength = getWaveLength(stage, wave);
            ArrayList<SpawningInstruction> spawnList = new ArrayList<>();
            SpawnUnit unit = new SpawnUnit(waveLength, false, spawnList);
            populateSpawnList(spawnList, stage, wave);

            constructEntity(new Component[]{new Component_Spawning(unit), Comps.lifetime(waveLength + 1)});
        }

        private static int getWaveLength(int stage, int wave){
            switch(stage){
                case 1:
                    switch(wave){
                        case 1:
                        case 2:
                            return 45 * 3;
                        case 3:
                        case 8:
                            return 1;
                        case 4:
                            return 60 * 22;
                        case 5:
                            return 60 * 25;
                        case 6:
                            return (60 * 33) + 1;
                        case 7:
                            return (60 * 23) + 1;
                    }
                    break;
                case 2:
                    switch(wave){
                        case 1:
                            return 60 * 15;
                        case 2:
                            return 60 * 10;
                        case 3:
                            return 60 * 11;
                        case 4:
                            return 60 * 10;
                        case 5:
                            return 1;
                        case 6:
                            return 60 * 9;
                    }
                    break;
                case 3:
                    switch(wave){
                        case 1:
                        case 2:
                            return 60 * 7;
                        case 3:
                            return (60 * 23) + 1;
                        case 4:
                            return 1;
                        case 5:
                            return 60 * 5;
                    }
                    break;
                case 4:
                    switch(wave){
                        case 1:
                        case 8:
                            return (6 * 4 * 60) + 1;
                        case 2:
                            return 60 * 5;
                        case 3:
                            return 60 * 8;
                        case 4:
                            return 60 * 5;
                        case 5:
                            return 60 * 8;
                        case 6:
                            return 60 * 15;
                        case 7:
                            return 1;
                    }
                    break;
                case 5:
                    switch(wave){
                        case 1:
                        case 2:
                            return 60 *14;
                        case 3:
                        case 4:
                            return 60 * 4;
                        case 5:
                        case 6:
                            return 30 * 5;
                        case 7:
                        case 9:
                            return (80 * 4) + 1;
                        case 8:
                        case 10:
                            return 30 * 7;
                        case 11:
                            return 1;
                    }
                    break;
                case 6:
                    switch(wave){
                        case 1:
                            return 60 * 18;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                            return 1;
                    }
                    break;
                case 7:
                    switch(wave){
                        case 1:
                            return 60 * 15;
                        case 2:
                        case 3:
                        case 11:
                        case 12:
                            return 130 * 4;
                        case 4:
                        case 6:
                            return 60 * 6;
                        case 5:
                        case 7:
                            return 60 * 4;
                        case 8:
                            return 1;
                        case 9:
                            return 60 * 12;
                        case 10:
                            return 60 * 15;
                        case 13:
                            return 60 * 10;
                        case 14://176.5 -> 190 = 13.5 seconds
                            return 60 * 8;

                    }
                    break;
                default:
                    return 0;
            }
            return 0;
        }
        private static void populateSpawnList(ArrayList<SpawningInstruction> list, int stage, int wave){
            switch(stage){
                case 1:
                    switch(wave){
                        case 1:
                            list1_1(list);
                            break;
                        case 2:
                            list1_2(list);
                            break;
                        case 3:
                            list1_3(list);
                            break;
                        case 4:
                            list1_4(list);
                            break;
                        case 5:
                            list1_5(list);
                            break;
                        case 6:
                            list1_6(list);
                            break;
                        case 7:
                            list1_7(list);
                            break;
                        case 8:
                            list1_8(list);
                            break;
                    }
                    break;
                case 2:
                    switch(wave){
                        case 1:
                            list2_1(list);
                            break;
                        case 2:
                            list2_2(list);
                            break;
                        case 3:
                            list2_3(list);
                            break;
                        case 4:
                            list2_4(list);
                            break;
                        case 5:
                            list2_5(list);
                            break;
                        case 6:
                            list2_6(list);
                            break;
                    }
                    break;
                case 3:
                    switch(wave){
                        case 1:
                            list3_1(list);
                            break;
                        case 2:
                            list3_2(list);
                            break;
                        case 3:
                            list3_3(list);
                            break;
                        case 4:
                            list3_4(list);
                            break;
                        case 5:
                            list3_5(list);
                            break;
                    }
                    break;
                case 4:
                    switch(wave){
                        case 1:
                            list4_1(list);
                            break;
                        case 2:
                        case 3:
                            list4_2(list);
                            break;
                        case 4:
                        case 5:
                            list4_4(list);
                            break;
                        case 6:
                            list4_6(list);
                            break;
                        case 7:
                            list4_7(list);
                            break;
                        case 8:
                            list4_8(list);
                            break;
                    }
                    break;
                case 5:
                    switch(wave){
                        case 1:
                            list5_1(list);
                            break;
                        case 2:
                            list5_2(list);
                            break;
                        case 3:
                            list5_3(list);
                            break;
                        case 4:
                            list5_4(list);
                            break;
                        case 5:
                            list5_5(list);
                            break;
                        case 6:
                            list5_6(list);
                            break;
                        case 7:
                            list5_7(list);
                            break;
                        case 8:
                            list5_8(list);
                            break;
                        case 9:
                            list5_9(list);
                            break;
                        case 10:
                            list5_10(list);
                            break;
                        case 11:
                            list5_11(list);
                            break;
                    }
                    break;
                case 6:
                    switch(wave){
                        case 1:
                            list6_1(list);
                            break;
                        case 2:
                            list6_2(list);
                            break;
                        case 3:
                            list6_3(list);
                            break;
                        case 4:
                            list6_4(list);
                            break;
                        case 5:
                            list6_5(list);
                            break;
                        case 6:
                            list6_6(list);
                            break;
                        case 7:
                            list6_7(list);
                            break;
                        case 8:
                            list6_8(list);
                            break;
                        case 9:
                            list6_9(list);
                            break;
                    }
                    break;
                case 7:
                    switch(wave){
                        case 1:
                            list7_1(list);
                            break;
                        case 2:
                            list7_2(list);
                            break;
                        case 3:
                            list7_3(list);
                            break;
                        case 4:
                            list7_4(list);
                            break;
                        case 5:
                            list7_5(list);
                            break;
                        case 6:
                            list7_6(list);
                            break;
                        case 7:
                            list7_7(list);
                            break;
                        case 8:
                            list7_8(list);
                            break;
                        case 9:
                            list7_9(list);
                            break;
                        case 10:
                            list7_10(list);
                            break;
                        case 11:
                            list7_11(list);
                            break;
                        case 12:
                            list7_12(list);
                            break;
                        case 13:
                            list7_13(list);
                            break;
                        case 14:
                            list7_14(list);
                            break;
                    }
                    break;
                default:
            }
        }
        private static void list1_1(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(45, "1_1_1"));
        }
        private static void list1_2(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(45, "1_2_1"));
        }
        private static void list1_3(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("1_3_1"));
        }
        private static void list1_4(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(0, 60 * 6, 30, "1_4_1"));
            list.add(new SIMultipleTick(60 * 7, (60 * 12) + 30, 30, "1_4_2"));
            list.add(new SIMultipleTick(60 * 13, 60 * 18, 30, "1_4_3"));
            list.add(new SIMultipleTick(60 * 18, 60 * 22, 30, "1_4_4"));
        }
        private static void list1_5(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(95, "1_5_1"));
            list.add(new SIMultipleTick(210, "1_5_2"));
        }
        private static void list1_6(ArrayList<SpawningInstruction> list){
            //33 seconds
            list.add(new SIMultipleTick(0, 60*3, 7, "1_6_1"));
            list.add(new SIMultipleTick(60*4,60*7, 7, "1_6_2"));
            list.add(new SIMultipleTick(60*8, 60*11, 7, "1_6_1"));
            list.add(new SIMultipleTick(60*12,60*15, 7, "1_6_2"));
            list.add(new SIMultipleTick(60*18,60*21, 7, "1_6_3"));
            list.add(new SIMultipleTick(60*22,60*25, 7, "1_6_4"));
            list.add(new SIMultipleTick(60*26,60*29, 7, "1_6_3"));
            list.add(new SIMultipleTick(60*30,60*33, 7, "1_6_4"));
        }
        private static void list1_7(ArrayList<SpawningInstruction> list){
            //23 seconds
            list.add(new SIMultipleTick(64, "1_7_1"));
        }
        private static void list1_8(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("1_8_1"));
        }
        private static void list2_1(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(60, "2_1_1"));
        }
        private static void list2_2(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("2_2_1"));
            list.add(new SISingleTick(50, "2_2_2"));
            list.add(new SISingleTick(100, "2_2_3"));
            list.add(new SISingleTick(7 * 60, "2_2_4"));
            list.add(new SISingleTick(50 + (7 * 60), "2_2_5"));
            list.add(new SISingleTick(100 + (7 * 60), "2_2_6"));
        }
        private static void list2_3(ArrayList<SpawningInstruction> list){
            //11 secs
            list.add(new SIMultipleTick(0, 8 * 60, 42, "2_3_1"));
            list.add(new SIMultipleTick(110, 9 * 60, 77, "2_3_2"));
            list.add(new SIMultipleTick(180, 7 * 60, 48, "2_3_3"));
            list.add(new SIMultipleTick(212, 11 * 60, 65, "2_3_4"));
        }
        private static void list2_4(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("2_4_1"));
            list.add(new SISingleTick(50, "2_4_2"));
            list.add(new SISingleTick(100, "2_4_3"));
            list.add(new SISingleTick(7 * 60, "2_4_4"));
            list.add(new SISingleTick(50 + (7 * 60), "2_4_5"));
            list.add(new SISingleTick(100 + (7 * 60), "2_4_6"));
        }
        private static void list2_5(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("2_5_1"));
        }
        private static void list2_6(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(60, "2_1_1"));
        }
        private static void list3_1(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("3_1_1"));
            list.add(new SIMultipleTick(90, 60 * 7, 30, "3_1_2"));
        }
        private static void list3_2(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("3_2_1"));
            list.add(new SIMultipleTick(90, 60 * 7, 30, "3_2_2"));
        }
        private static void list3_3(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(0, 60 * 6, 40, "3_3_1"));
            list.add(new SIMultipleTick(60 * 3, 60 * 9, 40, "3_3_2"));
            list.add(new SIMultipleTick(60 * 14, 60 * 20, 40, "3_3_3"));
            list.add(new SIMultipleTick(60 * 17, 60 * 23, 40, "3_3_4"));
        }
        private static void list3_4(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("3_4_1"));
            list.add(new SISingleSpawn("3_4_2"));
            list.add(new SISingleSpawn("3_4_3"));
        }
        private static void list3_5(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(40, "3_5_1"));
        }
        private static void list4_1(ArrayList<SpawningInstruction> list){
            int multi = 10;
            //3 seconds of spawn = 240
            //6 secs total = 360
            //lower bound and upper bound are both inclusive
            list.add(new SIMultipleTick(0, 3 * 60, multi, "4_1_1"));
            list.add(new SIMultipleTick(6 * 60, 9 * 60, multi, "4_1_2"));
            list.add(new SIMultipleTick(12 * 60, 15 * 60, multi, "4_1_1"));
            list.add(new SIMultipleTick(18 * 60, (21 * 60) - 10, multi, "4_1_2"));
            //gives a life
            list.add(new SISingleTick(21 * 60, "4_1_3"));
        }
        private static void list4_2(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(40, "4_2"));
        }
        private static void list4_4(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(40, "4_4"));
        }
        private static void list4_6(ArrayList<SpawningInstruction> list){
            list.add(new SIMultipleTick(51, "4_6"));
        }
        private static void list4_7(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("4_7"));
        }
        private static void list4_8(ArrayList<SpawningInstruction> list){
            int multi = 10;
            //3 seconds of spawn = 240
            //6 secs total = 360
            //lower bound and upper bound are both inclusive
            list.add(new SIMultipleTick(0, 3 * 60, multi, "4_8_1"));
            list.add(new SIMultipleTick(6 * 60, 9 * 60, multi, "4_8_2"));
            list.add(new SIMultipleTick(12 * 60, 15 * 60, multi, "4_8_1"));
            list.add(new SIMultipleTick(18 * 60, (21 * 60) - 10, multi, "4_8_2"));
            //gives a life
            list.add(new SISingleTick(21 * 60, "4_8_3"));
        }
        private static void list5_1(ArrayList<SpawningInstruction> list){
            int freq = 9;
            //6 seconds of spawn for each side

            list.add(new SIMultipleTick(0, 60 * 6, freq, "5_1_1"));
            list.add(new SIMultipleTick((60 * 7) + 30, (60 * 13) + 30, freq, "5_1_2"));
        }
        private static void list5_2(ArrayList<SpawningInstruction> list){
            int freq = 9;

            list.add(new SIMultipleTick(0, 60 * 6, freq, "5_2_1"));
            list.add(new SIMultipleTick((60 * 7) + 30, (60 * 13) + 30, freq, "5_2_2"));
        }
        private static void list5_3(ArrayList<SpawningInstruction> list){
            int freq = 9;
            list.add(new SIMultipleTick(freq, "5_3"));
        }
        private static void list5_4(ArrayList<SpawningInstruction> list){
            int freq = 9;
            list.add(new SIMultipleTick(freq, "5_4"));
        }
        private static void list5_5(ArrayList<SpawningInstruction> list){
            int freq = 9;
            list.add(new SIMultipleTick(freq, "5_5"));
        }
        private static void list5_6(ArrayList<SpawningInstruction> list){
            int freq = 9;
            list.add(new SIMultipleTick(freq, "5_6"));
        }
        private static void list5_7(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("5_7_1"));
            list.add(new SISingleTick(80, "5_7_2"));
            list.add(new SISingleTick(80 * 2, "5_7_3"));
            list.add(new SISingleTick(80 * 3, "5_7_4"));
            list.add(new SISingleTick(80 * 4, "5_7_5"));
        }
        private static void list5_8(ArrayList<SpawningInstruction> list){
            for(int i = 0; i < 7; i++){
                list.add(new SISingleTick(i * 30, "5_8_" + (i + 1)));
            }
        }
        private static void list5_9(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("5_9_1"));
            list.add(new SISingleTick(80, "5_9_2"));
            list.add(new SISingleTick(80 * 2, "5_9_3"));
            list.add(new SISingleTick(80 * 3, "5_9_4"));
            list.add(new SISingleTick(80 * 4, "5_9_5"));
        }
        private static void list5_10(ArrayList<SpawningInstruction> list){
            for(int i = 0; i < 7; i++){
                list.add(new SISingleTick(i * 30, "5_10_" + (i + 1)));
            }
        }
        private static void list5_11(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("5_11"));
        }
        private static void list6_1(ArrayList<SpawningInstruction> list){
            int freq = 5;
            list.add(new SIMultipleTick(freq, "6_1"));
        }
        private static void list6_2(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_2"));
        }
        private static void list6_3(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_3"));
        }
        private static void list6_4(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_4"));
        }
        private static void list6_5(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_5"));
        }
        private static void list6_6(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_6"));
        }
        private static void list6_7(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_7"));
        }
        private static void list6_8(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_8"));
        }
        private static void list6_9(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("6_9"));
        }
        private static void list7_1(ArrayList<SpawningInstruction> list){
            int freq = 15;
            list.add(new SIMultipleTick(freq, "7_1"));
        }
        private static void list7_2(ArrayList<SpawningInstruction> list){
            int freq = 130;
            list.add(new SISingleSpawn("7_2_1"));
            list.add(new SISingleTick(freq - 1, "7_2_2"));
            list.add(new SISingleTick((2 * freq) - 1, "7_2_1"));
            list.add(new SISingleTick((3 * freq) - 1, "7_2_2"));
        }
        private static void list7_3(ArrayList<SpawningInstruction> list){
            int freq = 130;
            list.add(new SISingleSpawn("7_3_1"));
            list.add(new SISingleTick(freq - 1, "7_3_2"));
            list.add(new SISingleTick((2 * freq) - 1, "7_3_1"));
            list.add(new SISingleTick((3 * freq) - 1, "7_3_2"));
        }
        private static void list7_4(ArrayList<SpawningInstruction> list){
            int freq = 8;
            list.add(new SIMultipleTick(freq, "7_4"));
        }
        private static void list7_5(ArrayList<SpawningInstruction> list){
            int freq = 8;
            list.add(new SIMultipleTick(freq, "7_5"));
        }
        private static void list7_6(ArrayList<SpawningInstruction> list){
            int freq = 8;
            list.add(new SIMultipleTick(freq, "7_6"));
        }
        private static void list7_7(ArrayList<SpawningInstruction> list){
            int freq = 8;
            list.add(new SIMultipleTick(freq, "7_7"));
        }
        private static void list7_8(ArrayList<SpawningInstruction> list){
            list.add(new SISingleSpawn("7_8"));
        }
        private static void list7_9(ArrayList<SpawningInstruction> list){
            int freq = 3;
            list.add(new SIMultipleTick(0, (60 * 8) - 1, freq, "7_9_1"));
            list.add(new SIMultipleTick(60, (60 * 9) - 1, freq, "7_9_2"));
            list.add(new SIMultipleTick((60 * 2) - 1, (60 * 10) - 1, freq, "7_9_3"));
            list.add(new SIMultipleTick((60 * 3) - 1, (60 * 11) - 1, freq, "7_9_4"));
            list.add(new SIMultipleTick((60 * 4) - 1, (60 * 12) - 1, freq, "7_9_5"));
        }
        private static void list7_10(ArrayList<SpawningInstruction> list){
            int freq = 35;
            list.add(new SIMultipleTick(freq, "7_10"));
        }
        private static void list7_11(ArrayList<SpawningInstruction> list){
            int freq = 130;
            list.add(new SISingleSpawn("7_11_1"));
            list.add(new SISingleTick(freq - 1, "7_11_2"));
            list.add(new SISingleTick((2 * freq) - 1, "7_11_1"));
            list.add(new SISingleTick((3 * freq) - 1, "7_11_2"));
        }
        private static void list7_12(ArrayList<SpawningInstruction> list){
            int freq = 130;
            list.add(new SISingleSpawn("7_12_1"));
            list.add(new SISingleTick(freq - 1, "7_12_2"));
            list.add(new SISingleTick((2 * freq) - 1, "7_12_1"));
            list.add(new SISingleTick((3 * freq) - 1, "7_12_2"));
        }
        private static void list7_13(ArrayList<SpawningInstruction> list){
            int freq = 3;
            list.add(new SIMultipleTick(0, (60 * 8) - 1, freq, "7_13_1"));
            list.add(new SIMultipleTick(60, (60 * 9) - 1, freq, "7_13_2"));
            list.add(new SIMultipleTick((60 * 2) - 1, (60 * 10) - 1, freq, "7_13_3"));
        }
        private static void list7_14(ArrayList<SpawningInstruction> list){
            int freq = 10;
            list.add(new SIMultipleTick(0, 60, freq, "7_14_1"));
            list.add(new SIMultipleTick(120, 180, freq, "7_14_2"));
            list.add(new SIMultipleTick(240, 300, freq, "7_14_1"));
            list.add(new SIMultipleTick(360, 419, freq, "7_14_2"));
            list.add(new SISingleTick(420, "7_14_3"));
        }

        private static Vector spawnVTop(double x){
            return new Vector(x, -45);
        }
        private static Vector spawnVBot(double x){
            return new Vector(x, GameDriver.getGameHeight() + 45);
        }
        private static Vector spawnVLeft(double y){
            return new Vector(-45, y);
        }
        private static Vector spawnVRight(double y){
            return new Vector(GameDriver.getGameWidth() + 45, y);
        }

        private static Vector endVLeft(double y){
            return new Vector(-55, y);
        }
        private static Vector endVRight(double y){
            return new Vector(GameDriver.getGameWidth() + 55, y);
        }
        private static Vector endVBot(double x){
            return new Vector(x, GameDriver.getGameHeight() + 55);
        }
        private static Vector bossIdlePos(){
            return new Vector(GameDriver.getGameWidth()/2, 160);
        }
        private static class BossHelper {
            private static final double INIT_SPEED = 3.5;
            private static final int INIT_WAIT_TIME = 100;
            private static Vector spawn(){
                return spawnVTop(GameDriver.getGameWidth()/2);
            }
            private static Vector mid(){
                return new Vector(GameDriver.getGameWidth()/2, 160);
            }

            //go to mid
            //add dialogue
            //wait for dialogue completion -> move on
            private static Routine bossInit(){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", mid(), INIT_SPEED)));
                al.add(new BehaviorVector("timer", new Vector(INIT_WAIT_TIME)));
                al.add(new BehaviorSU("set_su", new SpawnUnit(1, false, new SISingleSpawn("dialogue_pre"))));
                al.add(new Behavior("wait_dialogue_over"));
                return new Routine("routine", al, false);
            }
            //use: b5 stands in the middle of the screen the entire time
            private static Routine bossInitMid(){
                Vector midOfScreen = new Vector(GameDriver.getGameWidth()/2, GameDriver.getGameHeight()/2);
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", midOfScreen, INIT_SPEED)));
                al.add(new BehaviorVector("timer", new Vector(INIT_WAIT_TIME + 30)));
                al.add(new BehaviorSU("set_su", new SpawnUnit(1, false, new SISingleSpawn("dialogue_pre"))));
                al.add(new Behavior("wait_dialogue_over"));
                return new Routine("routine", al, false);
            }

            private static ArrayList<Behavior> attackPhaseInit(double hp, String id){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorDouble("set_hp", hp));
                al.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("hp_bar"))));
                al.add(new BehaviorBM("write_message", new BoardMessage("boss_hp", 2, "gameplay", new Object[]{hp, id})));
                return al;
            }
            //id = YOUR ID
            private static Routine attackInPlace(SpawnUnit su, double hp, String id){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorDouble("set_hp", hp));
                al.add(new BehaviorSU("set_su", su));
                //su to spawn hp
                al.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("hp_bar"))));
                //write boardmessage behavior
                al.add(new BehaviorBM("write_message", new BoardMessage("boss_hp", 2, "gameplay", new Object[]{hp, id})));
                return new Routine("boss_routine", al, false);
            }
            //time1 = still, time2 = move
            private static Routine moveBetweenAttacks(SpawnUnit su, double time1, double time2, double hp, String id){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorDouble("set_hp", hp));
                //su to spawn hp
                al.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("hp_bar"))));
                //write boardmessage behavior
                al.add(new BehaviorBM("write_message", new BoardMessage("boss_hp", 2, "gameplay", new Object[]{hp, id})));
                ArrayList<Behavior> attack = new ArrayList<>();
                //add the su
                //time = ticks between moves
                al.add(new BehaviorSU("add_su", su));
                if(time1 > 0) {
                    attack.add(new BehaviorVector("timer", new Vector(time1)));
                }
                attack.add(new BehaviorDouble("boss_point", time2));
                attack.add(new BehaviorVector("timer", new Vector(time2)));

                al.add(new Routine("routine", attack, true));
                return new Routine("boss_routine", al, false);
            }
            private static Routine moveBetweenAttacks(SpawnUnit[] sus, double time1, double time2, double hp, String id){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorDouble("set_hp", hp));
                //su to spawn hp
                al.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("hp_bar"))));
                //write boardmessage behavior
                al.add(new BehaviorBM("write_message", new BoardMessage("boss_hp", 2, "gameplay", new Object[]{hp, id})));
                ArrayList<Behavior> attack = new ArrayList<>();
                //add the su
                //time = ticks between moves
                for(SpawnUnit su : sus){
                    al.add(new BehaviorSU("add_su", su));
                }

                if(time1 > 0) {
                    attack.add(new BehaviorVector("timer", new Vector(time1)));
                }
                attack.add(new BehaviorDouble("boss_point", time2));
                attack.add(new BehaviorVector("timer", new Vector(time2)));

                al.add(new Routine("routine", attack, true));
                return new Routine("boss_routine", al, false);
            }
            private static Routine moveBetweenAttacksLimited(SpawnUnit su, double time1, double time2, double hp, String id){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorDouble("set_hp", hp));
                //su to spawn hp
                al.add(new BehaviorSU("add_su", new SpawnUnit(1, false, new SISingleSpawn("hp_bar"))));
                //write boardmessage behavior
                al.add(new BehaviorBM("write_message", new BoardMessage("boss_hp", 2, "gameplay", new Object[]{hp, id})));
                ArrayList<Behavior> attack = new ArrayList<>();
                //add the su
                //time = ticks between moves
                al.add(new BehaviorSU("add_su", su));

                attack.add(new BehaviorVector("timer", new Vector(time1)));
                attack.add(new BehaviorDouble("boss_point_limited", time2));
                attack.add(new BehaviorVector("timer", new Vector(time2)));

                al.add(new Routine("routine", attack, true));
                return new Routine("boss_routine", al, false);
            }

            private static Routine endPhase(int powerGain, boolean returnToCenter){
                ArrayList<Behavior> al = new ArrayList<>();
                //powergain
                if(powerGain > 0) {
                    int powerRemaining = powerGain;
                    ArrayList<SpawningInstruction> spawnAL = new ArrayList<>();
                    while(powerRemaining > 0){
                        if(powerRemaining >= 8){
                            powerRemaining -= 8;
                            spawnAL.add(new SISingleSpawn("pickup_power_large"));
                        }
                        else{
                            --powerRemaining;
                            spawnAL.add(new SISingleSpawn("pickup_power_small"));
                        }
                    }
                    SpawnUnit su = new SpawnUnit(1, false, spawnAL);
                    al.add(new BehaviorSU("set_su", su));
                }
                //need to clear spawning either way
                else{
                    al.add(new Behavior("clear_spawning"));
                }
                //send message to kill hp bar
                al.add(new BehaviorBM("write_message", new BoardMessage("game_message", 2, "gameplay", new Object[]{7})));
                //and to clear the board -> internalMessage                                   //lifetime to 4 (it was 3 before lol) just in case lol
                al.add(new BehaviorBM("write_message", new BoardMessage("clear", 4, "gameplay")));
                if(returnToCenter){
                    al.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", mid(), INIT_SPEED)));
                }
                //need to clear movement either way
                else{
                    al.add(new Behavior("clear_movement"));
                }
                //need to make boss hp large for boss routine to work
                al.add(new BehaviorDouble("set_hp", 100000000));
                return new Routine("routine", al, false);
            }
            private static Routine endBoss(boolean deactivate){
                //assume spawning and movement have been dealt with
                //as well as the hp bar and clear
                //just add the dialogue and maybe deactivate
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorSU("set_su", new SpawnUnit(1, false, new SISingleSpawn("dialogue_post"))));
                if(deactivate){
                    al.add(new Behavior("deactivate"));
                }
                return new Routine("routine", al, false);
            }
            private static Routine endCampaign(){
                ArrayList<Behavior> al = new ArrayList<>();
                al.add(new BehaviorSU("set_su", new SpawnUnit(1, false, new SISingleSpawn("game_ender"))));
                al.add(new Behavior("deactivate"));
                return new Routine("routine", al, false);
            }

            private static Behavior tripleSprite(){
                return new Behavior("boss_graphics_triple_sprite");
            }
            private static Behavior tripleAnim(){
                return new BehaviorInt("boss_graphics_triple_anim", 0);
            }
        }
    }
    private static class DanmakuSpawns{
        //stage 1
        private static void spawnFairy_1(Entity fairy){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", fairy.getEntityID());
            if(pComp == null){
                return;
            }
            Vector fPos = pComp.getPrevPosition();
            switch(GameDriver.getCurrentDifficulty()){
                case 4:
                    constructDownProjectile(fPos, "medium", "green", 4);
                case 3:
                    constructDownProjectile(fPos, "medium", "green", 3);
                case 2:
                    constructDownProjectile(fPos, "medium", "green", 2);
                    break;
            }
        }
        private static void spawnFairy_2(Entity fairy){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", fairy.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            switch(GameDriver.getCurrentDifficulty()){
                case 4:
                    for(int i = 0; i < 2; i++){
                        constructFairy_2(pos, GameDriver.randomDouble() * 360);
                    }
                case 3:
                    constructFairy_2(pos, 50);
                    constructFairy_2(pos, -50);
                case 2:
                    constructFairy_2(pos, 0);
                    break;
            }
        }
        private static void constructFairy_2(Vector pos, double angle){
            double baseSpeed = 2;
            double initAngleChange = 2;
            double baseAngleChangeCompound = .89;

            double speedVariation = 2;
            double angleChangeCompoundVariation = .01;

            double speed = baseSpeed + (GameDriver.randomDouble() * speedVariation);
            double angleChangeCompound = baseAngleChangeCompound + (GameDriver.randomDouble() * angleChangeCompoundVariation);

            constructHomingProjectile(pos, "sharp", "green", new Vector(MiscUtil.fixAngle(angle), speed), initAngleChange, angleChangeCompound);
        }
        private static void spawnFairy_3(Entity fairy){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", fairy.getEntityID());
            if(pComp == null){
                return;
            }
            Vector fPos = pComp.getPrevPosition();
                                                                   //slow down or speed up growth
            double angle = MiscUtil.fixAngle((GameDriver.getTick() * 1.2));
            double speedSlow = 2.1;
            switch(GameDriver.getCurrentDifficulty()){
                case 4:
                    double speedFast = speedSlow + 1;
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle + 90, speedFast));
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle - 90, speedFast));
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle + 180, speedFast));
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle, speedFast));
                case 3:
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle + 90, speedSlow));
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle - 90, speedSlow));
                case 2:
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle + 180, speedSlow));
                case 1:
                    constructStraightProjectile(fPos, "small", "white", new Vector(angle, speedSlow));
                    break;
            }
        }
        private static void spawnFairy_4(Entity fairy){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", fairy.getEntityID());
            if(pComp == null){
                return;
            }
            Vector fPos = pComp.getPrevPosition();
            //slow down or speed up growth
            double speed = 2.5;
            //  bpr = bullets per ring
            int bpr = GameDriver.getCurrentDifficulty() * 4;
            double angle = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double aAdd = 360d/bpr;
            for(int i = 0; i < bpr; i++){
                constructFairy_4(fPos, new Vector(angle, speed), true);
                angle += aAdd;
            }
            angle = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < bpr; i++){
                constructFairy_4(fPos, new Vector(angle, speed), false);
                angle += aAdd;
            }
        }
        private static void constructFairy_4(Vector pos, Vector iPolar, boolean dir){
            double iAngleChange = 2;
            //angle change compound
            double acc = .99;
            if(dir){
                constructDecayingCurveProjectile(pos, "sharp", "green", iPolar, iAngleChange, acc);
            }
            else{
                constructDecayingCurveProjectile(pos, "sharp", "yellow", iPolar, -iAngleChange, acc);
            }
        }
        private static void spawnWispDeath_1(Entity pos){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", pos.getEntityID());
            if(pComp == null){
                return;
            }
            Vector wPos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();

            double angle = Vector.getAngle(wPos, pPos);
            double slowSpeed = 2;
            switch(GameDriver.getCurrentDifficulty()){
                case 4:
                    constructStraightProjectile(wPos, "sharp", "white", new Vector(angle, slowSpeed));
                case 3:
                    constructStraightProjectile(wPos, "sharp", "white", new Vector(angle, slowSpeed + 1.75));
                case 2:
                    constructStraightProjectile(wPos, "sharp", "white", new Vector(angle, slowSpeed + 3.5));
                    break;
            }
        }
        private static void spawnWispDeath_2(Entity pos){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", pos.getEntityID());
            if(pComp == null){
                return;
            }
            Vector wPos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();

            double angle = Vector.getAngle(wPos, pPos);
            double angle_ = 20;
            double speed = 4.5;
            switch(GameDriver.getCurrentDifficulty()){
                case 4:
                    constructStraightProjectile(wPos, "sharp", "teal", new Vector(angle + (2 * angle_), speed));
                    constructStraightProjectile(wPos, "sharp", "teal", new Vector(angle - (2 * angle_), speed));
                case 3:
                    constructStraightProjectile(wPos, "sharp", "teal", new Vector(angle + angle_, speed));
                    constructStraightProjectile(wPos, "sharp", "teal", new Vector(angle - angle_, speed));
                case 2:
                    constructStraightProjectile(wPos, "sharp", "teal", new Vector(angle, speed));
                    break;
            }
        }
        //boss 1
        private static void spawnB1_1_1(Entity boss) {
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", boss.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            double spawns = 0;
            switch (GameDriver.getCurrentDifficulty()) {
                case 1:
                    spawns = 20;
                    break;
                case 2:
                    spawns = 40;
                    break;
                case 3:
                    spawns = 60;
                    break;
                case 4:
                    spawns = 80;
                    break;
            }
            double angleD = 360d/spawns;
            double angle = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double iac = MiscUtil.rangeRandom(-3, 3, GameDriver.randomDouble());
            double acc = MiscUtil.rangeRandom(.98, .995, GameDriver.randomDouble());
            for(int i = 0; i < spawns; i++){
                double speed = MiscUtil.rangeRandom(1, 4, GameDriver.randomDouble());
                Vector iPol = new Vector(angle, speed);
                angle += angleD;
                constructDecayingCurveProjectile(pos, "sharp", "orange", iPol, iac, acc);
            }
        }
        private static void spawnB1_1_2(Entity boss){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", boss.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            double spawns = 0;
            double layers = 0;
            switch (GameDriver.getCurrentDifficulty()) {
                case 1:
                    layers = 1;
                    spawns = 15;
                    break;
                case 2:
                    layers = 1;
                    spawns = 20;
                    break;
                case 3:
                    layers = 2;
                    spawns = 25;
                    break;
                case 4:
                    layers = 3;
                    spawns = 30;
                    break;
            }
            double baseAngle = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double innerSpeed = 2;
            double speedInc = 1;
            for(int i = 0; i < layers; i++){
                double speed = innerSpeed + (speedInc * i);
                double angleD = 360d/spawns;
                double angle = baseAngle;
                for(int j = 0; j < spawns; j++){
                    constructStraightProjectile(pos, "medium", "yellow", new Vector(angle, speed));
                    angle += angleD;
                }
            }

        }
        private static void spawnB1_1_3(Entity boss){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", boss.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            int extraLanes = 0;
            int spawns = 0;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 5;
                    break;
                case 2:
                    spawns = 7;
                    break;
                case 3:
                    extraLanes = 1;
                    spawns = 9;
                    break;
                case 4:
                    extraLanes = 2;
                    spawns = 11;
                    break;
            }
            double angle = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double laneAngleV = 16;
            double baseSpeed = 1;
            double speedAdd = .65;
            for(int i = 0; i < spawns; i++){
                constructStraightProjectile(pos, "small", "white", new Vector(angle, baseSpeed + (speedAdd * i)));
            }
            for(int i = 0; i < extraLanes; i++){
                for(int j = 0; j < spawns; j++){
                    constructStraightProjectile(pos, "small", "white", new Vector(angle + (laneAngleV * (i + 1)), baseSpeed + (speedAdd * j)));
                    constructStraightProjectile(pos, "small", "white", new Vector(angle - (laneAngleV * (i + 1)), baseSpeed + (speedAdd * j)));
                }
            }
        }
        private static void spawnB1_2(){
            int length = GameDriver.getGameWidth() + GameDriver.getGameHeight();
            double r = MiscUtil.rangeRandom(0, length, GameDriver.randomDouble());
            Vector spawn;
            if(r > GameDriver.getGameHeight()){
                spawn = EnemySpawns.spawnVTop(r - GameDriver.getGameHeight());
            }
            else{
                spawn = EnemySpawns.spawnVLeft(r);
            }

            double angle = 45;
            double ad = 10;
            angle += MiscUtil.rangeRandom(-ad, ad, GameDriver.randomDouble());

            double speed = 2.5;
            double sd = 1;
            speed += MiscUtil.rangeRandom(-sd, sd, GameDriver.randomDouble());

            constructStraightProjectile(spawn, "sharp", "green", new Vector(angle, speed));
        }
        private static void spawnB1_3(Entity boss){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", boss.getEntityID());
            if (pComp == null) {
                return;
            }
            double speed = 3;
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 60;
                    break;
                case 2:
                    freq = 50;
                    break;
                case 3:
                    freq = 40;
                    break;
                case 4:
                    freq = 30;
                    break;
                default:
                    freq = 60;
            }
            int tick = (int)MiscUtil.rangeRandom(0, freq-1, GameDriver.randomDouble());
            Vector pos = pComp.getPrevPosition();
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate("medium"), Comps.ccEP("medium"), Comps.epSprite("medium", "red"),
                    Comps.moveVel(new Vector(0, speed)), new Component_Spawning(new SpawnUnit(freq, true, new SISingleTick(tick,"b1_3_sub")))});
        }
        private static void spawnB1_3_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            double speed = .85;
            double bAng = 90;
            double angle = bAng + MiscUtil.rangeRandom(-20, 40, GameDriver.randomDouble());
            constructStraightProjectile(pos, "sharp", "red", new Vector(angle, speed));
            constructStraightProjectile(pos, "sharp", "red", new Vector(-angle, speed));
        }
        //stage 2
        private static void spawnWispDeath_3(Entity e){
            //away cross
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();

            double pAngle = Vector.getAngle(pos, pPos);
            double bAngle = MiscUtil.fixAngle(pAngle + 45);
            double bSpeed = 1.2;

            for(int i = 0; i < 360; i+=90){
                double angle = bAngle + i;
                for(int j = 1; j <= GameDriver.getCurrentDifficulty(); j++){
                    constructStraightProjectile(pos, "small", "white", new Vector(angle, bSpeed * j));
                }
            }
        }
        private static void spawnWispDeath_4(Entity e){
            //5 arc
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();
            double bAngle = Vector.getAngle(pos, pPos);
            double angVar = 20;
            double speed = 2.7;
            constructStraightProjectile(pos, "sharp", "teal", new Vector(bAngle, speed));
            if(GameDriver.getCurrentDifficulty() >= 2){
                constructStraightProjectile(pos, "sharp", "teal", new Vector(bAngle - angVar, speed));
                constructStraightProjectile(pos, "sharp", "teal", new Vector(bAngle + angVar, speed));
                if(GameDriver.getCurrentDifficulty() == 4){
                    constructStraightProjectile(pos, "sharp", "teal", new Vector(bAngle - (2*angVar), speed));
                    constructStraightProjectile(pos, "sharp", "teal", new Vector(bAngle + (2*angVar), speed));
                }
            }
        }
        private static void spawnWisp_1(Entity e){
            //spirals
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();

            double degSec = 60;
            double degTick = degSec/60;
            double bAng = (GameDriver.getTick() * degTick)%360;
            double bSpeed;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                case 3:
                    bSpeed = 2;
                    break;
                case 4:
                    bSpeed = 3;
                    break;
                default:
                    bSpeed = 3;
                    break;
            }
            double backSpeed = .3;

            for(int i = 0; i < 360; i += 360/3){
                double ang = MiscUtil.fixAngle(bAng + i);
                double ang2 = MiscUtil.fixAngle(ang - 3.3);

                switch(GameDriver.getCurrentDifficulty()) {
                    case 4:
                    case 3:
                        constructStraightProjectile(pos, "sharp", "blue", new Vector(ang2, (bSpeed - backSpeed) * 1.5));
                        constructStraightProjectile(pos, "sharp", "blue", new Vector(ang, bSpeed * 1.5));
                    case 2:
                        constructStraightProjectile(pos, "sharp", "blue", new Vector(ang2, (bSpeed - backSpeed) * .5));
                        constructStraightProjectile(pos, "sharp", "blue", new Vector(ang, bSpeed * .5));
                    case 1:
                        constructStraightProjectile(pos, "sharp", "blue", new Vector(ang2, bSpeed - backSpeed));
                        constructStraightProjectile(pos, "sharp", "blue", new Vector(ang, bSpeed));
                        break;
                }
            }
        }
        private static void spawnBird_1(Entity e){
            //line
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();

            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 1;
                    break;
                case 2:
                    spawns = 2;
                    break;
                case 3:
                    spawns = 3;
                    break;
                case 4:
                    spawns = 4;
                    break;
                default:
                    spawns = 2;
                    break;
            }

            double ang = Vector.getAngle(pos, pPos);
            double bSpeed =  1.5;
            double speedVar = .75;
            for(int i = 0; i < spawns; i++){
                constructStraightProjectile(pos, "small", "blue", new Vector(ang, bSpeed + (i * speedVar)));
            }
        }
        private static void spawnBird_2(Entity e){
            //random
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            for(int i = 0; i < GameDriver.getCurrentDifficulty() * 2; i++){
                double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(2, 3, GameDriver.randomDouble());
                constructStraightProjectile(pos, "medium", "red", new Vector(ang, speed));
            }
        }
        private static void spawnBird_3(Entity e){
            //arc
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();
            double bAngle = Vector.getAngle(pos, pPos);
            double angVar = 30;
            double speed = 2.7;
            constructStraightProjectile(pos, "sharp", "blue", new Vector(bAngle, speed));
            if(GameDriver.getCurrentDifficulty() >= 2){
                constructStraightProjectile(pos, "sharp", "blue", new Vector(bAngle - angVar, speed));
                constructStraightProjectile(pos, "sharp", "blue", new Vector(bAngle + angVar, speed));
                if(GameDriver.getCurrentDifficulty() >= 3){
                    constructStraightProjectile(pos, "sharp", "blue", new Vector(bAngle - (2*angVar), speed));
                    constructStraightProjectile(pos, "sharp", "blue", new Vector(bAngle + (2*angVar), speed));
                }
            }
        }
        private static void spawnBird_4(Entity e){
            //bifurcate
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();
            double bAngle = Vector.getAngle(pos, pPos);
            double angVar = 20;
            double bSpeed = 1.5;
            double speedVar = 1;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 1;
                    break;
                case 2:
                    spawns = 2;
                    break;
                case 3:
                case 4:
                    spawns = 3;
                    break;
                default:
                    spawns = 2;
                    break;
            }
            for(int i = 0; i < spawns; i++){
                constructStraightProjectile(pos, "small", "red", new Vector(bAngle + angVar, bSpeed + (speedVar * i)));
                constructStraightProjectile(pos, "small", "red", new Vector(bAngle - angVar, bSpeed + (speedVar * i)));
            }
        }
        private static void spawnWisp_2(Entity e){
            //another spiral
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();

            double degSec = 60;
            double degTick = degSec/60;
            double bAng = (GameDriver.getTick() * degTick)%360;
            double bSpeed;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                case 3:
                    bSpeed = 1.5;
                    break;
                case 4:
                    bSpeed = 2;
                    break;
                default:
                    bSpeed = 1.5;
                    break;
            }
            double altSpeed = 2;

            for(int i = 0; i < 360; i += 360/3){
                double ang = MiscUtil.fixAngle(bAng + i);
                double ang2 = MiscUtil.fixAngle(ang + 45);

                switch(GameDriver.getCurrentDifficulty()) {
                    case 4:
                    case 3:
                        constructStraightProjectile(pos, "sharp", "red", new Vector(ang, bSpeed * 1.5));
                    case 2:
                        constructStraightProjectile(pos, "sharp", "red", new Vector(ang, bSpeed * .5));
                    case 1:
                        constructStraightProjectile(pos, "sharp", "red", new Vector(ang2, bSpeed + altSpeed));
                        constructStraightProjectile(pos, "sharp", "red", new Vector(ang, bSpeed));
                        break;
                }
            }
        }
        private static void spawnWisp_3(Entity e){
            //slow/fast
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            if(pComp == null){
                return;
            }
            Vector pos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();

            double degSec = 164;
            double degTick = degSec/60;
            double bAng = MiscUtil.fixAngle((GameDriver.getTick() * degTick)%360 + Vector.getAngle(pos, pPos));
            double speedFast = 4;
            double speedSlow = 1.2;

            for(int i = 0; i < 360; i += 360/3){
                double ang = MiscUtil.fixAngle(bAng + i);
                double ang2 = MiscUtil.fixAngle(ang + 45);

                constructStraightProjectile(pos, "sharp", "teal", new Vector(ang, speedFast));
//                constructStraightProjectile(pos, "sharp", "teal", new Vector(ang2, speedFast));
                constructStraightProjectile(pos, "sharp", "orange", new Vector(ang2, speedSlow));
            }
        }
        //boss 2
        private static void spawnB2_1(Entity boss){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", boss.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector bPos = pComp.getPrevPosition();

            double dist = 60;
            double ang2Off = 30;

            double speedMulti = 1.05;
            double speedMed = 3.5 * speedMulti;
            double speedSmall = 2.78 * speedMulti;

            double degSec1 = 218 * (6d/8);
            double degSec2 = -134 * (6d/8);
            double degTick1 = degSec1/60;
            double degTick2 = degSec2/60;

            long seed = GameDriver.getSeed() - 8479L;
            Random r = new Random(seed);
            double bAng1 = MiscUtil.fixAngle(r.nextDouble() + GameDriver.getTick() * degTick1);
            double bAng2 = MiscUtil.fixAngle(r.nextDouble() + GameDriver.getTick() * degTick2);
            for(int i = 0; i < 360; i += 120){
                double ang = MiscUtil.fixAngle(bAng1 + i);
                Vector pos = Vector.add(bPos, Vector.toVelocity(new Vector(ang, dist)));

                for(int j = 0; j < 360; j += 120){
                    double ang1 = MiscUtil.fixAngle(bAng2 + j + (i/2));
                    double ang2 = MiscUtil.fixAngle(ang1 + ang2Off);

                    constructStraightProjectile(pos, "medium", "blue", new Vector(ang1, speedMed));
                    constructStraightProjectile(pos, "small", "teal", new Vector(ang2, speedSmall));
                }
            }
        }
        private static void spawnB2_2(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector bPos = pComp.getPrevPosition();

            double dist = 20;
            double angShift = 3;
            double speed = 4.5;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            int num;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                    num = 8;
                    break;
                default:
                    num = 12;
                    break;
            }
            for(double i = 0; i < 360; i+= 360d/num){
                double ang = MiscUtil.fixAngle(bAng + i);
                Vector pos = Vector.add(bPos, Vector.toVelocity(new Vector(ang, dist)));
                constructB2_2(pos, new Vector(ang, speed), angShift, b);
            }
        }
        private static void constructB2_2(Vector pos, Vector polar, double angShift, boolean b){
            String color;
            if(b){
                angShift *= -1;
                color = "blue";
            }
            else{
                color = "green";
            }
            //true 1, false 2

            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 25;
                    break;
                case 2:
                    freq = 21;
                    break;
                case 3:
                    freq = 19;
                    break;
                case 4:
                default:
                    freq = 16;
                    break;
            }
            String spawnID;
            if(b){
                spawnID = "b2_2_1_sub";
            }else{
                spawnID = "b2_2_2_sub";
            }

            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(freq - 5, spawnID));
            Component_Spawning sComp = new Component_Spawning(su);

            Component_Movement mComp = new Component_Movement(new MoveModeAddAngleChange("angle_change", polar.deepClone(), angShift));

            //behavior comp -> timer, then curve the opposite direction
            ArrayList<Behavior> bs = new ArrayList<>();
            int time = (int)(360/(Math.abs(angShift)));
            bs.add(new BehaviorVector("timer", new Vector(time)));
            bs.add(new BehaviorMM("set_mm", new MoveModeAddAngleChange("angle_change_compounding",
                    Vector.add(polar.deepClone(), new Vector(0, 2)), -angShift * 2, .96)));
            Component_Behavior bComp = new Component_Behavior(new Routine("routine", bs, false));

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP("large"), Comps.epSprite("large", color),
                mComp, bComp, sComp});
        }
        private static void spawnB2_2_sub(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            if(pos.getB() >= GameDriver.getGameHeight() || pos.getA() <= 0 || pos.getA() >= GameDriver.getGameWidth()){
                return;
            }

            double speed = 3.5;
            String color;
            double ang;
            double inc = .005;
            if(b){
                color = "blue";
//                ang = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
                ang = Vector.getAngle(pos, new Vector(GameDriver.getGameWidth()/2, GameDriver.getGameHeight()/2));
                constructStraightProjectileSlowStart(pos, "medium", color, new Vector(ang, speed), inc);
            }
            else{
                color = "green";
                ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                constructStraightProjectileSlowStart(pos, "medium", color, new Vector(ang, speed), inc);
            }
        }
        private static void spawnB2_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector bPos = pComp.getPrevPosition();

            double dist = 60;

            double speed = 2;

            double degSec1 = 72;
            double degSec2 = -88;
            double degTick1 = degSec1/60;
            double degTick2 = degSec2/60;

            long seed = GameDriver.getSeed() - 84729L;
            Random r = new Random(seed);
            double bAng1 = MiscUtil.fixAngle(r.nextDouble() + GameDriver.getTick() * degTick1);
            double bAng2 = MiscUtil.fixAngle(r.nextDouble() + GameDriver.getTick() * degTick2);
            for(int i = 0; i < 360; i += 120){
                double ang = MiscUtil.fixAngle(bAng1 + i);
                Vector pos = Vector.add(bPos, Vector.toVelocity(new Vector(ang, dist)));

                for(int j = 0; j < 360; j += 120){
                    double ang1 = MiscUtil.fixAngle(bAng2 + j + (i/2));

                    constructStraightProjectile(pos, "sharp", "blue", new Vector(ang1, speed));
                }
            }
        }
        private static void spawnB2_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector bPos = pComp.getPrevPosition();
            Vector pPos = GameDriver.getCurrentPlayerPosition();

            double bAng = Vector.getAngle(bPos, pPos);
            //spawn double helix
            double angDiff = 7;
            double hAng1 = bAng + angDiff;
            double hAng2 = bAng - angDiff;
            double angShift = 25;
            double acSpeed = 19;
            double polSpeed = 6.6;
            constructB2_4(bPos, new Vector(hAng1, polSpeed), angShift, acSpeed, true);
            constructB2_4(bPos, new Vector(hAng2, polSpeed), angShift, acSpeed, false);
//            constructB2_4(bPos, new Vector(bAng, polSpeed), angShift, acSpeed, false);
//            constructB2_4(bPos, new Vector(bAng, polSpeed), angShift, acSpeed, true);
            //spawn fan
            //nofirenag = both sides cannot fire from player
            double noFireAng = 50;
            double spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 10;
                    break;
                case 2:
                    spawns = 14;
                    break;
                case 3:
                    spawns = 17;
                    break;
                case 4:
                default:
                    spawns = 20;
                    break;
            }
            angShift = (360d - (2 * noFireAng))/spawns;
            double fanAng = bAng + noFireAng;
            //somehow needs <=
            for(int i = 0; i <= spawns; i++){
                for(int j = 0; j < 3; j++){
                    double speed = 2 + (j * .65);
                    constructSingleBounceProjectile(bPos, "sharp", "teal", new Vector(fanAng, speed));
                }
                fanAng += angShift;
            }

        }
        private static void constructB2_4(Vector pos, Vector polar, double angShift, double acSpeed, boolean b){
            String color = "blue";
            if(b){
                angShift *= -1;
            }
            //true 1, false 2
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                    freq = 3;
                    break;
                case 3:
                    freq = 2;
                    break;
                case 4:
                default:
                    freq = 1;
                    break;
            }
            String spawnID;
            if(b){//coming back to this for b7_10... isn't this hilarious? //that being said this isn't a true orbit, just AC
                spawnID = "b2_4_sub";
            }else{
                spawnID = "b2_4_sub";
            }

            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(freq - 1, spawnID));
            Component_Spawning sComp = new Component_Spawning(su);

            Vector acPol;
            if(b){
                acPol = new Vector(MiscUtil.fixAngle(polar.getA() - 90), acSpeed);
            }
            else{
                acPol = new Vector(MiscUtil.fixAngle(polar.getA() + 90), acSpeed);
            }

            ArrayList<MoveModeAdd> addAL = new ArrayList<>();
            addAL.add(new MoveModeAddAngleChange("angle_change", acPol, angShift));
            addAL.add(new MoveModeAddVector("polar", polar.deepClone()));
            Component_Movement mComp = new Component_Movement(addAL);

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP("large"), Comps.epSprite("large", color),
                    mComp, sComp});
        }
        private static void spawnB2_4_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            double degSec = 60;
            double degTick = degSec/60;
            double ang = MiscUtil.fixAngle((GameDriver.getTick() * degTick) + (pos.getA() + pos.getB()));
            double speed = 2;
            double inc = .004;
            constructStraightProjectileSlowStart(pos, "sharp", "purple", new Vector(ang, speed), inc);
        }
        //stage 3
        private static void spawnAuto_1(Entity e){
            //big purp hex walls
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            double angModifier = -41;
            double degTick = 97d/60;
            double bAng = MiscUtil.fixAngle((GameDriver.getTick() * degTick) + angModifier);
            double bSpeed = 2;
            double speedV = 1;
            for(double i = 0; i < 360; i += 360d/6){
                double ang = MiscUtil.fixAngle(bAng + i);
                for(int j = 0; j < GameDriver.getCurrentDifficulty(); j++){
                    double speed = bSpeed + (speedV * j);
                    constructStraightProjectile(pos, "large", "purple", new Vector(ang, speed));
                }
            }
        }
        private static void spawnAuto_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double bSpeed = 3.5;
            double speedV = 2;

            //in deg +-
            double spread[] = new double[]{2, 4, 8, 16};
            for(int j = 0; j < GameDriver.getCurrentDifficulty(); j++) {
                double speed = MiscUtil.rangeRandom(bSpeed, bSpeed + speedV, GameDriver.randomDouble());
                double angle = MiscUtil.rangeRandom(bAng - spread[j], bAng + spread[j], GameDriver.randomDouble());
                constructStraightProjectile(pos, "small", "red", new Vector(angle, speed));
            }
        }
        private static void spawnAuto_3(Entity e){
            //single to hex
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            if(pos.getA() <= 0 || pos.getA() >= GameDriver.getGameWidth()){
                return;
            }
            double ang = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            constructAuto_3(pos, ang);
        }
        private static void constructAuto_3(Vector pos, double ang){
            double iSpeed = 7;
            double speedMulti = .96;
            int time = 60;

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
            bList.add(new Behavior("deactivate"));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);

            Component_Movement mComp = Comps.movePol(new Vector(ang, iSpeed));
            mComp.addMode(new MoveModeTransVector("speed_compounding", new Vector(1, speedMulti)));

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP("medium"), Comps.epSprite("medium", "blue"),
                mComp, bComp, Comps.dSpawn("auto_3_sub")});
        }
        private static void spawnAuto_3_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            double bAng = 30;
            for(int i = 0; i < 360; i += 60){
                double ang = MiscUtil.fixAngle(bAng + i);
                constructAuto_3_sub(pos, ang);
            }
        }
        private static void constructAuto_3_sub(Vector pos, double ang){
            double iSpeed = 3;
            double speedMulti = .97;
            double endSpeed = 4;
            int time = 60;

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
            bList.add(new BehaviorDouble("set_polar_to_player", endSpeed));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);

            Component_Movement mComp = Comps.movePol(new Vector(ang, iSpeed));
            mComp.addMode(new MoveModeTransVector("speed_compounding", new Vector(1, speedMulti)));

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP("medium"), Comps.epSprite("medium", "blue"),
                    mComp, bComp});
        }
        private static void spawnAuto_4(Entity e){
            //walls of 3
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            if(pos.getA() <= 20 || pos.getA() >= GameDriver.getGameWidth() - 20){
                return;
            }

            //large random number
            double degTick = 12375.198d;
            double angInc = 2.5;

            double bSpeed = 3.54;
            double speedInc = .05;

            long seed = GameDriver.getSeed() - 89;
            Random r = new Random(seed);
            double rAngle = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle((GameDriver.getTick() * degTick) + rAngle);

            for(int i = 0; i < 6; i++){
                double bAng_ = MiscUtil.fixAngle(bAng + (i * (360d/6)));
                for(int j = 0; j < 3; j++){
                    double ang  = bAng_ + (j * angInc);
                    double speed = bSpeed + (j * speedInc);

                    constructStraightProjectile(pos, "medium", "orange", new Vector(ang, speed));
                }
            }
        }
        private static void spawnAuto_5(Entity e){
            //aimed hex
            //use vectors
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            if(pos.getA() <= 20 || pos.getA() >= GameDriver.getGameWidth() - 20){
                return;
            }

            double centerSpeed = 3;
            double hexOut = 200;
            double hexDist = 75;

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            Vector hexPos = Vector.add(pos, Vector.toVelocity(new Vector(bAng, hexOut)));

            for(int i = 0; i < 360; i += 360/6){
                double hexAng = MiscUtil.fixAngle(bAng + i);
                Vector cornerPos = Vector.add(hexPos, Vector.toVelocity(new Vector(hexAng, hexDist)));
                double speed = (centerSpeed/hexOut) * Vector.getDistance(pos, cornerPos);
                double ang = Vector.getAngle(pos, cornerPos);
                constructStraightProjectile(pos, "medium", "blue", new Vector(ang, speed));
            }
        }
        private static void spawnAuto_6(Entity e){
            //walls of 3
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            if(pos.getA() <= 20 || pos.getA() >= GameDriver.getGameWidth() - 20){
                return;
            }

            //large random number
            double degTick = 12375.198d;
            double angInc = 2.5;

            double bSpeed = 3.54;
            double speedInc = .05;

            long seed = GameDriver.getSeed() - 89134;
            Random r = new Random(seed);
            double rAngle = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle((GameDriver.getTick() * degTick) + rAngle);

            for(int i = 0; i < 6; i++){
                double bAng_ = MiscUtil.fixAngle(bAng + (i * (360d/6)));
                for(int j = 0; j < 3; j++){
                    double ang  = bAng_ - (j * angInc);
                    double speed = bSpeed + (j * speedInc);

                    constructStraightProjectile(pos, "medium", "orange", new Vector(ang, speed));
                }
            }
        }
        private static void spawnCrystal_1(Entity e){
            constructYellowCrystal(e, true);
        }
        private static void spawnCrystal_2(Entity e){
            constructYellowCrystal(e, false);
        }
        private static void constructYellowCrystal(Entity e, boolean left){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            Random r = new Random(GameDriver.getSeed() - 2936);
            r.nextDouble();

            double bSpeed1 = 4.22;
            double bSpeed2 = bSpeed1 - .91;
            double speedInc = .13;
            double degTick = 42.64d/46;
            double bAng_ = MiscUtil.fixAngle(MiscUtil.rangeRandom(0, 360, r.nextDouble()) + (GameDriver.getTick() * degTick));
            if(left){
                bAng_ = - bAng_;
            }
            double angInc = 3;
            for(int i = 0; i < 6; i++){
                double bAng = bAng_ + (i * 360/6);
                double bAng2 = bAng + 30;
                switch(GameDriver.getCurrentDifficulty()){
                    case 4:
                        constructYellowCrystalHelper(pos, bAng2, angInc, 2, bSpeed2, speedInc, -2);
                        constructYellowCrystalHelper(pos, bAng2, angInc, -2, bSpeed2, speedInc, -2);
                        constructYellowCrystalHelper(pos, bAng2, angInc, 1, bSpeed2, speedInc, -1);
                        constructYellowCrystalHelper(pos, bAng2, angInc, -1, bSpeed2, speedInc, -1);
                    case 3:
                        constructYellowCrystalHelper(pos, bAng2, angInc, 0, bSpeed2, speedInc, 0);
                    case 2:
                        constructYellowCrystalHelper(pos, bAng, angInc, 2, bSpeed1, speedInc, -2);
                        constructYellowCrystalHelper(pos, bAng, angInc, -2, bSpeed1, speedInc, -2);
                    case 1:
                        constructYellowCrystalHelper(pos, bAng, angInc, 1, bSpeed1, speedInc, -1);
                        constructYellowCrystalHelper(pos, bAng, angInc, -1, bSpeed1, speedInc, -1);
                        constructYellowCrystalHelper(pos, bAng, angInc, 0, bSpeed1, speedInc, 0);
                }
            }
        }
        private static void constructYellowCrystalHelper(Vector pos, double bAng, double angInc, int multi, double bSpeed, double speedInc, int multi2){
            constructStraightProjectile(pos, "medium", "red", new Vector(bAng + (angInc * multi), bSpeed + (speedInc * multi2)));
        }
        private static void spawnCrystal_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();

            double speed = 4.123;
            double degTick = (158d/60) % 60;

            Random r = new Random(GameDriver.getSeed() - 21936);
            r.nextDouble();
            double bAng = MiscUtil.fixAngle(MiscUtil.rangeRandom(0, 360, r.nextDouble()) + (GameDriver.getTick() * degTick));

            for(int i = 0; i < 6; i++){
                double ang = bAng + (i * 360/6);
                constructCrystal_3(pos, new Vector(ang, speed));
                if(GameDriver.getCurrentDifficulty() >= 3){
                    constructCrystal_3(pos, new Vector(-ang, speed));
                }
            }
        }
        private static void constructCrystal_3(Vector pos, Vector polar){
//            int delay = ((int)(((double)(GameDriver.getTick()/7)) * 11)) % 80;
            int delay = 20;
//            int freq = 80;
//            SpawnUnit su = new SpawnUnit(freq, false, new SISingleTick(delay, "crystal_3_sub"));
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP("medium"), Comps.epSprite("medium", "blue"), Comps.movePol(polar),
//                    new Component_Spawning(su)});
                    Comps.singleSpawnDelay(delay, "crystal_3_sub")});
        }
        private static void spawnCrystal_3_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            double pAng = Vector.getAngle(pComp.getOldPosition(), pos);
            double speed = 2;
            constructStraightProjectile(pos, "medium", "purple", new Vector(pAng + 90, speed));
            constructStraightProjectile(pos, "medium", "purple", new Vector(pAng - 90, speed));
        }
        private static void spawnCrystalDeath_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double bSpeed = 3.8;
            double speedInc = .8;
            int mainSpawns = 0;
            int mainDensity = 0;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    mainSpawns = 8;
                    mainDensity = 1;
                    break;
                case 2:
                    mainSpawns = 12;
                    mainDensity = 1;
                    break;
                case 3:
                    mainSpawns = 12;
                    mainDensity = 2;
                    break;
                case 4:
                    mainSpawns = 16;
                    mainDensity = 3;
                    break;
            }
            for(int i = 0; i < mainSpawns; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/mainSpawns)));
                for(int j = 0; j < mainDensity; j++){
                    double speed = bSpeed + (j * speedInc);
                    constructStraightProjectile(pos, "medium", "white", new Vector(ang, speed));
                }
                if(mainDensity - 1 > 0){
                    double ang2 = ang + (.5 * 360d/mainSpawns);
                    for(int j = 0; j < mainDensity - 1; j++){
                        double speed = bSpeed + ((j + .5) * speedInc);
                        constructStraightProjectile(pos, "medium", "white", new Vector(ang2, speed));
                    }
                }
            }
        }
        private static void spawnCrystalDeath_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            double speed = 1.5;
            double spawnMulti = 17;
            double spawns = GameDriver.getCurrentDifficulty() * spawnMulti;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < spawns; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/spawns)));
                constructStraightProjectile(pos, "medium", "white", new Vector(ang, speed));
            }
        }
        //boss 3
        private static void spawnB3_1_1(Entity e){
            //3-symmetry
            //slow moving -> first up to bat
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String m = "medium";
            String c = "red";
            double degTick = 95d/60;
            double speed1= 1.9;
            double speed2 = 1.35;
            int sym = 3;

            Random r = new Random(GameDriver.getSeed() + 124);
            r.nextDouble();

            double angR = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(angR + (GameDriver.getTick() * degTick));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructStraightProjectile(pos, m, c, new Vector(ang, speed1));
                if(GameDriver.getCurrentDifficulty() >= 3){
                    constructStraightProjectile(pos, m, c, new Vector(ang, speed2));
                }
            }
        }
        private static void spawnB3_1_2(Entity e){
            //4 sym
            //shock and awe
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String m = "medium";
            String c = "orange";
            double degTick = -119d/60;
            double bSpeed= 3.9;
            double speedInc = -.7;
            int sym = 4;

            Random r = new Random(GameDriver.getSeed() + 12124);
            r.nextDouble();

            double angR = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(angR + (GameDriver.getTick() * degTick));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < GameDriver.getCurrentDifficulty(); j++){
                    constructStraightProjectile(pos, m, c, new Vector(ang, bSpeed + (j * speedInc)));
                }
            }
        }
        private static void spawnB3_1_3(Entity e){
            //5 sym
            //slow
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String m = "medium";
            String c = "yellow";
            double degTick = 112d/60;
            double bSpeed= 2.4;
            int sym = 5;

            Random r = new Random(GameDriver.getSeed() + 12555224);
            r.nextDouble();

            double angR = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(angR + (GameDriver.getTick() * degTick));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructStraightProjectile(pos, m, c, new Vector(ang, bSpeed));
            }
        }
        private static void spawnB3_1_4(Entity e){
            //6 sym
            //cover
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String m = "medium";
            String c = "green";
            double degTick = -78.7d/60;
            double bSpeed= 4.51;
            double speedInc = -.713;
            int sym = 6;

            Random r = new Random(GameDriver.getSeed() + 14);
            r.nextDouble();

            double angR = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(angR + (GameDriver.getTick() * degTick));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < GameDriver.getCurrentDifficulty(); j++){
                    constructStraightProjectile(pos, m, c, new Vector(ang, bSpeed + (j * speedInc)));
                }
            }
        }
        private static void spawnB3_1_5(Entity e){
            //7 sym
            //faster
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String m = "medium";
            String c = "blue";
            double degTick = 58d/60;
            double speed1 = 5.45;
            double speed2 = 3.87;
            int sym = 7;

            Random r = new Random(GameDriver.getSeed() + 1124);
            r.nextDouble();

            double angR = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(angR + (GameDriver.getTick() * degTick));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructStraightProjectile(pos, m, c, new Vector(ang, speed1));
                if(GameDriver.getCurrentDifficulty() >= 3){
                    constructStraightProjectile(pos, m, c, new Vector(ang, speed2));
                }
            }
        }
        private static void spawnB3_1_6(Entity e){
            //8 sym
            //cool down
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String m = "medium";
            String c = "purple";
            double degTick = -84.25d/70;
            double speed1 = 2.59275;
            double speed2 = 2.25;
            int sym = 8;

            Random r = new Random(GameDriver.getSeed() + 155524);
            r.nextDouble();

            double angR = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(angR + (GameDriver.getTick() * degTick));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructStraightProjectile(pos, m, c, new Vector(ang, speed1));
                if(GameDriver.getCurrentDifficulty() >= 4){
                    constructStraightProjectile(pos, m, c, new Vector(ang, speed2));
                }
            }
        }
        private static void spawnB3_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            double maxSpeed = 20;
            double multi = .0125;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 20;
                    break;
                case 2:
                    spawns = 30;
                    break;
                case 3:
                    spawns = 40;
                    break;
                case 4:
                default:
                    spawns = 50;
                    break;
            }
            for(int i = 0; i < spawns; i++){
                double ang = (MiscUtil.rangeRandom(0, 360d/spawns, GameDriver.randomDouble())) + (i * (360d/spawns));
                constructStraightProjectileSlowStartGeometric(pos, "medium", "blue", new Vector(ang, maxSpeed), multi);
            }
        }
        private static void spawnB3_3_1(Entity e){
            //walls
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String t = "medium";
            String c = "red";
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 26;
                    break;
                case 2:
                    sym = 28;
                    break;
                case 3:
                    sym = 30;
                    break;
                case 4:
                default:
                    sym = 32;
                    break;
            }

            double speed = 3.1;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double iac = -4;
            double acc = .98;
            int time = 70;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        Comps.moveAngleChangeCompounding(new Vector(ang, speed), iac, acc), Comps.lifetimeDspawn(time, "b3_3_1_sub")});
            }
        }
        private static void spawnB3_3_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String t = "medium";
            String c = "orange";
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 11;
                    break;
                case 2:
                    sym = 12;
                    break;
                case 3:
                    sym = 13;
                    break;
                case 4:
                default:
                    sym = 15;
                    break;
            }

            double speed1 = 3.1;
            double speed2 = speed1 * .7;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition()) - 5;
            double angInc = (6.5 * 15)/sym;
            double speedComp = .99;

            int time = 70;
            for(int i = 0; i < sym; i++){
                double ang1 = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                double ang2 = ang1 + angInc;
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        new Component_Movement(new MoveModeAddVector("polar", new Vector(ang1, speed1)),
                                new MoveModeTransVector("speed_compounding", new Vector(1, speedComp))),
                        Comps.lifetimeDspawn(time, "b3_3_2_sub")});
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        new Component_Movement(new MoveModeAddVector("polar", new Vector(ang2, speed2)),
                                new MoveModeTransVector("speed_compounding", new Vector(1, speedComp))),
                        Comps.lifetimeDspawn(time, "b3_3_2_sub")});
            }
        }
        private static void spawnB3_3_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String t = "medium";
            String c = "yellow";
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                    sym = 10;
                    break;
                case 3:
                    sym = 11;
                    break;
                case 4:
                default:
                    sym = 12;
                    break;
            }

            double speed = 2.8;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            int time = 70;
            double ac = 2.3;
            double speedComp = .995;

            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                double ang2 = ang + ((i * 360d/sym)/2);
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        Comps.movePol(new Vector(ang, speed)), Comps.lifetimeDspawn(time, "b3_3_3_sub")});
                Component_Movement mComp2 = Comps.moveHoming(new Vector(ang2, speed), GameDriver.getCurrentPlayerPosition(), ac);
                mComp2.addMode(new MoveModeTransVector("speed_compounding", new Vector(1, speedComp)));
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        mComp2, Comps.lifetimeDspawn(time, "b3_3_3_sub")});
            }
        }
        private static void spawnB3_3_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String t = "medium";
            String c = "green";
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 26;
                    break;
                case 2:
                    sym = 28;
                    break;
                case 3:
                    sym = 30;
                    break;
                case 4:
                default:
                    sym = 32;
                    break;
            }

            double speed = 3.1;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double iac = 4;
            double acc = .98;
            int time = 70;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        Comps.moveAngleChangeCompounding(new Vector(ang, speed), iac, acc), Comps.lifetimeDspawn(time, "b3_3_4_sub")});
            }
        }
        private static void spawnB3_3_5(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String t = "medium";
            String c = "blue";
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 11;
                    break;
                case 2:
                    sym = 12;
                    break;
                case 3:
                    sym = 13;
                    break;
                case 4:
                default:
                    sym = 15;
                    break;
            }

            double speed1 = 3.1;
            double speed2 = speed1 * .7;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition()) + 5;
            double angInc = -(6.5 * 15)/sym;
            double speedComp = .99;

            int time = 70;
            for(int i = 0; i < sym; i++){
                double ang1 = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                double ang2 = ang1 + angInc;
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        new Component_Movement(new MoveModeAddVector("polar", new Vector(ang1, speed1)),
                                new MoveModeTransVector("speed_compounding", new Vector(1, speedComp))),
                        Comps.lifetimeDspawn(time, "b3_3_5_sub")});
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        new Component_Movement(new MoveModeAddVector("polar", new Vector(ang2, speed2)),
                                new MoveModeTransVector("speed_compounding", new Vector(1, speedComp))),
                        Comps.lifetimeDspawn(time, "b3_3_5_sub")});
            }
        }
        private static void spawnB3_3_6(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String t = "medium";
            String c = "purple";
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                    sym = 10;
                    break;
                case 3:
                    sym = 11;
                    break;
                case 4:
                default:
                    sym = 12;
                    break;
            }

            double speed = 2.8;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            int time = 70;
            double ac = 2.3;
            double speedComp = .995;

            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                double ang2 = ang + ((i * 360d/sym)/2);
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        Comps.movePol(new Vector(ang, speed)), Comps.lifetimeDspawn(time, "b3_3_6_sub")});
                Component_Movement mComp2 = Comps.moveHoming(new Vector(ang2, speed), GameDriver.getCurrentPlayerPosition(), ac);
                mComp2.addMode(new MoveModeTransVector("speed_compounding", new Vector(1, speedComp)));
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                        mComp2, Comps.lifetimeDspawn(time, "b3_3_6_sub")});
            }
        }
        private static void spawnB3_3_7(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = pComp.getPrevPosition();
            String t = "medium";
            String c = randomRainbowColor();
            double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = MiscUtil.rangeRandom(2.5, 6, GameDriver.randomDouble());
            constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            if(GameDriver.getCurrentDifficulty() == 3 && GameDriver.randomBoolean()){
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        //for the subs -> using deathspawn (dspawn uses the same position comp, so we can backcalculate polar)
        private static void spawnB3_3_sub(Entity e, int type){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector prevPos = getPos(e);
            Vector pos = getRealPos(e);
            String c = "";
            double speedInc = 0;
            int spawns = 0;
            double relativeAngle = 0;
            switch(type){
                case 1:
                    c = "red";
                    speedInc = .5;
                    spawns = 3 + (int)(1.3 * GameDriver.getCurrentDifficulty());
                    relativeAngle = 130;
                    break;
                case 2:
                    c = "orange";
                    speedInc = .65;
                    spawns = 4 + GameDriver.getCurrentDifficulty();
                    relativeAngle = 0;
                    break;
                case 3:
                    c = "yellow";
                    speedInc = .7;
                    spawns = 2 + GameDriver.getCurrentDifficulty();
                    relativeAngle = 0;
                    break;
                case 4:
                    c = "green";
                    speedInc = .5;
                    spawns = 3 + (int)(1.3 * GameDriver.getCurrentDifficulty());
                    relativeAngle = -130;
                    break;
                case 5:
                    c = "blue";
                    speedInc = .65;
                    spawns = 4 + GameDriver.getCurrentDifficulty();
                    relativeAngle = 0;
                    break;
                case 6:
                    c = "purple";
                    speedInc = .7;
                    spawns = 2 + GameDriver.getCurrentDifficulty();
                    relativeAngle = 0;
                    break;
            }
            Vector bPolar = Vector.toPolar(Vector.subtract(pos, prevPos));
            constructB3_3_sub(c, pos, bPolar, speedInc, spawns, relativeAngle);
        }
        private static void constructB3_3_sub(String c, Vector pos, Vector bPolar, double speedInc, int spawns, double relativeAngle){
            double ang = bPolar.getA() + relativeAngle;
            for(int i = 0; i < spawns; i++){
                constructStraightProjectile(pos, "medium", c, new Vector(ang, bPolar.getB() + (i * speedInc)));
            }
        }
        private static void spawnB3_4_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t= "sharp";
            String c = "green";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 4.5;
            double angOut = 23 - GameDriver.getCurrentDifficulty() * 2;

            constructStraightProjectile(pos, t, c, new Vector(bAng + angOut, speed));
            constructStraightProjectile(pos, t, c, new Vector(bAng - angOut, speed));
        }
        private static void spawnB3_4_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t= "medium";
            String c = "blue";
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 4;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 16;
                    break;
                case 2:
                    spawns = 20;
                    break;
                case 3:
                    spawns = 25;
                    break;
                case 4:
                default:
                    spawns = 32;
                    break;
            }
            for(int i = 0; i < spawns; i++){
                double ang = bAng + (i * (360d/spawns));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed * .8));
            }
        }
        private static void spawnB3_4_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t= "large";
            String c = "blue";
            double ang = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 2.5;
            int freq = 30;

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(freq * 3)));
            bList.add(new Behavior("clear_spawning"));
            Routine r = new Routine("routine", bList, false);
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c), Comps.movePol(new Vector(ang, speed)),
                    new Component_Spawning(new SpawnUnit(freq, true, new SISingleTick(freq/2, "b3_4_3_sub"))),
                    new Component_Behavior(r)});
        }
        private static void spawnB3_4_3_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t= "small";
            String c = "blue";
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 4.3;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 12;
                    break;
                case 2:
                    spawns = 16;
                    break;
                case 3:
                    spawns = 20;
                    break;
                case 4:
                default:
                    spawns = 24;
                    break;
            }
            for(int i = 0; i < spawns; i++){
                double ang = bAng + (i * (360d/spawns));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnB3_5_1(Entity e){
            B3_5_helper(e, 1);
        }
        private static void spawnB3_5_2(Entity e){
            B3_5_helper(e, 2);
        }
        private static void spawnB3_5_3(Entity e){
            B3_5_helper(e, 3);
        }
        private static void spawnB3_5_4(Entity e){
            B3_5_helper(e, 4);
        }
        private static void spawnB3_5_5(Entity e){
            B3_5_helper(e, 5);
        }
        private static void spawnB3_5_6(Entity e){
            B3_5_helper(e, 6);
        }
        private static void B3_5_helper(Entity e, int type){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "large";
            String c;
            switch(type){
                case 1:
                    c = "red";
                    break;
                case 2:
                    c = "orange";
                    break;
                case 3:
                    c = "yellow";
                    break;
                case 4:
                    c = "green";
                    break;
                case 5:
                    c = "blue";
                    break;
                case 6:
                default:
                    c = "purple";
                    break;
            }
            int baseTime = 120;
            boolean dir = type % 2 == 0;
            double angOutside = -10;
            double angRange = 180-angOutside;
            double bAng = 90 - angOutside;
            if(dir){
                bAng = -bAng;
            }
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 7;
                    break;
                case 2:
                    spawns = 9;
                    break;
                case 3:
                    spawns = 10;
                    break;
                case 4:
                default:
                    spawns = 11;
                    break;
            }
            spawns -= type/2;
            double angInc = angRange/(spawns - 1);
            if(!dir){
                angInc = -angInc;
            }
            double speedLow = 5;
            double speedHigh = 6;
            double speedComp = .97;
            double ang = bAng;
            for(int i = 0; i < spawns; i++){
//                double ang = bAng + MiscUtil.rangeRandom(-2, 2, GameDriver.randomDouble());
//                double bSpeed = MiscUtil.rangeRandom(speedLow, speedHigh, ((double)i)/spawns);
//                double speed = bSpeed + MiscUtil.rangeRandom(0, .5, GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(speedLow, speedHigh, ((double)i)/spawns);
                int time = (int)(baseTime + MiscUtil.rangeRandom(-20, 20, GameDriver.randomDouble()));
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c),
                        new Component_Movement(new MoveModeAddVector("polar", new Vector(ang, speed)),
                                new MoveModeTransVector("speed_compounding", new Vector(1, speedComp))),
                        Comps.lifetimeDspawn(time, "b3_5_" + type + "_sub")});
                ang += angInc;
            }
        }
        private static void spawnB3_5_sub(Entity e, int type){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c;
            int spawns = type + 2;
            double speed;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            switch(type){
                case 1:
                    c = "red";
                    speed = 2;
                    break;
                case 2:
                    c = "orange";
                    speed = 3.1;
                    break;
                case 3:
                    c = "yellow";
                    speed = 3.3;
                    break;
                case 4:
                    c = "green";
                    speed = 2.6;
                    break;
                case 5:
                    c = "blue";
                    speed = 2.3;
                    break;
                case 6:
                default:
                    c = "purple";
                    speed = 2.8;
                    break;
            }
            speed += MiscUtil.rangeRandom(0, .7, GameDriver.randomDouble());
            for(int i = 0; i < spawns; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/spawns));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }

        }
        //stage 4
        private static void spawnWispDeath_5(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "red";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speedInc = 1;
            double bSpeed = 3 + (GameDriver.getCurrentDifficulty() * (speedInc/2));
            double spawns = GameDriver.getCurrentDifficulty();
            for(int i = 0; i < 4; i++) {
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/4)));
                for (int j = 0; j < spawns; j++) {
                    double speed = bSpeed - (j * speedInc);
                    Vector polar = new Vector(ang, speed);
                    constructStraightProjectile(pos, t, c, polar);
                }
            }
        }
        private static void spawnWispDeath_6(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "red";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speedInc = 1;
            double bSpeed = 3 + (GameDriver.getCurrentDifficulty() * (speedInc/2));
            double spawns = GameDriver.getCurrentDifficulty();
            for(int i = 0; i < 8; i++) {
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/8)));
                for (int j = 0; j < spawns; j++) {
                    double speed = bSpeed - (j * speedInc);
                    Vector polar = new Vector(ang, speed);
                    constructStraightProjectile(pos, t, c, polar);
                }
            }
        }
        private static void spawnCloud_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "blue";

            double gAccel = MiscUtil.rangeRandom(.02, .04, GameDriver.randomDouble());
            double drag = MiscUtil.rangeRandom(.003, .006, GameDriver.randomDouble());

            double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = MiscUtil.rangeRandom(1.5, 2.5, GameDriver.randomDouble());

            constructGravityProjectile(pos, t, c, new Vector(ang, speed), gAccel, drag);
        }
        private static void spawnCloudDeath_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "red";

            double speed = 2;
            double gAccel = MiscUtil.rangeRandom(.02, .04, GameDriver.randomDouble());
            double drag = MiscUtil.rangeRandom(.003, .006, GameDriver.randomDouble());
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 8;
                    break;
                case 2:
                    sym = 10;
                    break;
                case 3:
                    sym = 12;
                    break;
                case 4:
                default:
                    sym = 16;
                    break;
            }
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                Vector polar = new Vector(ang, speed);
                constructGravityProjectile(pos, t, c, polar, gAccel, drag);
            }
        }
        private static void spawnCloud_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "teal";

            double gAccel = MiscUtil.rangeRandom(.02, .04, GameDriver.randomDouble());
            double drag = MiscUtil.rangeRandom(.003, .006, GameDriver.randomDouble());

            double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = MiscUtil.rangeRandom(1.5, 2.5, GameDriver.randomDouble());

            double angAim = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            constructGravityDirProjectile(pos, t, c, new Vector(ang, speed), angAim, gAccel, drag);
        }
        private static void spawnWater_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "red";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            //spawn line to player
            int spawns1;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns1 = 2;
                    break;
                case 2:
                    spawns1 = 4;
                    break;
                case 3:
                case 4:
                default:
                    spawns1 = 6;
                    break;
            }
            double botSpeed = 2.5;
            double topSpeed = 7;
            for(int i = 0; i < spawns1; i++){
                //inclusive bot, exlusive top
                double speed = MiscUtil.rangeRandom(botSpeed, topSpeed, ((double)i)/spawns1);
                Vector pol = new Vector(bAng, speed);
                constructStraightProjectile(pos, t, c, pol);
            }

//            //wings
//            int spawns2; //per side
//            switch(GameDriver.getCurrentDifficulty()){
//                case 1:
//                    spawns2 = 2;
//                    break;
//                case 2:
//                    spawns2 = 3;
//                    break;
//                case 3:
//                    spawns2 = 4;
//                    break;
//                case 4:
//                default:
//                    spawns2 = 6;
//                    break;
//            }
//            botSpeed = 3;
//            topSpeed = 5;
//            double endSpeed = 4;
//            double speedComp = .95;
//            int delay = 75;
//            double botAng = 25;
//            double angInc = 20;
//            for(int i = 0; i < spawns2; i++){
//                double ang = MiscUtil.fixAngle(botAng + (i * angInc) + bAng);
//                double speed = MiscUtil.rangeRandom(botSpeed, topSpeed, ((double)i)/spawns2);
//
//                ArrayList<Behavior> bList = new ArrayList<>();
//                bList.add(new BehaviorVector("timer", new Vector(delay)));
//                bList.add(new BehaviorDouble("set_polar_to_player", endSpeed));
//                Routine r = new Routine("routine", bList, false);
//                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c),
//                        new Component_Movement(new MoveModeAddVector("polar", new Vector(ang, speed)),
//                                new MoveModeTransVector("speed_compounding", new Vector(1, speedComp))),
//                        new Component_Behavior(r)});
//                ang = MiscUtil.fixAngle(-(botAng + (i * angInc)) + bAng);
//                bList = new ArrayList<>();
//                bList.add(new BehaviorVector("timer", new Vector(delay)));
//                bList.add(new BehaviorDouble("set_polar_to_player", endSpeed));
//                r = new Routine("routine", bList, false);
//                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c),
//                        new Component_Movement(new MoveModeAddVector("polar", new Vector(ang, speed)),
//                                new MoveModeTransVector("speed_compounding", new Vector(1, speedComp))),
//                        new Component_Behavior(r)});
//            }

        }
        private static void spawnWaterDeath_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "blue";

            //radial
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            int sym;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 8;
                    spawns = 1;
                    break;
                case 2:
                    sym = 10;
                    spawns = 2;
                    break;
                case 3:
                    sym = 10;
                    spawns = 3;
                    break;
                case 4:
                default:
                    sym = 14;
                    spawns = 3;
                    break;
            }
            double bSpeed = 2.7;
            double topSpeed = 5.1;

            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < spawns; j++){
                    double speed = MiscUtil.rangeRandom(bSpeed, topSpeed, ((double)j+1)/(spawns+1));
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
            //water_small
            spawns = 3;
            for(int i = 0; i < spawns; i++){
                double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(2, 3.8, GameDriver.randomDouble());
                Vector iPol = new Vector(ang, speed);
                double gAccel = MiscUtil.rangeRandom(.02, .04, GameDriver.randomDouble());
                double drag = MiscUtil.rangeRandom(.015, .02, GameDriver.randomDouble());

                EnemySpawns.construct4_waterSub(pos, iPol, gAccel, drag);
            }
        }
        private static void spawnWaterDeath_2(Entity e, boolean playerKill){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "small";
            String c;
            if(playerKill){
                c = "blue";
            }else{
                c = "red";
            }

            double speed = 2.251;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 7;
                    break;
                case 2:
                    sym = 10;
                    break;
                case 3:
                    sym = 13;
                    break;
                case 4:
                default:
                    sym = 16;
                    break;
            }
            if(!playerKill){
                sym = (int)(sym * 1.5);
            }
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector pol = new Vector(ang, speed);
                constructStraightProjectile(pos, t, c, pol);
            }
        }
        private static void spawnWater_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            int spawns = 6;
            if(GameDriver.getCurrentDifficulty() >= 3){
                ++spawns;
            }
            for(int i = 0; i < spawns; i++){
                double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(3, 4.8, GameDriver.randomDouble());
                Vector iPol = new Vector(ang, speed);
                double gAccel = MiscUtil.rangeRandom(.01, .03, GameDriver.randomDouble());
                double drag = MiscUtil.rangeRandom(.025, .03, GameDriver.randomDouble());

                EnemySpawns.construct4_waterSub(pos, iPol, gAccel, drag);
            }
        }
        //boss 4
        private static void spawnB4_1_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "medium";
            String c = "blue";

            Random r = new Random(GameDriver.getSeed() - 17);
            r.nextDouble();
            double rAng = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double degTick = -157.4/60;
            double bAng = MiscUtil.fixAngle(rAng + (degTick * GameDriver.getTick()));
            double speed = 3.5;
            double gAccel = .02;
            double drag = .008;

            int sym = 3;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructGravityProjectile(pos, t, c, new Vector(ang, speed), gAccel, drag);
            }
        }
        private static void spawnB4_1_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "large";
            String c = "blue";

            double speed = 4.2;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            int sym;
            if(GameDriver.getCurrentDifficulty() == 3){
                sym = 8;
            }
            else{
                sym = 12;
            }
            for(int i = 0; i < sym; i++) {
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnB4_2(boolean b){
//            if(!b){
//                return;
//            }
            String t = "medium";
            String c;
            if(b){
                c = "teal";
            }else{
                c = "blue";
            }

            double gAccel = .027;
            double dragHigh = .1;
            double dragLow = .0005;
            double outsideSpace = 5;
            //how far from the top it spawns (spawns inside of the field)
            double spawnInsideDist = 10;
            double spacing;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spacing = 42;
                    break;
                case 2:
                    spacing = 37;
                    break;
                case 3:
                    spacing = 30;
                    break;
                case 4:
                default:
                    spacing = 24;
                    break;
            }
            double exp = 1;
            double multi = 1d/333;
            double initX = MiscUtil.rangeRandom(-outsideSpace, -outsideSpace + spacing, GameDriver.randomDouble());
            for(double x = initX; x < GameDriver.getGameWidth() + outsideSpace; x+= spacing){
                Vector pos;
                double gAng;
                if(b) {
                    gAng = 0;
                    pos = new Vector(x, spawnInsideDist);
                }else{
                    gAng = 180;
                    pos = new Vector(x, GameDriver.getGameHeight() - spawnInsideDist);
                }
                double dist = Math.abs((GameDriver.getGameWidth()/2) - x);
                double var = 1d/Math.pow(1 + (dist*multi), exp);
                var = 1 - var;
                double drag = MiscUtil.rangeRandom(dragLow, dragHigh, var);
                constructGravityDirProjectile(pos, t, c, new Vector(), gAng, gAccel, drag);
            }
        }
        private static void spawnB4_3_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "medium";
            String c = "blue";

            Random r = new Random(GameDriver.getSeed() - 17);
            r.nextDouble();
            double rAng = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double degTick = -157.4/60;
            double bAng = MiscUtil.fixAngle(rAng + (degTick * GameDriver.getTick()));
            double speed = 3.9;
            double angDiff = 2;
            double speedDiff = -.55;

            int sym = 3;
            int length;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    length = 1;
                    break;
                case 2:
                case 3:
                    length = 2;
                    break;
                case 4:
                default:
                    length = 3;
                    break;
            }
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < length; j++) {
                    constructStraightProjectile(pos, t, c, new Vector(MiscUtil.fixAngle(ang + (angDiff * j)), speed + (speedDiff * j)));
                }
            }
        }
        private static void spawnB4_3_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "large";
            String c = "teal";

            double speed = 3.8;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double gAccel = .02;
            double drag = .01;
            int sym;
            if(GameDriver.getCurrentDifficulty() == 3){
                sym = 6;
            }
            else{
                sym = 9;
            }
            for(int i = 0; i < sym; i++) {
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructGravityProjectile(pos, t, c, new Vector(ang, speed), gAccel, drag);
            }
        }
        private static void spawnB4_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "large";
            String c1 = "blue", c2 = "red";

            double speed = 3.5;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < 3; i++){
                constructB4_4(pos, t, c1, new Vector(bAng + (i * (360d/3)), speed), true);
                constructB4_4(pos, t, c2, new Vector(bAng + (i * (360d/3)) + (360d/6), speed), false);
            }
        }
        private static void constructB4_4(Vector pos, String t, String c, Vector iPol, boolean b){
            int freqBig = 50;
            int freqSmall;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freqSmall = 23;
                    break;
                case 2:
                    freqSmall = 20;
                    break;
                case 3:
                    freqSmall = 18;
                    break;
                case 4:
                default:
                    freqSmall = 16;
                    break;
            }
            String spawnBig, spawnSmall;
            SpawnUnit suBig;
            if(b){
                spawnBig = "b4_4_sub_1";
                spawnSmall = "b4_4_sub_3";
                suBig = new SpawnUnit(freqBig * 2, false, new SISingleTick(freqBig - 1, spawnBig));
            }else{
                spawnBig = "b4_4_sub_2";
                spawnSmall = "b4_4_sub_4";
                suBig = new SpawnUnit(freqBig * 2, false, new SISingleTick((freqBig * 2) - 1, spawnBig));
            }
            SpawnUnit suSmall = new SpawnUnit(freqSmall, true, new SISingleTick(freqSmall - 1, spawnSmall));
            ArrayList<SpawnUnit> suList = new ArrayList<>();
            suList.add(suBig);
            suList.add(suSmall);
            Component_Spawning sComp = new Component_Spawning(suList);
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), Comps.movePol(iPol), sComp});
        }
        //b = blue, !b = red
        private static void spawnB4_4_sub_1(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "large";
            String c;
            if(b){
                c = "blue";
            }else{
                c = "red";
            }

            double speed = 3.5;
            //backwards; +- 30
            double bAng = Vector.getAngle(pos, pComp.getOldPosition());

            constructB4_4_sub_1(pos, t, c, new Vector(bAng + 60, speed), b);
            constructB4_4_sub_1(pos, t, c, new Vector(bAng - 60, speed), b);
        }
        private static void constructB4_4_sub_1(Vector pos, String t, String c, Vector iPol, boolean b){
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 23;
                    break;
                case 2:
                    freq = 20;
                    break;
                case 3:
                    freq = 18;
                    break;
                case 4:
                default:
                    freq = 16;
                    break;
            }
            String spawnSmall;
            if(b){
                spawnSmall = "b4_4_sub_3";
            }else{
                spawnSmall = "b4_4_sub_4";
            }
            SpawnUnit suSmall = new SpawnUnit(freq, true, new SISingleTick(freq - 1, spawnSmall));
            Component_Spawning sComp = new Component_Spawning(suSmall);
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), Comps.movePol(iPol), sComp});
        }
        private static void spawnB4_4_sub_2(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(!isInBounds(pos, 0)){
                return;
            }

            String t = "medium";
            String c;
            if(b){
                c = "blue";
            }else{
                c = "red";
            }
            double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double gAccel = .02;
            double drag = .006;

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorInt("wait_game_message", 0));
            bList.add(new BehaviorMM("add_mm", new MoveModeAddGravity("gravity", new Vector(), ang, gAccel, drag)));
//            bList.add(new Behavior("deactivate"));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), bComp});
        }
        private static void spawnB4_5_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "medium";
            String c = "blue";

            Random r = new Random(GameDriver.getSeed() - 17);
            r.nextDouble();
            double rAng = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double degTick = -157.4/60;
            double bAng = MiscUtil.fixAngle(rAng + (degTick * GameDriver.getTick()));
            double speed = 5;
            double gAccel = .01;
            double drag = .011;

            int sym = 6;
            for(int i = 0; i < sym - 1; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                constructGravityDirProjectile(pos, t, c, new Vector(ang, speed), ang, gAccel, drag);
            }
        }
        private static void spawnB4_5_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "large";
            String c = "red";
            double speed = 6;
            double gAccel = .01;
            double drag = .009;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            int sym;
            if(GameDriver.getCurrentDifficulty() == 3){
                sym = 8;
            }
            else{
                sym = 12;
            }
            for(int i = 0; i < sym; i++) {
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructGravityDirProjectile(pos, t, c, new Vector(ang, speed), ang, gAccel, drag);
            }
        }
        private static void spawnB4_6_1(){
            //putting this here - no time; quick fix
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 47;
                    break;
                case 2:
                    freq = 43;
                    break;
                case 3:
                    freq = 41;
                    break;
                case 4:
                default:
                    freq = 38;
                    break;
            }
            if(GameDriver.getTick() % freq != 0){
                return;
            }

            String t = "sharp";
            String c = "teal";

            double spawns = 8;
            double angBound = .3;
            double speedLow = 2.8, speedHigh = speedLow + .027;

            for(int i = 0; i < spawns; i++) {
                double x = (i+1) * (GameDriver.getGameWidth()/(spawns + 1));
                Vector pos = EnemySpawns.spawnVTop(x);
                double ang = MiscUtil.rangeRandom(-angBound, angBound, GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(speedLow, speedHigh, GameDriver.randomDouble());

                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnB4_6_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            switch(GameDriver.getCurrentDifficulty()){
                case 4:
                    B4_6_2_helper(pos, 4);
                case 3:
                    B4_6_2_helper(pos, 3);
                case 2:
                    B4_6_2_helper(pos, 2);
                case 1:
                    B4_6_2_helper(pos, 1);
            }
        }
        private static void B4_6_2_helper(Vector pos, int id){
            if(id <= 0 || id > 4){
                return;
            }

            String t = "medium";
            String c;
            switch(id){
                case 1:
                    c = "orange";
                    break;
                case 2:
                    c = "blue";
                    break;
                case 3:
                    c = "red";
                    break;
                case 4:
                default:
                    c = "purple";
                    break;
            }
            String dSpawnID = "b4_6_2_sub_" + id;
            double iSpeedLow = 4.5,
                    iSpeedHigh = 6;
            double speedMultiLow = .975,
                    speedMultiHigh = .981;
            double gAccelLow = .011,
                    gAccelHigh = .013;
            double dragLow = .008,
                    dragHigh = .012;
            int timeLow = 90,
                    timeHigh = 150;

            double ang = MiscUtil.rangeRandom(-90, 90, GameDriver.randomDouble());
            double iSpeed = MiscUtil.rangeRandom(iSpeedLow, iSpeedHigh, GameDriver.randomDouble());
            double speedMulti = MiscUtil.rangeRandom(speedMultiLow, speedMultiHigh, GameDriver.randomDouble());
            double gAccel = MiscUtil.rangeRandom(gAccelLow, gAccelHigh, GameDriver.randomDouble());
            double drag = MiscUtil.rangeRandom(dragLow, dragHigh, GameDriver.randomDouble());
            Vector iPol = new Vector(ang, iSpeed);
            int time = (int)(MiscUtil.rangeRandom(timeLow, timeHigh, GameDriver.randomDouble()));

            Component_Movement mComp = Comps.moveGravity(iPol, 180, gAccel, drag);
            mComp.addMode(new MoveModeTransVector("speed_compounding", new Vector(1, speedMulti)));

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
            bList.add(new Behavior("deactivate"));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c),
                    mComp, bComp, Comps.dSpawn(dSpawnID)});
        }
        private static void spawnB4_6_2_sub_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "orange";

            double rAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            int sym = 4;
            for(int i = 0; i < sym; i++){
                double bAng = MiscUtil.fixAngle(rAng + (i * (360d/sym)));
//                constructStraightProjectile(pos, t, c, new Vector(bAng, 2.4));

                constructStraightProjectile(pos, t, c, new Vector(bAng + 11, 2.05));
                constructStraightProjectile(pos, t, c, new Vector(bAng - 11, 2.05));

                constructStraightProjectile(pos, t, c, new Vector(bAng + 18, 1.67));
                constructStraightProjectile(pos, t, c, new Vector(bAng - 18, 1.67));

                constructStraightProjectile(pos, t, c, new Vector(bAng + (360d/(sym * 2)), 1.2));
            }
        }
        private static void spawnB4_6_2_sub_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "blue";

            double rAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            int sym = 6;
            for(int i = 0; i < sym; i++){
                double bAng = MiscUtil.fixAngle(rAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(bAng, 2.8));

                constructStraightProjectile(pos, t, c, new Vector(bAng + 8, 2.41));
                constructStraightProjectile(pos, t, c, new Vector(bAng - 8, 2.41));

                constructStraightProjectile(pos, t, c, new Vector(bAng + (360d/(sym * 2)), 2.2));
            }
            rAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            sym = 3;
            for(int i = 0; i < sym; i++){
                double bAng = MiscUtil.fixAngle(rAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(bAng, .9));
            }
        }
        private static void spawnB4_6_2_sub_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "red";

            double rAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            int sym = 5;
            double bSpeed = 2.51;
            for(int i = 0; i < sym; i++){
                double bAng1 = MiscUtil.fixAngle(rAng + (i * (360d/sym)));

                constructStraightProjectile(pos, t, c, new Vector(bAng1, bSpeed));
                constructStraightProjectile(pos, t, c, new Vector(bAng1 + 10, bSpeed * .9));
                constructStraightProjectile(pos, t, c, new Vector(bAng1 + 20, bSpeed * .8));
//                constructStraightProjectile(pos, t, c, new Vector(bAng1 + 30, bSpeed * .7));

                double bAng2 = bAng1 + MiscUtil.rangeRandom(-34, 2, GameDriver.randomDouble());

                constructStraightProjectile(pos, t, c, new Vector(bAng2, bSpeed * .75));
                constructStraightProjectile(pos, t, c, new Vector(bAng2 + 11, bSpeed * .6));
                constructStraightProjectile(pos, t, c, new Vector(bAng2 + 22, bSpeed * .45));
//                constructStraightProjectile(pos, t, c, new Vector(bAng2 + 33, bSpeed * .3));
            }
        }
        private static void spawnB4_6_2_sub_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "purple";

            double rAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            int sym = 7;
            for(int i = 0; i < sym; i++){
                double bAng = MiscUtil.fixAngle(rAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(bAng, 1.9));
                constructStraightProjectile(pos, t, c, new Vector(bAng + (360d/(sym * 2)), .85));
//
                constructStraightProjectile(pos, t, c, new Vector(bAng + 10.5, 2.15));
                constructStraightProjectile(pos, t, c, new Vector(bAng - 10.5, 2.15));
//
//                constructStraightProjectile(pos, t, c, new Vector(bAng + 18, 1.67));
//                constructStraightProjectile(pos, t, c, new Vector(bAng - 18, 1.67));
//
//                constructStraightProjectile(pos, t, c, new Vector(bAng + (360d/(sym * 2)), 1.2));
            }
        }
        //stage 5
        private static void spawnBird_5(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "small";
            String c = "blue";

            int sym, spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 9;
                    spawns = 1;
                    break;
                case 2:
                    sym = 12;
                    spawns = 1;
                    break;
                case 3:
                    sym = 15;
                    spawns = 2;
                    break;
                case 4:
                default:
                    sym = 18;
                    spawns = 3;
                    break;
            }

            double lSpeed = 1.5, hSpeed = 8;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++){
                    double speed = MiscUtil.rangeRandom(lSpeed, hSpeed, ((double)(j+1))/(spawns+1));
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnBird_6(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "small";
            String c = "red";

            int sym, spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 5;
                    spawns = 3;
                    break;
                case 2:
                    sym = 7;
                    spawns = 4;
                    break;
                case 3:
                    sym = 8;
                    spawns = 5;
                    break;
                case 4:
                default:
                    sym = 9;
                    spawns = 7;
                    break;
            }

            double speed = 6.2;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double angDiff = 2;
            for(int i = 0; i < sym; i++){
                double ang_ = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                double angLow = MiscUtil.fixAngle((((spawns-1) * -.5) * angDiff) + ang_);
                for(int j = 0; j < spawns; j++){
                    constructStraightProjectile(pos, t, c, new Vector(angLow + (j * angDiff), speed));
                }
            }
        }
        private static void spawnBird_7(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c;
            if(b){
                c = "blue";
            }else{
                c = "red";
            }

            int sym, spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 4;
                    spawns = 2;
                    break;
                case 2:
                    sym = 5;
                    spawns = 3;
                    break;
                case 3:
                    sym = 6;
                    spawns = 4;
                    break;
                case 4:
                default:
                    sym = 7;
                    spawns = 5;
                    break;
            }

            double speed = 6.2;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double angDiff = 3;
            for(int i = 0; i < sym; i++){
                double ang_ = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                double angLow = MiscUtil.fixAngle((((spawns-1) * -.5) * angDiff) + ang_);
                for(int j = 0; j < spawns; j++){
                    constructStraightProjectile(pos, t, c, new Vector(angLow + (j * angDiff), speed));
                }
            }
        }
        private static void spawnEmber_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String c = "orange";

            double pAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            //rings
            if(GameDriver.getCurrentDifficulty() > 1) {
                String t = "medium";
                int sym;
                int rings;
                switch(GameDriver.getCurrentDifficulty()){
                    case 2:
                        rings = 1;
                        sym = 8;
                        break;
                    case 3:
                        rings = 3;
                        sym = 10;
                        break;
                    case 4:
                    default:
                        rings = 5;
                        sym = 12;
                        break;
                }
                double lSpeed = .7, hSpeed = 2.9;
                for(int i = 0; i < rings; i++){
                    double speed = MiscUtil.rangeRandom(lSpeed, hSpeed, ((double)(i+1))/(rings+1));
                    for(int j = 0; j < sym; j++){
                        double ang = MiscUtil.fixAngle(pAng + (j * (360d/sym)));
                        //every other ring in other words
                        if(i % 2 != 0){
                            ang += ((360d/sym)/2);
                            ang = MiscUtil.fixAngle(ang);
                        }
                        constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                    }
                }
            }
            //triangle
            String t = "small";
            int sym = 3;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 3;
                    break;
                case 2:
                    spawns = 5;
                    break;
                case 3:
                    spawns = 7;
                    break;
                case 4:
                default:
                    spawns = 10;
                    break;
            }
            double lSpeed = .7, hSpeed = 10.3;
            double angDiff = 1.5;
            for(int s = 0; s < sym; s++) {
                double bAng = MiscUtil.fixAngle(pAng + (s * (360d/sym)));
                for (int i = 0; i < spawns; i++) {
                    double speed = MiscUtil.rangeRandom(lSpeed, hSpeed, 1d - ((double) (i + 1)) / (spawns + 1));
                    double angLow = MiscUtil.fixAngle((((i) * -.5) * angDiff) + bAng);
                    for (int j = 0; j < i + 1; j++) {
                        constructStraightProjectile(pos, t, c, new Vector(angLow + (j * angDiff), speed));
                    }
                }
            }

        }
        private static void spawnEmber_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String c = "red";

            double pAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            //rings
            if(GameDriver.getCurrentDifficulty() > 1) {
                String t = "medium";
                int sym;
                int rings;
                switch(GameDriver.getCurrentDifficulty()){
                    case 2:
                        rings = 1;
                        sym = 10;
                        break;
                    case 3:
                        rings = 2;
                        sym = 10;
                        break;
                    case 4:
                    default:
                        rings = 3;
                        sym = 10;
                        break;
                }
                double lSpeed = 1.3, hSpeed = 4.9;
                for(int i = 0; i < rings; i++){
                    double speed = MiscUtil.rangeRandom(lSpeed, hSpeed, ((double)(i+1))/(rings+1));
                    for(int j = 0; j < sym; j++){
                        double ang = MiscUtil.fixAngle(pAng + (j * (360d/sym)));
                        //every other ring in other words
                        if(i % 2 != 0){
                            ang += ((360d/sym)/2);
                            ang = MiscUtil.fixAngle(ang);
                        }
                        constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                    }
                }
            }

            //shotgun
            String t = "small";
            int sym = 3;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 20;
                    break;
                case 2:
                    spawns = 25;
                    break;
                case 3:
                    spawns = 30;
                    break;
                case 4:
                default:
                    spawns = 40;
                    break;
            }
            double lSpeed = 3.5, hSpeed = 9.5;
            double angOut = 4;
            double maxOut = 30;
            for(int i = 0; i < sym; i++) {
                double bAng = MiscUtil.fixAngle(pAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++){
                    double out = MiscUtil.rangeRandom(-maxOut, maxOut, GameDriver.randomDouble());
                    double ang = MiscUtil.rangeRandom(-angOut, angOut, GameDriver.randomDouble()) + bAng;
                    double speed = MiscUtil.rangeRandom(lSpeed, hSpeed, GameDriver.randomDouble());

                    double posVarAng = pAng + 90;
                    Vector spawnPos = Vector.add(pos, Vector.toVelocity(new Vector(posVarAng, out)));
                    constructStraightProjectile(spawnPos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnEmber_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);



            double degTick = 50d/60;
            double bAng_ = degTick * GameDriver.getTick();

            //rings
            if(GameDriver.getTick() % 4 == 0) {
                Random r = new Random(GameDriver.getSeed() - 2);
                r.nextDouble();
                r.nextDouble();
                double angAdd = MiscUtil.rangeRandom(0, 360, r.nextDouble());
                String c = "orange";
                String t = "medium";
                int sym;
                switch (GameDriver.getCurrentDifficulty()) {
                    case 1:
                        sym = 8;
                        break;
                    case 2:
                        sym = 10;
                        break;
                    case 3:
                        sym = 12;
                        break;
                    case 4:
                    default:
                        sym = 14;
                        break;
                }
                double speed = 2.9;
                for (int j = 0; j < sym; j++) {
                    //still needs bAng_ because that's the part that rotates
                    double ang = MiscUtil.fixAngle(bAng_ + (j * (360d / sym) + angAdd));
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }

            //shotgun
            String t = "small";
            String c = "red";
            int sym = 3;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 20;
                    break;
                case 2:
                    spawns = 25;
                    break;
                case 3:
                    spawns = 30;
                    break;
                case 4:
                default:
                    spawns = 40;
                    break;
            }
            double lSpeed = 3.5, hSpeed = 8.5;
            double angOut = 3;
            double maxOut = 25;
            for(int i = 0; i < sym; i++) {
                double bAng = MiscUtil.fixAngle(bAng_ + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++){
                    double out = MiscUtil.rangeRandom(-maxOut, maxOut, GameDriver.randomDouble());
                    double ang = MiscUtil.rangeRandom(-angOut, angOut, GameDriver.randomDouble()) + bAng;
                    double speed = MiscUtil.rangeRandom(lSpeed, hSpeed, GameDriver.randomDouble());

                    double posVarAng = bAng_ + 90;
                    Vector spawnPos = Vector.add(pos, Vector.toVelocity(new Vector(posVarAng, out)));
                    constructStraightProjectile(spawnPos, t, c, new Vector(ang, speed));
                }
            }
        }
        //boss 5
        private static void spawnB5_1(Entity e){
            MiscSpawns.spawnSwordSwipe(e);
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 1;
                    break;
                case 2:
                case 3:
                    sym = 2;
                    break;
                case 4:
                default:
                    sym = 3;
                    break;
            }
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 12;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector pol = new Vector(ang, speed);
                constructB5_1(pos, pol);
            }
        }
        private static void constructB5_1(Vector pos, Vector polar){
            String t = "small";
            String c = "white";
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                    freq = 4;
                    break;
                default:
                    freq = 3;
                    break;
            }
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn("b5_1_sub"));
            //needs to be typed ep for the despawning system to pick it up
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.movePol(polar), new Component_Spawning(su), Comps.ccEP(t), Comps.epSprite(t, c)});
        }
        private static void spawnB5_1_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(!isInBounds(pos, 0)){
                return;
            }
            String t = "small";
            String c = "white";

            int waitTime = (3 * 60) + 20;
            double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = 2.62;

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(waitTime)));
            bList.add(new BehaviorVector("set_polar", new Vector(ang, speed)));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), bComp});
        }
        private static void spawnB5_2(Entity e){
            MiscSpawns.spawnSwordSwipe(e);
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 12;
            Vector pol = new Vector(bAng, speed);
            constructB5_1(pos, pol);
        }
        private static void spawnB5_4(Entity e){
            MiscSpawns.spawnSwordSwipe(e);
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 9.5;
            int spawns = GameDriver.getCurrentDifficulty() + 1;
            double angDiff = 3;
            double ang = bAng - ((angDiff/2) * (spawns-1));
            for(int i = 0; i < spawns; i++){
                constructB5_1(pos, new Vector(MiscUtil.fixAngle(ang), speed));
                ang += angDiff;
            }
        }
        private static void spawnB5_5(Entity e){
            MiscSpawns.spawnSwordSwipe(e);
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            int sym = 2;
            double degTick = 20d/60;
            Random r = new Random(GameDriver.getSeed() + 1);
            r.nextDouble();
            double bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(bAng_ + (degTick * GameDriver.getTick()));
            double speed = 12;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector pol = new Vector(ang, speed);
                constructB5_1(pos, pol);
            }
        }
        //stage 6
        private static void spawnWisp_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c;
            if(GameDriver.randomBoolean()){
                c = "orange";
            }else{
                c = "red";
            }
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 4;
                    break;
                case 2:
                    spawns = 5;
                    break;
                case 3:
                    spawns = 7;
                    break;
                case 4:
                default:
                    spawns = 9;
                    break;
            }
            double speedlow = 1, speedhigh = 3;
            for(int i = 0; i < spawns; i++){
                double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
                Vector pol = new Vector(ang, speed);
                constructStraightProjectile(pos, t, c, pol);
            }
        }
        private static void spawnTrap_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "small";
            String c = "white";

            double ang = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            double speedlow = 1, speedhigh = 12;
            for(int i = 0; i < GameDriver.getCurrentDifficulty(); i++){
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh, (((double)(i + 1))/(GameDriver.getCurrentDifficulty() + 1)));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnTrap_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "small";
            String c = "white";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            double speedlow = 1, speedhigh = 12;
            double angOut = 5;
            for(int i = 0; i < GameDriver.getCurrentDifficulty(); i++){
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh, (((double)(i + 1))/(GameDriver.getCurrentDifficulty() + 1)));
                constructStraightProjectile(pos, t, c, new Vector(bAng - angOut, speed));
                constructStraightProjectile(pos, t, c, new Vector(bAng + angOut, speed));
                constructStraightProjectile(pos, t, c, new Vector(bAng, speed));
            }
        }
        private static void spawnTrap_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "small";
            String c = "white";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            double speedlow = 1, speedhigh = 12;
            double angOut = 2.5;
            for(int i = 0; i < GameDriver.getCurrentDifficulty(); i++){
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh, (((double)(i + 1))/(GameDriver.getCurrentDifficulty() + 1)));
                constructStraightProjectile(pos, t, c, new Vector(bAng - angOut, speed));
                constructStraightProjectile(pos, t, c, new Vector(bAng + angOut, speed));
                //whatever man
                constructStraightProjectile(pos, t, c, new Vector(bAng - angOut - angOut, speed));
                constructStraightProjectile(pos, t, c, new Vector(bAng + angOut + angOut, speed));
                constructStraightProjectile(pos, t, c, new Vector(bAng, speed));
            }
        }
        private static void spawnTrap_4(Entity e, int lvl){
            //lvl = spawn more traps
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "small";
            String c = "white";

            //spawn the ring
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = MiscUtil.rangeRandom(2, 4, GameDriver.randomDouble());
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 13;
                    break;
                case 2:
                    spawns = 16;
                    break;
                case 3:
                    spawns = 19;
                    break;
                case 4:
                default:
                    spawns = 22;
                    break;
            }
            for(int i = 0; i < spawns; i++){
                double ang = bAng + (i * (360d/spawns));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                if(i % 2 == 0 && GameDriver.getCurrentDifficulty() >= 3){
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed * 1.5));
                }
            }

            //spawn extra traps
            if(lvl > 0){
                int newlvl = lvl - 1;
                int extraTraps = lvl * 3;
                int lowerBound = 200;
                double maxDist = 200;

                for(int i = 0; i < extraTraps; i++){
                    Vector sPos = new Vector(0, 0);
                    while(!isInBounds(sPos, 50) || sPos.getB() > lowerBound) {
                        double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                        double dist = MiscUtil.rangeRandom(0, maxDist, GameDriver.randomDouble());
                        sPos = Vector.add(pos, Vector.toVelocity(new Vector(ang, dist)));
                    }
                    if(newlvl > 0) {
                        EnemySpawns.constructTrap(sPos, "trap_4_sub_" + (lvl - 1));
                    }else{
                        EnemySpawns.constructTrap(sPos, "trap_4");
                    }
                }
            }
        }
        //boss 6
        private static void spawnB6_1_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "large";
            String c = "purple";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speedhigh = 12, speedlow = 4;
            int sym;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 30;
                    spawns = 4;
                    break;
                case 2:
                    sym = 35;
                    spawns = 5;
                    break;
                case 3:
                    sym = 40;
                    spawns = 6;
                    break;
                case 4:
                default:
                    sym = 50;
                    spawns = 8;
                    break;
            }
            bAng += 360d/sym;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++) {
                    double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)(j + 1))/(spawns + 1));
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnB6_1_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String c = "red";
            String t;

            double speedlow = 6.5, speedhigh = 9;
            int spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawns = 1;
                    if(GameDriver.randomBoolean()){
                        ++spawns;
                    }
                    break;
                case 2:
                    spawns = 2;
                    break;
                case 3:
                    spawns = 2;
                    if(GameDriver.randomBoolean()){
                        ++spawns;
                    }
                    break;
                case 4:
                default:
                    spawns = 3;
                    break;
            }

            for(int i = 0; i < spawns; i++) {
                double ang = MiscUtil.rangeRandom(360d/spawns * i, (360d/spawns) * (i + 1), GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
                if(GameDriver.randomBoolean()){
                    t = "medium";
                }else{
                    t = "small";
                    speed *= .8;
                }

                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnB6_2(int dir){
            Vector pos1, pos2;
            double ang;
            double speed;
            //ticks to traverse a side
            double traverseTicks = 90;
            double multi;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    multi = .5;
                    break;
                case 2:
                case 3:
                    multi = .6;
                    break;
                case 4:
                default:
                    multi = 2d/3;
                    break;
            }
            int lifetime = (int)(traverseTicks * multi);
            int freq = 8;
            int b = 51;
            if(dir == 4){
                pos1 = new Vector(b, b);
                pos2 = new Vector(GameDriver.getGameWidth() - b, b);
                ang = 0;
                speed = ((double)GameDriver.getGameHeight())/traverseTicks;
            }else if(dir == 3){
                pos1 = new Vector(b, GameDriver.getGameHeight() - b);
                pos2 = new Vector(GameDriver.getGameWidth() - b, GameDriver.getGameHeight() - b);
                ang = 180;
                speed = ((double)GameDriver.getGameHeight())/traverseTicks;
            }else if(dir == 1){
                pos1 = new Vector(b, b);
                pos2 = new Vector(b, GameDriver.getGameHeight() - b);
                ang = 90;
                speed = ((double)GameDriver.getGameWidth())/traverseTicks;
            }else{
                pos1 = new Vector(GameDriver.getGameWidth() - b, b);
                pos2 = new Vector(GameDriver.getGameWidth() - b, GameDriver.getGameHeight() - b);
                ang = 270;
                speed = ((double)GameDriver.getGameWidth())/traverseTicks;
            }
            Vector pol = new Vector(ang, speed);
            if(dir <= 2){
                EnemySpawns.constructTrapSpawnerStraightLifetime(pos1, pol, freq, lifetime, "b6_2_sub_2");
                EnemySpawns.constructTrapSpawnerStraightLifetime(pos2, pol, freq, lifetime, "b6_2_sub_1");
            }else{
                EnemySpawns.constructTrapSpawnerStraightLifetime(pos1, pol, freq, lifetime, "b6_2_sub_3");
                EnemySpawns.constructTrapSpawnerStraightLifetime(pos2, pol, freq, lifetime, "b6_2_sub_4");
            }
        }
        private static void spawnB6_2_sub(Entity e, int dir){
            //1 = up
            //2 = down
            //3 = right
            //4 = left
            double ang;
            switch(dir){
                case 1:
                    ang = 180;
                    break;
                case 2:
                    ang = 0;
                    break;
                case 3:
                    ang = 90;
                    break;
                case 4:
                default:
                    ang = 270;
                    break;
            }
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            constructB6_2_sub(pos, ang);
        }
        private static void constructB6_2_sub(Vector pos, double ang){
            double maxDist = 50;
            double speedhigh = 8;
            double speedlow = .3;
            int spawnsA, spawnsB;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    spawnsA = 1;
                    spawnsB = 3;
                    break;
                case 2:
                    spawnsA = 2;
                    spawnsB = 3;
                    break;
                case 3:
                    spawnsA = 2;
                    spawnsB = 4;
                    break;
                case 4:
                default:
                    spawnsA = 3;
                    spawnsB = 4;
                    break;
            }
            String t = "large";
            String c = "red";

            for(int i = 0; i < spawnsA; i++) {
                double oAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double oDist = MiscUtil.rangeRandom(0, maxDist, GameDriver.randomDouble());
                Vector sPos = Vector.add(pos, Vector.toVelocity(new Vector(oAng, oDist)));
                constructStraightProjectile(sPos, t, c, new Vector(ang, speedhigh));
            }
            t = "medium";
            for(int i = 0; i < spawnsB; i++){
                double oAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double oDist = MiscUtil.rangeRandom(0, maxDist, GameDriver.randomDouble());
                Vector sPos = Vector.add(pos, Vector.toVelocity(new Vector(oAng, oDist)));
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh-5, GameDriver.randomDouble());
                constructStraightProjectile(sPos, t, c, new Vector(ang, speed));
                if(GameDriver.getCurrentDifficulty() == 4 || i == 0) {
                    constructStraightProjectile(sPos, t, c, new Vector(ang + 90, speed));
                    constructStraightProjectile(sPos, t, c, new Vector(ang + 270, speed));
                }
                constructStraightProjectile(sPos, t, c, new Vector(ang + 180, speed));
            }
        }
        private static void spawnB6_3_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String c = "red";
            double speed = 3.1;
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 11;
                    break;
                case 2:
                    sym = 13;
                    break;
                case 3:
                    sym = 15;
                    break;
                case 4:
                default:
                    sym = 18;
                    break;
            }
            Random r = new Random(GameDriver.getSeed() - 12);
            r.nextDouble();
            double bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double degTick = 12d/sym;
            double bAng = MiscUtil.fixAngle(bAng_ + (degTick * GameDriver.getTick()));
            for(int i = 0; i < sym; i++){
                double ang1 = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                double ang2 = MiscUtil.fixAngle(ang1 + (360d/(2 * sym)));
                constructStraightProjectile(pos, "medium", c, new Vector(ang1, speed));
                constructStraightProjectile(pos, "small", c, new Vector(ang2, speed));
            }
        }
        private static void spawnB6_3_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "large";
            String c = "purple";

            double yTarget = 300;
            Vector target = new Vector(GameDriver.getGameWidth()/2, yTarget);
            double ang = Vector.getAngle(pos, target);
            double angRand = 7;
            ang += MiscUtil.rangeRandom(-angRand, angRand, GameDriver.randomDouble());

            double speedlow = 2.5, speedhigh = 3.5;
            double speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
            int lifelow = 30, lifehigh = 40;
            int lifetime = (int)(MiscUtil.rangeRandom(lifelow, lifehigh, GameDriver.randomDouble()));

            Vector pol = new Vector(ang, speed);
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), Comps.movePol(pol),
                    Comps.lifetimeDspawn(lifetime, "b6_3_2_sub")});
        }
        private static void spawnB6_3_2_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "purple";

            boolean spawnTwo = GameDriver.getCurrentDifficulty() >= 3;
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 16;
                    break;
                case 2:
                    sym = 18;
                    break;
                case 3:
                    sym = 20;
                    break;
                case 4:
                default:
                    sym = 22;
                    break;
            }
            double speed1 = 3.12, speed2 = 2.37;
            int time = 40;
            double angShift = 44;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector pol1 = new Vector(ang, speed1);
                constructB6_3_2_sub(pos, pol1, time, angShift, t, c);
                constructB6_3_2_sub(pos, pol1, time, -angShift, t, c);
                if(spawnTwo){
                    Vector pol2 = new Vector(ang, speed2);
                    constructB6_3_2_sub(pos, pol2, time, angShift, t, c);
                    constructB6_3_2_sub(pos, pol2, time, -angShift, t, c);
                }
            }
        }
        private static void constructB6_3_2_sub(Vector pos, Vector pol, int time, double angShift, String t, String c){
            Vector newPol = Vector.add(pol, new Vector(angShift, 0));
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
            bList.add(new BehaviorVector("set_polar", newPol));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = Comps.sharpRotate(t);
            bComp.addBehavior(r);
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), Comps.movePol(pol), bComp});
        }
        private static void spawnB6_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "large";
            String c = "green";
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 2.9;
            constructStraightProjectile(pos, t, c, new Vector(bAng, speed));

            double yTarget = 300;
            Vector target = new Vector(GameDriver.getGameWidth()/2, yTarget);
            double ang = Vector.getAngle(pos, target);
            double angRand = 10;
            ang += MiscUtil.rangeRandom(-angRand, angRand, GameDriver.randomDouble());
            speed = 3;
            Vector pol = new Vector(ang, speed);
            double ac = 1;
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 24;
                    break;
                case 2:
                    freq = 20;
                    break;
                case 3:
                    freq = 17;
                    break;
                case 4:
                default:
                    freq = 13;
                    break;
            }
            int lifetime = 100;
            String trapID = "b6_4_sub";
            EnemySpawns.constructTrapSpawnerHomingLifetime(pos, pol, ac, freq, lifetime, trapID);
        }
        private static void spawnB6_4_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "green";
            int sym = 4;
            double angVar = 20;
            double bAng = MiscUtil.fixAngle(MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble()));
            double iSpeed = 9.5;
            double gAccel = .1;
            double drag = .1;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector iPol = new Vector(ang, iSpeed);
                constructGravityDirProjectile(pos, t, c, iPol, ang, gAccel, drag);
            }
            c = "red";
            sym = 3;
            bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            iSpeed = 6;
            gAccel = .1;
            drag = .07;
            angVar = 10;
            double gAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            gAng = MiscUtil.fixAngle(gAng + MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble()));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector iPol = new Vector(ang, iSpeed);
                constructGravityDirProjectile(pos, t, c, iPol, gAng, gAccel, drag);
            }
        }
        private static void spawnB6_5(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            //radial
            String t = "sharp";
            String c = "green";
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speedlow = 1.7, speedhigh = 3.6;
            double sym, spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 16;
                    spawns = 1;
                    break;
                case 2:
                    sym = 18;
                    spawns = 2;
                    break;
                case 3:
                    sym = 20;
                    spawns = 2;
                    break;
                case 4:
                default:
                    sym = 24;
                    spawns = 3;
                    break;
            }
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++){
                    double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)j + 1)/(spawns + 1));
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
            //side spawners
            double ang = 180;
            double speed = 5;
            int freq;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    freq = 23;
                    break;
                case 2:
                    freq = 21;
                    break;
                case 3:
                    freq = 19;
                    break;
                case 4:
                default:
                    freq = 17;
                    break;
            }
            String trapID = "b6_5_sub";
            Vector posA = new Vector(50, GameDriver.getGameHeight() - 50);
            Vector posB = new Vector(GameDriver.getGameWidth() -50, GameDriver.getGameHeight() - 50);
            EnemySpawns.constructTrapSpawnerStraight(posA, new Vector(ang, speed), freq, trapID);
            EnemySpawns.constructTrapSpawnerStraight(posB, new Vector(ang, speed), freq, trapID);
        }
        private static void spawnB6_5_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "red";
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = 1.4;
            double sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                case 2:
                    sym = 5;
                    break;
                case 3:
                case 4:
                default:
                    sym = 6;
                    break;
            }
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnB6_6(Entity e, int dir){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "large";
            String c = "blue";
            spawnB6_6_sub(e, true);
            double coord = MiscUtil.rangeRandom(0, GameDriver.getGameWidth(), GameDriver.randomDouble());
            Vector target;
            String boundMode;
            double bound;
            //1 = left | 2 = top | 3 = right
            switch(dir){
                case 1:
                    target = new Vector(0, coord);
                    boundMode = "boundary_left";
                    bound = 0;
                    break;
                case 2:
                    target = new Vector(coord, 0);
                    boundMode = "boundary_top";
                    bound = 0;
                    break;
                case 3:
                default:
                    target = new Vector(GameDriver.getGameWidth(), coord);
                    boundMode = "boundary_right";
                    bound = GameDriver.getGameWidth();
                    break;
            }
            double ang = Vector.getAngle(pos, target);
            double speed = 3.5;
            Vector pol = new Vector(ang, speed);

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorDouble(boundMode, bound));
            bList.add(new BehaviorString("set_dspawn", "b6_6_sub"));
            bList.add(new Behavior("deactivate"));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);
            constructEntity(new Component[]{Comps.pos(pos), Comps.movePol(pol), Comps.ccEP(t), Comps.epSprite(t, c), Comps.tep(), bComp});
        }
        private static void spawnB6_6_sub(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c;
            if(b){
                c = "red";
            }else{
                c = "blue";
            }
            int sym, spawns;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 22;
                    spawns = 1;
                    break;
                case 2:
                    sym = 25;
                    spawns = 1;
                    break;
                case 3:
                    sym = 19;
                    spawns = 2;
                    break;
                case 4:
                default:
                    sym = 22;
                    spawns = 2;
                    break;
            }
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speedlow = 2, speedhigh = 3;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 1; j <= spawns; j++){
                    double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)j)/(spawns + 1));
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnB6_7(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "large";
            String c = "red";
            int sym;
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 18;
                    break;
                case 2:
                    sym = 20;
                    break;
                case 3:
                    sym = 22;
                    break;
                case 4:
                default:
                    sym = 25;
                    break;
            }
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double acVar = .05;
            double speed_ = 4.07;
            double speedVar = 1;

            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                double speed = MiscUtil.rangeRandom(speed_ - speedVar, speed_ + speedVar, GameDriver.randomDouble());
                double ac = MiscUtil.rangeRandom(-acVar, acVar, GameDriver.randomDouble());
                Vector iPol = new Vector(ang, speed);
                constructCurvingProjectile(pos, t, c, iPol, ac);
            }
        }
        private static void spawnB6_8_1(){
            Vector mid = new Vector(GameDriver.getGameWidth()/2, GameDriver.getGameHeight()/2);
            double dist = 100;
            double degTick = 2;
            double ang = MiscUtil.fixAngle(GameDriver.getTick() * degTick);
            Vector pos1 = Vector.add(mid, Vector.toVelocity(new Vector(ang, dist)));
            Vector pos2 = Vector.add(mid, Vector.toVelocity(new Vector(ang + 180, dist)));
            EnemySpawns.constructTrap(pos1, "b6_8_1_sub_1");
            EnemySpawns.constructTrap(pos2, "b6_8_1_sub_2");
        }
        private static void spawnB6_8_1_sub(Entity e, boolean bool){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t1 = "sharp";
            String t2 = "medium";
            String c1 = "red";
            String c2 = "blue";
            Vector mid = new Vector(GameDriver.getGameWidth()/2, GameDriver.getGameHeight()/2);
            double degTick = 4;
            double toTarget = MiscUtil.fixAngle(GameDriver.getTick() * degTick);
            double dist = 50;
            if(bool){
                toTarget = MiscUtil.fixAngle(toTarget + 180);
            }
            Vector target = Vector.add(mid, Vector.toVelocity(new Vector(toTarget, dist)));
            double bAng = Vector.getAngle(pos, target);
            double bAngM = Vector.getAngle(mid, pos);
            double speeda = 3.9;//main sharp
            double speedb = 3.3;//secondary sharp
            double speedc = (speeda + speedb)/2;//side sharps
            double speedm = 3.4;//medium
            double angOutSharp = 16;
            double angOutMedium = 21;

            //two sharps
            constructStraightProjectile(pos, t1, c1, new Vector(bAng, speeda));
            constructStraightProjectile(pos, t1, c1, new Vector(bAng, speedb));
            //middle medium out
            constructStraightProjectile(pos, t2, c2, new Vector(bAngM, speedm));
            //medium outs
            constructStraightProjectile(pos, t2, c2, new Vector(bAngM + angOutMedium, speedm));
            constructStraightProjectile(pos, t2, c2, new Vector(bAngM - angOutMedium, speedm));
            if (GameDriver.getCurrentDifficulty() >= 3) {
                //sharp sides
                constructStraightProjectile(pos, t1, c1, new Vector(bAng + angOutSharp, speedc));
                constructStraightProjectile(pos, t1, c1, new Vector(bAng - angOutSharp, speedc));
                //medium to 5
                constructStraightProjectile(pos, t2, c2, new Vector(bAngM + angOutMedium + angOutMedium, speedm));
                constructStraightProjectile(pos, t2, c2, new Vector(bAngM - angOutMedium - angOutMedium, speedm));
            }
        }
        private static void spawnB6_8_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            double dist = 80;
            double outSpeedMulti = .75;
            Vector l = Vector.add(pos, new Vector(-dist, 0));
            Vector r = Vector.add(pos, new Vector(dist, 0));

            constructB6_8_2(pos, 1);
            constructB6_8_2(l, outSpeedMulti);
            constructB6_8_2(r, outSpeedMulti);
        }
        private static void constructB6_8_2(Vector pos, double speedMulti){
            int sym;
            //projectiles are 5*sym
            switch(GameDriver.getCurrentDifficulty()){
                case 1:
                    sym = 6;
                    break;
                case 2:
                    sym = 8;
                    break;
                case 3:
                    sym = 10;
                    break;
                case 4:
                default:
                    sym = 12;
            }
            double bAng_ = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double angVar = 360d/(sym * 5);
            double targetDist = 390;
            double speed = 2.77;
            for(int i = 0; i < sym; i++){
                double bAng = MiscUtil.fixAngle(bAng_ + (i * (360d/sym)));
                Vector target = Vector.add(pos, Vector.toVelocity(new Vector(bAng, targetDist)));

                constructB6_8_2_helper(pos, bAng, speed * speedMulti, target);
                constructB6_8_2_helper(pos, bAng + angVar, speed * speedMulti, target);
                constructB6_8_2_helper(pos, bAng + angVar + angVar, speed * speedMulti, target);
                constructB6_8_2_helper(pos, bAng - angVar, speed * speedMulti, target);
                constructB6_8_2_helper(pos, bAng - angVar - angVar, speed * speedMulti, target);
            }
        }
        private static void constructB6_8_2_helper(Vector pos, double iAng, double iSpeed, Vector target){
            double speedMulti = .99;
            int time = 70;
            String t = "small";
            String c = "red";

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
            bList.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", target, iSpeed)));
            bList.add(new BehaviorVector("timer", new Vector(2)));
            bList.add(new Behavior("continue_trajectory"));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);
            Component_Movement mComp = new Component_Movement(new MoveModeAddVector("polar",
                    new Vector(iAng, iSpeed)), new MoveModeTransVector("speed_compounding", new Vector(1, speedMulti)));
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), mComp, bComp});
        }
        //stage 7
        private static void spawnSWisp_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(pos.getB() >= GameDriver.getGameHeight()){
                return;
            }
            String t = "sharp";
            String c = "red";
            int sym = 12;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = 3.47;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnSCloud_1(Entity e, boolean type){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c;
            if(type){
                c = "blue";
            }else{
                c = "red";
            }

            int sym = 5;
            int spawns = 3;
            double bSpeed = 2.2;
            double speedVar = 1.1;

            Random r = new Random(GameDriver.getSeed() - 2);
            r.nextDouble();
            double bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double degTick = 2.3;
            if(!type){
                degTick = - degTick;
                bAng_ += 24.32;
            }
            double bAng = MiscUtil.fixAngle(bAng_ + (degTick * GameDriver.getTick()));

            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++){
                    double speed = bSpeed + (j * speedVar);
                    Vector pol = new Vector(ang, speed);
                    constructStraightProjectile(pos, t, c, pol);
                }
            }
        }
        private static void spawnSWisp_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(pos.getB() >= GameDriver.getGameHeight()){
                return;
            }
            String t = "small";
            String c = "red";
            int sym = 11;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speedlow = 1.3, speedhigh = 3.8;
            double speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnSWisp_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(pos.getB() >= GameDriver.getGameHeight()){
                return;
            }
            String t = "medium";
            String c = "red";
            int sym = 18;
            int spawns = 4;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double bSpeed = 3.5;
            double speedVar = 1.2;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++) {
                    double speed = bSpeed + (j * speedVar);
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnSWisp_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(pos.getB() >= GameDriver.getGameHeight()){
                return;
            }
            String t = "small";
            String c = "yellow";
            int sym = 6;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speedlow = .9, speedhigh = 2.1;
            double speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed * 2));
            }
        }
        private static void spawnSWisp_5(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(pos.getB() >= GameDriver.getGameHeight()){
                return;
            }
            String t = "large";
            String c = "red";
            int sym = 18;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double speed = 8;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnSHumanoid_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t1 = "medium", t2 = "sharp";
            String c1 = "teal", c2 = "blue";

            //sharp
            int sym = 3;
            double speed = 3.2;
            double degTick1 = 1.5, degTick2 = -1.2;
            Random r = new Random(GameDriver.getSeed() + 21414);
            r.nextDouble();
            double bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng1 = MiscUtil.fixAngle(bAng_ + (degTick1 * (GameDriver.getTick() - (80 * 60))));
            double bAng2 = MiscUtil.fixAngle(bAng_ + (degTick2 * (GameDriver.getTick() - (80 * 60))));
            for(int i = 0; i < sym; i++){
                double ang1 = MiscUtil.fixAngle(bAng1 + (i * (360d/sym)));
                double ang2 = MiscUtil.fixAngle(bAng2 + (i * (360d/sym)));
                constructStraightProjectile(pos, t1, c1, new Vector(ang1, speed));
                constructStraightProjectile(pos, t1, c1, new Vector(ang2, speed));
                constructStraightProjectile(pos, t1, c1, new Vector(ang1, speed * 1.5));
                constructStraightProjectile(pos, t1, c1, new Vector(ang2, speed * 1.5));
            }
            //med
            speed = 2.6;
            degTick1 = 2.8;
            degTick2 = -2.3;
            r = new Random(GameDriver.getSeed() + 115);
            r.nextDouble();
            bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            bAng1 = MiscUtil.fixAngle(bAng_ + (degTick1 * (GameDriver.getTick() - (80 * 60))));
            bAng2 = MiscUtil.fixAngle(bAng_ + (degTick2 * (GameDriver.getTick() - (80 * 60))));
            for(int i = 0; i < sym; i++){
                double ang1 = MiscUtil.fixAngle(bAng1 + (i * (360d/sym)));
                double ang2 = MiscUtil.fixAngle(bAng2 + (i * (360d/sym)));
                constructStraightProjectile(pos, t2, c2, new Vector(ang1, speed));
                constructStraightProjectile(pos, t2, c2, new Vector(ang2, speed));
                constructStraightProjectile(pos, t2, c2, new Vector(ang1, speed * 1.5));
                constructStraightProjectile(pos, t2, c2, new Vector(ang2, speed * 1.5));
            }
        }
        private static void spawnSHumanoid_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c;
            if(GameDriver.randomBoolean()){
                c = "teal";
            }else{
                c = "blue";
            }

            int angVar = 2;
            double bAng = 0;
            double ang = bAng + MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble());
            double speedlow = 1.5, speedhigh = 4.5;
            double speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
            constructStraightProjectile(pos, t, c, new Vector(ang, speed));
            constructStraightProjectile(pos, t, c, new Vector(ang + 180, speed));
        }
        private static void spawnSHumanoid_3(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(pos.getB() <= 20){
                return;
            }
            String t = "large";
            String c1 = "blue", c2 = "green";
            double ang1 = 90, ang2 = 270;
            double speed = 2.8;
            Vector pol1 = new Vector(ang1, speed);
            Vector pol2 = new Vector(ang2, speed);
            double bound1 = GameDriver.getGameWidth(), bound2 = 0;
            String bm1 = "boundary_right", bm2 = "boundary_left";
            String dID1 = "shumanoid_3_sub_1";
            String dID2 = "shumanoid_3_sub_2";
            constructSHumanoid_3(pos, t, c1, pol1, bound1, bm1, dID1);
            constructSHumanoid_3(pos, t, c2, pol2, bound2, bm2, dID2);
        }
        private static void constructSHumanoid_3(Vector pos, String t, String c, Vector pol, double bound, String boundMode, String dID){
            double angVar = 2.5;
            pos.setB(pos.getB() + MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble()));
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorDouble(boundMode, bound));
            bList.add(new BehaviorString("set_dspawn", dID));
            bList.add(new Behavior("deactivate"));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);
            constructEntity(new Component[]{Comps.pos(pos), Comps.movePol(pol), Comps.ccEP(t), Comps.epSprite(t, c), Comps.tep(), bComp});
        }
        private static void spawnSHumanoid_3_sub_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "blue";

            double bAng = 270;
            double angVar = 12;
            double speedlow = .1, speedhigh = 10;
            double drag = .01;
            double gAccellow = 0.05, gAccelhigh = 0.088;
            int spawns = 7;
            for(int i = 0; i < spawns; i++){
                double ang = bAng + MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble());
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
                double gAccel = MiscUtil.rangeRandom(gAccellow, gAccelhigh, GameDriver.randomDouble());
                Vector iPol = new Vector(ang, speed);
                constructGravityProjectile(pos, t, c, iPol, gAccel, drag);
            }

        }
        private static void spawnSHumanoid_3_sub_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "green";

            double ang = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());

            int spawns = 3;
            double bSpeed = 2.1;
            double speedVar = 1.1;
            for(int i = 0; i < spawns; i++){
                double speed = bSpeed + (i * speedVar);
                Vector pol = new Vector(ang, speed);
                constructStraightProjectile(pos, t, c, pol);
            }
        }
        private static void spawnSCloud_3(Entity e, boolean type){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c;
            if(type){
                c = "teal";
            }else{
                c = "green";
            }

            int sym = 3;
            int spawns = 2;
            double bSpeed = 2.1;
            double speedVar = 1.1;

            Random r = new Random(GameDriver.getSeed() - 1232);
            r.nextDouble();
            double bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double degTick = 2.3;
            if(!type){
                degTick = - degTick;
                bAng_ += 24.32;
            }
            double bAng = MiscUtil.fixAngle(bAng_ + (degTick * GameDriver.getTick()));

            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++){
                    double speed = bSpeed + (j * speedVar);
                    Vector pol = new Vector(ang, speed);
                    constructSingleBounceProjectile(pos, t, c, pol);
                }
            }
        }
        private static void spawnSHumanoid_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c;
            if(GameDriver.randomBoolean()){
                c = "purple";
            }else{
                c = "red";
            }
            int angVar = 2;
            double bAng = 0;
            double ang = bAng + MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble());
            double speedlow = 3.9, speedhigh = 8.5;
            int spawns = 2;
            for(int i = 0; i < spawns; i++) {
                double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)(i + 1))/(spawns + 1));
                constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                constructStraightProjectile(pos, t, c, new Vector(ang + 180, speed));
            }
        }
        private static void spawnSWisp_6(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            if(pos.getB() >= GameDriver.getGameHeight()){
                return;
            }
            String t = "sharp";
            String c1 = "red", c2 = "blue";
            int sym = 5;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = 3.47;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructStraightProjectile(pos, t, c1, new Vector(ang, speed));
                double ang2 = MiscUtil.fixAngle(ang + (180d/sym));
                constructSWisp_6(pos, t, c2, new Vector(ang2, speed));
            }
        }
        private static void constructSWisp_6(Vector pos, String t, String c, Vector iPol){
            int time = 50;
            double fSpeed = 4.2;
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(time)));
            bList.add(new BehaviorDouble("set_polar_to_player", fSpeed));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);
            bComp.addBehavior(new Behavior("rotate_sprite_with_angle"));

            double compound = .98;
            Component_Movement mComp = new Component_Movement(new MoveModeAddVector("polar", iPol),
                    new MoveModeTransVector("speed_compounding", new Vector(1, compound)));

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), bComp, mComp});
        }
        //boss 7
        private static void spawnB7_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "blue";

            int sym = 3;
            double speed = 4.1;
            double degTick = -2.2;
            Random r = new Random(GameDriver.getSeed() - 14);
            r.nextDouble();
            double bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(bAng_ + (degTick * GameDriver.getTick()));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector pol = new Vector(ang, speed);
                constructSingleBounceProjectile(pos, t, c, pol);
            }
        }
        private static void spawnB7_2_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            double out = 100;
            constructB7_2_1(Vector.add(pos, new Vector(out, 0)), true);
            constructB7_2_1(Vector.add(pos, new Vector(out + out, 0)), true);
            constructB7_2_1(Vector.add(pos, new Vector(-out, 0)), false);
            constructB7_2_1(Vector.add(pos, new Vector(-out - out, 0)), false);
        }
        private static void constructB7_2_1(Vector pos, boolean side){
            String t = "sharp";
            String c = "teal";
            double speed = 2.2;
            double degTick = 148.2345d/80;
            double bAng = GameDriver.getTick() * degTick;
            if(side){
                bAng = - bAng;
            }
            constructMultiBounceProjectile(pos, t, c, new Vector(bAng, speed), 2);
        }
        private static void spawnB7_2_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "large";
            String c = "red";

            double speed = 3.5;

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double angVar = 35;

            constructStraightProjectile(pos, t, c, new Vector(bAng, speed));
            constructStraightProjectile(pos, t, c, new Vector(bAng + angVar, speed));
            constructStraightProjectile(pos, t, c, new Vector(bAng - angVar, speed));
        }
        private static void spawnB7_3_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "green";

            int sym = 5;
            double speed = 2.3;
            double degTick = 3.2;
            Random r = new Random(GameDriver.getSeed() - 14);
            r.nextDouble();
            double bAng_ = MiscUtil.rangeRandom(0, 360, r.nextDouble());
            double bAng = MiscUtil.fixAngle(bAng_ + (degTick * GameDriver.getTick()));
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector pol = new Vector(ang, speed);
                constructSingleBounceProjectile(pos, t, c, pol);
            }
        }
        private static void spawnB7_3_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "medium";
            String c = "teal";

            double degTick = -2.15;
            double ang = degTick * GameDriver.getTick();

            double speed = 2.8;
            constructSingleBounceProjectile(pos, t, c, new Vector(ang, speed));
        }
        private static void spawnB7_4(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            boolean rand = GameDriver.randomBoolean();

            constructB7_4(pos, rand, false);
            constructB7_4(pos, !rand, true);
        }
        private static void constructB7_4(Vector pos, boolean type, boolean side){
            String t = "large";
            String c;
            if(type){
                c = "red";
            }else{
                c = "purple";
            }

            int freq = 8;
            String aID;
            if(type){
                aID = "b7_4_sub_1";
            }else{
                aID = "b7_4_sub_2";
            }
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleSpawn(aID));
            Component_Spawning sComp = new Component_Spawning(su);

            double boundTop = 0;
            String boundSideMode;
            double boundSide;
            if(side){
                boundSideMode = "boundary_right";
                boundSide = GameDriver.getGameWidth();
            }else{
                boundSideMode = "boundary_left";
                boundSide = 0;
            }


            double gAccel = .15;
            double drag = .018;
            double gAng1 = 180;
            double gAng3 = 0;
            double gAng2;
            if(side){
                gAng2 = 90;
            }else{
                gAng2 = -90;
            }

            double speed1low = .5, speed1high = 1;
            double speed2low = 8, speed2high = 10.5;
            double speed3low = 6, speed3high = 8;

            double speed1 = MiscUtil.rangeRandom(speed1low, speed1high, GameDriver.randomDouble());
            double speed2 = MiscUtil.rangeRandom(speed2low, speed2high, GameDriver.randomDouble());
            double speed3 = MiscUtil.rangeRandom(speed3low, speed3high, GameDriver.randomDouble());

            //ipol angle
            double ang1 = gAng2;
            double ang2 = 0;
            double ang3 = - ang1;

            Vector iPol1 = new Vector(ang1, speed1);
            Vector iPol2 = new Vector(ang2, speed2);
            Vector iPol3 = new Vector(ang3, speed3);
            Vector iVel1 = Vector.toVelocity(iPol1);
            Vector iVel2 = Vector.toVelocity(iPol2);
            Vector iVel3 = Vector.toVelocity(iPol3);

            MoveModeAddGravity mm1 = new MoveModeAddGravity("gravity", iVel1, gAng1, gAccel, drag);
            MoveModeAddGravity mm2 = new MoveModeAddGravity("gravity", iVel2, gAng2, gAccel, drag);
            MoveModeAddGravity mm3 = new MoveModeAddGravity("gravity", iVel3, gAng3, gAccel, drag);

            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorDouble("boundary_top", boundTop));
            bList.add(new BehaviorMM("set_mm", mm2));
            bList.add(new BehaviorDouble(boundSideMode, boundSide));
            bList.add(new BehaviorMM("set_mm", mm3));
            Routine r = new Routine("routine", bList, false);
            Component_Behavior bComp = new Component_Behavior(r);

            Component_Movement mComp = new Component_Movement(mm1);

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), bComp, mComp, sComp});
        }
        private static void spawnB7_4_sub(Entity e, boolean type){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t;
            String c;
            if(type){
                c = "red";
                t = "sharp";
            }else{
                c = "purple";
                t = "medium";
            }
            if(!type) {
                int spawns = 3;
                double maxDist = 13;
                for (int i = 0; i < spawns; i++) {
                    double rAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                    Vector rPol = new Vector(rAng, MiscUtil.rangeRandom(0, maxDist, GameDriver.randomDouble()));
                    Vector sPos = Vector.add(pos, Vector.toVelocity(rPol));
                    if (!isInBounds(rPol, 0)) {
                        continue;
                    }
                    double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                    double fSpeed = 3.2;
                    double multi = .03;
                    constructStraightProjectileSlowStartGeometric(sPos, t, c, new Vector(ang, fSpeed), multi);
                }
            }
            else{
                if(!isInBounds(pos, 0)){
                    return;
                }
                Vector pastPos = pComp.getOldPosition().deepClone();
                double bAng = Vector.getAngle(pastPos, pos);
                double speed = 3.2;
                double multi = .03;
                constructStraightProjectileSlowStartGeometric(pos, t, c, new Vector(bAng + 90, speed), multi);
                constructStraightProjectileSlowStartGeometric(pos, t, c, new Vector(bAng - 90, speed), multi);
            }
        }
        private static void spawnB7_5(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c = "purple";

            int sym = 40;
            double speed = 2.4;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructSingleBounceProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnB7_6_1(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t1 = "medium";
            String c1 = "purple";
            String t2 = "large";
            String c2 = "red";

            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());

            int sym = 22;
            int spawns = 3;
            double speedlow = 1.8, speedhigh = 4;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                for(int j = 0; j < spawns; j++){
                    double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)(j + 1))/(spawns + 1));
                    constructStraightProjectile(pos, t1, c1, new Vector(ang, speed));
                }
            }

            double speed = 4;
            constructStraightProjectile(pos, t2, c2, new Vector(Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition()), speed));
        }
        private static void spawnB7_6_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            Vector pPos = GameDriver.getCurrentPlayerPosition();

            double elDist = pos.getA();
            double plDist = pPos.getA();
            double erDist = GameDriver.getGameWidth() - pos.getA();
            double prDist = GameDriver.getGameWidth() - pPos.getA();

            double lRatio = elDist/(elDist + plDist);
            double rRatio = erDist/(erDist + prDist);

            double dist = -(pos.getB() - pPos.getB());

            Vector lTarget = new Vector(0, pos.getB() + (dist * lRatio));
            Vector rTarget = new Vector(GameDriver.getGameWidth(), pos.getB() + (dist * rRatio));
            double lAng = Vector.getAngle(pos, lTarget);
            double rAng = Vector.getAngle(pos, rTarget);

            constructB7_6_2(pos, lAng);
            constructB7_6_2(pos, rAng);
        }
        private static void constructB7_6_2(Vector pos, double ang){
            String t = "sharp";
            String c = "red";

            int spawns = 6;
            double bSpeed = 2.5;
            double speedVar = .35;

            for(int i = 0; i < spawns; i++){
                double speed = bSpeed + (speedVar * i);
                constructSingleBounceProjectile(pos, t, c, new Vector(ang, speed));
            }
        }
        private static void spawnB7_7(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "sharp";
            String c;
            if(GameDriver.getTick() % 2 == 0){
                c = "red";
            }else{
                c = "orange";
            }

            double speed1 = 3.4, speed2 = speed1 + .8;

            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            int sym = 12;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                constructSingleBounceProjectile(pos, t, c, new Vector(ang, speed1));
                constructSingleBounceProjectile(pos, t, c, new Vector(ang + 180d/sym, speed2));
            }
        }
        private static void spawnB7_8_1_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "sharp";
            String c = "purple";

            double speed = 2.5;
            constructDownProjectile(pos, t, c, speed);
        }
        private static void spawnB7_8_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "medium";
            String c = "purple";

            int sym = 35;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double speed = 1.9;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * (360d/sym)));
                Vector pol = new Vector(ang, speed);
                constructSingleBounceProjectile(pos, t, c, pol);
            }
        }
        private static void spawnB7_9(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "sharp";
            String c = "teal";

            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double sym = 16;
            double spawns = 5;
            double speedlow = 2.5, speedhigh = 4.3;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < spawns; j++){
                    double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)(j + 1))/(spawns + 1));
                    constructSingleBounceProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnB7_10(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "medium";
            String c1 = "blue";
            String c2 = "red";

            double dist = 40;
            int sym = 23;
            double speed = 2.3;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double angAdd = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                Vector pol = new Vector(ang, speed * .7);
                constructB7_10(pos, t, c2, pol, dist, ang + angAdd, true);

                ang += 180d/sym;
                pol = new Vector(ang, speed);
                constructB7_10(pos, t, c1, pol, dist, ang + angAdd, false);
            }
        }
        private static void constructB7_10(Vector pos, String t, String c, Vector pol, double dist, double ang, boolean dir){
            //so we're just going to do AC and manually figure it out
            double degTick = 94d/60;
            if(!dir){
                degTick = -degTick;
            }
            //take ratio of arc to circumference
            double acSpeed = (degTick/360) * ((dist + dist) * Math.PI);

            Vector pos1 = Vector.add(pos, Vector.toVelocity(new Vector(ang, dist)));
            Vector pos2 = Vector.add(pos, Vector.toVelocity(new Vector(ang, -dist)));
            Vector acPol1 = new Vector(MiscUtil.fixAngle(ang + 90), acSpeed);
            Vector acPol2 = new Vector(MiscUtil.fixAngle(ang - 90), acSpeed);

            ArrayList<MoveModeAdd> mm1 = new ArrayList<>();
            mm1.add(new MoveModeAddAngleChange("angle_change", acPol1, degTick));
            mm1.add(new MoveModeAddVector("polar", pol.deepClone()));
            Component_Movement mComp1 = new Component_Movement(mm1);

            ArrayList<MoveModeAdd> mm2 = new ArrayList<>();
            mm2.add(new MoveModeAddAngleChange("angle_change", acPol2, degTick));
            mm2.add(new MoveModeAddVector("polar", pol.deepClone()));
            Component_Movement mComp2 = new Component_Movement(mm2);

            constructEntity(new Component[]{Comps.pos(pos1), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), mComp1});
            constructEntity(new Component[]{Comps.pos(pos2), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), mComp2});
        }
        private static void spawnB7_11(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "sharp";
            String c = "purple";

            double degTick = 30d/60;
            double bAng = MiscUtil.fixAngle(degTick * GameDriver.getTick());
            double sym = 5;
            double spawns = 4;
            double speedlow = 4, speedhigh = 6;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < spawns; j++){
                    double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)(j + 1))/(spawns + 1));
                    constructMultiBounceProjectile(pos, t, c, new Vector(ang, speed), 2);
                }
            }
        }
        private static void spawnB7_12_1(){
            double speed = 8;
            //top left
            constructB7_12_1(new Vector(0, 0), new Vector(0, speed));
            //top right
            constructB7_12_1(new Vector(GameDriver.getGameWidth(), 0), new Vector(0, speed));
            //bottom left
            constructB7_12_1(new Vector(0, GameDriver.getGameHeight()), new Vector(180, speed));
            //bottom right
            constructB7_12_1(new Vector(GameDriver.getGameWidth(), GameDriver.getGameHeight()), new Vector(180, speed));
        }
        private static void constructB7_12_1(Vector pos, Vector pol){
            String aID = "b7_12_1_sub";
            int freq = 10;
            int tick = (int)(MiscUtil.rangeRandom(0, freq, GameDriver.randomDouble()));
            SpawnUnit su = new SpawnUnit(freq, true, new SISingleTick(tick, aID));
            Component sComp = new Component_Spawning(su);
            double multi = .06;
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), sComp,
                    new Component_Movement(new MoveModeAddVector("polar", pol),
                            new MoveModeTransVector("speed_compounding_to_1", new Vector(0, multi)))});
        }
        private static void spawnB7_12_1_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "sharp";
            String c = "red";
            double ang;
            double speed = 3.5;
            //ukwim
            if(pos.getA() < 100){
                ang = 90;
            }else{
                ang = 270;
            }
            constructStraightProjectile(pos, t, c, new Vector(ang, speed));
        }
        private static void spawnB7_12_2(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            //big
            String t = "large";
            String c = "red";
            double speed = 8.2;
            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double angVar = 21;
            //doubled both sides
            double outSpawns = 5;
            constructStraightProjectile(pos, t, c, new Vector(bAng, speed));
            for(int i = 1; i <= outSpawns; i++){
                double ang1 = MiscUtil.fixAngle(bAng + (i * angVar));
                double ang2 = MiscUtil.fixAngle(bAng - (i * angVar));
                constructStraightProjectile(pos, t, c, new Vector(ang1, speed));
                constructStraightProjectile(pos, t, c, new Vector(ang2, speed));
            }

            //medium
            t = "medium";
            double spawns = 18;
            double speedlow = 1, speedhigh = 3;
            for(int i = 0; i < spawns; i++){
                double ang = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                speed = MiscUtil.rangeRandom(speedlow, speedhigh, GameDriver.randomDouble());
                Vector pol = new Vector(ang, speed);
                constructStraightProjectile(pos, t, c, pol);
            }
        }
        private static void spawnB7_13(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "large";
            String c = "blue";

            int time = 60;
            String dID = "b7_13_sub";
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            double multi = .99;
            double iSpeed = 3;
            int sym = 5;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                Vector iPol = new Vector(ang, iSpeed);
                Component mComp = new Component_Movement(new MoveModeAddBounce("bounce", Vector.toVelocity(iPol)),
                        new MoveModeTransVector("speed_compounding", new Vector(1, multi)));
                ArrayList<Behavior> bList = new ArrayList<>();
                bList.add(new BehaviorVector("timer", new Vector(time)));
                bList.add(new BehaviorString("set_dspawn", dID));
                bList.add(new Behavior("deactivate"));
                Routine r = new Routine("routine", bList, false);
                Component bComp = new Component_Behavior(r);
                constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), mComp, bComp});
            }
        }
        private static void spawnB7_13_sub(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            String t = "sharp";
            String c = "blue";

            double bAng = Vector.getAngle(pos, GameDriver.getCurrentPlayerPosition());
            double sym = 7;
            double spawns = 3;
            double speedlow = 3, speedhigh = 5;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < spawns; j++){
                    double speed = MiscUtil.rangeRandom(speedlow, speedhigh, ((double)(j + 1))/(spawns + 1));
                    constructSingleBounceProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnB7_14(Entity e){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);

            double dist = 20;
            boolean b = GameDriver.randomBoolean();
            double iSpeed = 44;
            double angleVar = 2;
            double speedVar = 5;

            constructB7_14(Vector.add(pos, new Vector(0, dist)),
                    new Vector(90 + MiscUtil.rangeRandom(-angleVar, angleVar, GameDriver.randomDouble()),
                            iSpeed + MiscUtil.rangeRandom(-speedVar, speedVar, GameDriver.randomDouble())), b);
            constructB7_14(Vector.add(pos, new Vector(0, -dist)),
                    new Vector(-90 + MiscUtil.rangeRandom(-angleVar, angleVar, GameDriver.randomDouble()),
                            iSpeed + MiscUtil.rangeRandom(-speedVar, speedVar, GameDriver.randomDouble())), !b);
        }
        private static void constructB7_14(Vector pos, Vector pol, boolean type){
            String t = "large";
            String c;
            if(type){
                c = "blue";
            }else{
                c = "red";
            }
            double speedMulti = .98;
            int freq1 = 4;

            int cd = 30;//for side spawns
            double sideBound = 20;
            double gAccel = .15;
            double drag = .018;
            int lifetime = 120;

            //mcomp initial

            Component_Movement mComp = new Component_Movement(new MoveModeAddBounce("bounce", Vector.toVelocity(pol), 4),
                    new MoveModeTransVector("speed_compounding", new Vector(1, speedMulti)));

            //falling part

            int tick1 = (int)(MiscUtil.rangeRandom(0, freq1, GameDriver.randomDouble()));
            String aID1;
            if(type){
                aID1 = "b7_14_sub_1";
            }else{
                aID1 = "b7_14_sub_2";
            }
            SpawnUnit su1 = new SpawnUnit(freq1, true, new SISingleTick(tick1, aID1));
            Component_Spawning sComp = new Component_Spawning(su1);

            //behaviors for side spawning

            String aID2;
            if(type){
                aID2 = "b7_14_sub_3";
            }else{
                aID2 = "b7_14_sub_4";
            }
            ArrayList<Behavior> bListl = new ArrayList<>();
            bListl.add(new BehaviorDouble("boundary_left", sideBound));
            bListl.add(new BehaviorString("single_spawn", aID2));
            bListl.add(new BehaviorVector("timer", new Vector(cd)));
            Routine l = new Routine("routine", bListl, true);

            ArrayList<Behavior> bListr = new ArrayList<>();
            bListr.add(new BehaviorDouble("boundary_right", GameDriver.getGameWidth() - sideBound));
            bListr.add(new BehaviorString("single_spawn", aID2));
            bListr.add(new BehaviorVector("timer", new Vector(cd)));
            Routine r = new Routine("routine", bListr, true);

            //behavior for falling

            MoveModeAddGravity mm = new MoveModeAddGravity("gravity", new Vector(), gAccel, drag);

            ArrayList<Behavior> bListMain = new ArrayList<>();
            bListMain.add(new BehaviorVector("timer", new Vector(lifetime)));
            bListMain.add(new Behavior("clear_spawning"));
            bListMain.add(new BehaviorMM("continue_trajectory_gravity", mm));
            bListMain.add(new BehaviorString("delete_component", "behavior"));
            Routine main = new Routine("routine", bListMain, false);

            ArrayList<Behavior> masterList = new ArrayList<>();
            masterList.add(main);
            masterList.add(l);
            masterList.add(r);
            Component_Behavior bComp = new Component_Behavior(masterList);

            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.ccEP(t), Comps.epSprite(t, c), bComp, mComp, sComp});

        }
        private static void spawnB7_14_sub_1(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String t = "sharp";
            String c;
            double speed;
            if(b){
                c = "blue";
                speed = 2.4;
            }else{
                c = "red";
                speed = 3.4;
            }

            double speedVar = .25;
            double angVar = 3;

            constructStraightProjectile(pos, t, c, new Vector(0 + MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble()),
                    speed + MiscUtil.rangeRandom(-speedVar, speedVar, GameDriver.randomDouble())));
            constructStraightProjectile(pos, t, c, new Vector(180 + MiscUtil.rangeRandom(-angVar, angVar, GameDriver.randomDouble()),
                    speed + MiscUtil.rangeRandom(-speedVar, speedVar, GameDriver.randomDouble())));
        }
        private static void spawnB7_14_sub_3(Entity e, boolean b){
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            String t = "medium";
            String c;
            double bSpeed;
            int sym;
            int spawns;
            if(b){
                c = "blue";
                bSpeed = 2;
                sym = 20;
                spawns = 2;
            }else{
                c = "red";
                bSpeed = 3;
                sym = 15;
                spawns = 3;
            }
            double speedVar = .5;
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                for(int j = 0; j < spawns; j++){
                    double speed = bSpeed + (speedVar * j);
                    constructStraightProjectile(pos, t, c, new Vector(ang, speed));
                }
            }
        }
        private static void spawnB7_15(Entity e){
            //e in case want stuff in center      //i do
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            b7_15_helper(pos);
            b7_15_helper(GameDriver.getCurrentPlayerPosition().deepClone());
        }
        private static void b7_15_helper(Vector pos){
            String c = "purple";
            String t = "sharp";
            double dist = 90;
            double bound = dist + 10;

            if(pos.getA() < bound){
                pos.setA(bound);
            }
            else if(pos.getA() > GameDriver.getGameWidth() - bound){
                pos.setA(GameDriver.getGameWidth() - bound);
            }
            if(pos.getB() < bound){
                pos.setB(bound);
            }
            else if(pos.getB() > GameDriver.getGameHeight() - bound){
                pos.setB(GameDriver.getGameHeight() - bound);
            }

            int bounces = 3;
            double speed = 1.8;
            double speedMulti = .08;
            int sym = 30;
            double bAng = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
            for(int i = 0; i < sym; i++){
                double ang = MiscUtil.fixAngle(bAng + (i * 360d/sym));
                Vector sPos = Vector.add(pos, Vector.toVelocity(new Vector(ang, dist)));
                Vector pol = new Vector(ang, speed);
                constructB7_15(sPos, t, c, pol, bounces, speedMulti);
            }
        }
        private static void constructB7_15(Vector pos, String t, String c, Vector pol, int bounces, double speedMulti){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                    new Component_Movement(new MoveModeAddBounce("bounce_bottom", Vector.toVelocity(pol), bounces),
                            new MoveModeTransVector("speed_compounding_to_1", new Vector(0, speedMulti)))});
        }

        private static void constructDownProjectile(Vector pos, String type, String color, double speed){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color),
                    Comps.moveVel(new Vector(0, speed))});
        }
        private static void constructStraightProjectile(Vector pos, String type, String color, Vector polar){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color), Comps.movePol(polar)});
        }
        private static void constructStraightProjectileSlowStart(Vector pos, String type, String color, Vector polar, double inc){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color),
                    new Component_Movement(new MoveModeAddVector("polar", polar), new MoveModeTransVector("speed_incrementing_to_1", new Vector(0, inc)))});
        }
        private static void constructStraightProjectileSlowStartGeometric(Vector pos, String type, String color, Vector polar, double multi){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color),
                    new Component_Movement(new MoveModeAddVector("polar", polar), new MoveModeTransVector("speed_compounding_to_1", new Vector(0, multi)))});
        }
        private static void constructCurvingProjectile(Vector pos, String t, String c, Vector iPol, double ac){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                    Comps.moveAngleChange(iPol, ac)});
        }
        private static void constructHomingProjectile(Vector pos, String type, String color, Vector initPolar, double initAngleChange, double angleChangeCompound){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color),
                    Comps.moveHomingCompounding(initPolar, GameDriver.getCurrentPlayerPosition(), initAngleChange, angleChangeCompound)});
        }
        private static void constructDecayingCurveProjectile(Vector pos, String type, String color, Vector iPolar, double initAngleChange, double angleChangeCompound){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color),
                    Comps.moveAngleChangeCompounding(iPolar, initAngleChange, angleChangeCompound)});
        }
        private static void constructSingleBounceProjectile(Vector pos, String type, String color, Vector polar){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color),
                    new Component_Movement(new MoveModeAddBounce("bounce", Vector.toVelocity(polar)))});
        }
        private static void constructMultiBounceProjectile(Vector pos, String type, String color, Vector polar, int bounces){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(type), Comps.ccEP(type), Comps.epSprite(type, color),
                    new Component_Movement(new MoveModeAddBounce("bounce", Vector.toVelocity(polar), bounces))});
        }
        private static void constructGravityProjectile(Vector pos, String t, String c, Vector initPolar, double gAccel, double dragMulti){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                    new Component_Movement(new MoveModeAddGravity("gravity", Vector.toVelocity(initPolar), gAccel, dragMulti))});
        }
        private static void constructGravityDirProjectile(Vector pos, String t, String c, Vector initPolar, double gAng, double gAccel, double drag){
            constructEntity(new Component[]{Comps.pos(pos), Comps.tep(), Comps.sharpRotate(t), Comps.ccEP(t), Comps.epSprite(t, c),
                    new Component_Movement(new MoveModeAddGravity("gravity", Vector.toVelocity(initPolar), gAng, gAccel, drag))});
        }

        private static String randomRainbowColor(){
            double r = GameDriver.randomDouble();
            if(r < 1d/6){
                return "red";
            }
            if(r < 2d/6){
                return "orange";
            }
            if(r < 3d/6){
                return "yellow";
            }
            if(r < 4d/6){
                return "green";
            }
            if(r < 5d/6){
                return "blue";
            }
            return "purple";
        }
        private static boolean isInBounds(Vector pos, int bound){
            return (pos.getA() >= bound && pos.getA() <= GameDriver.getGameWidth() - bound && pos.getB() >= bound && pos.getB() <= GameDriver.getGameHeight() - bound);
        }
    }
    private static class PickupSpawns{

        private static void spawnPowerSmallRNG(Entity e, double chance){
            //chance -> 1/5 = .2, so lower than chance = spawns
            if(GameDriver.randomDouble() < chance){
                spawnPowerSmall(e, false);
            }
        }
        private static void spawnPowerLargeRNG(Entity e){
            if (GameDriver.randomBoolean()) {
                spawnPowerLarge(e, false);
            }else{
                spawnPowerSmall(e, false);
            }
        }
        private static void spawnLife(Entity e){
            spawnPickup(Component_Pickup.PickupType.LIFE, e, false);
        }
        private static void spawnBomb(Entity e){
            spawnPickup(Component_Pickup.PickupType.BOMB, e, false);
        }
        private static void spawnPowerLarge(Entity e, boolean playerSpawned){
            spawnPickup(Component_Pickup.PickupType.POWER_LARGE, e, playerSpawned);
        }
        private static void spawnPowerSmall(Entity e, boolean playerSpawned){
            spawnPickup(Component_Pickup.PickupType.POWER_SMALL, e, playerSpawned);
        }
        private static void spawnPickup(Component_Pickup.PickupType type, Entity e, boolean playerSpawned){
            if(!playerSpawned) {
                double angleOut = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double distOut = MiscUtil.rangeRandom(0, 20, GameDriver.randomDouble());
                Vector polarOut = new Vector(angleOut, distOut);
                Vector spawnPos = Vector.add(getPos(e), Vector.toVelocity(polarOut));
                constructPickup(type, spawnPos, -5, false);
            }
            else{
                double angleOut = MiscUtil.rangeRandom(0, 360, GameDriver.randomDouble());
                double distOut = MiscUtil.rangeRandom(0, 100, GameDriver.randomDouble());
                Vector polarOut = new Vector(angleOut, distOut);
                Vector spawnPos = Vector.add(getPos(e), Vector.toVelocity(polarOut));

                constructPickup(type, spawnPos, -7, true);
            }
        }
        private static void constructPickup(Component_Pickup.PickupType type, Vector spawnPos, double baseSpeed, boolean playerSpawned){
            spawnPos = spawnPos.deepClone();
            checkX(spawnPos);
            double y = spawnPos.getB();
            //larger ratio = higher up on the screen
            double speed;
            if(!playerSpawned) {
                double ratio = (777 - y) / 777;
                double randMulti = (GameDriver.randomDouble() * .2);
                speed = baseSpeed * (1 + (ratio * 0.5) + randMulti);
            }
            else{
                //higher = lower for playerSpawned
                double ratio = y/777;
                double randMulti = (GameDriver.randomDouble() * .2);
                speed = baseSpeed * (1 + (ratio * .8) + randMulti);
            }
            Component_Movement mComp = new Component_Movement(new MoveModeAddDouble("pickup_movement", speed));
            Component_Graphics gComp = null;
            Component_Collision cComp = null;
            switch(type){
                case LIFE:
                    gComp = Comps.sprite("life", new Vector(-20, -20));
                    cComp = Comps.ccBasic(34);
                    break;
                case BOMB:
                    gComp = Comps.sprite("bomb", new Vector(-20, -20));
                    cComp = Comps.ccBasic(34);
                    break;
                case POWER_LARGE:
                    gComp = Comps.sprite("power_large", new Vector(-20, -20));
                    cComp = Comps.ccBasic(34);
                    break;
                case POWER_SMALL:
                    gComp = Comps.sprite("power_small", new Vector(-14, -14));
                    cComp = Comps.ccBasic(23.3);
                    break;
            }

            constructEntity(new Component[]{Comps.pos(spawnPos), mComp, gComp, cComp, new Component_Pickup(type), Comps.tp()});
        }
        private static void checkX(Vector v){
            double bound = 12.5;
            double randMax = 5;
            double x = v.getA();
            if(x < bound){
                v.setA(bound + (GameDriver.randomDouble() * randMax));
            }
            else if(x > GameDriver.getGameWidth() - bound){
                v.setA((GameDriver.getGameWidth() - bound) - (GameDriver.randomDouble() * randMax));
            }
        }
    }
    private static class BackgroundSpawns{
        private static int[] imgLengths = new int[]{5817, 5277, 6387, 5577, 6627, 5277, 9327};
        private static double[] scrollSpeeds = new double[]{36, 30, 51, 30, 45, 75, 45};
        private static Vector wh = new Vector(666, 777);

        private static void spawnBackground(int stage){
            int startPos = imgLengths[stage - 1] - 777;
            SImageSubImage simage = new SImageSubImage("background_s" + stage, new Vector(0,0),
                    new Vector(0, startPos), wh.deepClone());
            Component_Graphics gComp = new Component_Graphics(simage);
            //background_scroll behavior
            constructEntity(new Component[]{Comps.pos(new Vector(0,0)), gComp,
                    new Component_Behavior(new BehaviorVector("background_scroll", new Vector(scrollSpeeds[stage - 1]/60, startPos)))});

            //putting the music in here, i guess it makes sense since backgrounds exclusively start at the beginning of stages
            String trackName = "stage" + GameDriver.getCurrentStage();
            MusicDriver.playTrack(trackName, true);
        }
    }
    private static class MiscSpawns{
        //using type boss to draw above everything else
        private static void spawnHPBar(){
            //reads blackboard to find the hp total message
            double hp = 0;
            String id = null;
            ArrayList<BoardMessage> al = SystemManager.getECSBoard().getMessages();
            for(BoardMessage bm : al){
                if(bm.getSender().equals("gameplay")){
                    if(bm.getMessage().equals("boss_hp")){
                        try{
                            hp = (double)bm.getData()[0];
                            id = (String)bm.getData()[1];
                            break;
                        }catch(Exception e){
                            return;
                        }
                    }
                }
            }
            if(id == null || hp <= 0){
                return;
            }
            Vector pos = new Vector(33, 20);
            Component_Graphics gComp = new Component_Graphics(new SImageSubImage("hp_bar", new Vector(), new Vector()));
            ArrayList<Behavior> b = new ArrayList<>();
            ArrayList<Behavior> r1 = new ArrayList<>();
            ArrayList<Behavior> r2 = new ArrayList<>();

            r1.add(new BehaviorHPBar("hp_bar_graphics", id, hp));
            r1.add(new Behavior("deactivate"));

            r2.add(new BehaviorInt("wait_game_message", 7));
            r2.add(new Behavior("deactivate"));

            b.add(new Routine("routine", r1, false));
            b.add(new Routine("routine", r2, false));
            Component_Behavior bComp = new Component_Behavior(b);

            constructEntity(new Component[]{Comps.pos(pos), gComp, bComp, Comps.tb()});
        }
        //using type player to draw above boss
        private static void spawnSwordSwipe(Entity e){
            //position is the boss's position
            Component_Position pComp = (Component_Position) ComponentManager.getComponent("position", e.getEntityID());
            if (pComp == null) {
                return;
            }
            Vector pos = getPos(e);
            String id = "sword_swipe";
            int lifetime = 5;
            double xOff = MiscUtil.rangeRandom(-12, 2, GameDriver.randomDouble());
            double yOff = MiscUtil.rangeRandom(0, 12, GameDriver.randomDouble());
            Vector offset = new Vector(xOff, yOff);
            double rotate = MiscUtil.rangeRandom(-180, 0, GameDriver.randomDouble());
            Component_Graphics gComp = new Component_Graphics(new SImage(id, new Vector(), offset, 1, rotate, 1));

            constructEntity(new Component[]{Comps.pos(pos), gComp, Comps.lifetime(lifetime), Comps.tplayer()});
        }
        private static void spawnGameEnder(){
            int waitTime = 200;
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(waitTime)));
            bList.add(new Behavior("end_game"));
            bList.add(new Behavior("deactivate"));
            Routine r = new Routine("routine", bList, false);
            constructEntity(new Component[]{new Component_Behavior(r)});
        }
    }

    private static class Comps {

        private static Component_Position pos(Vector pos){
            return new Component_Position(pos.deepClone());
        }

        private static Component_Type tpp(){
            return new Component_Type(EntityType.PLAYER_P);
        }
        private static Component_Type tpb(){
            return new Component_Type(EntityType.PLAYER_B);
        }
        private static Component_Type te(){
            return new Component_Type(EntityType.ENEMY);
        }
        private static Component_Type tep(){
            return new Component_Type(EntityType.ENEMY_P);
        }
        private static Component_Type tb(){
            return new Component_Type(EntityType.BOSS);
        }
        private static Component_Type tp(){
            return new Component_Type(EntityType.PICKUP);
        }
        private static Component_Type tplayer(){return new Component_Type(EntityType.PLAYER);};

        private static Component_Movement moveVel(Vector vel){
            return new Component_Movement(new MoveModeAddVector("velocity", vel));
        }
        private static Component_Movement movePol(Vector pol){
            return new Component_Movement(new MoveModeAddVector("polar", pol));
        }
        private static Component_Movement movePath(ArrayList<Vector> points, double speed){
            return new Component_Movement(new MoveModeAddPath("path", points, speed));
        }
        private static Component_Movement movePathLooping(ArrayList<Vector> points, double speed){
            return new Component_Movement(new MoveModeAddPath("path_looping", points, speed));
        }
        private static Component_Movement movePathFinalPolar(ArrayList<Vector> points, double speed){
            return new Component_Movement(new MoveModeAddPath("path_final_polar", points, speed));
        }
        private static Component_Movement movePoint(Vector point, double speed){
            return new Component_Movement(new MoveModeAddPoint("point", point, speed));
        }
        private static Component_Movement moveHoming(Vector polar, Vector target, double angleChange){
            return new Component_Movement(new MoveModeAddHoming("homing", polar, angleChange, target));
        }
        private static Component_Movement moveHomingCompounding(Vector polar, Vector target, double initAngleChange, double compound){
            return new Component_Movement(new MoveModeAddHoming("homing_compounding", polar, initAngleChange, compound, target));
        }
        private static Component_Movement moveAngleChangeCompounding(Vector polar, double initAngleChange, double compound){
            return new Component_Movement(new MoveModeAddAngleChange("angle_change_compounding", polar, initAngleChange, compound));
        }
        private static Component_Movement moveAngleChange(Vector polar, double angleChange){
            return new Component_Movement(new MoveModeAddAngleChange("angle_change", polar, angleChange));
        }
        private static Component_Movement moveGravity(Vector iPol, double gAng, double gAccel, double drag){
            return new Component_Movement(new MoveModeAddGravity("gravity", Vector.toVelocity(iPol), gAng, gAccel, drag));
        }

        private static Component_Collision ccBasic(double radius){
            return new Component_Collision(new AABB(radius), 1, 1);
        }
        private static Component_Collision ccSquareProj(double radius, double dmg){
            return new Component_Collision(new AABB(radius), 1, dmg);
        }
        private static Component_Collision ccSquare(double radius, double hp, double dmg){
            return new Component_Collision(new AABB(radius), hp, dmg);
        }
        private static Component_Collision ccXYProj(double xRad, double yRad, double dmg){
            return ccXY(xRad, yRad, 1, dmg);
        }
        private static Component_Collision ccXY(double xRad, double yRad, double hp, double dmg){
            return new Component_Collision(new AABB(-xRad, xRad, -yRad, yRad), hp, dmg);
        }
        private static Component_Collision ccMob(String id, double hp){
            switch(id){
                case "automaton":
                case "auto":
                    return ccXY(18, 25, hp, 1);
                case "bird":
                    return ccXY(24, 10, hp, 1);
                case "cloud":
                case "shadow_cloud":
                    return ccXY(24, 15, hp, 1);
                case "crystal":
                case "crystal_red":
                case "crystal_yellow":
                    return ccXY(21, 35, hp, 1);
                case "ember":
                    return new Component_Collision(new AABB(-30, 31, -34, 28), hp, 1);
                case "fairy":
                    return ccXY(20, 25, hp, 1);
                case "shadow_humanoid":
                    return ccXY(25, 35, hp, 1);
                case "water_big":
                    return ccSquare(25, hp, 1);
                case "water_small":
                    return ccSquare(18, hp, 1);
                case "wisp":
                case "shadow_wisp":
                default:
                    return ccSquare(24, hp, 1);
            }
        }
        private static Component_Collision ccBoss(){
            return ccXY(25, 50, 10000000, 1);
        }
        private static Component_Collision ccEP(String type){
            switch(type){
                case "sharp":
                    return ccSquareProj(5.7, 1);
                case "small":
                    return ccSquareProj(5.3, 1);
                case "medium":
                    return ccSquareProj(9.3, 1);
                case "large":
                default:
                    return ccSquareProj(17.8, 1);
            }

        }

        private static Component_Graphics sprite(String id){
            return new Component_Graphics(new SImage(id));
        }
        private static Component_Graphics sprite(String id, Vector offset){
            return new Component_Graphics(new SImage(id, new Vector(), offset));
        }
        private static Component_Graphics sprite(String id, Vector offset, double scale){
            return new Component_Graphics(new SImage(id, new Vector(), offset, scale, 0, 1));
        }
        private static Component_Graphics mobSprite(String id){
            switch(id){
                case "automaton":
                case "auto":
                    Vector offset = new Vector(-16, -19);
                    int maxTick = 17;
                    return loopAnim("automaton", 3, offset,1.4, maxTick);
                case "bird":
                    offset = new Vector(-25, -9);
                    return sprite("bird", offset);
                case "cloud":
                    offset = new Vector(-24, -16);
                    maxTick = 22;
                    return loopAnim("cloud", 3, offset, maxTick);
                case "crystal_red":
                    offset = new Vector(-19, -40);
                    maxTick = 10;
                    return loopAnim("crystal_red", 3, offset, maxTick);
                case "crystal_yellow":
                    offset = new Vector(-19, -40);
                    maxTick = 10;
                    return loopAnim("crystal_yellow", 3, offset, maxTick);
                case "ember":
                    offset = new Vector(-34, -38);
                    return sprite("ember", offset);
                case "fairy":
                    offset = new Vector(-18, -18);
                    maxTick = 17;
                    return loopAnim("fairy", 3, offset, 1.4,maxTick);
                case "shadow_cloud":
                    offset = new Vector(-24, -16);
                    maxTick = 22;
                    return loopAnim("shadow_cloud", 3, offset, maxTick);
                case "shadow_humanoid":
                    offset = new Vector(-25, -25);
                    return sprite("shadow_humanoid", offset, 1.5);
                case "shadow_wisp":
                    offset = new Vector(-15, -15);
                    return sprite("shadow_wisp", offset, 1.7);
                //lol
                case "trap":
                    offset = new Vector(-34, -34);
                    maxTick = 2;
                    int degMulti = 15;
                    ArrayList<SImage> frames = new ArrayList<>();
                    for(int i = 0; i < 19; i++){
                        frames.add(new SImage("trap_" + (i + 1), new Vector(), offset, 1, degMulti * i, 1));
                    }
                    return loopAnim(frames, maxTick);
                case "water_big":
                    offset = new Vector(-30, -30);
                    return sprite("water_1", offset);
                case "water_small":
                    offset = new Vector(-20, -20);
                    return sprite("water_2", offset);
                case "wisp":
                default:
                    offset = new Vector(-15, -15);
                    return sprite("wisp", offset, 1.7);
            }
        }
        private static Component_Graphics bossSprite(int stage){
            double sizeMulti = 1.1;
            switch(stage){
                case 1:
                    Vector offset = new Vector(-40, -40);
                    ArrayList<SImage> al = new ArrayList<>();
                    al.add(new SImage("e1_idle", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e1_left", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e1_right", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    return new Component_GraphicsMultiSprite(al, 0);
                case 2:
                    int maxTick = 18;
                    offset = new Vector(-38, -38);
                    al = new ArrayList<>();
                    al.add(new SImage("e2_idle_1", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e2_idle_2", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e2_idle_3", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    return new Component_GraphicsLoopAnim(al, maxTick);
                case 3:
                    offset = new Vector(-40, -37);
                    al = new ArrayList<>();
                    al.add(new SImage("e3_idle", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e3_left", new Vector(), Vector.add(offset, new Vector(0, 2)), sizeMulti, 0, 1));
                    al.add(new SImage("e3_right", new Vector(), Vector.add(offset, new Vector(0, 2)), sizeMulti, 0, 1));
                    return new Component_GraphicsMultiSprite(al, 0);
                case 4:
                    offset = new Vector(-43, -55);
                    al = new ArrayList<>();
                    al.add(new SImage("e4_idle", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e4_left", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e4_right", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    return new Component_GraphicsMultiSprite(al, 0);
                case 5:
                    maxTick = 18;
                    offset = new Vector(-38, -40);
                    al = new ArrayList<>();
                    al.add(new SImage("e5_idle_1", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e5_idle_2", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    al.add(new SImage("e5_idle_3", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    return new Component_GraphicsLoopAnim(al, maxTick);
                case 6:
                    maxTick = 15;
                    offset = new Vector(-38, -48);
                    ArrayList<SImage> a1 = new ArrayList<>();
                    ArrayList<SImage> a2 = new ArrayList<>();
                    ArrayList<SImage> a3 = new ArrayList<>();
                    a1.add(new SImage("e6_idle_1", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    a1.add(new SImage("e6_idle_2", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    a1.add(new SImage("e6_idle_3", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    a2.add(new SImage("e6_left_1", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    a2.add(new SImage("e6_left_2", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    a2.add(new SImage("e6_left_3", new Vector(), offset.deepClone(), sizeMulti, 0, 1));
                    a3.add(new SImage("e6_right_1", new Vector(), Vector.add(offset, new Vector(-4, 0)), sizeMulti, 0, 1));
                    a3.add(new SImage("e6_right_2", new Vector(), Vector.add(offset, new Vector(-4, 0)), sizeMulti, 0, 1));
                    a3.add(new SImage("e6_right_3", new Vector(), Vector.add(offset, new Vector(-4, 0)), sizeMulti, 0, 1));
                    ArrayList<ArrayList<SImage>> l = new ArrayList<>();
                    l.add(a1);
                    l.add(a2);
                    l.add(a3);
                    return new Component_GraphicsMultiAnim(l, 0, maxTick);
                case 7:
                default:
                    offset = new Vector(-37, -42);
                    return new Component_Graphics(new SImage("e7", new Vector(), offset, sizeMulti, 0, 1));
            }
        }
        private static Component_Graphics epSprite(String type, String color){
            //////colors//////
            //    white
            //    red
            //    orange
            //    yellow
            //    green
            //    blue
            //    teal
            //    purple
            //////////////////
            Vector offset = new Vector();
            switch(type){
                case "small":
                    offset = new Vector(-8, -8);
                    break;
                case "medium":
                    offset = new Vector(-16, -16);
                    break;
                case "large":
                    offset = new Vector(-30, -30);
                    break;
                case "sharp":
                default:
                    offset = new Vector(-8, -16);
                    break;
            }
            return sprite("ep_" + type + "_" + color, offset);
        }
        private static Component_Graphics ppSprite(String id, Vector offset){
            return new Component_Graphics(new SImage(id, new Vector(), offset, 1, 0, .8));
        }
        private static Component_Graphics ppSpriteRotate(String id, Vector offset, double rotation){
            return new Component_Graphics(new SImage(id, new Vector(), offset, 1, rotation, .8));
        }
        private static Component_Graphics loopAnim(ArrayList<SImage> frames, int maxTick){
            return new Component_GraphicsLoopAnim(frames, maxTick);
        }
        private static Component_Graphics loopAnim(String[] ids, Vector offset, int maxTick){
            ArrayList<SImage> frames = new ArrayList<>();
            for(String s : ids){
                frames.add(new SImage(s, new Vector(), offset));
            }
            return loopAnim(frames, maxTick);
        }
        private static Component_Graphics loopAnim(String id, int num, Vector offset, int maxTick){
            String[] ids = new String[num];
            for(int i = 1; i <= num; i++){
                ids[i - 1] = id + "_" + i;
            }
            return loopAnim(ids, offset, maxTick);
        }
        private static Component_Graphics loopAnim(String[] ids, Vector offset, double scale, int maxTick){
            ArrayList<SImage> frames = new ArrayList<>();
            for(String s : ids){
                frames.add(new SImage(s, new Vector(), offset, scale, 0, 1));
            }
            return loopAnim(frames, maxTick);
        }
        private static Component_Graphics loopAnim(String id, int num, Vector offset, double scale, int maxTick){
            String[] ids = new String[num];
            for(int i = 1; i <= num; i++){
                ids[i - 1] = id + "_" + i;
            }
            return loopAnim(ids, offset, scale, maxTick);
        }

        private static Component_DeathSpawn dSpawn(String id){
            return new Component_DeathSpawn(id);
        }

        private static Component_Behavior lifetime(int time){
            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorVector("timer", new Vector(time)));
            al.add(new Behavior("deactivate"));
            return new Component_Behavior(new Routine("routine", al, false));
        }
        private static Component_Behavior lifetimeDspawn(int time, String spawn){
            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorVector("timer", new Vector(time)));
            al.add(new BehaviorString("set_dspawn", spawn));
            al.add(new Behavior("deactivate"));
            return new Component_Behavior(new Routine("routine", al, false));
        }
        private static Component_Behavior trapLifetime(){
            return lifetime((19 * 2) - 1);
        }
        private static Component_Behavior wispSpinning(){
            return spriteSpinning(8);
        }
        private static Component_Behavior spriteSpinning(double rSpeed){
            return new Component_Behavior(new BehaviorDouble("rotate_sprite", rSpeed));
        }
        private static Component_Behavior spriteRotating(){
            return new Component_Behavior(new Behavior("rotate_sprite_with_angle"));
        }
        private static Component_Behavior sharpRotate(String sharp){
            if(sharp.equals("sharp")){
                return spriteRotating();
            }
            return null;
        }
        //timer 1 = between arrival and shooting
        //timer 2 = between beginning of shooting and fPolar
        private static Component_Behavior singleAttack(Vector attackPoint, double speed, int delay1, int delay2, Vector finalPolar, SpawnUnit spawn){
            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorMM("set_mm", new MoveModeAddPoint("point", attackPoint, speed)));
            al.add(new BehaviorVector("wait_near_position", attackPoint));
            if(delay1 != 0) {
                al.add(new BehaviorVector("timer", new Vector(delay1)));
            }
            al.add(new BehaviorSU("add_spawn_unit", spawn));
            if(delay2 != 0) {
                al.add(new BehaviorVector("timer", new Vector(delay2)));
            }
            al.add(new BehaviorVector("set_polar", finalPolar));
            return new Component_Behavior(new Routine("routine", al, false));
        }
        private static Component_Behavior singleSpawnDelay(int delay, String attack){
            ArrayList<Behavior> al = new ArrayList<>();
            al.add(new BehaviorVector("timer", new Vector(delay)));
            al.add(new BehaviorSU("set_su", new SpawnUnit(1, false, new SISingleSpawn(attack))));
            Routine r = new Routine("routine", al, false);
            return new Component_Behavior(r);
        }
        private static Component_Behavior timerAimPlayer(int delay, double endSpeed){
            ArrayList<Behavior> bList = new ArrayList<>();
            bList.add(new BehaviorVector("timer", new Vector(delay)));
            bList.add(new BehaviorDouble("set_polar_to_player", endSpeed));
            Routine r = new Routine("routine", bList, false);
            return new Component_Behavior(r);
        }
    }

    //deepclone pos vector
    private static Vector getPos(Entity e){
        return ((Component_Position)(ComponentManager.getComponent("position", e.getEntityID()))).getPrevPosition().deepClone();
    }
    //this returns the literal vector
    private static Vector getRealPos(Entity e){
        return ((Component_Position)(ComponentManager.getComponent("position", e.getEntityID()))).getPosition();
    }

    //original intended use of returning String value for linking entities e.g. wasp homing detector
    private static String constructEntity(Component[] comps){
        String id = EntityManager.addEntity();
        for(Component c : comps) {
            if (c != null) {
                ComponentManager.setComponent(c, id);
            }
        }
        return id;
    }
    //for if you premake the id yourself
    private static void constructEntity(Component[] comps, String id){
        for(Component c : comps) {
            if (c != null) {
                ComponentManager.setComponent(c, id);
            }
        }
    }

    @Override
    public ComponentFilter getCompfilter(){
        return COMPFILTER;
    }

    public static ComponentFilter getCompFilter(){
        return COMPFILTER;
    }
}

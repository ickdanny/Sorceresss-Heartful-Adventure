import java.util.ArrayList;

public class System_EntityCollision implements ECSSystem {
    private static final ComponentFilter COMPFILTER = new ComponentFilter(new String[]{"position", "collision", "type"});

    public System_EntityCollision(){
    }

    @Override
    public void update(ArrayList<Entity> entities) {
        QuadTree quadTree = new QuadTree(0, new AABB(0, GameDriver.getGameWidth(), 0, GameDriver.getGameHeight()));

        /////////////////////////////////////
        ArrayList<Component_Collision> toSend = new ArrayList<>();

        for(Entity e : entities){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            Component_Collision cComp = (Component_Collision)ComponentManager.getComponent("collision", e.getEntityID());
            if(cComp == null || pComp == null){
                continue;
            }
//            Component_Type tComp = (Component_Type)ComponentManager.getComponent("type", e.getEntityID());

            //calculate true hitbox and twoframe
            AABB oldTruePos = cComp.getTruePos();
            //use previous position
            cComp.setTruePos(AABB.calculateTruePos(pComp.getPrevPosition(), cComp.getHitbox()));

            cComp.setTwoFrame(AABB.calculateTwoFrame(oldTruePos, cComp.getTruePos()));

            //quadtree time
            ArrayList<Entity> collidedWithEList = new ArrayList<>();
            quadTree.insert(e, collidedWithEList, false);
            for(Entity collidedWithE : collidedWithEList){
                collideBehavior(e, collidedWithE);
            }

            //////////////////////////
            toSend.add(cComp);
        }

        ///////////////////////////////////////
        GraphicsDriver.collisionUpdate(toSend, quadTree);
    }

    private void collideBehavior(Entity a, Entity b){
        EntityType aType = ((Component_Type)ComponentManager.getComponent("type", a.getEntityID())).getType();
        EntityType bType = ((Component_Type)ComponentManager.getComponent("type", b.getEntityID())).getType();
        switch(aType){
            case PLAYER:
                switch(bType){
                    case ENEMY:
                    case BOSS:
                        collidePlayerEnemy(a, b);
                        break;
                    case ENEMY_P:
                        collidePlayerEnemyP(a, b);
                        break;
                    case PICKUP:
                        collidePlayerPickup(a, b);
                        break;
                }
                break;
            case PLAYER_P:
                switch(bType){
                    case ENEMY:
                        collidePlayerPEnemy(a, b);
                        break;
                    case BOSS:
                        collidePlayerPBoss(a, b);
                        break;
                }
                break;
            case PLAYER_B:
                switch(bType){
                    case ENEMY:
                        collidePlayerBEnemy(a, b);
                        break;
                    case BOSS:
                        collidePlayerBBoss(a, b);
                        break;
                    case ENEMY_P:
                        collidePlayerBEnemyP(a, b);
                        break;
                }
                break;
            case ENEMY:
                switch(bType){
                    case PLAYER:
                        collidePlayerEnemy(b, a);
                        break;
                    case PLAYER_P:
                        collidePlayerPEnemy(b, a);
                        break;
                    case PLAYER_B:
                        collidePlayerBEnemy(b, a);
                        break;
                }
                break;
            case BOSS:
                switch(bType){
                    case PLAYER:
                        collidePlayerEnemy(b, a);
                        break;
                    case PLAYER_P:
                        collidePlayerPBoss(b, a);
                        break;
                    case PLAYER_B:
                        collidePlayerBBoss(b, a);
                        break;
                }
                break;
            case ENEMY_P:
                switch(bType){
                    case PLAYER:
                        collidePlayerEnemyP(b, a);
                        break;
                    case PLAYER_B:
                        collidePlayerBEnemyP(b, a);
                        break;
                }
                break;
            case PICKUP:
                switch(bType){
                    case PLAYER:
                        collidePlayerPickup(b, a);
                        break;
                }
                break;
        }
    }

    private void collidePlayerEnemy(Entity player, Entity enemy){
//        Component_Collision pcc = ((Component_Collision)ComponentManager.getComponent("collision", player.getEntityID()));
//        Component_Collision ecc = ((Component_Collision)ComponentManager.getComponent("collision", enemy.getEntityID()));
//
//        pcc.setHp(pcc.getHp() - ecc.getDmg());
//        //player system will handle negative hp value
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerEnemy", 1, this, new Object[]{player, enemy}));
    }
    private void collidePlayerEnemyP(Entity player, Entity projectile){
//        projectile.deactivate();
//        Component_Collision pcc = ((Component_Collision)ComponentManager.getComponent("collision", player.getEntityID()));
//        Component_Collision epcc = ((Component_Collision)ComponentManager.getComponent("collision", projectile.getEntityID()));
//
//        pcc.setHp(pcc.getHp() - epcc.getDmg());
//        //player system will handle negative hp value
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerEnemyP", 1, this, new Object[]{player, projectile}));

    }
    private void collidePlayerPickup(Entity player, Entity pickup){
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerPickup", 1, this, new Object[]{player, pickup}));
    }
    private void collidePlayerPEnemy(Entity projectile, Entity enemy){
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerPEnemy", 1, this, new Object[]{projectile, enemy}));
    }
    private void collidePlayerPBoss(Entity projectile, Entity boss){
//        projectile.deactivate();
//        Component_Collision bcc = ((Component_Collision)ComponentManager.getComponent("collision", boss.getEntityID()));
//        Component_Collision pcc = ((Component_Collision)ComponentManager.getComponent("collision", projectile.getEntityID()));
//
//        bcc.setHp(bcc.getHp() - pcc.getDmg());
//        //boss system will handle negative hp values
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerPBoss", 1, this, new Object[]{projectile, boss}));
    }
    private void collidePlayerBEnemy(Entity bomb, Entity enemy){
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerBEnemy", 1, this, new Object[]{bomb, enemy}));
    }
    private void collidePlayerBBoss(Entity bomb, Entity boss){
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerBBoss", 1, this, new Object[]{bomb, boss}));
    }
    private void collidePlayerBEnemyP(Entity bomb, Entity projectile){
        SystemManager.getECSBoard().write(new BoardMessage("collidePlayerBEnemyP", 1, this, new Object[]{bomb, projectile}));
    }

    @Override
    public ComponentFilter getCompfilter(){
        return COMPFILTER;
    }

    public static ComponentFilter getCompFilter(){
        return COMPFILTER;
    }
}

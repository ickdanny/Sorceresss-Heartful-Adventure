import java.util.ArrayList;

public class System_InternalMessage implements ECSSystem {
    public System_InternalMessage(){}

    private static int PLAYER_DEATH_TIMER = 15;

    @Override
    public void update(ArrayList<Entity> entities){
        //read and tick ECS blackboard
        Blackboard ECSBoard = SystemManager.getECSBoard();

        //need to shallow clone the arrayList for us to modify the board
        for(BoardMessage m : (ArrayList<BoardMessage>)ECSBoard.getMessages().clone()){
            parseMessage(m, entities);
        }

        ECSBoard.update();
    }

    private void parseMessage(BoardMessage message, ArrayList<Entity> entities){
        Object sender = message.getSender();
        if(sender instanceof System_EntityCollision){
            parseCollision(message);
        }
        else if(sender.equals("gameplay")){
            parseGameplayMessage(message, entities);
        }
        else if(sender instanceof System_InternalMessage){
            parseSelfMessage(message);
        }
    }

    private void parseCollision(BoardMessage message){
        //message = type of collision
        //data 1 and 2 = entities
        switch(message.getMessage()){
            case "collidePlayerEnemy":
                Entity player = ((Entity)message.getData()[0]);
                Component_Player pc = ((Component_Player)ComponentManager.getComponent("player", player.getEntityID()));
                if(pc.canBeHit()) {
                    SystemManager.getECSBoard().write(new BoardMessage("playerDeathTimer", PLAYER_DEATH_TIMER, this));
                }
                //write collided message even if the player cannot be hit - they still collide
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerEnemy", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
            case "collidePlayerEnemyP":
                player = ((Entity)message.getData()[0]);
                pc = ((Component_Player)ComponentManager.getComponent("player", player.getEntityID()));
                if(pc.canBeHit()) {
                    SystemManager.getECSBoard().write(new BoardMessage("playerDeathTimer", PLAYER_DEATH_TIMER, this));
                }
                Entity projectile = ((Entity)message.getData()[1]);
                projectile.deactivate();
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerEnemyP", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
            case "collidePlayerPickup":
                player = ((Entity)message.getData()[0]);
                Entity pickup = ((Entity)message.getData()[1]);
                pc = ((Component_Player)ComponentManager.getComponent("player", player.getEntityID()));
                //can input -> don't pick up the new power spawns when you're dead
                if(pc.canInput()) {
                    switch (((Component_Pickup) ComponentManager.getComponent("pickup", pickup.getEntityID())).getType()) {
                        case POWER_SMALL:
                            pc.setPower(Math.min(pc.getPower() + 1, GameDriver.getMaxPower()));
                            break;
                        case POWER_LARGE:
                            pc.setPower(Math.min(pc.getPower() + 8, GameDriver.getMaxPower()));
                            break;
                        case LIFE:
                            pc.setLives(Math.min(pc.getLives() + 1, GameDriver.getMaxLives()));
                            break;
                        case BOMB:
                            pc.setBombs(Math.min(pc.getBombs() + 1, GameDriver.getMaxBombs()));
                            break;
                    }
                    pickup.deactivate();
                }
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerPickup", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
            case "collidePlayerPEnemy":
                projectile = ((Entity)message.getData()[0]);
                if(!projectile.isActive()){
                    break;
                }
                Entity enemy = ((Entity)message.getData()[1]);
                projectile.deactivate();
                Component_Collision ecc = ((Component_Collision)ComponentManager.getComponent("collision", enemy.getEntityID()));
                Component_Collision pcc = ((Component_Collision)ComponentManager.getComponent("collision", projectile.getEntityID()));
                ecc.setHp(ecc.getHp() - pcc.getDmg());
                if(ecc.getHp() <= 0){
                    enemy.deactivate();
                }
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerPEnemy", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
            case "collidePlayerPBoss":
                projectile = ((Entity)message.getData()[0]);
                if(!projectile.isActive()){
                    break;
                }
                Entity boss = ((Entity)message.getData()[1]);
                projectile.deactivate();
                ecc = ((Component_Collision)ComponentManager.getComponent("collision", boss.getEntityID()));
                pcc = ((Component_Collision)ComponentManager.getComponent("collision", projectile.getEntityID()));
                ecc.setHp(ecc.getHp() - pcc.getDmg());
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerPBoss", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
            //assume bomb will do spread damage as opposed to single instance
            case "collidePlayerBEnemy":
                Entity bomb = ((Entity)message.getData()[0]);
                enemy = ((Entity)message.getData()[1]);
                ecc = ((Component_Collision)ComponentManager.getComponent("collision", enemy.getEntityID()));
                Component_Collision bcc = ((Component_Collision)ComponentManager.getComponent("collision", bomb.getEntityID()));
                ecc.setHp(ecc.getHp() - bcc.getDmg());
                if(ecc.getHp() <= 0){
                    enemy.deactivate();
                }
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerBEnemy", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
            case "collidePlayerBBoss":
                bomb = ((Entity)message.getData()[0]);
                boss = ((Entity)message.getData()[1]);
                ecc = ((Component_Collision)ComponentManager.getComponent("collision", boss.getEntityID()));
                bcc = ((Component_Collision)ComponentManager.getComponent("collision", bomb.getEntityID()));
                ecc.setHp(ecc.getHp() - bcc.getDmg());
                //boss behavior detects neg hp
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerBBoss", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
            case "collidePlayerBEnemyP":
                ((Entity)(message.getData()[1])).deactivate();
                SystemManager.getECSBoard().write(new BoardMessage("collidedLastTickPlayerBEnemyP", 2, "gameplay", new Object[]{((Entity)message.getData()[0]).getEntityID(), ((Entity)message.getData()[1]).getEntityID()}));
                break;
        }
    }
    private void parseGameplayMessage(BoardMessage message, ArrayList<Entity> entities){
        switch(message.getMessage()){
            //case "bomb": //currently no use
            case "bombPress":
            case "playerDeathLastTick":
                SystemManager.getECSBoard().delete("playerDeathTimer");
                SystemManager.getECSBoard().delete("playerDeath");
                break;
            case "clear":
                for(Entity e : entities){
                    Component_Type tComp = (Component_Type)(ComponentManager.getComponent("type", e.getEntityID()));
                    if(tComp != null){
                        if(tComp.getType() == EntityType.ENEMY_P){
                            e.deactivate();
                        }
                    }
                }
                break;
        }
    }

    private void parseSelfMessage(BoardMessage message){
        switch(message.getMessage()){
            case "playerDeathTimer":
                //when tickdown -> zero = remove
                if(message.getLifetime() <= 1){
                    //lets make the timer internal but the death gameplay
                    //playerDeath message is received by System_Player
                    SystemManager.getECSBoard().write(new BoardMessage("playerDeath", 2, "gameplay"));
                    SystemManager.getECSBoard().delete("playerDeathTimer");
                }
        }
    }


    @Override
    public ComponentFilter getCompfilter(){
        return null;
    }
}

import java.util.ArrayList;

//deactivates entities... removal is done in cleanup
public class System_EntityDespawning implements ECSSystem {

    private static final ComponentFilter COMPFILTER  = new ComponentFilter(new String[]{"position", "type"});

    public System_EntityDespawning(){
    }

    @Override
    public void update(ArrayList<Entity> entities){
        for(Entity e : entities){
            Component_Type tComp = (Component_Type)(ComponentManager.getComponent("type", e.getEntityID()));
            switch(tComp.getType()){
                case PLAYER_P:
//                case PLAYER_B:
                case ENEMY_P:
                case ENEMY:
                    projectileBounds(e);
                    break;
                case PICKUP:
                    pickupBounds(e);
                    break;
            }
        }
    }

    private void projectileBounds(Entity e){
        Component_Position pComp = (Component_Position)(ComponentManager.getComponent("position", e.getEntityID()));
        Vector pos = pComp.getPrevPosition();
        int boundary = 50;
        if(pos.getA() < -boundary || pos.getA() > boundary + GameDriver.getGameWidth()
                || pos.getB() < -boundary || pos.getB() > boundary + GameDriver.getGameHeight()){
            e.deactivate();
            //no death_spawn
            ComponentManager.deleteComponent("death_spawn", e.getEntityID());
        }
    }

    private void pickupBounds(Entity e){
        Component_Position pComp = (Component_Position)(ComponentManager.getComponent("position", e.getEntityID()));
        Vector pos = pComp.getPrevPosition();
        int boundary = 50;
        //lower = higher
        if(pos.getB() > boundary + GameDriver.getGameHeight()){
            e.deactivate();
            //no death_spawn
            ComponentManager.deleteComponent("death_spawn", e.getEntityID());
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

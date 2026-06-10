import java.util.ArrayList;

//tells entityManager to remove inactive entities
public class System_EntityCleanup implements ECSSystem {

    private static final ComponentFilter COMPFILTER = new ComponentFilter(new String[]{});

    public System_EntityCleanup(){
    }

    @Override
    public void update(ArrayList<Entity> entities){
        for(Entity e : entities){
            if(!e.isActive()){
                //death behavior using ghost entity
                deathSpawn(e);

                //entityManager will tell componentManager to clear all associated components
                EntityManager.deleteEntity(e.getEntityID());
            }
        }
    }

    private void deathSpawn(Entity e){
        Object test = ComponentManager.getComponent("death_spawn", e.getEntityID());
        if(test != null){
            Entity ghost = EntityManager.addEntityRetEntity();
            //killed next tick
            ghost.deactivate();

            Component_DeathSpawn d = (Component_DeathSpawn)test;
            //spawningComponent (and possibly position)
            SISingleSpawn instruction = new SISingleSpawn(d.getSpawnID());
            SpawnUnit unit = new SpawnUnit(0, false, instruction);
            Component_Spawning spawnC = new Component_Spawning(unit);
            ComponentManager.setComponent(spawnC, ghost.getEntityID());

            //position
            test = ComponentManager.getComponent("position", e.getEntityID());
            if(test != null){
                //using the literal same position component, as opposed to cloning
                Component_Position posC = (Component_Position)test;
                ComponentManager.setComponent(posC, ghost.getEntityID());
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
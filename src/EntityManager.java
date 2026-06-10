import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class EntityManager {
    //storage for entities
    //arraylist - iterate through once, sort systems, deletion iterate once
    private static ArrayList<Entity> entities;
    private static int counter;

    //creation and deletion of entities
    public static String addEntity(){
        return addEntityRetEntity().getEntityID();
    }

    public static Entity addEntityRetEntity(){
        Entity e = new Entity("" + counter);
        counter++;
        entities.add(e);
        return e;
    }

    public static void deleteEntity(String entityID){
        Iterator<Entity> iter = entities.iterator();
        while(iter.hasNext()){
            if(iter.next().getEntityID().equals(entityID)){
                iter.remove();
                ComponentManager.deleteAllComponents(entityID);
                break;
            }
        }
    }

    public static Entity getEntity(String entityID){
        for(Entity e : entities){
            if(e.getEntityID().equals(entityID)){
                return e;
            }
        }
        return null;
    }

    //iterate through entities once, and filter for each system
    public static HashMap<ComponentFilter, ArrayList<Entity>> filterEntities(ArrayList<ComponentFilter> filters){
        HashMap<ComponentFilter, ArrayList<Entity>> toRet = new HashMap<>();
        for(ComponentFilter filter : filters){
            toRet.put(filter, new ArrayList<>());
        }
        for(Entity e : entities){
            for(ComponentFilter f : filters){
                if(f == null || f.filterEntity(e.getEntityID())){
                    toRet.get(f).add(e);
                }
            }
        }
        return toRet;
    }

    public static void init(){
        entities = new ArrayList<>();
        counter = Integer.MIN_VALUE;
    }
}
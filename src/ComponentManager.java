import java.util.*;

public class ComponentManager {
    //component storage as map((componentType), map(entityid, component)) //array instead?
    private static HashMap<String, HashMap<String, Component>> components;

    //creation and deletion of components
    public static void setComponent(Component c, String entityID){
        //if map already contains mapping, will overwrite
        components.get(c.getComponentType()).put(entityID, c);
    }
    public static void deleteComponent(String type, String entityID){
        if(components.get(type) != null) {
            components.get(type).remove(entityID);
        }
    }
    //delete all components for a given entity; effectively clearing the entity
    public static void deleteAllComponents(String entityID){
        //for each type of component
        for(HashMap<String, Component> componentMap : mapsAsArrayList()){
            //delete the component given the entityID
            componentMap.remove(entityID);
        }
    }
    public static Component getComponent(String type, String entityID){
        return components.get(type).get(entityID);
    }

    public static void clear(){
        for(HashMap h : mapsAsArrayList()){
            h.clear();
        }
    }

    //returns components as an arrayList of hashmaps
    private static ArrayList<HashMap<String, Component>> mapsAsArrayList(){
        return new ArrayList<>(components.values());
    }

    //init maps for each component type
    public static void init(){
        components = new HashMap<>();

        //initiating maps for each component type
        components.put("behavior", new HashMap<>());
        components.put("collision", new HashMap<>());
        components.put("death_spawn", new HashMap<>());
        components.put("graphics", new HashMap<>());
        components.put("movement", new HashMap<>());
        components.put("pickup", new HashMap<>());
        components.put("player", new HashMap<>());
        components.put("position", new HashMap<>());
        components.put("spawning", new HashMap<>());
        components.put("type", new HashMap<>());
    }
}
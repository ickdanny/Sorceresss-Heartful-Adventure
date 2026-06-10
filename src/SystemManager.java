import java.util.ArrayList;
import java.util.HashMap;

//systems within game loop only
public class SystemManager {
    //storage for systems (in update order)
    private static ArrayList<ECSSystem> systems;

    private static Blackboard ECSBoard;
    private static ArrayList<ComponentFilter> compFilters;
    private static HashMap<ComponentFilter, ArrayList<Entity>> sortedEntities;

    //update every system
    //ECSBoard updates and management is done in System_InternalMessage not here
    public static void update(){
        sortedEntities = EntityManager.filterEntities(compFilters);

        for(ECSSystem s : systems){
            s.update(sortedEntities.get(s.getCompfilter()));
        }
    }

    public static Blackboard getECSBoard() {
        return ECSBoard;
    }
    public static HashMap<ComponentFilter, ArrayList<Entity>> getSortedEntities() {
        return sortedEntities;
    }

    //initialize systems; add in update order
    public static void init(){
        ECSBoard = new Blackboard();
        systems = new ArrayList<>();
        compFilters = new ArrayList<>();
        sortedEntities = new HashMap<>();

        systems.add(new System_EntityMovement());
        systems.add(new System_EntityDespawning());
        systems.add(new System_EntityBehavior());
        systems.add(new System_EntitySpawning());
        systems.add(new System_EntityCollision());
        systems.add(new System_EntityGraphics());
        //putting this here for now
        systems.add(new System_Player());
        systems.add(new System_InternalMessage());

        //comes last or else may get null components
        systems.add(new System_EntityCleanup());

        initCompFilters();
    }

    private static void initCompFilters(){
        for(ECSSystem s : systems){
            ComponentFilter c = s.getCompfilter();
//            //filter out duplicates
//            //null can be added | or maybe it doesn't matter?
//            if(compFilters.isEmpty()){
//                compFilters.add(c);
//            }
//            else{
//                boolean contained = false;
//                if(c == null){
//                    contained = true;
//                }
//                else {
//                    for (ComponentFilter check : compFilters) {
//                        //if c is already contained in compFilters don't add it
//                        if (c.equals(check)) {
//                            contained = true;
//                        }
//                    }
//                }
//                if(!contained){
//                    compFilters.add(c);
//                }
//            }
            //since hashmap would treat identical compFilters as separate objects, should be no big deal
            compFilters.add(c);
        }
    }

    public static ArrayList<ComponentFilter> getCompFilters() {
        return compFilters;
    }
}
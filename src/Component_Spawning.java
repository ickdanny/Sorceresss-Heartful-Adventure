import java.util.ArrayList;

public class Component_Spawning implements Component {
    //each spawn unit is a timer (for most attacks you only have 1)
    private ArrayList<SpawnUnit> spawnUnits;

    public Component_Spawning(){
        spawnUnits = new ArrayList<>();
    }
    public Component_Spawning(SpawnUnit unit){
        spawnUnits = new ArrayList<>();
        spawnUnits.add(unit);
    }
    public Component_Spawning(ArrayList<SpawnUnit> spawnUnits){
        this.spawnUnits = spawnUnits;
    }

    public ArrayList<SpawnUnit> getSpawnUnits() {
        return spawnUnits;
    }

    public void addUnit(SpawnUnit unit){
        spawnUnits.add(unit);
    }
    public void removeUnit(SpawnUnit unit){
        spawnUnits.remove(unit);
    }
    public void clear(){
        spawnUnits.clear();
    }

    @Override
    public String getComponentType(){
        return "spawning";
    }
}
public class Component_DeathSpawn implements Component {
    private String spawnID;

    public Component_DeathSpawn(String spawnID) {
        this.spawnID = spawnID;
    }

    public String getSpawnID() {
        return spawnID;
    }

    @Override
    public String getComponentType(){
        return "death_spawn";
    }
}

public class Entity {
    private String entityID;
    private boolean active;

    public Entity(String entityID){
        this.entityID = entityID;
        active = true;
    }

    public String getEntityID(){
        return entityID;
    }
    public boolean isActive() {
        return active;
    }
    public void deactivate(){
        active = false;
    }
}
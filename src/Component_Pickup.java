public class Component_Pickup implements Component {

    private PickupType type;

    public Component_Pickup(PickupType type){
        this.type = type;
    }

    public PickupType getType() {
        return type;
    }

    public void setType(PickupType type) {
        this.type = type;
    }

    @Override
    public String getComponentType(){
        return "pickup";
    }

    public enum PickupType{
        POWER_SMALL, POWER_LARGE, LIFE, BOMB
    }
}
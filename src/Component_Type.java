public class Component_Type implements Component {
    private EntityType type;

    public Component_Type(EntityType type){
        this.type = type;
    }

    public EntityType getType() {
        return type;
    }

    @Override
    public String getComponentType(){
        return "type";
    }
}
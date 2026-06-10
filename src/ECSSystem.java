import java.util.ArrayList;

public interface ECSSystem {
    void update(ArrayList<Entity> entities);
    //null for systems that operate outside of components; metasystems
    ComponentFilter getCompfilter();
}
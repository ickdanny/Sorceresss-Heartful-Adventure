import java.util.Arrays;

public class ComponentFilter {

    //list of components that will get filtered (i.e. position + graphics)
    private String[] filter;
    public ComponentFilter(String[] filter){
        this.filter = filter;
    }

    //true if entity contains every component in filter
    //if filter is empty returns true always
    public boolean filterEntity(String entityID){
        for(String s : filter){
            if(ComponentManager.getComponent(s, entityID) == null){
                return false;
            }
        }
        return true;
    }

    public String[] getFilter() {
        return filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentFilter that = (ComponentFilter) o;
        return Arrays.equals(filter, that.filter);
    }

    @Override
    public String toString(){
        String toRet = "";
        for(String s : filter){
            toRet = toRet.concat(", " + s);
        }
        if(toRet.contains(",")){
            toRet = toRet.substring(2);
        }
        return toRet;
    }
}
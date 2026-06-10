import java.util.ArrayList;

public class System_EntityGraphics implements ECSSystem {

    private static final ComponentFilter COMPFILTER = new ComponentFilter(new String[]{"graphics"});

    public System_EntityGraphics(){
    }

    @Override
    public void update(ArrayList<Entity> entities){
        for(Entity e : entities){
            Component_Graphics gComp = (Component_Graphics)ComponentManager.getComponent("graphics", e.getEntityID());

            //update animated sprites
            if(gComp instanceof Component_GraphicsLoopAnim){
                ((Component_GraphicsLoopAnim) gComp).setTick(((Component_GraphicsLoopAnim) gComp).getTick() + 1);
                if(((Component_GraphicsLoopAnim) gComp).getTick() >= ((Component_GraphicsLoopAnim) gComp).getMaxTick()){
                    ((Component_GraphicsLoopAnim) gComp).setTick(0);
                    int nextIndex = ((Component_GraphicsLoopAnim) gComp).getIndex();
                    if(nextIndex >= ((Component_GraphicsLoopAnim) gComp).getCurrentAnim().size() - 1){
                        nextIndex = 0;
                    }
                    else{
                        ++nextIndex;
                    }
                    ((Component_GraphicsLoopAnim) gComp).setIndex(nextIndex);
                    gComp.setCurrentSImage(((Component_GraphicsLoopAnim) gComp).getCurrentAnim().get(nextIndex));
                }
            }

            //changing anim / sprite done by behavior
        }
    }

    @Override
    public ComponentFilter getCompfilter(){
        return COMPFILTER;
    }

    public static ComponentFilter getCompFilter(){
        return COMPFILTER;
    }
}
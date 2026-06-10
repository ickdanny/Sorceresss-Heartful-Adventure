import java.util.ArrayList;
//import java.util.HashMap;

public class Component_GraphicsMultiAnim extends Component_GraphicsLoopAnim {
    private ArrayList<ArrayList<SImage>> anims;

    public Component_GraphicsMultiAnim(ArrayList<ArrayList<SImage>> anims, int initAnim, int maxTick){
        super(anims.get(initAnim), maxTick);
        this.anims = anims;
    }

    public void setCurrentAnim(int anim){
        setCurrentAnim(anims.get(anim));
    }
}

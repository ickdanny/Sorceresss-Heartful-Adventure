import java.util.ArrayList;
//import java.util.HashMap;

public class Component_GraphicsMultiSprite extends Component_Graphics {
    //instead of a hashmap can just use an array
//    private HashMap<String, SImage> sImages;
    private ArrayList<SImage> sImages;
    public Component_GraphicsMultiSprite(ArrayList<SImage> sImages, int initImage){
        super(sImages.get(initImage));
        this.sImages = sImages;
    }

    public void setCurrentSImage(int image){
        this.setCurrentSImage(sImages.get(image));
    }
}
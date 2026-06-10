public class Component_Graphics implements Component {
    //position vector in simage is moot; use position component instead within ECS
    private SImage currentSImage;

    public Component_Graphics(SImage currentSImage) {
        this.currentSImage = currentSImage;
    }

    public SImage getCurrentSImage() {
        return currentSImage;
    }

    public void setCurrentSImage(SImage currentSImage) {
        this.currentSImage = currentSImage;
    }

    @Override
    public String getComponentType(){
        return "graphics";
    }
}
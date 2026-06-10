//a class which holds an image (in the form of a string) as well as the position to draw the image
public class SImage implements DrawableElement{

    private String image;
    private Vector position;
    //add to position to get true position
    private Vector offset;
    private double scale;
    private double rotation;
    //1 = fully opaque, 0 = fully transparent
    private double transparency;

    private SImage(){
        image = null;
        position = null;
        offset = null;
        scale = 0;
        rotation = 0;
        transparency = 0;
    }

    public SImage(String image){
        this.image = image;
        this.position = new Vector(0, 0);
        this.offset = new Vector(0, 0);
        this.scale = 1;
        this.rotation = 0;
        this.transparency = 1;
    }

    public SImage(String image, Vector position){
        this.image = image;
        this.position = position;
        this.offset = new Vector(0, 0);
        this.scale = 1;
        this.rotation = 0;
        this.transparency = 1;
    }

    public SImage(String image, Vector position, Vector offset){
        this.image = image;
        this.position = position;
        this.offset = offset;
        this.scale = 1;
        this.rotation = 0;
        this.transparency = 1;
    }

    public SImage(String image, Vector position, Vector offset, double scale, double rotation, double transparency) {
        this.image = image;
        this.position = position;
        this.offset = offset;
        this.scale = scale;
        this.rotation = rotation;
        this.transparency = transparency;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public void setPosition(Vector position) {
        this.position = position;
    }
    public void setOffset(Vector offset) {
        this.offset = offset;
    }
    public void setScale(double scale) {
        this.scale = scale;
    }
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public String getImage() {
        return image;
    }
    public Vector getPosition() {
        return position;
    }
    public Vector getOffset() {
        return offset;
    }
    public Vector getTruePosition() {
        return Vector.add(position, offset);
    }
    public double getRotation() {
        return rotation;
    }
    public double getScale() {
        return scale;
    }
    public double getTransparency() {
        return transparency;
    }

    //the position should be changed elsewhere
    public SImage deepClone(){
        SImage toRet = new SImage();
        toRet.image = image;
        toRet.position = position.deepClone();
        toRet.offset = offset.deepClone();
        toRet.scale = scale;
        toRet.rotation = rotation;
        toRet.transparency = transparency;
        return toRet;
    }
}
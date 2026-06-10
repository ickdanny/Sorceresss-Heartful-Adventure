//to display the background
public class SImageSubImage extends SImage {
    private Vector subPos;
    //width height
    private Vector wh;

    public SImageSubImage(String image, Vector subPos, Vector wh) {
        super(image);
        this.subPos = subPos;
        this.wh = wh;
    }
    public SImageSubImage(String image, Vector position, Vector subPos, Vector wh) {
        super(image, position);
        this.subPos = subPos;
        this.wh = wh;
    }
    public SImageSubImage(String image, Vector position, Vector offset, Vector subPos, Vector wh) {
        super(image, position, offset);
        this.subPos = subPos;
        this.wh = wh;
    }
    public SImageSubImage(String image, Vector position, Vector offset, double scale, double rotation, double transparency, Vector subPos, Vector wh) {
        super(image, position, offset, scale, rotation, transparency);
        this.subPos = subPos;
        this.wh = wh;
    }

    public Vector getSubPos() {
        return subPos;
    }
    public void setSubPos(Vector subPos) {
        this.subPos = subPos;
    }
    public Vector getWh() {
        return wh;
    }
    public void setWh(Vector wh) {
        this.wh = wh;
    }
}

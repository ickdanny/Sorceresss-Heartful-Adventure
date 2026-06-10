//pretty much not gonna have a lot of features -> initial use case is solely dialogue boxes
public class SText implements DrawableElement{

    private String toDraw;
    private Vector position;
    //maximum characters per line
    private int maxChars;
    private int fontSize;

    public SText() {
        toDraw = "";
        position = null;
        maxChars = fontSize = -1;
    }
    public SText(String toDraw, Vector position, int fontSize) {
        this.toDraw = toDraw;
        this.position = position;
        maxChars = -1;
        this.fontSize = fontSize;
    }
    public SText(String toDraw, Vector position, int maxChars, int fontSize) {
        this.toDraw = toDraw;
        this.position = position;
        this.maxChars = maxChars;
        this.fontSize = fontSize;
    }

    public String getToDraw() {
        return toDraw;
    }
    public void setToDraw(String toDraw) {
        this.toDraw = toDraw;
    }
    public Vector getPosition() {
        return position;
    }
    public void setPosition(Vector position) {
        this.position = position;
    }
    public int getMaxChars() {
        return maxChars;
    }
    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }
    public int getFontSize() {
        return fontSize;
    }
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}

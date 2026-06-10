public class MoveModeAddString extends MoveModeAdd {
    private String string;
    public MoveModeAddString(String mode, String string){
        super(mode);
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}

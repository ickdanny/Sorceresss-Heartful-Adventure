public class BehaviorString extends Behavior {
    private String string;
    public BehaviorString(String mode, String string){
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

public class BehaviorInt extends Behavior {
    private int integer;
    public BehaviorInt(String mode, int integer){
        super(mode);
        this.integer = integer;
    }

    public int getInt(){
        return integer;
    }
    public void setInt(int integer){
        this.integer = integer;
    }
}

public class Behavior {
    private String mode;

    public Behavior(String mode){
        this.mode = mode;
    }

    public String getMode(){
        return mode;
    }

    @Override
    public String toString(){
        return mode;
    }
}
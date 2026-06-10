public class BehaviorMM extends Behavior {

    private MoveMode mm;

    public BehaviorMM(String mode, MoveMode mm) {
        super(mode);
        this.mm = mm;
    }

    public MoveMode getMM() {
        return mm;
    }

    public void setMM(MoveMode mm) {
        this.mm = mm;
    }
}

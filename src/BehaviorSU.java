public class BehaviorSU extends Behavior {
    private SpawnUnit su;

    public BehaviorSU(String mode, SpawnUnit su) {
        super(mode);
        this.su = su;
    }

    public SpawnUnit getSU() {
        return su;
    }

    public void setSU(SpawnUnit su) {
        this.su = su;
    }
}

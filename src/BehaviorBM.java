public class BehaviorBM extends Behavior {
    private BoardMessage bm;

    public BehaviorBM(String mode, BoardMessage bm) {
        super(mode);
        this.bm = bm;
    }

    public BoardMessage getBm() {
        return bm;
    }

    public void setBm(BoardMessage bm) {
        this.bm = bm;
    }
}

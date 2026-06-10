public class SIArrayTick extends SpawningInstruction {
    private int[] ticks;

    public SIArrayTick(int[] ticks, String spawnID) {
        super(spawnID);
        this.ticks = ticks;
    }

    public int[] getTicks() {
        return ticks;
    }
}

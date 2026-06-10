public class SISingleTick extends SpawningInstruction {
    private int tick;
    public SISingleTick(int tick, String spawnID){
        super(spawnID);
        this.tick = tick;
    }

    public int getTick(){
        return tick;
    }
}
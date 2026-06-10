//no fuckin clue how to name this

import java.util.ArrayList;

//each instance of this class is basically a timer
public class SpawnUnit {
    //tick goes from 0 to maxTick - 1
    private int tick;
    private int maxTick;
    private boolean loop;

    private ArrayList<SpawningInstruction> spawningInstructions;

    public SpawnUnit(int maxTick, boolean loop){
        tick = 0;
        this.maxTick = maxTick;
        this.loop = loop;
        spawningInstructions = new ArrayList<>();
    }
    public SpawnUnit(int maxTick, boolean loop, SpawningInstruction si){
        this(maxTick, loop);
        spawningInstructions.add(si);
    }
    public SpawnUnit(int maxTick, boolean loop, ArrayList<SpawningInstruction> spawningInstructions){
        tick = 0;
        this.maxTick = maxTick;
        this.loop = loop;
        this.spawningInstructions = spawningInstructions;
    }

    public ArrayList<SpawningInstruction> getSpawningInstructions() {
        return spawningInstructions;
    }
    public int getMaxTick() {
        return maxTick;
    }
    public int getTick() {
        return tick;
    }
    public void setTick(int tick) {
        this.tick = tick;
    }
    public boolean isLoop() {
        return loop;
    }
}
public class SIMultipleTick extends SpawningInstruction {
    //lowerBound and upperBound are inclusive
    private int lowerBound, upperBound, multiple;
    public SIMultipleTick(int multiple, String spawnID){
        super(spawnID);
        lowerBound = upperBound = -1;
        this.multiple = multiple;
    }
    public SIMultipleTick(int lowerBound, int upperBound, int multiple, String spawnID){
        super(spawnID);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.multiple = multiple;
    }

    public int getLowerBound() {
        return lowerBound;
    }
    public int getUpperBound() {
        return upperBound;
    }
    public int getMultiple() {
        return multiple;
    }
}

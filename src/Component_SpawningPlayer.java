//for attack, we can use the spawnComponent naturally as we just add a spawnUnit whenever z is pressed...
//for bombing we can also read the playerComponent and make it such that when the player is invulnerable you cannot bomb
public class Component_SpawningPlayer extends Component_Spawning {

    private int shotType;
    public Component_SpawningPlayer(int shotType){
        super();
        this.shotType = shotType;
    }

    public int getShotType() {
        return shotType;
    }
}

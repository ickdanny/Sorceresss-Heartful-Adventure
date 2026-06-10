public class Component_Player implements Component {
    private int lives, bombs, power;

    //0 = can be hit
    private int noHitTimer;
    //means what you think it means - used for player death
    private int noInputTimer;

    public Component_Player(int lives, int bombs) {
        this.lives = lives;
        this.bombs = bombs;
        power = 0;
        noHitTimer = 0;
        noInputTimer = 0;
    }
    public Component_Player(int lives, int bombs, int power) {
        this.lives = lives;
        this.bombs = bombs;
        this.power = power;
        noHitTimer = 0;
        noInputTimer = 0;
    }

    public int getLives() {
        return lives;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }
    public int getBombs() {
        return bombs;
    }
    public void setBombs(int bombs) {
        this.bombs = bombs;
    }
    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }

    //player system ticks noHit
    public void setNoHitTimer(int noHitTimer) {
        this.noHitTimer = noHitTimer;
    }
    public int getNoHitTimer() {
        return noHitTimer;
    }
    public int getNoInputTimer() {
        return noInputTimer;
    }
    public void setNoInputTimer(int noInputTimer) {
        this.noInputTimer = noInputTimer;
    }

    public boolean canBeHit(){
        return noHitTimer == 0;
    }
    public boolean canInput(){
        return noInputTimer == 0;
    }

    public String getComponentType(){
        return "player";
    }
}
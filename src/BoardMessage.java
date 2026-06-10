//3 parts
//1: String for the message
//2: int for lifetime
//3: Object for sender (or null)
public class BoardMessage {
    private String message;
    //-1 = permanent
    private int lifetime;
    private Object sender;
    private Object[] data;

    public BoardMessage(String message, int lifetime, Object sender){
        this.message = message;
        this.lifetime = lifetime;
        this.sender = sender;
    }
    public BoardMessage(String message, Object sender){
        this.message = message;
        this.lifetime = 1;
        this.sender = sender;
    }
    public BoardMessage(String message, int lifetime, Object sender, Object[] data){
        this.message = message;
        this.lifetime = lifetime;
        this.sender = sender;
        this.data = data;
    }
    public BoardMessage(String message, Object sender, Object[] data){
        this.message = message;
        this.lifetime = 1;
        this.sender = sender;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }
    public int getLifetime() {
        return lifetime;
    }
    public Object getSender() {
        return sender;
    }
    public Object[] getData(){
        return data;
    }

    //false = safe, true = delete message
    //lifetime of 1 = lasts for 1 tick
    //lifetime of -1 = permanent message
    public boolean tickDown(){
        if(lifetime != -1){
            lifetime--;
        }
        return lifetime == 0;
    }

    @Override
    public String toString(){
        return message;
    }
}
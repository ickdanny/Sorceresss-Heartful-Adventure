import java.util.ArrayList;
import java.util.Iterator;

//container class for BoardMessage
public class Blackboard {
    private ArrayList<BoardMessage> messages;
    public Blackboard(){
        messages = new ArrayList<>();
    }

    //delete all messages with listed name
    public void delete(String messageName){
        Iterator i = messages.iterator();
        String str;
        while(i.hasNext()){
            str = ((BoardMessage)i.next()).getMessage();
            if(str.equals(messageName)){
                i.remove();
            }
        }
    }

    //delete all messages
    public void clear(){
        messages = new ArrayList<>();
    }

    //update the lifetime of all messages and delete if necessary
    public void update(){
        Iterator i = messages.iterator();
        while(i.hasNext()){
            if(((BoardMessage)i.next()).tickDown()){
                i.remove();
            }
        }
    }

    //add method
    public void write(BoardMessage toWrite){
        if(toWrite != null) {
            messages.add(toWrite);
        }
    }

    public ArrayList<BoardMessage> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        String toRet = "";
        for(BoardMessage m : messages){
            toRet = toRet.concat(m + "\n");
        }
        return toRet;
    }
}
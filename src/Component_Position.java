public class Component_Position implements Component {
    private Vector position, prevPosition, oldPosition;

    public Component_Position(){
        position = new Vector();
        prevPosition = new Vector();
        oldPosition = new Vector();
    }
    public Component_Position(Vector position){
        this.position = position;
        prevPosition = new Vector();
        prevPosition.copy(position);
        oldPosition = new Vector();
        oldPosition.copy(prevPosition);
    }

    public Vector getPosition() {
        return position;
    }
    public void setPosition(Vector position) {
        if(position != null) {
//        this.position = position;
            (this.position).copy(position);
        }
        else{
            this.position = null;
        }
    }
    public void resetPosition(Vector position){
        this.position = position;
        prevPosition.copy(position);
        oldPosition.copy(prevPosition);
    }

    public Vector getPrevPosition() {
        return prevPosition;
    }
    public Vector getOldPosition() {
        return oldPosition;
    }

    //using copy so can store vector pointer elsewhere
    public void stepPositions(){
        oldPosition.copy(prevPosition);
        prevPosition.copy(position);
    }

    @Override
    public String getComponentType(){
        return "position";
    }
}
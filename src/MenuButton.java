public class MenuButton {
    //this is absolute position from 0,0 not relative to menu
    private Vector position;
    private Vector selectedPosition;
    private boolean available;
    //Strings that will be fed to resourceLoader to retrieve the bufferedimage
    private String selected, unselected, unavailable;
    //assume all are the same size
    private SImage selectedImage, unselectedImage, unavailableImage;

    private String command;

    public MenuButton(Vector position, Vector selectedPosition, boolean available, String selected, String unselected, String unavailable, String command){
        this.position = position;
        this.selectedPosition = selectedPosition;
        this.available = available;
        this.selected = selected;
        this.unselected = unselected;
        this.unavailable = unavailable;
        this.command = command;

        selectedImage = new SImage(selected, selectedPosition);
        unselectedImage = new SImage(unselected, position);
        unavailableImage = new SImage(unavailable, position);
    }
    public MenuButton(Vector position, boolean available, String selected, String unselected, String unavailable, String command){
        this.position = position;
        this.selectedPosition = position;
        this.available = available;
        this.selected = selected;
        this.unselected = unselected;
        this.unavailable = unavailable;
        this.command = command;

        selectedImage = new SImage(selected, selectedPosition);
        unselectedImage = new SImage(unselected, position);
        unavailableImage = new SImage(unavailable, position);
    }

    //likely to remain unused; the simages already store the correct positions
    public Vector getPosition() {
        return position;
    }
    public Vector getSelectedPosition() {
        return selectedPosition;
    }

    public boolean isAvailable() {
        return available;
    }

    public SImage getSelected() {
        return selectedImage;
    }

    public SImage getUnselected() {
        return unselectedImage;
    }

    public SImage getUnavailable() {
        return unavailableImage;
    }

    public String getCommand() {
        return command;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
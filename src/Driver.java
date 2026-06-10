public class Driver {

    private final static int WIDTH = 1200;
    private final static int HEIGHT = 900;

    //default UPS = 60
    private final static int UPDATES_PER_SECOND = 60;
    private final static int MAX_FRAMES_PER_SECOND = 120;
    private final static int SKIP_TICKS = 1000 / UPDATES_PER_SECOND;
    private final static int MIN_GRAPHICS_SKIP_TICKS = 1000 / MAX_FRAMES_PER_SECOND;
    private final static int MAX_FRAME_SKIP = 5;

    private static MainFrame frame;

    public static void main(String[] args){
        init();
        GameLoop.gameLoop(MAX_FRAME_SKIP, SKIP_TICKS, MIN_GRAPHICS_SKIP_TICKS);
    }

    public static void init(){
        ResourceManager.init();
        frame = new MainFrame(WIDTH, HEIGHT, ResourceManager.getFrameSizeMulti());
        GraphicsDriver.init(frame.getPanel(), WIDTH, HEIGHT, ResourceManager.getFrameSizeMulti());
        InputManager.init(frame.getPanel());
        MenuManager.init();
        MusicDriver.init();
    }
}

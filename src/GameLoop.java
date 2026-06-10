import java.lang.System;

public class GameLoop {

    //the main loop of the game
    public static void gameLoop(final int MAX_FRAME_SKIP, final int SKIP_TICKS, final int MIN_GRAPHICS_SKIP_TICKS){
        long nextGameTick = System.currentTimeMillis();
        long nextGraphicsTick = System.currentTimeMillis();
        int loops;
        double deltaTime;

        for(;;) {
            loops = 0;
            //logic
            while (System.currentTimeMillis() > nextGameTick && loops < MAX_FRAME_SKIP) {
                MenuManager.update();
                InputManager.update();
                MusicDriver.update();
                nextGameTick += SKIP_TICKS;
                loops++;
            }
            //graphics
            if (System.currentTimeMillis() > nextGraphicsTick) {
                deltaTime = (double) (System.currentTimeMillis() + SKIP_TICKS - nextGameTick) / (double) SKIP_TICKS;
                GraphicsDriver.updateImage(MenuManager.getImages(deltaTime));
                nextGraphicsTick += MIN_GRAPHICS_SKIP_TICKS;
            }
        }
    }
}
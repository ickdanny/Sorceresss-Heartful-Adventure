import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//double buffered main panel
public class MainPanel extends JPanel {

    private BufferedImage toDraw;

    public MainPanel(int width, int height){
        toDraw = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void updateToDraw(BufferedImage newImage){
        toDraw = newImage;
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(toDraw, 0, 0, toDraw.getWidth(), toDraw.getHeight(), this);
    }
}
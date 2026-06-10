import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {

    private MainPanel panel;

    public MainFrame(final int WIDTH, final int HEIGHT, final double FRAME_SIZE_MULTI){
        super("Eucatastrophe: Sorceress's Heartful Adventure");
        setResizable(false);
        setSize((int)(WIDTH * FRAME_SIZE_MULTI),  (int)(HEIGHT * FRAME_SIZE_MULTI));
        setLayout(new BorderLayout());
        setLocation(500, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new MainPanel(WIDTH, HEIGHT);
        panel.setPreferredSize(new Dimension((int)(WIDTH * FRAME_SIZE_MULTI),  (int)(HEIGHT * FRAME_SIZE_MULTI)));
        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    public void updateToDraw(BufferedImage newImage){
        panel.updateToDraw(newImage);
    }

    public void repaint(){
        panel.repaint();
    }

    public MainPanel getPanel() {
        return panel;
    }
}
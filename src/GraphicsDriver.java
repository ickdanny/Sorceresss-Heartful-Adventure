//class that converts StringImages to BufferedImage
//should be the only class (other than ResourceLoader) that uses BufferedImage
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GraphicsDriver {

    private static final boolean DRAW_BACKGROUND = true;
    private static MainPanel panel;
    private static int width, height;
    private static double frameSizeMulti;

    private static ArrayList<Component_Collision> cComps = new ArrayList<>();
    private static QuadTree qt = new QuadTree(0, new AABB(0, 0, 0, 0));

    private GraphicsDriver(){}

    //dt is handled separately by the GameDriver and GameFrame
    private static BufferedImage createImage(ArrayList<DrawableElement> toBeDrawn, int width, int height, double scaleFactor){
        BufferedImage toDraw = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = toDraw.createGraphics();

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, width, height);

        //hitboxes drawn below
//        drawHitboxes(toDraw);

        for(DrawableElement e : toBeDrawn){
            if(e instanceof SImage) {
                SImage s = (SImage)e;
                BufferedImage beforeTransform = ResourceManager.getImage(s.getImage());

                //for backgrounds
                if (s instanceof SImageSubImage) {
                    if (!DRAW_BACKGROUND) {
                        continue;
                    }
                    Vector subPos = ((SImageSubImage) s).getSubPos();
                    Vector wh = ((SImageSubImage) s).getWh();
                    beforeTransform = beforeTransform.getSubimage((int) subPos.getA(), (int) subPos.getB(), (int) wh.getA(), (int) wh.getB());
                }

                boolean transformed = false;
                BufferedImage afterTransform = beforeTransform;
                if (s.getScale() != 1) {
                    afterTransform = scaleImage(afterTransform, s.getScale());
                    transformed = true;
                }
                if (s.getRotation() != 0) {
                    afterTransform = rotateImage(afterTransform, s.getRotation());
                    transformed = true;
                }
                if (s.getTransparency() != 1) {
                    afterTransform = transparentImage(afterTransform, s.getTransparency());
                }

                //assuming a constant center around which scaling and transforming is done
                //calculate the difference in height and width,
                //and draw the image such that the untransformed center is equal to the new center
                Vector originalPos = s.getTruePosition();
                Vector drawPos = originalPos;
                if (transformed) {
                    double oldW = beforeTransform.getWidth();
                    double oldH = beforeTransform.getHeight();
                    double newW = afterTransform.getWidth();
                    double newH = afterTransform.getHeight();
                    double wDiff = oldW - newW;
                    double hDiff = oldH - newH;
                    drawPos = Vector.add(originalPos, new Vector(wDiff / 2, hDiff / 2));
                }
                g2d.drawImage(afterTransform, (int) drawPos.getA(), (int) drawPos.getB(), null);
            }
            else if(e instanceof SText) {
                SText s = (SText) e;
                Vector pos = s.getPosition().deepClone();
                int maxChars = s.getMaxChars();
                g2d.setFont(new Font("Monospaced", Font.BOLD, s.getFontSize()));
                g2d.setColor(Color.white);
                String text = s.getToDraw().trim();
                if (maxChars < 0 && !text.isEmpty()) {
                    g2d.drawString(text, (float) pos.getA(), (float) pos.getB());
                }
                else if(!text.isEmpty()){
                    int yDist = s.getFontSize();
                    while (!text.isEmpty()) {
                        //step 1 - cut off
                        String cutOff;
                        if (maxChars < text.length()) {
                            cutOff = text.substring(0, maxChars);
                        }
                        //this should mean that we're clear to just draw and move on
                        else {
                            g2d.drawString(text.trim(), (float) pos.getA(), (float) pos.getB());
                            break;
//                            cutOff = text;
//                            text = "";
                        }
                        //step 2 - find last space
                        int lastSpace = cutOff.lastIndexOf(' ');
                        if (lastSpace != -1) {
                            String wholeWords = cutOff.substring(0, lastSpace);
                            g2d.drawString(wholeWords, (float) pos.getA(), (float) pos.getB());
                            if(!text.isEmpty()){
                                text = text.substring(lastSpace).trim();
                            }
                        }
                        else {
                            g2d.drawString(cutOff, (float) pos.getA(), (float) pos.getB());
                            if (!text.isEmpty()) {
                                text = text.substring(maxChars).trim();
                            }
                        }
                        pos.setB(pos.getB() + yDist);
                    }
                }
            }
        }

        //hitboxes drawn above
//        drawHitboxes(toDraw);

        if(scaleFactor != 1){
            toDraw = scaleImage(toDraw, scaleFactor);
        }

        return toDraw;
    }

    //////////////////////////////////////////////////////////////////////////////////

    //drawn elements are offset from UI
    public static void collisionUpdate(ArrayList<Component_Collision> collisionComponents, QuadTree qt){
        GraphicsDriver.cComps = collisionComponents;
        GraphicsDriver.qt = qt;
    }
    private static void drawHitboxes(BufferedImage drawOnTop){
        Vector offset = GameDriver.getGameWindowOffset();
        Graphics2D buffer = drawOnTop.createGraphics();
        buffer.setColor(Color.MAGENTA);
        //first draw AABBs
        for(Component_Collision c : cComps){
            AABB truePos = MiscUtil.addAABBToVector(c.getTruePos(), offset);
            int xlow = (int)truePos.getXlow();
            int xhigh = (int)truePos.getXhigh();
            int ylow = (int)truePos.getYlow();
            int yhigh = (int)truePos.getYhigh();

            buffer.fillRect(xlow, ylow, xhigh - xlow, yhigh - ylow);

            AABB twoFrame = MiscUtil.addAABBToVector(c.getTwoFrame(), offset);
            xlow = (int)twoFrame.getXlow();
            xhigh = (int)twoFrame.getXhigh();
            ylow = (int)twoFrame.getYlow();
            yhigh = (int)twoFrame.getYhigh();

            buffer.drawRect(xlow, ylow, xhigh - xlow, yhigh - ylow);
        }

        //finally draw qt
        drawQuadTree(qt, buffer);
    }
    private static void drawQuadTree(QuadTree toDraw, Graphics2D buffer){
        if(toDraw.getNodes()[0] != null){
            drawQuadTree(toDraw.getNodes()[0], buffer);
            drawQuadTree(toDraw.getNodes()[1], buffer);
            drawQuadTree(toDraw.getNodes()[2], buffer);
            drawQuadTree(toDraw.getNodes()[3], buffer);
        }

        AABB aabb = MiscUtil.addAABBToVector(toDraw.getBounds(), GameDriver.getGameWindowOffset());
        int xlow = (int)aabb.getXlow();
        int xhigh = (int)aabb.getXhigh();
        int ylow = (int)aabb.getYlow();
        int yhigh = (int)aabb.getYhigh();

        buffer.drawRect(xlow, ylow, xhigh - xlow, yhigh - ylow);
    }

    /////////////////////////////////////////////////////////////////////////////////

    //dt is handled by the MenuManager
    public static void updateImage(ArrayList<DrawableElement> toBeDrawn){
        //update image panel draws
//        panel.updateToDraw(createImage(toBeDrawn, width, height, frameSizeMulti));
        BufferedImage toDraw = createImage(toBeDrawn, width, height, frameSizeMulti);
        panel.updateToDraw(toDraw);

        //have panel draw the image
        panel.repaint();
    }

    private static BufferedImage scaleImage(BufferedImage toScale, int newWidth, int newHeight){
        BufferedImage scaled = new BufferedImage(newWidth, newHeight, toScale.getType());
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(toScale, 0, 0, newWidth, newHeight, 0, 0, toScale.getWidth(), toScale.getHeight(), null);
        g.dispose();
        return scaled;
    }
    private static BufferedImage scaleImage(BufferedImage toScale, double scale){
        return scaleImage(toScale, (int)(scale * toScale.getWidth()), (int)(scale * toScale.getHeight()));
    }
    //angle in deg
    private static BufferedImage rotateImage(BufferedImage toRotate, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = toRotate.getWidth();
        int h = toRotate.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(toRotate, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
    private static BufferedImage transparentImage(BufferedImage toTransparent, double transparency){
        int w = toTransparent.getWidth();
        int h = toTransparent.getHeight();

        BufferedImage transparented = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = transparented.createGraphics();
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)transparency);
        g2d.setComposite(ac);
        g2d.drawImage(toTransparent, 0, 0, null);
        g2d.dispose();

        return transparented;
    }

    public static void init(MainPanel panel, int width, int height, double frameSizeMulti){
        GraphicsDriver.panel = panel;
        GraphicsDriver.width = width;
        GraphicsDriver.height = height;
        GraphicsDriver.frameSizeMulti = frameSizeMulti;
    }
}

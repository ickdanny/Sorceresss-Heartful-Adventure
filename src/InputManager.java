import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class InputManager {

    private static InputTable liveInputTable;

    public static void update(){
        liveInputTable.newTurn();
    }

    public static void init(MainPanel panel){
        liveInputTable = new InputTable();

        InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "UpArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "ReleaseLeftArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "ReleaseRightArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "ReleaseUpArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "ReleaseDownArrow");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK, false), "LeftArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK, false), "RightArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK, false), "UpArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK, false), "DownArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK, true), "ReleaseLeftArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK, true), "ReleaseRightArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK, true), "ReleaseUpArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK, true), "ReleaseDownArrow");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, false), "ZKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false), "XKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, true), "ReleaseZKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, true), "ReleaseXKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_DOWN_MASK, false), "ZKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.SHIFT_DOWN_MASK, false), "XKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_DOWN_MASK, true), "ReleaseZKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.SHIFT_DOWN_MASK, true), "ReleaseXKey");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK, false), "ShiftKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true), "ReleaseShiftKey");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "EscapeKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "ReleaseEscapeKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.SHIFT_DOWN_MASK, false), "EscapeKey");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.SHIFT_DOWN_MASK, true), "ReleaseEscapeKey");

        am.put("LeftArrow", new KeyAction("LeftArrow"));
        am.put("RightArrow", new KeyAction("RightArrow"));
        am.put("UpArrow", new KeyAction("UpArrow"));
        am.put("DownArrow", new KeyAction("DownArrow"));
        am.put("ReleaseLeftArrow", new KeyAction("ReleaseLeftArrow"));
        am.put("ReleaseRightArrow", new KeyAction("ReleaseRightArrow"));
        am.put("ReleaseUpArrow", new KeyAction("ReleaseUpArrow"));
        am.put("ReleaseDownArrow", new KeyAction("ReleaseDownArrow"));

        am.put("ZKey", new KeyAction("ZKey"));
        am.put("XKey", new KeyAction("XKey"));
        am.put("ReleaseZKey", new KeyAction("ReleaseZKey"));
        am.put("ReleaseXKey", new KeyAction("ReleaseXKey"));

        am.put("ShiftKey", new KeyAction("ShiftKey"));
        am.put("ReleaseShiftKey", new KeyAction("ReleaseShiftKey"));

        am.put("EscapeKey", new KeyAction("EscapeKey"));
        am.put("ReleaseEscapeKey", new KeyAction("ReleaseEscapeKey"));
    }

    //when key is pressed automatically updates liveInputTable
    private static class KeyAction extends AbstractAction {
        private String keyID;

        KeyAction(String keyID){
            this.keyID = keyID;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            liveInputTable.keyAction(keyID);
        }
    }

    public static InputTable getLiveInputTable(){
        return liveInputTable;
    }
}

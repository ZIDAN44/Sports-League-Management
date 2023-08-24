package GUI.Common;

import javax.swing.*;
import java.awt.*;

public class CustomCursor {

    private static Cursor createScaledCursor(String resourcePath, int width, int height, String cursorName) {
        ImageIcon cursorImage = new ImageIcon(CustomCursor.class.getResource(resourcePath));
        Image cursor = cursorImage.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        Point hotspot = new Point(0, 0);
        return Toolkit.getDefaultToolkit().createCustomCursor(cursor, hotspot, cursorName);
    }

    public static void setCustomCursor(JFrame frame) {
        frame.setCursor(createScaledCursor("/GUI/Pictures/Common/Cursor.png", 22, 22, "custom cursor"));
    }

    public static void setCustomCursor(JDialog dialog) {
        dialog.setCursor(createScaledCursor("/GUI/Pictures/Common/Cursor.png", 22, 22, "custom cursor"));
    }

    public static void setHandCursor(JButton button) {
        button.setCursor(createScaledCursor("/GUI/Pictures/Common/HandCursor.png", 22, 22, "hand cursor"));
    }

    public static void setHandCursor(JTextField field) {
        field.setCursor(createScaledCursor("/GUI/Pictures/Common/HandCursor.png", 22, 22, "hand cursor"));
    }

    public static void setHandCursor(Component component) {
        component.setCursor(createScaledCursor("/GUI/Pictures/Common/HandCursor.png", 22, 22, "hand cursor"));
    }
}

package GUI.Common;

import org.pushingpixels.substance.api.skin.*;
import javax.swing.*;

public class LookAndFeelUtil {

    public static void setSubstanceNebulaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new SubstanceNebulaLookAndFeel());
            SwingUtilities.updateComponentTreeUI(new JFrame());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void setSubstanceMistSilverLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new SubstanceMistSilverLookAndFeel());
            SwingUtilities.updateComponentTreeUI(new JFrame());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}

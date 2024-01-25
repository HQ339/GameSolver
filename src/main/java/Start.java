import ui.Startui;

import javax.swing.*;

/**
 * 开始，右键运行即可
 *
 * @author HQ
 */
public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Startui::createAndShowGui);
    }
}

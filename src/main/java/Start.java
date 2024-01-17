import ui.Startui;

import javax.swing.*;

/**
 * @author HQ
 */
public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Startui::createAndShowGui);
    }
}

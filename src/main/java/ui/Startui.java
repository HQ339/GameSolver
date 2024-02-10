package ui;

import javax.swing.*;
import java.awt.*;

/**
 * 开始界面
 *
 * @author HQ
 */
public class Startui {

    public static void createAndShowGui() {
        JFrame frame = new JFrame("游戏选择");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("选择一个游戏:");
        panel.add(label);

        JComboBox<String> gameComboBox = new JComboBox<>(UiConstant.GAMES);
        panel.add(gameComboBox);

        JButton startButton = new JButton("选择完毕");
        startButton.addActionListener(event -> {
            String selectedGame = (String) gameComboBox.getSelectedItem();
            if (selectedGame != null) {
                if (UiConstant.GAMES[0].equals(selectedGame)) {
                    System.out.println("八数码");
                    SwingUtilities.invokeLater(() -> {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(frame, exception.getMessage());
                            System.out.println(exception.getMessage());
                        }
                        new NPuzzleUi().createAndShowGui();
                    });
                    frame.setVisible(false);
                } else if (UiConstant.GAMES[1].equals(selectedGame)){
                    System.out.println("数独");
                    SwingUtilities.invokeLater(SudokuSolverGUI::new);
                    frame.setVisible(false);
                }
            }
        });
        panel.add(startButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }
}



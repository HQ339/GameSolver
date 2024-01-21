package ui;

import games.eight.puzzle.EightPuzzleConstant;

import javax.swing.*;
import java.awt.*;

/**
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

        JComboBox<String> gameComboBox = new JComboBox<>(EightPuzzleConstant.GAMES);
        panel.add(gameComboBox);

        JButton startButton = new JButton("选择完毕");
        startButton.addActionListener(event -> {
            String selectedGame = (String) gameComboBox.getSelectedItem();
            if (selectedGame != null) {
                if (EightPuzzleConstant.GAMES[0].equals(selectedGame)) {
                    System.out.println("八数码");
                    SwingUtilities.invokeLater(() -> {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                        new EightPuzzleUi().createAndShowGui();
                    });
                    frame.setVisible(false);
                } else {
                    // todo
                    System.out.println("其他游戏");
                }
            }
        });
        panel.add(startButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }
}



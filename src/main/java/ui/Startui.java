package ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author HQ
 */
public class Startui {
    private static final String[] GAMES = {"八数码(数字华容道)", "其他游戏"};

    public static void createAndShowGui() {
        JFrame frame = new JFrame("游戏选择");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("选择一个游戏:");
        panel.add(label);

        JComboBox<String> gameComboBox = new JComboBox<>(GAMES);
        panel.add(gameComboBox);

        JButton startButton = new JButton("选择完毕");
        startButton.addActionListener(e -> {
            String selectedGame = (String) gameComboBox.getSelectedItem();
            if (selectedGame != null) {
                if (GAMES[0].equals(selectedGame)) {
                    // todo
                    System.out.println("八数码");
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



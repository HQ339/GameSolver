package ui;

import games.eight.puzzle.EightPuzzleConstant;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author HQ
 */
public class EightPuzzleSolutionViewer {
    private JFrame frame;

    public void createAndShowGui() {
        frame = new JFrame("解决方案查看器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea solutionArea = new JTextArea(20, 40);
        solutionArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(solutionArea);

        loadSolution(solutionArea);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel backPanel = getBackPanel();

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }

    private JPanel getBackPanel() {
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
                new EightPuzzleUi().createAndShowGui();
            });
            frame.setVisible(false);
        });
        JPanel backPanel = new JPanel();
        backPanel.add(backButton);
        return backPanel;
    }

    private void loadSolution(JTextArea solutionArea) {
        File file = new File(EightPuzzleConstant.SOLUTION_FILE_PATH);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "解决方案文件不存在：" + EightPuzzleConstant.SOLUTION_FILE_PATH);
            return;
        }

        StringBuilder solutionText = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                solutionText.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "无法加载解决方案：" + e.getMessage());
        }

        solutionArea.setText(solutionText.toString());
    }
}

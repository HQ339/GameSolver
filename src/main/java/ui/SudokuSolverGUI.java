package ui;

import sudoku.SudokuConstant;
import sudoku.SudokuSolver;
import sudoku.SudokuUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author HQ
 */
public class SudokuSolverGUI {
    private final JFrame frame;
    private final JTextField[][] sudokuFields;

    public SudokuSolverGUI() {
        frame = new JFrame("数独求解器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel sudokuPanel = new JPanel(new GridLayout(9, 9));
        sudokuFields = new JTextField[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuFields[i][j] = new JTextField(1);
                sudokuPanel.add(sudokuFields[i][j]);
            }
        }


        JButton solveButton = new JButton("解数独");
        solveButton.addActionListener(e -> solveSudoku());
        JButton backButton = new JButton("返回游戏选择界面");
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(Startui::createAndShowGui);
            frame.setVisible(false);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(backButton);

        JLabel instructionLabel = new JLabel("提示：只需输入带有数字的位置，需要填空的位置无需填写。");

        frame.add(sudokuPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(instructionLabel, BorderLayout.NORTH);

        frame.pack();
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    private void solveSudoku() {
        int[][] sudokuGrid = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String value = sudokuFields[i][j].getText();
                if (!value.isEmpty()) {
                    sudokuGrid[i][j] = Integer.parseInt(value);
                } else {
                    sudokuGrid[i][j] = 0;
                }
            }
        }
        SudokuUtils.printAndWriteGrid(sudokuGrid);

        boolean solved = SudokuSolver.search(sudokuGrid);

        if (solved) {
            String solution = readSolutionFromFile();
            JOptionPane.showMessageDialog(frame, "数独已成功求解！" + System.lineSeparator() +
                    "解:" + System.lineSeparator() + solution);
        } else {
            JOptionPane.showMessageDialog(frame, "数独求解失败。");
        }
    }

    private String readSolutionFromFile() {
        StringBuilder solution = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(SudokuConstant.SOLUTION_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                solution.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, e);
        }
        return solution.toString();
    }
}

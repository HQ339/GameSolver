package ui;

import games.eight.puzzle.EightPuzzleConstant;
import games.eight.puzzle.EightPuzzleSearch;
import games.eight.puzzle.EightPuzzleState;
import games.eight.puzzle.EightPuzzleUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

/**
 * 数字华容道（数码问题）求解界面
 *
 * @author HQ
 */
public class EightPuzzleUi {
    private int order;
    private int[][] initialState;
    private int[][] targetState;
    private JFrame frame;
    private JComboBox<Integer> orderComboBox;
    private JTextArea initialStateArea;
    private JTextArea targetStateArea;

    public void createAndShowGui() {
        frame = new JFrame("八数码问题界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel orderPanel = new JPanel();
        JLabel orderLabel = new JLabel("选择阶数 (" + EightPuzzleConstant.MIN_LEVEL + "-" + EightPuzzleConstant.MAX_LEVEL + "): ");
        ArrayList<Integer> orderList = new ArrayList<>();

        for (int i = EightPuzzleConstant.MIN_LEVEL; i <= EightPuzzleConstant.MAX_LEVEL; i++) {
            orderList.add(i);
        }

        Integer[] orders = orderList.toArray(new Integer[0]);
        orderComboBox = new JComboBox<>(orders);
        JButton submitOrderButton = new JButton("提交阶数");
        submitOrderButton.addActionListener(e -> handleOrderSubmission());

        orderPanel.add(orderLabel);
        orderPanel.add(orderComboBox);
        orderPanel.add(submitOrderButton);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));

        JLabel initialStateLabel = new JLabel("初始状态:");
        initialStateArea = new JTextArea(5, 5);
        JScrollPane initialStateScrollPane = new JScrollPane(initialStateArea);

        JLabel targetStateLabel = new JLabel("目标状态:");
        targetStateArea = new JTextArea(5, 5);
        JScrollPane targetStateScrollPane = new JScrollPane(targetStateArea);

        inputPanel.add(initialStateLabel);
        inputPanel.add(initialStateScrollPane);
        inputPanel.add(targetStateLabel);
        inputPanel.add(targetStateScrollPane);

        JPanel commonButtonPanel = getCommonButtonPanel();

        mainPanel.add(orderPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(commonButtonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private JPanel getCommonButtonPanel() {
        JButton solveQuicklyButton = new JButton("快速求解");
        solveQuicklyButton.addActionListener(e -> handleSolveButton(true));

        JButton solveBestButton = new JButton("求较佳解");
        solveBestButton.addActionListener(e -> handleSolveButton(false));

        // 返回按钮
        JButton backButton = new JButton("返回游戏选择界面");
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(Startui::createAndShowGui);
            frame.setVisible(false);
        });

        JPanel commonButtonPanel = new JPanel();
        commonButtonPanel.add(solveQuicklyButton);
        commonButtonPanel.add(solveBestButton);
        commonButtonPanel.add(backButton);
        return commonButtonPanel;
    }

    private void handleOrderSubmission() {
        Object selectedItem = orderComboBox.getSelectedItem();
        order = selectedItem == null ? 3 : (Integer) selectedItem;

        initialState = getInputMatrix("输入初始状态 (用空格分隔, 0表示空白):");
        targetState = getInputMatrix("输入目标状态 (用空格分隔, 0表示空白):");

        displayMatrices(initialState, targetState);

        JOptionPane.showMessageDialog(frame, "状态输入成功.");
    }

    private int[][] getInputMatrix(String message) {
        JTextArea inputArea = new JTextArea(12, 12);
        JScrollPane scrollPane = new JScrollPane(inputArea);

        JOptionPane.showMessageDialog(frame, scrollPane, message, JOptionPane.PLAIN_MESSAGE);

        String[] rows = inputArea.getText().split("\n");

        int[][] matrix = new int[order][order];

        for (int i = 0; i < order; i++) {
            String[] values = rows[i].split("\\s+");
            for (int j = 0; j < order; j++) {
                matrix[i][j] = Integer.parseInt(values[j]);
            }
        }

        return matrix;
    }

    private void handleSolveButton(boolean solveQuickly) {
        EightPuzzleUtils.solveQuickly = solveQuickly;
        if (order == EightPuzzleConstant.MAX_LEVEL && !solveQuickly) {
            // 四阶求较佳解需要很长时间
            int choice = JOptionPane.showOptionDialog(frame,
                    "阶数为" + order + "，建议选择快速求解，否则可能耗时较长。",
                    "提示",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"继续求较佳解", "使用快速求解"},
                    "继续求较佳解");

            if (choice == JOptionPane.INFORMATION_MESSAGE) {
                // 设置为快速求解
                EightPuzzleUtils.solveQuickly = true;
            }
        }

        // 调用方法
        EightPuzzleSearch search = new EightPuzzleSearch(initialState, targetState, order);
        Map<String, Stack<EightPuzzleState>> result = search.search();
        String message = (String) result.keySet().toArray()[0];
        JOptionPane.showMessageDialog(frame, message);
        if (!EightPuzzleConstant.GET_SOLUTION_SUCCESS.equals(message)) {
            // 未求解成功
            return;
        }

        Stack<EightPuzzleState> solution = result.get(message);

        // 打印路径到控制台并写入文件
        File file = new File(EightPuzzleConstant.SOLUTION_FILE_PATH);
        System.out.println(file.getAbsolutePath());
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            int steps = 0;
            while (!solution.isEmpty()) {
                EightPuzzleState curr = solution.pop();
                String path = curr.getPath();

                if (path != null) {
                    System.out.println(path);
                    out.write(path);
                    out.newLine();
                }

                int[][] state = curr.getState();
                for (int[] row : state) {
                    StringBuilder rowStr = getRowStr(row);
                    System.out.println(rowStr);
                    out.write(rowStr.toString());
                    out.newLine();
                }
                System.out.println();
                steps = curr.getSteps();
            }
            out.write("步数: " + steps);
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 求解成功并写入文件后展示解
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            new EightPuzzleSolutionViewer().createAndShowGui();
        });
        frame.setVisible(false);
    }

    private StringBuilder getRowStr(int[] row) {
        StringBuilder rowStr = new StringBuilder();
        rowStr.append("[");
        for (int i = 0; i < row.length; i++) {
            if (i != 0) {
                rowStr.append(", ");
            }
            if (order >= 4) {
                rowStr.append(String.format("%-2d", row[i]));
            } else {
                rowStr.append(row[i]);
            }
        }
        rowStr.append("]");
        return rowStr;
    }

    private void displayMatrices(int[][] initialState, int[][] targetState) {
        StringBuilder initialStateStr = new StringBuilder("初始状态:\n");
        StringBuilder targetStateStr = new StringBuilder("目标状态:\n");

        for (int i = 0; i < initialState.length; i++) {
            for (int j = 0; j < initialState[i].length; j++) {
                initialStateStr.append(String.format("%-2d ", initialState[i][j]));
                targetStateStr.append(String.format("%-2d ", targetState[i][j]));
            }
            initialStateStr.append("\n");
            targetStateStr.append("\n");
        }

        initialStateArea.setText(initialStateStr.toString());
        targetStateArea.setText(targetStateStr.toString());
    }
}

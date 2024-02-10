package sudoku;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author HQ
 */
public class SudokuUtils {
    public static boolean inputJudge(int[][] grid) {
        if (grid == null) {
            return false;
        }
        // 有九行
        if (grid.length != 9) {
            return false;
        }
        for (int[] row : grid) {
            // 每行有九个数
            if (row.length != 9) {
                return false;
            }
        }
        // 不符合数独规则
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0 && !isValid(i, j, grid)) {
                    System.out.println(i + ", " + j);
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isValid(int row, int col, int[][] grid) {
        int num = grid[row][col];
        // 行列内符合规则
        for (int i = 0; i < 9; i++) {
            if (i != row && num == grid[i][col]) {
                return false;
            }
            if (i != col && num == grid[row][i]) {
                return false;
            }
        }

        // 宫内符合规则
        int subgridRowStart = (row / 3) * 3;
        int subgridColStart = (col / 3) * 3;
        for (int i = subgridRowStart; i < subgridRowStart + 3; i++) {
            for (int j = subgridColStart; j < subgridColStart + 3; j++) {
                boolean numEqual = (i != row || j != col) && num == grid[i][j];
                if (numEqual) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void printAndWriteGrid(int[][] grid) {
        if (grid == null) {
            return;
        }

        try (BufferedWriter out = new BufferedWriter(new FileWriter(SudokuConstant.SOLUTION_FILE_PATH))) {
            for (int i = 0; i < grid.length; i++) {
                StringBuilder rowStr = new StringBuilder();
                for (int j = 0; j < grid[i].length; j++) {
                    if (j % 3 == 0 && j != 0) {
                        rowStr.append(" | ");
                    }
                    rowStr.append(grid[i][j] == 0 ? "." : grid[i][j]);
                    if ((j + 1) % 3 == 0 && j != grid[i].length - 1) {
                        rowStr.append("  ");
                    }
                }

                System.out.println(rowStr);
                out.write(rowStr.toString());
                out.newLine();

                if ((i + 1) % 3 == 0 && i != grid.length - 1) {
                    String separator = "------+-------+------";
                    System.out.println(separator);
                    out.write(separator);
                    out.newLine();
                }
            }

            System.out.println();
            System.out.println("回溯次数: " + SudokuSolver.backtracksCnt);
            out.newLine();
            out.write("回溯次数: " + SudokuSolver.backtracksCnt);
        } catch (IOException e) {
            throw new RuntimeException("数独解文件写入失败: " + e.getMessage(), e);
        }
    }
}

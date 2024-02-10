package sudoku;

/**
 * @author HQ
 */
public class SudokuSolver {
    /**
     * 回溯次数
     */
    public static int backtracksCnt = 0;

    public static boolean search(int[][] grid) {
        if (!SudokuUtils.inputJudge(grid)) {
            return false;
        }
        System.out.println("开始求解");
        return search(0, grid);
    }


    public static boolean search(int n, int[][] grid) {
        if (n == 81) {
            SudokuUtils.printAndWriteGrid(grid);
            return true;
        }

        int row = n / 9, col = n % 9;
        if (grid[row][col] != 0) {
            return search(n + 1, grid);
        }

        for (int i = 0; i < 9; i++) {
            grid[row][col]++;
            if (SudokuUtils.isValid(row, col, grid)) {
                boolean allRight = search(n + 1, grid);
                if (allRight) {
                    return true;
                }
            }
        }

        grid[row][col] = 0;
        backtracksCnt++;
        return false;
    }

}

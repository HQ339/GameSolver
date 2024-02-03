package sudoku;

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

    public static void printGrid(int[][] grid) {
        if (grid == null) {
            return;
        }

        for (int i = 0; i < grid.length; i++) {
            StringBuilder rowStr = new StringBuilder();
            rowStr.append("[");
            for (int j = 0; j < grid[i].length; j++) {
                if (j % 3 == 0) {
                    rowStr.append("[");
                }
                rowStr.append(grid[i][j]);
                if ((j - 2) % 3 == 0) {
                    rowStr.append("]");
                }
            }
            rowStr.append("]");
            if (i % 3 == 0) {
                System.out.println("  ---  ---  ---  ");
            }
            System.out.println(rowStr);
        }
        System.out.println();
    }
}

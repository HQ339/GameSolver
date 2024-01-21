package games.eight.puzzle;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author HQ
 */
public class EightPuzzleUtils {
    public static boolean solveQuickly = false;

    /**
     * 检查是否可解
     *
     * @param initialState 初始状态
     * @param goal         目标状态
     * @return 可解性
     */
    public static boolean solvable(int[][] initialState, int[][] goal) {
        // 统计逆序数
        int initialStateInverseNum, goalInverseNum;
        initialStateInverseNum = getInverseNum(initialState);
        goalInverseNum = getInverseNum(goal);
        // 判断
        return initialStateInverseNum % 2 == goalInverseNum % 2;
    }

    /**
     * 获取逆序数的方法
     *
     * @param state 二维数组表示的状态
     * @return 逆序数
     */
    private static int getInverseNum(int[][] state) {
        // 将二维数组转换为一维 List
        List<Integer> stateList = Arrays.stream(state)
                // 将二维数组扁平化为 IntStream
                .flatMapToInt(Arrays::stream)
                // 将基本类型流转换为装箱流，即 Stream<Integer>
                .boxed()
                // 将 Stream 转换为 List
                .toList();

        // 使用 Stream API 计算逆序数
        // 生成索引范围的 IntStream
        return (int) IntStream.range(0, stateList.size())
                // 过滤掉值为 0 的元素
                .filter(i -> stateList.get(i) != 0)
                // 对于每个非零元素，再次生成索引范围的 IntStream
                .flatMap(i -> IntStream.range(0, i)
                        // 过滤掉小于当前元素的元素
                        .filter(j -> stateList.get(j) > stateList.get(i)))
                // 对逆序数进行计数
                .count();
    }

    public static List<EightPuzzleState> generateNeighborStates(EightPuzzleState curr) {
        if (curr == null) {
            return Collections.emptyList();
        }

        String path = curr.getPath();
        Map<String, Integer> blank = curr.getBlank();
        List<EightPuzzleState> neighborStates = new ArrayList<>();

        // 生成状态
        Integer row = blank.get(EightPuzzleConstant.BLANK_ROW);
        Integer col = blank.get(EightPuzzleConstant.BLANK_COLUMN);
        int[][] state = curr.getState();
        if (!EightPuzzleConstant.DOWN.equals(path) && row != 0) {
            // 上移，并排除重复状态
            int[][] neighborArr = swap(state, row, row - 1, col, col);
            EightPuzzleState neighbor = new EightPuzzleState(
                    neighborArr, curr.getSteps() + 1, calculateCost(neighborArr, curr.getSteps()), curr, EightPuzzleConstant.UP
            );
            neighborStates.add(neighbor);
        }
         if (!EightPuzzleConstant.UP.equals(path) && row != EightPuzzleState.level - 1) {
             // 下移
             int[][] neighborArr = swap(state, row, row + 1, col, col);
             EightPuzzleState neighbor = new EightPuzzleState(
                     neighborArr, curr.getSteps() + 1, calculateCost(neighborArr, curr.getSteps()), curr, EightPuzzleConstant.DOWN
             );
             neighborStates.add(neighbor);
         }
         if (!EightPuzzleConstant.RIGHT.equals(path) && col != 0) {
             // 左移
             int[][] neighborArr = swap(state, row, row, col, col - 1);
             EightPuzzleState neighbor = new EightPuzzleState(
                     neighborArr, curr.getSteps() + 1, calculateCost(neighborArr, curr.getSteps()), curr, EightPuzzleConstant.LEFT
             );
             neighborStates.add(neighbor);
         }
         if (!EightPuzzleConstant.LEFT.equals(path) && col != EightPuzzleState.level - 1) {
             // 右移
             int[][] neighborArr = swap(state, row, row, col, col + 1);
             EightPuzzleState neighbor = new EightPuzzleState(
                     neighborArr, curr.getSteps() + 1, calculateCost(neighborArr, curr.getSteps()), curr, EightPuzzleConstant.RIGHT
             );
             neighborStates.add(neighbor);
         }

        return neighborStates;
    }

    private static int[][] swap(int[][] state, Integer oldRow, Integer newRow, Integer oldCol, Integer newCol) {
        int[][] arr = new int[state.length][state[0].length];
        for (int i = 0; i < state.length; i++) {
            arr[i] = state[i].clone();
        }

        if (oldRow.intValue() == newRow.intValue()) {
            // 左移或右移
            // 临时量
            int temp = arr[oldRow][oldCol];
            arr[oldRow][oldCol] = arr[oldRow][newCol];
            arr[oldRow][newCol] = temp;
        } else if (oldCol.intValue() == newCol.intValue()) {
            // 上移或下移
            int temp = arr[oldRow][oldCol];
            arr[oldRow][oldCol] = arr[newRow][oldCol];
            arr[newRow][oldCol] = temp;
        }
        return arr;
    }

    public static Stack<EightPuzzleState> loadSolutionStack(EightPuzzleState curr) {
        Stack<EightPuzzleState> solution = new Stack<>();
        while (curr.getParent() != null) {
            solution.push(curr);
            curr = curr.getParent();
        }
        solution.push(curr);
        return solution;
    }

    public static boolean contains(Set<EightPuzzleState> visited, EightPuzzleState neighborState) {
        for (EightPuzzleState state : visited) {
            if (Arrays.deepEquals(state.getState(), neighborState.getState())) {
                return true;
            }
        }
        return false;
    }

    public static int calculateCost(int[][] state, int deepth) {
        if (solveQuickly) {
            return getEnhancedManhattanDistance(state);
        }
        return getEnhancedManhattanDistance(state) + deepth;
    }

    private static int getEnhancedManhattanDistance(int[][] state) {
        int manhattanDistance = 0;
        int linearConflict = 0;
        int length = state.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int tileValue = state[i][j];

                if (tileValue != 0) {
                    // 获取块的目标指数
                    int[] goalIndices = getIndex(tileValue);
                    int goalRow = goalIndices[0];
                    int goalCol = goalIndices[1];

                    // 计算曼哈顿距离
                    manhattanDistance += Math.abs(i - goalRow) + Math.abs(j - goalCol);

                    // 检查线性冲突
                    if (i == goalRow) {
                        for (int k = j + 1; k < length; k++) {
                            int conflictingTile = state[i][k];
                            if (conflictingTile != 0 && getIndex(conflictingTile)[1] < j) {
                                // 同一行中的线性冲突
                                linearConflict += 2;
                            }
                        }
                    }

                    if (j == goalCol) {
                        for (int k = i + 1; k < length; k++) {
                            int conflictingTile = state[k][j];
                            if (conflictingTile != 0 && getIndex(conflictingTile)[0] < i) {
                                // 同一列中的线性冲突
                                linearConflict += 2;
                            }
                        }
                    }
                }
            }
        }

        // 返回总启发式值（曼哈顿距离 + 线性冲突）
        return manhattanDistance + linearConflict;
    }


    private static int[] getIndex(int num) {
        int[][] goal = EightPuzzleState.goal;
        int length = goal.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (goal[i][j] == num) {
                    return new int[] {i, j};
                }
            }
        }
        return new int[] {-1, -1};
    }

}

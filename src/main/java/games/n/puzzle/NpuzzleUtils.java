package games.n.puzzle;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 工具类，使用的到的函数
 *
 * @author HQ
 */
public class NpuzzleUtils {
    /**
     * 是否快速求解
     */
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
        if (NpuzzleState.level % 2 != 0) {
            // 阶数为奇数时，仅需判断逆序数是否同奇偶即可
            return initialStateInverseNum % 2 == goalInverseNum % 2;
        }

        // 阶数为偶数时
        int dist = 0;
        for (int i = 0; i < initialState.length; i++) {
            for (int j = 0; j < initialState[i].length; j++) {
                if (initialState[i][j] == 0) {
                    dist += Math.abs(i - getIndex(0)[0]);
                }
            }
        }

        return (initialStateInverseNum + dist) % 2 == goalInverseNum % 2;
    }

    /**
     * 获取逆序数的方法
     *
     * @param state 二维数组表示的状态
     * @return 逆序数
     */
    private static int getInverseNum(int[][] state) {
        List<Integer> stateList = getStateList(state);

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

    /**
     * 将二维数组转换为一维 List
     *
     * @param state 待转换的数组
     * @return 转换得到的一维 List
     */
    private static List<Integer> getStateList(int[][] state) {
        return Arrays.stream(state)
                // 将二维数组扁平化为 IntStream
                .flatMapToInt(Arrays::stream)
                // 将基本类型流转换为装箱流，即 Stream<Integer>
                .boxed()
                // 将 Stream 转换为 List
                .toList();
    }

    /**
     * 生成邻近节点
     *
     * @param curr 目前的状态
     * @return 生成的邻居状态的集合
     */
    public static Map<String, int[][]> generateNeighborStates(NpuzzleState curr) {
        if (curr == null) {
            return Collections.emptyMap();
        }

        // 生成节点时的移动方向不与path相反，否则会得到当前节点的父节点
        String path = curr.getPath();
        Map<String, Integer> blank = curr.getBlank();
        Map<String, int[][]> neighborStates = new HashMap<>(4);

        // 生成状态
        Integer row = blank.get(NpuzzleConstant.BLANK_ROW);
        Integer col = blank.get(NpuzzleConstant.BLANK_COLUMN);
        int[][] state = curr.getState();
        if (!NpuzzleConstant.DOWN.equals(path) && row != 0) {
            // 上移，并排除重复状态
            int[][] neighborArr = swap(state, row, row - 1, col, col);
            neighborStates.put(NpuzzleConstant.UP, neighborArr);
        }
        if (!NpuzzleConstant.UP.equals(path) && row != NpuzzleState.level - 1) {
            // 下移
            int[][] neighborArr = swap(state, row, row + 1, col, col);
            neighborStates.put(NpuzzleConstant.DOWN, neighborArr);
        }
        if (!NpuzzleConstant.RIGHT.equals(path) && col != 0) {
            // 左移
            int[][] neighborArr = swap(state, row, row, col, col - 1);
            neighborStates.put(NpuzzleConstant.LEFT, neighborArr);
        }
        if (!NpuzzleConstant.LEFT.equals(path) && col != NpuzzleState.level - 1) {
            // 右移
            int[][] neighborArr = swap(state, row, row, col, col + 1);
            neighborStates.put(NpuzzleConstant.RIGHT, neighborArr);
        }

        return neighborStates;
    }

    /**
     * 将空白块与指定块交换
     *
     * @param state  状态数组
     * @param oldRow 空白块的行
     * @param newRow 欲交换的行
     * @param oldCol 空白块的列
     * @param newCol 待交换的列
     * @return 交换后得到的数组
     */
    private static int[][] swap(int[][] state, Integer oldRow, Integer newRow, Integer oldCol, Integer newCol) {
        // 克隆数组
        int[][] arr = new int[state.length][state[0].length];
        for (int i = 0; i < state.length; i++) {
            arr[i] = state[i].clone();
        }

        if (oldRow.intValue() == newRow.intValue()) {
            // 行内交换，列不变
            // 左移或右移
            // 临时量
            int temp = arr[oldRow][oldCol];
            arr[oldRow][oldCol] = arr[oldRow][newCol];
            arr[oldRow][newCol] = temp;
        } else if (oldCol.intValue() == newCol.intValue()) {
            // 列内交换，行不变
            // 上移或下移
            int temp = arr[oldRow][oldCol];
            arr[oldRow][oldCol] = arr[newRow][oldCol];
            arr[newRow][oldCol] = temp;
        }
        return arr;
    }

    /**
     * 加载解，生成包含由初始状态到目标状态的路径的栈
     *
     * @param curr 当前状态
     * @return 解
     */
    public static Stack<NpuzzleState> loadSolutionStack(NpuzzleState curr) {
        Stack<NpuzzleState> solution = new Stack<>();
        // 由当前状态开始回溯
        while (curr.getParent() != null) {
            // 入栈，迭代
            solution.push(curr);
            curr = curr.getParent();
        }
        // 将初始状态入栈
        solution.push(curr);
        return solution;
    }

    /**
     * 判断visited中是否包含该邻进状态
     *
     * @param visited       存放已经访问过的元素
     * @param neighborState 待判断的邻居状态
     * @return 包含与否
     */
    public static boolean contains(Set<NpuzzleState> visited, NpuzzleState neighborState) {
        for (NpuzzleState state : visited) {
            if (Arrays.deepEquals(state.getState(), neighborState.getState())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算代价，作为启发函数
     *
     * @param state  状态数组
     * @param deepth 深度
     * @return 代价
     */
    public static int calculateCost(int[][] state, int deepth) {
        if (solveQuickly) {
            // 快速求解，返回加强过的曼哈顿距离
            return getEnhancedManhattanDistance(state);
        }
        // 求较佳解，返回加强过的曼哈顿距离与深度的和
        return getEnhancedManhattanDistance(state) + deepth;
    }

    /**
     * 计算经过线性冲突法加强过的曼哈顿距离
     *
     * @param state 状态数组
     * @return 曼哈顿距离
     */
    private static int getEnhancedManhattanDistance(int[][] state) {
        // 曼哈顿距离
        int manhattanDistance = 0;
        // 线性冲突
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

    /**
     * 获取num的目标索引
     *
     * @param num 状态中的任一数值
     * @return 索引数组，0索引代表的是行坐标，1索引代表的是列坐标
     */
    private static int[] getIndex(int num) {
        int[][] goal = NpuzzleState.goal;
        int length = goal.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (goal[i][j] == num) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * 判断输入的状态是否合理
     *
     * @param initialState 初始状态
     * @param goal         目标状态
     * @return 状态合理与否（及每个数字都能对应上，同时存在空白快0）
     */
    public static boolean stateCorrect(int[][] initialState, int[][] goal) {
        // 转换为一维集合
        List<Integer> initialList = new ArrayList<>(getStateList(initialState));
        List<Integer> goalList = new ArrayList<>(getStateList(goal));

        // 排序
        Collections.sort(initialList);
        Collections.sort(goalList);

        // 是否存在空白块
        boolean findZero = false;
        // 比较
        for (int i = 0; i < initialList.size(); i++) {
            if (initialList.get(i).intValue() != goalList.get(i).intValue()) {
                return false;
            }
            if (initialList.get(i) == 0) {
                findZero = true;
            }
        }
        return findZero;
    }
}

package games.eight.puzzle;

import java.util.*;

/**
 * 搜索方法类，初始状态和目标状态如果使用一维数组代码会更简单，由于我之前写过一维数组的版本，在这里采用二维数组作为状态数组
 *
 * @author HQ
 */
public class EightPuzzleSearch {
    /**
     * 初始状态
     */
    private int[][] initialState;
    /**
     * 目标状态
     */
    private int[][] goal;

    public EightPuzzleSearch(int[][] initialState, int[][] goal, int level) {
        this.initialState = initialState;
        this.goal = goal;
        EightPuzzleState.level = level;
    }

    /**
     * 搜索方法，搜索由初始状态到目标状态的解
     *
     * @return 此次搜索的结果（成功与否）和解的栈
     */
    public Map<String, Stack<EightPuzzleState>> search() {
        EightPuzzleState.goal = this.goal;
        // 初始状态
        EightPuzzleState initialPuzzleState = new EightPuzzleState(
                initialState, 0, EightPuzzleUtils.calculateCost(initialState, 0), null, null
        );
        // 问题的解
        Stack<EightPuzzleState> solution;
        // 返回的结果
        Map<String, Stack<EightPuzzleState>> result = new HashMap<>(3);
        // 输入的状态错误
        if (!EightPuzzleUtils.stateCorrect(initialState, goal)) {
            result.put(EightPuzzleConstant.INPUT_STATE_ERROR, null);
            return result;
        }
        // 不可求解
        if (!EightPuzzleUtils.solvable(initialState, goal)) {
            result.put(EightPuzzleConstant.NOT_SOLVABLE, null);
            return result;
        }

        System.out.println("开始求解");

        // 记录时间
        long start = System.currentTimeMillis();

        // 构造优先队列，所有的状态按照EightPuzzleState里的cost的值排列
        Queue<EightPuzzleState> queue = new PriorityQueue<>(Comparator.comparingInt(EightPuzzleState::getCost));
        // 记录已经访问过的节点
        Set<EightPuzzleState> visited = new HashSet<>();
        // 初始状态入队
        queue.offer(initialPuzzleState);

        // 记录已经访问过的节点数
        int nodeCnt = 0;

        // 搜索
        while (!queue.isEmpty()) {
            nodeCnt++;
            // 将优先级最高的节点出队，作为本次判断以及生成邻居节点的对象
            EightPuzzleState curr = queue.poll();

            // 求解成功
            if (Arrays.deepEquals(curr.getState(), goal)) {
                // 装入解的路径
                solution = EightPuzzleUtils.loadSolutionStack(curr);
                result.put(EightPuzzleConstant.GET_SOLUTION_SUCCESS, solution);
                long end = System.currentTimeMillis();
                System.out.println("求解完毕，耗时：" + (end - start) + "ms, " + "步数: " + curr.getSteps() + "。");
                System.out.println("访问节点个数: " + nodeCnt + "。");
                return result;
            }
            // 将当前节点标记为已访问
            visited.add(curr);

            // 继续求解
            // 生成邻居状态
            List<EightPuzzleState> neighborStates = EightPuzzleUtils.generateNeighborStates(curr);
            if (!neighborStates.isEmpty()) {
                for (EightPuzzleState neighborState : neighborStates) {
                    // 如果该邻居节点未访问过，入队
                    if (!EightPuzzleUtils.contains(visited, neighborState)) {
                        queue.offer(neighborState);
                    }
                }
            }

            if (nodeCnt % 500 == 0) {
                System.out.println("当前访问节点数：" + nodeCnt);
            }
        }

        result.put(EightPuzzleConstant.GET_SOLUTION_FAIL, null);
        return result;
    }

    public int[][] getInitialState() {
        return initialState;
    }

    public void setInitialState(int[][] initialState) {
        this.initialState = initialState;
    }

    public int[][] getGoal() {
        return goal;
    }

    public void setGoal(int[][] goal) {
        this.goal = goal;
    }
}

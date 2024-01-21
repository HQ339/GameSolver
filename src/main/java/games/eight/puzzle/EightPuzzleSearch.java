package games.eight.puzzle;

import java.util.*;

/**
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

    public Map<String, Stack<EightPuzzleState>> search() {
        EightPuzzleState.goal = this.goal;
        EightPuzzleState initialPuzzleState = new EightPuzzleState(
                initialState, 0, EightPuzzleUtils.calculateCost(initialState, 0), null, null
        );
        // 问题的解
        Stack<EightPuzzleState> solution;
        Map<String, Stack<EightPuzzleState>> result = new HashMap<>(3);
        // 不可求解
        if (!EightPuzzleUtils.solvable(initialState, goal)) {
            result.put(EightPuzzleConstant.NOT_SOLVABLE, null);
            return result;
        }

        long start = System.currentTimeMillis();

        Queue<EightPuzzleState> queue = new PriorityQueue<>(Comparator.comparingInt(EightPuzzleState::getCost));
        Set<EightPuzzleState> visited = new HashSet<>();
        queue.offer(initialPuzzleState);

        while (!queue.isEmpty()) {
            EightPuzzleState curr = queue.poll();

            // 求解成功
            if (Arrays.deepEquals(curr.getState(), goal)) {
                // 装入解的路径
                solution = EightPuzzleUtils.loadSolutionStack(curr);
                result.put(EightPuzzleConstant.GET_SOLUTION_SUCCESS, solution);
                long end = System.currentTimeMillis();
                System.out.println("求解完毕，耗时：" + (end - start) + "ms, " + "步数: " + curr.getSteps() + "。");
                return result;
            }
            visited.add(curr);

            // 继续求解
            // 生成邻居状态
            List<EightPuzzleState> neighborStates = EightPuzzleUtils.generateNeighborStates(curr);
            if (!neighborStates.isEmpty()) {
                for (EightPuzzleState neighborState : neighborStates) {
                    if (!EightPuzzleUtils.contains(visited, neighborState)) {
                        queue.offer(neighborState);
                    }
                }
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

package games.eight.puzzle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 数码状态类，默认为八数码
 *
 * @author HQ
 */
public class EightPuzzleState {
    /**
     * 阶数
     */
    public static int level;
    /**
     * 目标状态
     */
    public static int[][] goal;
    /**
     * 空白块的位置
     */
    private final Map<String, Integer> blank;
    /**
     * 目前状态
     */
    private int[][] state;
    /**
     * 步数
     */
    private int steps;
    /**
     * 代价
     */
    private int cost;
    /**
     * 通往此状态的父节点
     */
    private EightPuzzleState parent;
    /**
     * 由父节点到此状态的移动方向
     */
    private String path;

    public EightPuzzleState(int[][] state, int steps, int cost, EightPuzzleState parent, String move) {
        this.blank = new HashMap<>();
        this.state = state;
        this.steps = steps;
        this.cost = cost;
        this.parent = parent;
        this.path = move;
    }

    public int[][] getState() {
        return state;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    /**
     * 查找空白块的坐标
     *
     * @return 空白块的位置
     */
    public Map<String, Integer> getBlank() {
        boolean flag = false;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] == 0) {
                    blank.put(EightPuzzleConstant.BLANK_ROW, i);
                    blank.put(EightPuzzleConstant.BLANK_COLUMN, j);
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
        return blank;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public EightPuzzleState getParent() {
        return parent;
    }

    public void setParent(EightPuzzleState parent) {
        this.parent = parent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EightPuzzleState that = (EightPuzzleState) o;
        return Arrays.deepEquals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }
}


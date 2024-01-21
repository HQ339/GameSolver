package games.eight.puzzle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HQ
 */
public class EightPuzzleState {
    /**
     * 阶数
     */
    public static int level;
    public static int[][] goal;
    private int[][] state;
    private final Map<String, Integer> blank;
    private int steps;
    private int cost;
    private EightPuzzleState parent;
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


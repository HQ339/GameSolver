package games.eight.puzzle;

import java.io.File;

/**
 * @author HQ
 */
public class EightPuzzleConstant {
    public static final String BLANK_ROW = "行";
    public static final String BLANK_COLUMN = "列";
    public static final String NOT_SOLVABLE = "该状态不可求解";
    public static final String GET_SOLUTION_SUCCESS = "求解成功";
    public static final String GET_SOLUTION_FAIL = "求解失败";
    public static final String UP = "上移";
    public static final String DOWN = "下移";
    public static final String LEFT = "左移";
    public static final String RIGHT = "右移";
    public static final int MIN_LEVEL = 2;
    public static final int MAX_LEVEL = 4;
    public static final String[] GAMES = {"八数码", "其他游戏"};
    public static final String SOLUTION_FILE_PATH = "eight" + File.separator + "puzzle" + File.separator + "path.txt";
}

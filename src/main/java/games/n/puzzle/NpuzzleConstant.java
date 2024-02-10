package games.n.puzzle;

import java.io.File;

/**
 * 常量类，包含数码问题需要用的的常量
 *
 * @author HQ
 */
public class NpuzzleConstant {
    public static final String BLANK_ROW = "行";
    public static final String BLANK_COLUMN = "列";
    public static final String INPUT_STATE_ERROR = "输入的状态错误，正确输入应包含一个空白快0" +
            "，同时初始状态中的每一个数都应该能从目标状态中找到，请重新输入";
    public static final String NOT_SOLVABLE = "该状态不可求解";
    public static final String GET_SOLUTION_SUCCESS = "求解成功";
    public static final String GET_SOLUTION_FAIL = "求解失败";
    public static final String UP = "上移";
    public static final String DOWN = "下移";
    public static final String LEFT = "左移";
    public static final String RIGHT = "右移";
    public static final int MIN_LEVEL = 2;
    public static final int MAX_LEVEL = 5;
    public static final String SOLUTION_FILE_PATH = "src" + File.separator +
            "main" + File.separator +
            "java" + File.separator +
            "games" + File.separator +
            "n" + File.separator +
            "puzzle" + File.separator +
            "puzzle" + File.separator +
            "path.txt";
}

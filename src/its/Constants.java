package its;

/**
 * 常量字符串
 * <p/>
 * created by C.L.Wang
 */
public class Constants {

    public static final int MAX_TIME = 1681; // 总时间

    /**
     * 调整的参数
     */
    public static final int MAX_LIGHT_INTERVAL = 2; // 红绿灯惩罚的最大间隔
    public static final int TRICK_TIME = 35; // 欺骗时间
    public static final float HISTORY_FLOW_PENALTY = 0.00f; // 历史流量惩罚系数

    public static final float[] TURN_PROB = {0.1f, 0.8f, 0.1f}; //转弯概率，左中右
    public static final float[] MAX_THROUGH_FLOW = {2.0f, 16.0f, 2.0f}; //最大通过量，左中右

    public static final String LIGHT_NONE = "#"; // 丁字路口标记

    // 全部开放通行
    public static final int[] SETTING_ALL = {
            1, 1, 1,
            1, 1, 1,
            1, 1, 1,
            1, 1, 1,
    };

    // 水平红绿灯全开
    public static final int[] SETTING_ZERO = {
            1, 1, 1,
            0, 1, 0,
            1, 1, 1,
            0, 1, 0,
    };

    // 竖直红绿灯全开
    public static final int[] SETTING_ONE = {
            0, 1, 0,
            1, 1, 1,
            0, 1, 0,
            1, 1, 1,
    };
}

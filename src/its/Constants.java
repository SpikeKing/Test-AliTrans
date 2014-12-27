package its;

/**
 * 测试结果:
 * l1(0.1)+l2(-0.5) = 21942391
 * l1(0.3)+l2(-0.5) = 22504853
 * l1(0.3)+l2(-0.5) = 21902372
 * l1(0.3)+l2(-0.3) = 22405522
 * l1(0.3)+l2(-0.7) = 22547624
 */

public class Constants {
    public final static int MAX_TIME = 1681; // 总时间
    public final static int ESTIMATE_INTERVAL = 3; // 预估时间段
    public final static float LAMBDA_1 = 0.3f; // 预估流量权重
    public final static float LAMBDA_2 = -0.5f; // 权重

    public final static float[] TURN_PROB = {0.1f, 0.8f, 0.1f}; //转弯概率，左中右
//    public final static float[] TURN_PROBA_REV = {0.1f, 0.8f, 0.1f}; //相反的转弯概率，回馈用

    public final static float[] MAX_THROUGH_FLOW = {2.0f, 16.0f, 2.0f}; //最大通过量，左中右
    public final static String LIGHT_NONE = "#";

    // 禁止从文件读取数据
//	public final static String FILENAME_TRAFFIC="/its/TrafficLightTable.txt";
//	public final static String FILENAME_FLOW_ADD="/its/flow0901.txt";

    public final static int MAX_LIGHT_INTERVAL = 2;
}

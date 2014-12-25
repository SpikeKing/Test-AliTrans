package its;

public class Constants 
{
	public final static int MAX_TIME = 1681; //总时间
	public final static int ESTIMATE_INTERVAL = 3; //预估时间段
	public final static float LAMBDA_1 =  0.1f; //权重
	public final static float LAMBDA_2 = -0.5f; //权重
	
	public final static float[] TURN_PROBA = {0.1f,0.8f,0.1f}; //转弯概率，左中右
	public final static float[] TURN_PROBA_REV = {0.1f, 0.8f, 0.1f}; //相反的转弯概率，回馈用
	
	public final static float[] MAX_THROUGH = {2.0f,16.0f,2.0f}; //最大通过量，左中右
	public final static String LIGHT_NONE = "#";
	
	// 禁止从文件读取数据
//	public final static String FILENAME_TRAFFIC="/its/TrafficLightTable.txt";
//	public final static String FILENAME_FLOW_ADD="/its/flow0901.txt";
	
	public final static int MAX_LIGHT_INTERVAL = 2;
}

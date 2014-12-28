package its;

import java.util.ArrayList;
import java.util.List;

/**
 * 交通十字路口，包含4个方向，由四个方向到路口的流量
 */
public class TrafficCrossroad {

    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;

    private String mId; //路口id
    private String[] mNeighbors; //相邻的路口,顺序为左上右下
    private int[] mCurrentFlow; //当前流量

    public int CurrentTime; //当前时间
    public List<int[]> FlowHistory; //历史流量
    public int[] LightSettingHistory; //历史设定状态

    //每个时间段增加的流量（预估），第1维表示时间，第2维表示4个邻居[0-3]
    private int[][] mFlowsOfNeighborAdd;

    // 四个方向的流量和
    private int[] mFlowsOfTotal;

    // 路口的ID - 字符串
    public TrafficCrossroad(String id) {

        mId = id;

        mFlowsOfNeighborAdd = new int[Constants.MAX_TIME][];
        for (int i = 0; i < mFlowsOfNeighborAdd.length; i++) {
            mFlowsOfNeighborAdd[i] = new int[4];
        }

        LightSettingHistory = new int[Constants.MAX_TIME + 1];
        FlowHistory = new ArrayList<int[]>();

        mNeighbors = new String[4];

        mFlowsOfTotal = new int[4]; // 全部流量
        mCurrentFlow = new int[4]; // 当前流量
    }

    // 历史数据的总体量
    public int[] getmFlowsOfTotal() {
        return mFlowsOfTotal;
    }

    // 添加全部四个方向的流量数据
    public void addFlowsOfTotal(int[] flowsOfTotal) {
        int len = flowsOfTotal.length;
        for (int i=0; i<len; ++i) {
            mFlowsOfTotal[i] += flowsOfTotal[i];
        }
    }

    // 添加单个方向的总体流量
    public void addFlowOfTotal(int direction, int flow) {
        mFlowsOfTotal[direction] += flow;
    }

    // 设置邻居
    public void setNeighbors(String left, String up, String right, String down) {
        mNeighbors[0] = left;
        mNeighbors[1] = up;
        mNeighbors[2] = right;
        mNeighbors[3] = down;
    }

    // 返回所有邻居
    public String[] getNeighbors() {
        return mNeighbors;
    }

    // 根据标号返回邻居，左上右下[0-3]
    public String getNeighbor(int i) {
        if (i < 0 || i > 3) {
            return null;
        }

        return mNeighbors[i];
    }

    public String getId() {
        return mId;
    }

    /**
     * 设置某个时间段，某个方向，增加的流量
     *
     * @param time      时间
     * @param direction 方向-左上右下
     * @param flow      流量
     */
    public void setFlowOfTimeAdd(int time, int direction, int flow) {
        if (time < 0 || time > Constants.MAX_TIME || direction < 0 || direction > 3)
            return;
        mFlowsOfNeighborAdd[time][direction] = flow;
    }

    /**
     * 获取某个时间返回的所有流量
     *
     * @param time 时间
     * @return 四个方向的流量
     */
    public int[] getAllFlowsOfTimeAdd(int time) {
        return mFlowsOfNeighborAdd[time];
    }

    public void setFlowOfCurrent(int direction, int flow) {
        mCurrentFlow[direction] = flow;
    }

    public int getFlowOfCurrent(int direction) {
        return mCurrentFlow[direction];
    }

    /**
     * 获得全部当前流量，四个方向
     *
     * @return 当前4个方向的流量
     */
    public int[] getFlowsOfCurrent() {
        return mCurrentFlow;
    }

    /**
     * 设置四个方向的流量
     *
     * @param flows 四个方向的流量数组
     */
    public void setFlowsOfCurrent(int[] flows) {
        mCurrentFlow = flows;
    }


    public void setLight(int setting, int time) {
        LightSettingHistory[time] = setting;
    }
}

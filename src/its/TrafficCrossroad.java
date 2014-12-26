package its;

import java.util.ArrayList;
import java.util.List;

/**
 * 交通十字路口
 */
public class TrafficCrossroad {

    public String mId; //路口id
    public String[] neighbors; //相邻的路口,顺序为左上右下

    public int[] currentFlow; //当前流量
    public int currentTime; //当前时间
    public List<int[]> flowHistory; //历史流量
    public int[] mLightSettingHistory; //历史设定状态

    //每个时间段增加的流量（预估），第1维表示流量，第2维表示方向[0-3]
    public int[][] mFlowNeighborAdd;

    // 设置邻居
    public void setNeighbors(
            String left, String up, String right, String down) {
        neighbors = new String[4];
        neighbors[0] = left;
        neighbors[1] = up;
        neighbors[2] = right;
        neighbors[3] = down;
    }

    // 路口的ID - 字符串
    public TrafficCrossroad(String id) {

        mId = id;

        mFlowNeighborAdd = new int[Constants.MAX_TIME][];

        for (int i = 0; i < mFlowNeighborAdd.length; i++) {
            mFlowNeighborAdd[i] = new int[4];
        }

        mLightSettingHistory = new int[Constants.MAX_TIME + 1];
        flowHistory = new ArrayList<int[]>();
    }

    public void setLight(int setting, int time) {
        mLightSettingHistory[time] = setting;
    }
}

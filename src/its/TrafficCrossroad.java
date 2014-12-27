package its;

import java.util.ArrayList;
import java.util.List;

/**
 * 交通十字路口
 */
public class TrafficCrossroad {

    public String Id; //路口id
    public String[] Neighbors; //相邻的路口,顺序为左上右下

    public int[] CurrentFlow; //当前流量
    public int CurrentTime; //当前时间
    public List<int[]> FlowHistory; //历史流量
    public int[] LightSettingHistory; //历史设定状态

    //每个时间段增加的流量（预估），第1维表示流量，第2维表示方向[0-3]
    public int[][] FlowNeighborAdd;

    // 设置邻居
    public void setNeighbors(
            String left, String up, String right, String down) {
        Neighbors = new String[4];
        Neighbors[0] = left;
        Neighbors[1] = up;
        Neighbors[2] = right;
        Neighbors[3] = down;
    }

    // 路口的ID - 字符串
    public TrafficCrossroad(String id) {

        Id = id;

        FlowNeighborAdd = new int[Constants.MAX_TIME][];

        for (int i = 0; i < FlowNeighborAdd.length; i++) {
            FlowNeighborAdd[i] = new int[4];
        }

        LightSettingHistory = new int[Constants.MAX_TIME + 1];
        FlowHistory = new ArrayList<int[]>();
    }

    public void setLight(int setting, int time) {
        LightSettingHistory[time] = setting;
    }
}

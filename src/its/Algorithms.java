package its;

import java.util.*;

/**
 * 核心算法
 * <p/>
 * created by C.L.Wang
 */
public class Algorithms {

    public static void Solve(TrafficInfo trafficInfo, int time) {

        // 新增流量
        Map<String, float[]> addFlowSet = trafficInfo.getAddFlowSet();

        // 总流量
        Map<String, float[]> totalFlowSet = trafficInfo.getTotalFlowSet();

        for (String id : addFlowSet.keySet()) {

            Crossroads crossroads = trafficInfo.getCrossesSet().get(id);

            float[] totalFlows = totalFlowSet.get(id); // 当前总流量
            float totalHorizon = totalFlows[0] + totalFlows[2]; // 当前水平总流量
            float totalVertical = totalFlows[1] + totalFlows[3]; // 当前竖直总流量

            float aveHorizon = (totalHorizon) / time;
            float aveVertical = (totalVertical) / time;

            float[] addFlows = addFlowSet.get(id);
            crossroads.addCurFlow(addFlows); // 添加12个方向的流量

            float[] f0 = crossroads.passCurFlowOfSettingZero(); // 设置0的预计通过流量
            float[] f1 = crossroads.passCurFlowOfSettingOne(); // 设置1的预计通过流量

            float total0 = 0.0f, total1 = 0.0f;

            for (float f : f0) {
                total0 += f;
            }

            for (float f : f1) {
                total1 += f;
            }

            // 添加历史流量惩罚
            total0 += aveHorizon * Constants.HISTORY_FLOW_PENALTY;
            total1 += aveVertical * Constants.HISTORY_FLOW_PENALTY;

            int setting = (total0 - total1) > 0 ? 0 : 1; // 选择设置

            // 欺骗
            if (time % 120 < Constants.TRICK_TIME || crossroads.getCurFlow() <= 25) {
                setting = -1;
            }

            crossroads.updateCurFlow(setting);  // 去除选择的流量数据

            crossroads.setSetting(setting); // 记录当前的状态并返回
        }
    }

}

package its;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * 主函数
 * <p/>
 * created by C.L.Wang
 */
public class Main {

    // 控制规则，红灯时间不能超过4个
    private static int mRuleZero[] = new int[Constants.MAX_TIME]; // 状态1
    private static int mRuleOne[] = new int[Constants.MAX_TIME]; // 状态2

    // 将路口的红绿灯状态转换为字符串
    public static String outputLightSettings(TrafficInfo trafficInfo, int time) {

        StringBuilder sb = new StringBuilder();

        int cnt = 0; // 控制分号
        int crossMark = 0; // 路口标记

        for (Map.Entry<String, Crossroads> entry : trafficInfo.getCrossesSet().entrySet()) {

            String cid = entry.getKey();
            Crossroads cross = entry.getValue();

            int setting = cross.getSetting();

            // 满足规则1，路口不能亮灯超过4T
            if (mRuleZero[crossMark] >= Constants.MAX_LIGHT_INTERVAL) {
                setting = 1;
            }

            if (mRuleOne[crossMark] >= Constants.MAX_LIGHT_INTERVAL) {
                setting = 0;
            }

            int[] status = new int[12]; // 12维的状态图

            if (setting == 0) { //水平方向
                status = Constants.SETTING_ZERO.clone();

                mRuleZero[crossMark]++;
                mRuleOne[crossMark] = 0; // 重置

            } else if (setting == 1) { //垂直方向
                status = Constants.SETTING_ONE.clone();

                mRuleOne[crossMark]++;
                mRuleZero[crossMark] = 0; //重置

            } else if (setting == -1) { // 欺骗 - 起始时，可以全部通行，减少压力
                status = Constants.SETTING_ALL.clone();
            }

            // 欺骗 - 起始时，可以全部通行，减少压力
//            if (time % 120 < Constants.TRICK_TIME) {
//                status = Constants.SETTING_ALL.clone();
//            }

            // 更新T型路口的方向
            for (int i = 0; i < 4; ++i) {
                String dir = cross.getNeighbor(i);
                if (dir.equals(Constants.LIGHT_NONE)) {
                    status[(i + 1) % 4 * 3 + 1] = -1;
                    status[(i + 2) % 4 * 3 + 2] = -1;
                    status[(i + 3) % 4 * 3] = -1;
                    break;
                }
            }

            for (int i = 0; i < 4; i++) {
                String dstId = cross.getNeighbor(i);
                if (!dstId.equals(Constants.LIGHT_NONE)) {

                    // 除了第一个和最后一个不加，每个结束都加一个";"
                    if (cnt > 0) {
                        sb.append(";");
                    }
                    cnt++;

                    String direction = cid + "," + dstId;
                    sb.append(direction);
                    for (int j = 0; j < 3; j++) {
                        String flow = "," + status[i * 3 + j];
                        sb.append(flow);
                    }
                }
            }

            crossMark++;
        }

        return sb.toString();
    }

    // 算法的处理函数
    public static String Process(String line, int time, TrafficInfo traffic) {

        // 输入
        Algorithms.Solve(traffic, time);

        return outputLightSettings(traffic, time);
    }

    // 程序运行的主函数，必须按照要求书写
    public static void main(String args[]) throws IOException {

        // 交通结构图
        TrafficInfo trafficInfo = new TrafficInfo();

        // 读入红绿灯的结构图
        trafficInfo.loadLights();

        // main process
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String flows_str = br.readLine();

        int time = 0; // 时间
        while (!"end".equalsIgnoreCase(flows_str)) {

            // 每1个小时，重置数据
            if (time % 120 == 0) {
                trafficInfo = new TrafficInfo();
                trafficInfo.loadLights();
            }

            // 流量
            trafficInfo.addFlows(flows_str, time);

            //TODO  你的代码，注意，数据输出需保证一行输出，除了数据结果，请不要将任何无关数据或异常打印输出。
            System.out.println(Process(flows_str, time, trafficInfo));

            // 获取下一个时间段的流量
            flows_str = br.readLine();

            time++;
        }

    }
}

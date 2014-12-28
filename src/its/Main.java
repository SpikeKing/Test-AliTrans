package its;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {

    /**
     * 将输入的字符串转换为流量map
     *
     * @param traffic - 交通结构图
     * @param line    - 输出字符串
     * @return 流量map
     */
    public static Map<String, int[]> String2Flow(TrafficGraph traffic, String line) {
        Map<String, int[]> ret = new HashMap<String, int[]>();

        for (String id : traffic.getCrosses().keySet()) {
            ret.put(id, new int[4]);
        }

        String[] parts = line.split(";");

        for (String part : parts) {
            String[] pp = part.split(",");
            String id = pp[0];
            String frmId = pp[1];
            int flow = Integer.parseInt(pp[2]);

            TrafficCrossRoad cr = traffic.getCrosses().get(id);

            for (int i = 0; i < cr.getNeighbors().length; i++) {
                if (cr.getNeighbor(i).compareTo(frmId) == 0) {
                    ret.get(id)[i] = flow;
                }
            }
        }

        return ret;
    }

    /**
     * 将路口的红绿灯状态转换为字符串
     *
     * @param traffic - 交通结构图
     * @param time    - 当前时间
     * @return 字符串
     */
    public static String OutputLightSetting(TrafficGraph traffic, int time) {

        StringBuilder sb = new StringBuilder();

        int cnt = 0;
        for (Map.Entry<String, TrafficCrossRoad> entry : traffic.getCrosses().entrySet()) {
            String cid = entry.getKey();
            TrafficCrossRoad cross = entry.getValue();

            int setting = cross.LightSettingHistory[time];

            int[] status = new int[12];
            if (setting == 0) {//水平方向
                status[0] = 1;
                status[1] = 1;
                status[2] = 1;
                status[3] = 0;
                status[4] = 1;
                status[5] = 0;
                status[6] = 1;
                status[7] = 1;
                status[8] = 1;
                status[9] = 0;
                status[10] = 1;
                status[11] = 0;
            } else {//垂直方向
                status[0] = 0;
                status[1] = 1;
                status[2] = 0;
                status[3] = 1;
                status[4] = 1;
                status[5] = 1;
                status[6] = 0;
                status[7] = 1;
                status[8] = 0;
                status[9] = 1;
                status[10] = 1;
                status[11] = 1;
            }

            // 更新T型路口的方向
            for (int i=0; i<4; ++i) {
                String dir = cross.getNeighbor(i);
                if (dir.equals(Constants.LIGHT_NONE)) {
                    status[(i+1)%4*3 + 1] = -1;
                    status[(i+3)%4*3] = -1;
                    status[(i+2)%4*3 + 2] = -1;
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
                    sb.append(cid + "," + dstId);
                    for (int j = 0; j < 3; j++) {
                        sb.append("," + status[i * 3 + j]);
                    }
                }
            }

        }

        return sb.toString();
    }

    /**
     * 根据流量输出红绿灯状态字符串
     *
     * @param line    每个时间段的增加的流量
     * @param time    时间，每个代表一个时间段
     * @param traffic 流量图 - 即时计算
     * @return 红路灯的字符串
     */
    public static String Process(String line, int time, TrafficGraph traffic) {

        Map<String, int[]> flow = String2Flow(traffic, line);
        Algorithms.Solve(traffic, flow, time);

        return OutputLightSetting(traffic, time);
    }


    public static void main(String args[]) throws IOException {

        // 交通结构图
        TrafficGraph traffic = new TrafficGraph();

        // 读入红绿灯的结构图
        traffic.loadLights();

        // main process
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String flows_str = br.readLine();

        int time = 0; // 时间
        while (!"end".equalsIgnoreCase(flows_str)) {

            // 流量
            traffic.addFlows(flows_str, time);

            //TODO  你的代码,注意，数据输出需保证一行输出，除了数据结果，请不要将任何无关数据或异常打印输出
            System.out.println(Process(flows_str, time, traffic));

            //获取下一个时间段的流量
            flows_str = br.readLine();

            time++;
        }

    }
}

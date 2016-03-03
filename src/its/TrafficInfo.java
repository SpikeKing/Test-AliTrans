package its;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交通结构图
 */
public class TrafficInfo {

    // 路口节点的集合，路口节点-附件节点
    private Map<String, Crossroads> mCrossesSet;

    public TrafficInfo() {
        mCrossesSet = new HashMap<String, Crossroads>();
    }

    // 返回路口集合
    public Map<String, Crossroads> getCrossesSet() {
        return mCrossesSet;
    }

    /**
     * 读取红绿灯的结构图，路口和十字路口信息；
     */
    public void loadLights() {

        Map<String, List<String[]>> preMap = new HashMap<String, List<String[]>>();

        String[] lines = LightsStructure.LIGHTS_STRUCTURE.split(";"); // 读取灯的结构图

        for (String line : lines) {
            if (line == null) {
                break;
            }

            line = line.trim(); // 去除首尾空格
            String[] lights = line.split(",");
            String[] aroundLights = Arrays.copyOfRange(lights, 1, lights.length);

            if (lights.length != 5) {
                System.out.println("Error: " + line);
                continue;
            }

            // 把周围的灯放入Map中
            if (preMap.containsKey(lights[0])) {
                preMap.get(lights[0]).add(aroundLights);
            } else {
                List<String[]> lists = new ArrayList<String[]>();
                lists.add(aroundLights);
                preMap.put(lights[0], lists);
            }

            // 测试红绿灯周围
//            System.out.println("当前灯:" + lights[0] + " | 周围灯["
//                    + aroundLights.length + "]: "
//                    + aroundLights[0] + ";"
//                    + aroundLights[1] + ";"
//                    + aroundLights[2] + ";"
//                    + aroundLights[3] + ";");
        }

        for (Map.Entry<String, List<String[]>> entry : preMap.entrySet()) {

            String light = entry.getKey().trim();
            List<String[]> lists = entry.getValue(); // 所有灯的四个方向

//            System.out.println("当前灯: " + light);
//            for (String[] i : lists) {
//                System.out.println("["
//                        + i.length + "]: "
//                        + i[0] + ";"
//                        + i[1] + ";"
//                        + i[2] + ";"
//                        + i[3] + ";");
//            }
//            System.out.println("End\n");

            String left = lists.get(0)[0].trim();
            String up = Constants.LIGHT_NONE;
            String right = Constants.LIGHT_NONE;
            String down = Constants.LIGHT_NONE;

            for (int i = 1; i < lists.size(); i++) {
                String[] rec = lists.get(i);
                String from = rec[0].trim();
                String leftTarget = rec[1].trim();
                String rightTarget = rec[2].trim();
                String straightTarget = rec[3].trim();

                if (leftTarget.compareTo(left) == 0) {
                    down = from;
                }
                if (rightTarget.compareTo(left) == 0) {
                    up = from;
                }
                if (straightTarget.compareTo(left) == 0) {
                    right = from;
                }
            }

            Crossroads cross = new Crossroads(light);
            cross.setNeighbors(left, up, right, down);

//            System.out.println("十字路口的灯:" + light + ";"
//             + "左:" + left + "; 右:" + right + "; 上:" + up + "; 下:" + down);

            mCrossesSet.put(light, cross);
        }
    }

    /**
     * 存储即时增量数据R
     *
     * @param line 所有流量段
     * @param time 时间节点
     */
    public void addFlows(String line, int time) {

        if (!"end".equalsIgnoreCase(line)) {

            String[] parts = line.split(";");

            for (String part : parts) {
                String[] pp = part.split(",");

                String dstId = pp[0]; // 目的
                String frmId = pp[1]; // 来源

                int flow = Integer.parseInt(pp[2]); // 流量

                // 十字路口的节点
                Crossroads cross = mCrossesSet.get(dstId);

                // 存储每个节点的增量数据
                if (cross != null) {

                    // 四个方向
                    for (int i = 0; i < 4; i++) {

                        // 找到流量的来源点
                        if (cross.getNeighbor(i).equals(frmId)) {

                            // 添加总流量
                            cross.addTotalFlowOfDirection(i, flow);

                            // 设置新增流量
                            cross.setAddFlowOfDirection(i, flow);

                        }
                    }
                }
            }
        }
    }

    // 获取总流量的集合
    public Map<String, float[]> getTotalFlowSet() {

        Map<String, float[]> ret = new HashMap<String, float[]>();

        for (Crossroads cross : mCrossesSet.values()) {

            float[] f = new float[4];
            for (int i=0; i<f.length; ++i) {
                f[i] = cross.getTotalFlowOfDirection(i); // 获得总流量
            }

            ret.put(cross.getId(), f);
        }

        return ret;
    }

    // 获取新增流量的集合
    public Map<String, float[]> getAddFlowSet() {
        Map<String, float[]> ret = new HashMap<String, float[]>();

        for (Crossroads cross : mCrossesSet.values()) {

            float[] f = new float[4];
            for (int i=0; i<f.length; ++i) {
                f[i] = cross.getAddFlowOfDirection(i); // 获得新增流量
            }

            ret.put(cross.getId(), f);
        }

        return ret;
    }

}

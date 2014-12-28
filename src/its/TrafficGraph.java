package its;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交通结构图
 */
public class TrafficGraph {

    // 路口节点的集合，路口节点-附件节点
    private Map<String, TrafficCrossroad> mCrosses;

    public TrafficGraph() {
        mCrosses = new HashMap<String, TrafficCrossroad>();
    }

    // 返回路口
    public Map<String, TrafficCrossroad> getCrosses() {
        return mCrosses;
    }

//    public static void main(String args[]) {
//        TrafficGraph tg = new TrafficGraph();
//        tg.loadLights();
//    }

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

            TrafficCrossroad cross = new TrafficCrossroad(light);
            cross.setNeighbors(left, up, right, down);

//            System.out.println("十字路口的灯:" + light + ";"
//             + "左:" + left + "; 右:" + right + "; 上:" + up + "; 下:" + down);

            mCrosses.put(light, cross);
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
                TrafficCrossroad cross = mCrosses.get(dstId);

                // 存储每个节点的增量数据
                if (cross != null) {

                    // 四个方向
                    for (int i = 0; i < 4; i++) {

                        // 找到流量的来源点
                        if (cross.getNeighbor(i).equals(frmId)) {

                            // 每个时间段增加的流量
                            cross.setFlowOfTimeAdd(time, i, flow);

                            // 添加总体流量
                            cross.addFlowOfTotal(i, flow);

                            // 设置当前的流量
                            cross.setFlowOfCurrent(i, flow);
                        }
                    }
                }

            }
        }
    }

    /**
     * 取得当前流量
     *
     * @return
     */
    public Map<String, float[]> getCurrentFlow() {
        Map<String, float[]> ret = new HashMap<String, float[]>();
        for (TrafficCrossroad cross : this.mCrosses.values()) {
            float[] f = new float[4];
            for (int i = 0; i < f.length; i++) {
                f[i] = (float) cross.getFlowOfCurrent(i);
            }
            ret.put(cross.getId(), f);
        }
        return ret;
    }

    /**
     * 取得第time时刻的流量
     *
     * @param time
     * @return
     */
    public Map<String, int[]> getFlow(int time) {
        Map<String, int[]> ret = new HashMap<String, int[]>();
        for (TrafficCrossroad cross : this.mCrosses.values()) {
            int[] f = new int[4];
            for (int i = 0; i < f.length; i++) {
                f[i] = (int) cross.FlowHistory.get(time)[i];
            }
            ret.put(cross.getId(), f);
        }
        return ret;
    }

    /**
     * 取得节点cid的第time时刻的流量
     *
     * @param cid
     * @param time
     * @return
     */
    public int[] getFlowAdd(String cid, int time) {
        return mCrosses.get(cid).getAllFlowsOfTimeAdd(time);
    }

    /**
     * 将第time时刻的流量加上flow
     *
     * @param flow
     * @param time
     */
    public void flowAdd(Map<String, float[]> flow, int time) {
        for (Map.Entry<String, float[]> entry : flow.entrySet()) {
            String cid = entry.getKey();
            float[] cflow = entry.getValue();
            int[] flowAdd = this.getFlowAdd(cid, time);

            Utils.ArrayAdd(cflow, flowAdd);
        }
    }

    public void setLight(String cid, int setting, int time) {
        this.mCrosses.get(cid).setLight(setting, time);
    }

    public void setLight(Map<String, Integer> setting, int time) {
        for (Map.Entry<String, Integer> entry : setting.entrySet()) {
            this.setLight(entry.getKey(), entry.getValue(), time);
        }
    }

    public void saveCurrentFlow() {
        for (Map.Entry<String, TrafficCrossroad> entry : mCrosses.entrySet()) {
            TrafficCrossroad cross = entry.getValue();
            cross.FlowHistory.add(cross.getFlowsOfCurrent().clone());
        }
    }

    public void setCurrentFlow(Map<String, int[]> flow) {
        for (Map.Entry<String, TrafficCrossroad> entry : this.mCrosses
                .entrySet()) {
            String cid = entry.getKey();
            TrafficCrossroad cross = entry.getValue();
            cross.setFlowsOfCurrent(flow.get(cid).clone());
        }
    }

    /**
     * 找出frmId在dstId的哪个方向
     *
     * @param dstId
     * @param frmId
     * @return
     */
    public int findNeighbourIndex(String dstId, String frmId) {
        TrafficCrossroad cr = this.mCrosses.get(dstId);
        for (int i = 0; i < 4; i++) {
            if (cr.getNeighbor(i).compareTo(frmId) == 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 根据当前时间time计算下一次的流量
     *
     * @param time
     * @return
     */
    public Map<String, int[]> computeNextFlow(int time) {
        Map<String, int[]> flow = this.getFlow(time);

        for (TrafficCrossroad cross : this.mCrosses.values()) {
            String cid = cross.getId();
            int setting = cross.LightSettingHistory[time];
            CrossFlow cf = Algorithms.calCrossFlow(cross.FlowHistory.get(time));
            if (setting == 0) {
                cf.flowD2L = 0;
                cf.flowD2U = 0;
                cf.flowU2D = 0;
                cf.flowU2R = 0;
            } else if (setting == 1) {
                cf.flowL2U = 0;
                cf.flowL2R = 0;
                cf.flowR2L = 0;
                cf.flowR2D = 0;
            }

            int[] f = flow.get(cid);
            f[0] -= cf.flowL2D + cf.flowL2R + cf.flowL2U;
            f[1] -= cf.flowU2D + cf.flowU2L + cf.flowU2R;
            f[2] -= cf.flowR2D + cf.flowR2L + cf.flowR2U;
            f[3] -= cf.flowD2L + cf.flowD2R + cf.flowD2U;

            int[] nis = new int[4];
            for (int i = 0; i < 4; i++) {
                nis[i] = findNeighbourIndex(cross.getNeighbor(0), cid);
            }

            if (cross.getNeighbor(0).compareTo(Constants.LIGHT_NONE) != 0) {
                flow.get(cross.getNeighbor(0))[nis[0]] += cf.flowD2L
                        + cf.flowD2R + cf.flowD2U;
            }

        }

        return null;
    }
}

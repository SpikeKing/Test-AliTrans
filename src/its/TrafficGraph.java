package its;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 
 * 交通结构图
 *
 */
public class TrafficGraph {
	
	// 红绿灯结构图的拓扑关系文件转化成的字符串
	private static final String TRAFFIC_TOPO = "tl44,tl42,tl43,#,tl19;tl44,tl43,tl19,tl42,#;tl44,tl19,#,tl43,tl42;tl43,tl44,tl41,tl18,#;tl43,tl41,#,tl44,tl18;tl43,tl18,tl44,#,tl41;tl42,tl26,tl41,#,tl44;tl42,tl41,tl44,tl26,#;tl42,tl44,#,tl41,tl26;tl41,tl42,tl25,tl43,tl40;tl41,tl25,tl40,tl42,tl43;tl41,tl40,tl43,tl25,tl42;tl41,tl43,tl42,tl40,tl25;tl40,tl41,tl24,tl17,tl39;tl40,tl24,tl39,tl41,tl17;tl40,tl39,tl17,tl24,tl41;tl40,tl17,tl41,tl39,tl24;tl39,tl40,tl23,tl16,#;tl39,tl23,#,tl40,tl16;tl39,tl16,tl40,#,tl23;tl38,tl12,tl37,#,tl5;tl38,tl37,tl5,tl12,#;tl38,tl5,#,tl37,tl12;tl37,tl38,tl11,tl4,tl36;tl37,tl11,tl36,tl38,tl4;tl37,tl36,tl4,tl11,tl38;tl37,tl4,tl38,tl36,tl11;tl36,tl37,tl10,tl3,#;tl36,tl10,#,tl37,tl3;tl36,tl3,tl37,#,tl10;tl35,tl52,tl58,tl28,tl34;tl35,tl58,tl34,tl52,tl28;tl35,tl34,tl28,tl58,tl52;tl35,tl28,tl52,tl34,tl58;tl34,tl35,tl57,tl27,tl33;tl34,tl57,tl33,tl35,tl27;tl34,tl33,tl27,tl57,tl35;tl34,tl27,tl35,tl33,tl57;tl33,tl34,tl56,tl26,tl32;tl33,tl56,tl32,tl34,tl26;tl33,tl32,tl26,tl56,tl34;tl33,tl26,tl34,tl32,tl56;tl32,tl33,tl55,tl25,tl31;tl32,tl55,tl31,tl33,tl25;tl32,tl31,tl25,tl55,tl33;tl32,tl25,tl33,tl31,tl55;tl31,tl32,#,tl24,tl30;tl31,tl30,tl24,#,tl32;tl31,tl24,tl32,tl30,#;tl30,tl31,tl54,tl23,tl29;tl30,tl54,tl29,tl31,tl23;tl30,tl29,tl23,tl54,tl31;tl30,tl23,tl31,tl29,tl54;tl29,tl30,tl53,tl22,tl51;tl29,tl53,tl51,tl30,tl22;tl29,tl51,tl22,tl53,tl30;tl29,tl22,tl30,tl51,tl53;tl28,tl35,tl27,#,tl21;tl28,tl27,tl21,tl35,#;tl28,tl21,#,tl27,tl35;tl27,tl28,tl34,tl20,tl26;tl27,tl34,tl26,tl28,tl20;tl27,tl26,tl20,tl34,tl28;tl27,tl20,tl28,tl26,tl34;tl26,tl27,tl33,tl42,tl25;tl26,tl33,tl25,tl27,tl42;tl26,tl25,tl42,tl33,tl27;tl26,tl42,tl27,tl25,tl33;tl25,tl26,tl32,tl41,tl24;tl25,tl32,tl24,tl26,tl41;tl25,tl24,tl41,tl32,tl26;tl25,tl41,tl26,tl24,tl32;tl24,tl25,tl31,tl40,tl23;tl24,tl31,tl23,tl25,tl40;tl24,tl23,tl40,tl31,tl25;tl24,tl40,tl25,tl23,tl31;tl23,tl24,tl30,tl39,tl22;tl23,tl30,tl22,tl24,tl39;tl23,tl22,tl39,tl30,tl24;tl23,tl39,tl24,tl22,tl30;tl22,tl23,tl29,tl14,#;tl22,tl29,#,tl23,tl14;tl22,tl14,tl23,#,tl29;tl21,tl28,tl20,#,tl6;tl21,tl20,tl6,tl28,#;tl21,tl6,#,tl20,tl28;tl20,tl21,tl27,#,tl19;tl20,tl27,tl19,tl21,#;tl20,tl19,#,tl27,tl21;tl19,tl20,tl44,tl12,tl18;tl19,tl44,tl18,tl20,tl12;tl19,tl18,tl12,tl44,tl20;tl19,tl12,tl20,tl18,tl44;tl18,tl19,tl43,tl11,tl17;tl18,tl43,tl17,tl19,tl11;tl18,tl17,tl11,tl43,tl19;tl18,tl11,tl19,tl17,tl43;tl17,tl18,tl40,tl10,tl16;tl17,tl40,tl16,tl18,tl10;tl17,tl16,tl10,tl40,tl18;tl17,tl10,tl18,tl16,tl40;tl16,tl17,tl39,tl9,tl15;tl16,tl39,tl15,tl17,tl9;tl16,tl15,tl9,tl39,tl17;tl16,tl9,tl17,tl15,tl39;tl15,tl16,#,tl8,tl14;tl15,tl14,tl8,#,tl16;tl15,tl8,tl16,tl14,#;tl14,tl15,tl22,tl7,#;tl14,tl22,#,tl15,tl7;tl14,tl7,tl15,#,tl22;tl12,tl19,tl11,#,tl38;tl12,tl11,tl38,tl19,#;tl12,tl38,#,tl11,tl19;tl11,tl12,tl18,tl37,tl10;tl11,tl18,tl10,tl12,tl37;tl11,tl10,tl37,tl18,tl12;tl11,tl37,tl12,tl10,tl18;tl10,tl11,tl17,tl36,tl9;tl10,tl17,tl9,tl11,tl36;tl10,tl9,tl36,tl17,tl11;tl10,tl36,tl11,tl9,tl17;tl9,tl10,tl16,tl2,tl8;tl9,tl16,tl8,tl10,tl2;tl9,tl8,tl2,tl16,tl10;tl9,tl2,tl10,tl8,tl16;tl8,tl9,tl15,#,tl7;tl8,tl15,tl7,tl9,#;tl8,tl7,#,tl15,tl9;tl7,tl8,tl14,tl1,tl13;tl7,tl14,tl13,tl8,tl1;tl7,tl13,tl1,tl14,tl8;tl7,tl1,tl8,tl13,tl14;tl1,tl2,tl7,#,tl45;tl1,tl7,tl45,tl2,#;tl1,tl45,#,tl7,tl2;tl2,tl47,tl3,tl1,tl9;tl2,tl3,tl9,tl47,tl1;tl2,tl9,tl1,tl3,tl47;tl2,tl1,tl47,tl9,tl3;tl3,tl4,tl36,#,tl2;tl3,tl36,tl2,tl4,#;tl3,tl2,#,tl36,tl4;tl4,tl48,tl5,tl3,tl37;tl4,tl5,tl37,tl48,tl3;tl4,tl37,tl3,tl5,tl48;tl4,tl3,tl48,tl37,tl5;tl5,tl49,tl6,tl4,tl38;tl5,tl6,tl38,tl49,tl4;tl5,tl38,tl4,tl6,tl49;tl5,tl4,tl49,tl38,tl6;tl6,tl50,tl46,tl5,tl21;tl6,tl46,tl21,tl50,tl5;tl6,tl21,tl5,tl46,tl50;tl6,tl5,tl50,tl21,tl46";
	
	public Map<String, TrafficCrossroad> mCrosses; // 所有节点

	TrafficGraph() {
		mCrosses = new HashMap<String, TrafficCrossroad>();
	}

	/***
	 * 从reader中读入交通结构
	 * 替换为使用loadv2()，从字符串读取交通结构
	 * 
	 * @param reader - 输入
	 * @throws IOException
	 */
	@Deprecated
	public void load(BufferedReader reader) throws IOException {
				
		Map<String, List<String[]>> preMap = new HashMap<String, List<String[]>>();
		
		String line = "";

		while (line != null) {
			line = reader.readLine();
			if (line == null) {
				break;
			}
			line = line.trim();
			String[] parts = line.split(",");
			String[] otherParts = Arrays.copyOfRange(parts, 1, parts.length);

			if (parts.length != 5) {
				System.out.println(line);
				reader.close();
				throw new RuntimeException("logic error" + "part's length:"
						+ parts.length + line);
			}
			if (preMap.containsKey(parts[0])) {
				preMap.get(parts[0]).add(otherParts);
			} else {
				List<String[]> lists = new ArrayList<String[]>();
				lists.add(otherParts);
				preMap.put(parts[0], lists);
			}
		}

		reader.close();

		for (Map.Entry<String, List<String[]>> entry : preMap.entrySet()) {
			String cid = entry.getKey().trim();
			List<String[]> lists = entry.getValue();
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

			TrafficCrossroad cross = new TrafficCrossroad(cid);
			cross.setNeightbours(left, up, right, down);
			mCrosses.put(cid, cross);
		}

	}
	@Deprecated
	public void load(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		this.load(reader);
	}
	
	// 使用固定的字符串读入红绿灯结构图
	public void loadv2() {
		
		Map<String, List<String[]>> preMap = new HashMap<String, List<String[]>>();
		
		String[] lines = TRAFFIC_TOPO.split(";");
		for (String line : lines) {
			if (line == null) {
				break;
			}
			line = line.trim();
			String[] parts = line.split(",");
			String[] otherParts = Arrays.copyOfRange(parts, 1, parts.length);

			if (parts.length != 5) {
				System.out.println(line);
				throw new RuntimeException("logic error" + "part's length:"
						+ parts.length + line);
			}
			
			if (preMap.containsKey(parts[0])) {
				preMap.get(parts[0]).add(otherParts);
			} else {
				List<String[]> lists = new ArrayList<String[]>();
				lists.add(otherParts);
				preMap.put(parts[0], lists);
			}
		}

		for (Map.Entry<String, List<String[]>> entry : preMap.entrySet()) {
			String cid = entry.getKey().trim();
			List<String[]> lists = entry.getValue();
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

			TrafficCrossroad cross = new TrafficCrossroad(cid);
			cross.setNeightbours(left, up, right, down);
			mCrosses.put(cid, cross);
		}
	}

	/***
	 * 读入各个路口突然出现的流量
	 * 替换为使用loadFlowAddv2()，根据时时输入的数据，计算交通结构
	 * 
	 * @param reader - 输入
	 * @throws IOException
	 */
	@Deprecated
	public void loadFlowAdd(BufferedReader reader) throws IOException {
		String line = reader.readLine();

		while (line != null) {
			line = line.trim();
			String parts[] = line.split(",");
			String frmId = parts[1];
			String dstId = parts[0];
			TrafficCrossroad vertex = mCrosses.get(dstId);
			if (vertex != null) {
				String[] flows = Arrays.copyOfRange(parts, 2, parts.length);
				for (int i = 0; i < 4; i++) {
					if (vertex.neighbours[i].compareTo(frmId) == 0) {
						for (int j = 0; j < flows.length; j++) {
							vertex.flowAdd[j][i] = Integer.parseInt(flows[j]);
						}
					}
				}
			}
			line = reader.readLine();
		}
	}
	@Deprecated
	public void loadFlowAdd(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		this.loadFlowAdd(reader);
		reader.close();
	}

	// 通过即时数据获取交通流量信息
	public void loadFlowAddv2(String line, int time) throws IOException {
		
		if (!"end".equalsIgnoreCase(line)) {
			
			String[] parts = line.split(";");
			
			for(String part : parts)
			{
				String[] pp = part.split(",");
				String dstId = pp[0];
				String frmId = pp[1];
				int flow = Integer.parseInt(pp[2]);
				
				TrafficCrossroad vertex = this.mCrosses.get(dstId);
				
				if (vertex != null) {
					for (int i = 0; i < 4; i++) {
						if (vertex.neighbours[i].compareTo(frmId) == 0) {
								vertex.flowAdd[time][i] = flow;
						}
					}
				}
				
			}
		}
	}

	/***
	 * 取得当前流量
	 * 
	 * @return
	 */
	public Map<String, float[]> getCurrentFlow() {
		Map<String, float[]> ret = new HashMap<String, float[]>();
		for (TrafficCrossroad cross : this.mCrosses.values()) {
			float[] f = new float[4];
			for (int i = 0; i < f.length; i++) {
				f[i] = (float) cross.currentFlow[i];
			}
			ret.put(cross.id, f);
		}
		return ret;
	}

	/***
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
				f[i] = (int) cross.flowHistory.get(time)[i];
			}
			ret.put(cross.id, f);
		}
		return ret;
	}

	/***
	 * 取得节点cid的第time时刻的流量
	 * 
	 * @param cid
	 * @param time
	 * @return
	 */
	public int[] getFlowAdd(String cid, int time) {
		return this.mCrosses.get(cid).flowAdd[time];
	}

	/***
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
			cross.flowHistory.add(cross.currentFlow.clone());
		}
	}

	public void setCurrentFlow(Map<String, int[]> flow) {
		for (Map.Entry<String, TrafficCrossroad> entry : this.mCrosses
				.entrySet()) {
			String cid = entry.getKey();
			TrafficCrossroad cross = entry.getValue();
			cross.currentFlow = flow.get(cid).clone();
		}
	}

	/***
	 * 找出frmId在dstId的哪个方向
	 * 
	 * @param dstId
	 * @param frmId
	 * @return
	 */
	public int findNeighbourIndex(String dstId, String frmId) {
		TrafficCrossroad cr = this.mCrosses.get(dstId);
		for (int i = 0; i < 4; i++) {
			if (cr.neighbours[i].compareTo(frmId) == 0) {
				return i;
			}
		}

		return -1;
	}

	/***
	 * 根据当前时间time计算下一次的流量
	 * 
	 * @param time
	 * @return
	 */
	public Map<String, int[]> computeNextFlow(int time) {
		Map<String, int[]> flow = this.getFlow(time);

		for (TrafficCrossroad cross : this.mCrosses.values()) {
			String cid = cross.id;
			int setting = cross.lightSettingHistory[time];
			CrossFlow cf = Algorithms.CalcCrossFlow(
					cross.flowHistory.get(time), Constants.TURN_PROBA);
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
				nis[i] = findNeighbourIndex(cross.neighbours[0], cid);
			}

			if (cross.neighbours[0].compareTo(Constants.LIGHT_NONE) != 0) {
				flow.get(cross.neighbours[0])[nis[0]] += cf.flowD2L
						+ cf.flowD2R + cf.flowD2U;
			}

		}

		return null;
	}
}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * 
 * @author sy
 * @time 2014-12-02
 * @description 输入：系统向参赛选手传递当前时段的流量,在args[0]中。
 *              流量的格式为:TrafficLightID,FromID,Flow;TrafficLightID,FromID,Flow;....
 *              交通图谱结构由参赛选手根据文档中的拓扑图自己构造。
 *              如构造成格式为:TrafficLightID,FromID1,ToID1,ToID2,ToID3;TrafficLightID,FromID1,ToID1,ToID2,ToID3;...的字符串
 *              输出：使用System.out.println()输出格式为:TrafficLightID,FromID1,leftStatus,rightStatus,straightStatus;TrafficLightID,FromID1,leftStatus,rightStatus,straightStatus;...的字符串
 */
public class Demo_v2 {
	
	//交通拓扑结构字符串，格式为：TrafficLightID,FromID1(来源),ToID1(左转),ToID2(右转),ToID3(直行);TrafficLightID,FromID1,ToID1,ToID2,ToID3;...,不能通行的用#表示
	private static String traffic_topo_str = "tl44,tl42,tl43,#,tl19;tl44,tl43,tl19,tl42,#;"
			+ "tl44,tl19,#,tl43,tl42;tl43,tl44,tl41,tl18,#;tl43,tl41,#,tl44,tl18;tl43,tl18,tl44,#,tl41;"
			+ "tl42,tl26,tl41,#,tl44;tl42,tl41,tl44,tl26,#;tl42,tl44,#,tl41,tl26;tl41,tl42,tl25,tl43,tl40;"
			+ "tl41,tl25,tl40,tl42,tl43;tl41,tl40,tl43,tl25,tl42;tl41,tl43,tl42,tl40,tl25;tl40,tl41,tl24,tl17,tl39;"
			+ "tl40,tl24,tl39,tl41,tl17;tl40,tl39,tl17,tl24,tl41;tl40,tl17,tl41,tl39,tl24;tl39,tl40,tl23,tl16,#;"
			+ "tl39,tl23,#,tl40,tl16;tl39,tl16,tl40,tl23,#;tl38,tl12,tl37,#,tl5;tl38,tl37,tl5,tl12,#;"
			+ "tl38,tl5,#,tl37,tl12;tl37,tl38,tl11,tl4,tl36;tl37,tl11,tl36,tl38,tl4;tl37,tl36,tl4,tl11,tl38;"
			+ "tl37,tl4,tl38,tl36,tl11;tl36,tl37,tl10,tl3,#;tl36,tl10,#,tl37,tl3;tl36,tl3,tl37,#,tl10;"
			+ "tl35,tl52,tl58,tl28,tl34;tl35,tl58,tl34,tl52,tl28;tl35,tl34,tl28,tl58,tl52;tl35,tl28,tl52,tl34,tl58;"
			+ "tl34,tl35,tl57,tl27,tl33;tl34,tl57,tl33,tl35,tl27;tl34,tl33,tl27,tl57,tl35;tl34,tl27,tl35,tl33,tl57;"
			+ "tl33,tl34,tl56,tl26,tl32;tl33,tl56,tl32,tl34,tl26;tl33,tl32,tl26,tl56,tl34;tl33,tl26,tl34,tl32,tl56;"
			+ "tl32,tl33,tl55,tl25,tl31;tl32,tl55,tl31,tl33,tl25;tl32,tl31,tl25,tl55,tl33;tl32,tl25,tl33,tl31,tl55;"
			+ "tl31,tl32,#,tl24,tl30;tl31,tl30,tl24,#,tl32;tl31,tl24,tl32,tl30,#;tl30,tl31,tl54,tl23,tl29;tl30,tl54,tl29,tl31,tl23;"
			+ "tl30,tl29,tl23,tl54,tl31;tl30,tl23,tl31,tl29,tl54;tl29,tl30,tl53,tl22,tl51;tl29,tl53,tl51,tl30,tl22;tl29,tl51,tl22,tl53,tl30;"
			+ "tl29,tl22,tl30,tl51,tl53;tl28,tl35,tl27,#,tl21;tl28,tl27,tl21,tl35,#;tl28,tl21,#,tl27,tl35;tl27,tl28,tl34,tl20,tl26;"
			+ "tl27,tl34,tl26,tl28,tl20;tl27,tl26,tl20,tl34,tl28;tl27,tl20,tl28,tl26,tl34;tl26,tl27,tl33,tl42,tl25;tl26,tl33,tl25,tl27,tl42;"
			+ "tl26,tl25,tl42,tl33,tl27;tl26,tl42,tl27,tl25,tl33;tl25,tl26,tl32,tl41,tl24;tl25,tl32,tl24,tl26,tl41;tl25,tl24,tl41,tl32,tl26;"
			+ "tl25,tl41,tl26,tl24,tl32;tl24,tl25,tl31,tl40,tl23;tl24,tl31,tl23,tl25,tl40;tl24,tl23,tl40,tl31,tl25;tl24,tl40,tl25,tl23,tl31;"
			+ "tl23,tl24,tl30,tl39,tl22;tl23,tl30,tl22,tl24,tl39;tl23,tl22,tl39,tl30,tl24;tl23,tl39,tl24,tl22,tl30;tl22,tl23,tl29,tl14,#;tl22,tl29,#,tl23,tl14;"
			+ "tl22,tl14,tl23,#,tl29;tl21,tl28,tl20,#,tl6;tl21,tl20,tl6,tl28,#;tl21,tl6,#,tl20,tl28;tl20,tl21,tl27,#,tl19;tl20,tl27,tl19,tl21,#;tl20,tl19,#,tl27,tl21;"
			+ "tl19,tl20,tl44,tl12,tl18;tl19,tl44,tl18,tl20,tl12;tl19,tl18,tl12,tl44,tl20;tl19,tl12,tl20,tl18,tl44;tl18,tl19,tl43,tl11,tl17;tl18,tl43,tl17,tl19,tl11;"
			+ "tl18,tl17,tl11,tl43,tl19;tl18,tl11,tl19,tl17,tl43;tl17,tl18,tl40,tl10,tl16;tl17,tl40,tl16,tl18,tl10;tl17,tl16,tl10,tl40,tl18;tl17,tl10,tl18,tl16,tl40;tl16,tl17,tl39,tl9,tl15;"
			+ "tl16,tl39,tl15,tl17,tl9;tl16,tl15,tl9,tl39,tl17;tl16,tl9,tl17,tl15,tl39;tl15,tl16,#,tl8,tl14;tl15,tl14,tl8,#,tl16;tl15,tl8,tl16,tl14,#;tl14,tl15,tl22,tl7,#;tl14,tl22,#,tl15,tl7;"
			+ "tl14,tl7,tl15,#,tl22;tl12,tl19,tl11,#,tl38;tl12,tl11,tl38,tl19,#;tl12,tl38,#,tl11,tl19;tl11,tl12,tl18,tl37,tl10;tl11,tl18,tl10,tl12,tl37;tl11,tl10,tl37,tl18,tl12;tl11,tl37,tl12,tl10,tl18;"
			+ "tl10,tl11,tl17,tl36,tl9;tl10,tl17,tl9,tl11,tl36;tl10,tl9,tl36,tl17,tl11;tl10,tl36,tl11,tl9,tl17;tl9,tl10,tl16,tl2,tl8;tl9,tl16,tl8,tl10,tl2;tl9,tl8,tl2,tl16,tl10;tl9,tl2,tl10,tl8,tl16;"
			+ "tl8,tl9,tl15,#,tl7;tl8,tl15,tl7,tl9,#;tl8,tl7,#,tl15,tl9;tl7,tl8,tl14,tl1,tl13;tl7,tl14,tl13,tl8,tl1;tl7,tl13,tl1,tl14,tl8;tl7,tl1,tl8,tl13,tl14;tl1,tl2,tl7,#,tl45;tl1,tl7,tl45,tl2,#;"
			+ "tl1,tl45,#,tl7,tl2;tl2,tl47,tl3,tl1,tl9;tl2,tl3,tl9,tl47,tl1;tl2,tl9,tl1,tl3,tl47;tl2,tl1,tl47,tl9,tl3;tl3,tl4,tl36,#,tl2;tl3,tl36,tl2,tl4,#;tl3,tl2,#,tl36,tl4;tl4,tl48,tl5,tl3,tl37;tl4,tl5,tl37,tl48,tl3;tl4,tl37,tl3,tl5,tl48;"
			+ "tl4,tl3,tl48,tl37,tl5;tl5,tl49,tl6,tl4,tl38;tl5,tl6,tl38,tl49,tl4;tl5,tl38,tl4,tl6,tl49;tl5,tl4,tl49,tl38,tl6;tl6,tl50,tl46,tl5,tl21;tl6,tl46,tl21,tl50,tl5;tl6,tl21,tl5,tl46,tl50;tl6,tl5,tl50,tl21,tl46;";
	//交通节点信息表，该表每一项是一个交通十字路口（或者T字路口）格式为：TrafficLightID:[FromID1,FromID2,FromID3,FromID4]
	private static Map<String, String[]> traffic_light_nodes_table = new HashMap<String, String[]>();
    //交通拓扑结构表,格式为：TrafficLightID:[FromID1,ToID1,ToID2,ToID3]
	private static Map<String, String[]> traffic_light_topo_table = new HashMap<String, String[]>();
    
    //某个时段T(i)所有路口车辆的 转向概率:[左转,右转,直行],初赛写死
	private static double[] turn_rate = new double[]{0.1,0.1,0.8};
  	
  	//某个时段T(i)所有路口车辆的 通过率:[左转,右转,直行],初赛写死
	private static int[] through_rate = new int[]{2,2,16};
  	
  	//某个时段T(i)所有路口红 绿灯的状态:[左转,右转,直行] (红灯为0，绿等为1,缺失-1)
	private static Map<String, Integer[]> all_road_best_status = new HashMap<String,Integer[]>();
	
	//每个红绿灯的候选状态,因为是局部版本，只考虑单节点，所以右转总是可行的
	private static int[][] candidate_status = {{0,1,0},{0,1,1},{1,1,0},{1,1,1}};
  	
    //某个小时内红绿灯的历史信息
	private static Map<String, ArrayList<Integer[]>> status_history = new HashMap<String,ArrayList<Integer[]>>();
   
  	//某个时段T(i)所有路口的流量
	private static Map<String, Integer> all_road_current_flow = new HashMap<String,Integer>();
	
	//临时存储十字路口的最好状态
	private static Map<String, Integer[]> best_status = new HashMap<String, Integer[]>();
	
	// 当前十字或者T字路口各个路口灯的状态
	private static Map<String, Integer[]> current_traffic_status = new HashMap<String, Integer[]>();
  	
  	//某个小时第几个时段
	private static int TIME_I = 0;
  	
  	//违规惩罚系数,初赛写死
	private static double ZETA = 0.5;
  	
    //每一个时间段，系统都会调用一次参赛选手的main函数，以参数的形式向选手传递当前时间段每个路口的流量(args[0])
  	//按照TrafficLightID,FromID,Flow;TrafficLightID,FromID,Flow;....格式
	public static void main(String[] args) throws NumberFormatException, IOException{
		//加载交通拓扑结构
		loadTrafficTable();
		//获取当前时刻流量字符串
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//获取某个时段T(i)整个交通输入流量
		String flows_str = br.readLine();
		while(!"end".equalsIgnoreCase(flows_str)){
			//清除上一时刻的状态
			all_road_best_status.clear();
			//清除上一时刻的流量
			all_road_current_flow.clear();
			//解析当前时刻流量字符串
			String[] flows = flows_str.trim().split(";");
			for(String flow:flows){
				String [] strs = flow.trim().split(",");
				String key = strs[0]+"-"+strs[1];
				all_road_current_flow.put(key, Integer.valueOf(strs[2]));
			}
			
			//初始化当前交通状态
			initCurrentStatus();
			
			//随机搜索，取其中最好的状态
			generateStatus();
			
			//获取下一个时间段的流量
			flows_str = br.readLine();
		
			++TIME_I;
		}
	}
	
	//从交通拓扑结构字符串中解析
	private static void loadTrafficTable(){
		String[] lights = traffic_topo_str.trim().split(";");
		for(String light:lights){
			String [] strs = light.trim().split(",");
			String key = strs[0]+"-"+strs[1];
			if(!traffic_light_nodes_table.containsKey(strs[0])){
				if("#".equals(strs[1])){
					traffic_light_nodes_table.put(strs[0], new String[]{strs[2],strs[3],strs[4]});
				}else if("#".equals(strs[2])){
					traffic_light_nodes_table.put(strs[0], new String[]{strs[1],strs[3],strs[4]});
				}else if("#".equals(strs[3])){
					traffic_light_nodes_table.put(strs[0], new String[]{strs[1],strs[2],strs[4]});
				}else if("#".equals(strs[4])){
					traffic_light_nodes_table.put(strs[0], new String[]{strs[1],strs[2],strs[3]});
			    }else{
			    	traffic_light_nodes_table.put(strs[0], new String[]{strs[1],strs[2],strs[3],strs[4]});
				}
				
			}
			traffic_light_topo_table.put(key, new String[]{strs[2],strs[3],strs[4]});
		}
	    
	}
	
	//生成状态
	private static void generateStatus() throws IOException{
		// 遍历每个交通路口（十字路口或者T字路口）
		Iterator<?> iter1 = traffic_light_nodes_table.entrySet().iterator();
		while (iter1.hasNext()) {
			// 该十字路口最好的状态的目标函数值
			double best_penalty = Integer.MAX_VALUE;
			best_status.clear();
			// 获取TrafficLightID,FromIDs
			Map.Entry<String, String[]> entry1 = (Map.Entry<String, String[]>) iter1.next();
			String traffic_light_id = (String) entry1.getKey();
			String[] from_ids = (String[]) entry1.getValue();
			
			//清除
			current_traffic_status.clear();
			
			//目的路口
			String[] destinations;
			
			//来源路口个数，T行为3，十字形为4
			int from_id_num = from_ids.length;
			if (from_id_num == 3) {
				for (int i1 = 0; i1 < 4; ++i1) {
					String key1 = traffic_light_id + "-" + from_ids[0];
					// 左转、右转、直行3个转态,0表示红灯，1表示绿灯，-1表示无路可走
					Integer[] status1 = new Integer[3];
					// 该路口可以通向的三个方向，分别为左转、右转、直行
					destinations = traffic_light_topo_table.get(key1);
					for (int idx = 0; idx < 3; ++idx) {
						if ("#".equals(destinations[idx])) {
							status1[idx] = -1;
						}else{
							status1[idx] = candidate_status[i1][idx];
						}
					}
					// 存储当前得到的转态
					current_traffic_status.put(key1, status1);
					for (int i2 = 0; i2 < 4; ++i2) {
						String key2 = traffic_light_id + "-" + from_ids[1];
						// 左转、右转、直行3个转态,0表示红灯，1表示绿灯，-1表示无路可走
						Integer[] status2 = new Integer[3];
						// 该路口可以通向的三个方向，分别为左转、右转、直行
						destinations = traffic_light_topo_table.get(key2);
						for (int idx = 0; idx < 3; ++idx) {
							if ("#".equals(destinations[idx])) {
								status2[idx] = -1;
							}else{
								status2[idx] = candidate_status[i2][idx];
							}
						}
						// 存储当前得到的转态
						current_traffic_status.put(key2, status2);
						for (int i3 = 0; i3 < 4; ++i3) {
							String key3 = traffic_light_id + "-" + from_ids[2];
							// 左转、右转、直行3个转态,0表示红灯，1表示绿灯，-1表示无路可走
							Integer[] status3 = new Integer[3];
							// 该路口可以通向的三个方向，分别为左转、右转、直行
							destinations = traffic_light_topo_table.get(key3);
							for (int idx = 0; idx < 3; ++idx) {
								if ("#".equals(destinations[idx])) {
									status3[idx] = -1;
								}else{
									status3[idx] = candidate_status[i3][idx];
								}
							}
							// 存储当前得到的转态
							current_traffic_status.put(key3, status3);
							// 更新状态表
							setStatus(current_traffic_status);
							// 计算该转态下的目标值
							double penalty = computePenalty(current_traffic_status);
							// 选择目标值最小的转态
							if (best_penalty > penalty) {
								best_penalty = penalty;
								for(String k:current_traffic_status.keySet()){
									best_status.put(k,current_traffic_status.get(k));
								}
							}
							
							
						}
					}
				}

			} else if (from_id_num == 4) {
				for (int i1 = 0; i1 < 4; ++i1) {
					String key1 = traffic_light_id + "-" + from_ids[0];
					// 左转、右转、直行3个转态,0表示红灯，1表示绿灯，-1表示无路可走
					Integer[] status1 = new Integer[3];
					// 该路口可以通向的三个方向，分别为左转、右转、直行
					destinations = traffic_light_topo_table.get(key1);
					for (int idx = 0; idx < 3; ++idx) {
						if ("#".equals(destinations[idx])) {
							status1[idx] = -1;
						}else{
							status1[idx] = candidate_status[i1][idx];
						}
					}
					// 存储当前得到的转态
					current_traffic_status.put(key1, status1);
					for (int i2 = 0; i2 < 4; ++i2) {
						String key2 = traffic_light_id + "-" + from_ids[1];
						// 左转、右转、直行3个转态,0表示红灯，1表示绿灯，-1表示无路可走
						Integer[] status2 = new Integer[3];
						// 该路口可以通向的三个方向，分别为左转、右转、直行
						destinations = traffic_light_topo_table.get(key2);
						for (int idx = 0; idx < 3; ++idx) {
							if ("#".equals(destinations[idx])) {
								status2[idx] = -1;
							}else{
								status2[idx] = candidate_status[i2][idx];
							}
						}
						// 存储当前得到的转态
						current_traffic_status.put(key2, status2);
						for (int i3 = 0; i3 < 4; ++i3) {
							String key3 = traffic_light_id + "-" + from_ids[2];
							// 左转、右转、直行3个转态,0表示红灯，1表示绿灯，-1表示无路可走
							Integer[] status3 = new Integer[3];
							// 该路口可以通向的三个方向，分别为左转、右转、直行
							destinations = traffic_light_topo_table.get(key3);
							for (int idx = 0; idx < 3; ++idx) {
								if ("#".equals(destinations[idx])) {
									status3[idx] = -1;
								}else{
									status3[idx] = candidate_status[i3][idx];
								}
							}
							// 存储当前得到的转态
							current_traffic_status.put(key3, status3);
							for (int i4 = 0; i4 < 4; ++i4) {
								String key4 = traffic_light_id + "-"
										+ from_ids[3];
								// 左转、右转、直行3个转态,0表示红灯，1表示绿灯，-1表示无路可走
								Integer[] status4 = new Integer[3];
								// 该路口可以通向的三个方向，分别为左转、右转、直行
								destinations = traffic_light_topo_table.get(key4);
								for (int idx = 0; idx < 3; ++idx) {
									if ("#".equals(destinations[idx])) {
										status4[idx] = -1;
									}else{
										status4[idx] = candidate_status[i4][idx];
									}
								}
								// 存储当前得到的转态
								current_traffic_status.put(key4, status4);
								// 更新状态表
								setStatus(current_traffic_status);
								// 计算该转态下的目标值
								double penalty = computePenalty(current_traffic_status);
								// 选择目标值最小的转态
								if (best_penalty > penalty) {
									best_penalty = penalty;
									for(String k:current_traffic_status.keySet()){
										best_status.put(k,current_traffic_status.get(k));
									}
								}
								
							}
						}
					}
				}
			}
			
			//更新状态表
			setStatus(best_status);
			
			Iterator<?> iter2 = best_status.entrySet().iterator();
			while(iter2.hasNext()){
				Map.Entry<String, Integer[]> entry2 = (Map.Entry<String, Integer[]>) iter2.next();
				all_road_best_status.put(entry2.getKey(), entry2.getValue());
			}
		}
			
		//输出状态
		printCurrentStatus();
		
	}
	
    //根据status_history、all_road_current_flow计算T(i)时刻某路口滞留车辆数
	private static int computeStay(String key) {
  		
  		//左转右转直行实际能够通过的车辆数
  		int left_through,right_through,straight_through;
  		left_through = right_through =  straight_through = 0;
  		
  		//判断是十字路口还是T字路口,-1:三个方向都可以走的路口,0:不能左转的路口,1:不能右转的路口,2:不能直行的路口
  		int turn_flag = -1;
  	    //绿灯时通过率才有效
  		int status = status_history.get(key).get(TIME_I)[0];
  		if (status==1) {
  			left_through = through_rate[0];
  		}else if(status==-1){
  			turn_flag = 0;
  		}
  		status = status_history.get(key).get(TIME_I)[0];
  		if (status_history.get(key).get(TIME_I)[1]==1) {
  			right_through = through_rate[1];
  		}else if(status==-1){
  			turn_flag = 1;
  		}
  		status = status_history.get(key).get(TIME_I)[0];
  		if (status_history.get(key).get(TIME_I)[2]==1) {
  			straight_through = through_rate[2];
  		}else if(status==-1){
  			turn_flag = 2;
  		}
  		
  		//获取当前时段该路口的流量
  		int i_flow = all_road_current_flow.get(key);
  		
  		
  		
  		//左转右转直行的滞留车辆
  		int left_stay,right_stay,straight_stay;
  		left_stay = right_stay = straight_stay = 0;
  		
  		//如果是T字路口，则其他两个方向需要平分不能通行的方向的转向率
  		double left_rate = turn_rate[0];
  		double right_rate = turn_rate[1];
  		double straight_rate = turn_rate[2];
  		switch(turn_flag){
  		    case 0:
  		    	right_rate += left_rate;
  		    	left_rate = 0;
  		    	break;
  		    case 1:
  		    	left_rate += right_rate;
  		    	right_rate = 0;
  		    	break;
  		    case 2:
  		    	left_rate += straight_rate*0.5;
  		    	right_rate += straight_rate*0.5;
  		    	straight_rate = 0;
  		    	break;
  		    default:
  		    	break;
  		}

  		//上取整可能会导致流量增大一点
  		left_stay = Math.max(0, (int)Math.ceil(i_flow*left_rate) - left_through);
  		right_stay = Math.max(0, (int)Math.ceil(i_flow*right_rate) - right_through);
  		straight_stay = Math.max(0,(int)Math.ceil(i_flow*straight_rate)-straight_through);
  		
  		//返回T(i)时刻滞留车辆数
  		return (left_stay + right_stay + straight_stay);
  		
  	}
  	
    //根据currentStatus计算penalty,该方法只计算某一个十字路口（或则交通路口）
	private static double computePenalty(Map<String, Integer[]> current_traffic_status){
  	    //代价
  	    double penalty = 0;
		// 每个路口T(i)时刻penalty: 左转滞留+右转滞留+直行滞留;违反交通规则扣分;违反公平性原则扣分
		for (String key : current_traffic_status.keySet()) {
			// 更新，车辆滞留部分
			penalty += computeStay(key);
			
			// 更新，加上红绿灯违反交通规则的惩罚 a:直行垂直直行惩罚 b:直行垂直左转惩罚
			double a, b;
			a = b = 0;
			
			String[] lights = key.split("-");
			String left_key = lights[0] + "-" + traffic_light_topo_table.get(key)[0];
			String right_key = lights[0] + "-" + traffic_light_topo_table.get(key)[1];

			// 垂直方向不能同时直行
			if (current_traffic_status.get(key)[2] == 1 && 
					((current_traffic_status.containsKey(left_key) && current_traffic_status.get(left_key)[2] == 1) 
							|| (current_traffic_status.containsKey(right_key)&& current_traffic_status.get(right_key)[2] == 1))) {
			    a += ZETA * all_road_current_flow.get(key);
				if (current_traffic_status.containsKey(left_key)) {
					a += ZETA * all_road_current_flow.get(left_key);	
				}
				if (current_traffic_status.containsKey(right_key)) {
					a += ZETA * all_road_current_flow.get(right_key);
			    }
			}
			// 直行时垂直方向右侧不能左转
			if (current_traffic_status.get(key)[2] == 1&& current_traffic_status.containsKey(right_key) && current_traffic_status.get(right_key)[0] == 1) {
					b += ZETA* (all_road_current_flow.get(right_key) +all_road_current_flow.get(key));
			} 

			// 违规扣分
			penalty += (0.5*a + b);

			// 更新，加上违反公平原则扣分 v*sqrt(r-4)
			if (TIME_I > 3) {
				for (int j = 0; j < 3; j++) {
					if (status_history.get(key).get(TIME_I)[j] == 0) {
						int waitTime = 1;
						int waitStart = TIME_I;
						while (waitStart>0&&status_history.get(key).get(waitStart - 1)[j] == 0) {
							waitTime += 1;
							waitStart -= 1;
						}
						penalty += Math.ceil(all_road_current_flow.get(key)* Math.sqrt(Math.max(waitTime - 4, 0)));
					}
				}
			}

		}

		return penalty;
  		
  	}
  	//输出当前整个交通状态字符串
  	//格式为：TrafficLightID,FromID,LeftStatus,RightStatus,StraightStatus;TrafficLightID,FromID,LeftStatus,RightStatus,StraightStatus;...
	private static void printCurrentStatus() throws IOException{
  		Iterator<Map.Entry<String, Integer[]>> it = all_road_best_status.entrySet().iterator();
  		StringBuilder sb = new StringBuilder();
  		while(it.hasNext()){
  			Map.Entry<String, Integer[]> entry = (Map.Entry<String, Integer[]>)it.next();
  			String[] keyStrs = entry.getKey().split("-");
  			Integer[] status = entry.getValue();
  		    //TrafficLightID,FromID
  			sb.append(keyStrs[0]+","+keyStrs[1]);
  			for(int s:status){
  				sb.append(","+s);
  			}
  			sb.append(";");
  		}
  		String status_str = sb.toString();
  		System.out.println(status_str);	
  	}
  	
    
    //初始化当前时间段的状态
	private static void initCurrentStatus(){
		//下一个小时，则历史清空(单位时间为30秒)
		if(TIME_I%120==0){
			TIME_I = TIME_I%120;
			status_history.clear();
		}
    	for(String key:traffic_light_topo_table.keySet()){
    		Integer[] status = new Integer[]{-1,-1,-1};
    		if(status_history.containsKey(key)){
    			status_history.get(key).add(status);
    		}else{
    			ArrayList<Integer[]> status_array = new ArrayList<Integer[]>();
				status_array.add(status);
				status_history.put(key, status_array);
    		}
    	}
    }
    
    //设置当前时间段某个交通灯（交通十字路口或者T字路口）的状态
	private static void setStatus(Map<String, Integer[]> aStatus) {
		for(String key :aStatus.keySet()){
			//左转
			status_history.get(key).get(TIME_I)[0] = aStatus.get(key)[0];
			//右转
			status_history.get(key).get(TIME_I)[1] = aStatus.get(key)[1];
			//直行
			status_history.get(key).get(TIME_I)[2] = aStatus.get(key)[2];
		}
	}
    
}

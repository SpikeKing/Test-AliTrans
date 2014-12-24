
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
 * @description ���룺ϵͳ�����ѡ�ִ��ݵ�ǰʱ�ε�����,��args[0]�С�
 *              �����ĸ�ʽΪ:TrafficLightID,FromID,Flow;TrafficLightID,FromID,Flow;....
 *              ��ͨͼ�׽ṹ�ɲ���ѡ�ָ����ĵ��е�����ͼ�Լ����졣
 *              �繹��ɸ�ʽΪ:TrafficLightID,FromID1,ToID1,ToID2,ToID3;TrafficLightID,FromID1,ToID1,ToID2,ToID3;...���ַ���
 *              �����ʹ��System.out.println()�����ʽΪ:TrafficLightID,FromID1,leftStatus,rightStatus,straightStatus;TrafficLightID,FromID1,leftStatus,rightStatus,straightStatus;...���ַ���
 */
public class Demo_v2 {
	
	//��ͨ���˽ṹ�ַ�������ʽΪ��TrafficLightID,FromID1(��Դ),ToID1(��ת),ToID2(��ת),ToID3(ֱ��);TrafficLightID,FromID1,ToID1,ToID2,ToID3;...,����ͨ�е���#��ʾ
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
	//��ͨ�ڵ���Ϣ���ñ�ÿһ����һ����ͨʮ��·�ڣ�����T��·�ڣ���ʽΪ��TrafficLightID:[FromID1,FromID2,FromID3,FromID4]
	private static Map<String, String[]> traffic_light_nodes_table = new HashMap<String, String[]>();
    //��ͨ���˽ṹ��,��ʽΪ��TrafficLightID:[FromID1,ToID1,ToID2,ToID3]
	private static Map<String, String[]> traffic_light_topo_table = new HashMap<String, String[]>();
    
    //ĳ��ʱ��T(i)����·�ڳ����� ת�����:[��ת,��ת,ֱ��],����д��
	private static double[] turn_rate = new double[]{0.1,0.1,0.8};
  	
  	//ĳ��ʱ��T(i)����·�ڳ����� ͨ����:[��ת,��ת,ֱ��],����д��
	private static int[] through_rate = new int[]{2,2,16};
  	
  	//ĳ��ʱ��T(i)����·�ں� �̵Ƶ�״̬:[��ת,��ת,ֱ��] (���Ϊ0���̵�Ϊ1,ȱʧ-1)
	private static Map<String, Integer[]> all_road_best_status = new HashMap<String,Integer[]>();
	
	//ÿ�����̵Ƶĺ�ѡ״̬,��Ϊ�Ǿֲ��汾��ֻ���ǵ��ڵ㣬������ת���ǿ��е�
	private static int[][] candidate_status = {{0,1,0},{0,1,1},{1,1,0},{1,1,1}};
  	
    //ĳ��Сʱ�ں��̵Ƶ���ʷ��Ϣ
	private static Map<String, ArrayList<Integer[]>> status_history = new HashMap<String,ArrayList<Integer[]>>();
   
  	//ĳ��ʱ��T(i)����·�ڵ�����
	private static Map<String, Integer> all_road_current_flow = new HashMap<String,Integer>();
	
	//��ʱ�洢ʮ��·�ڵ����״̬
	private static Map<String, Integer[]> best_status = new HashMap<String, Integer[]>();
	
	// ��ǰʮ�ֻ���T��·�ڸ���·�ڵƵ�״̬
	private static Map<String, Integer[]> current_traffic_status = new HashMap<String, Integer[]>();
  	
  	//ĳ��Сʱ�ڼ���ʱ��
	private static int TIME_I = 0;
  	
  	//Υ��ͷ�ϵ��,����д��
	private static double ZETA = 0.5;
  	
    //ÿһ��ʱ��Σ�ϵͳ�������һ�β���ѡ�ֵ�main�������Բ�������ʽ��ѡ�ִ��ݵ�ǰʱ���ÿ��·�ڵ�����(args[0])
  	//����TrafficLightID,FromID,Flow;TrafficLightID,FromID,Flow;....��ʽ
	public static void main(String[] args) throws NumberFormatException, IOException{
		//���ؽ�ͨ���˽ṹ
		loadTrafficTable();
		//��ȡ��ǰʱ�������ַ���
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//��ȡĳ��ʱ��T(i)������ͨ��������
		String flows_str = br.readLine();
		while(!"end".equalsIgnoreCase(flows_str)){
			//�����һʱ�̵�״̬
			all_road_best_status.clear();
			//�����һʱ�̵�����
			all_road_current_flow.clear();
			//������ǰʱ�������ַ���
			String[] flows = flows_str.trim().split(";");
			for(String flow:flows){
				String [] strs = flow.trim().split(",");
				String key = strs[0]+"-"+strs[1];
				all_road_current_flow.put(key, Integer.valueOf(strs[2]));
			}
			
			//��ʼ����ǰ��ͨ״̬
			initCurrentStatus();
			
			//���������ȡ������õ�״̬
			generateStatus();
			
			//��ȡ��һ��ʱ��ε�����
			flows_str = br.readLine();
		
			++TIME_I;
		}
	}
	
	//�ӽ�ͨ���˽ṹ�ַ����н���
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
	
	//����״̬
	private static void generateStatus() throws IOException{
		// ����ÿ����ͨ·�ڣ�ʮ��·�ڻ���T��·�ڣ�
		Iterator<?> iter1 = traffic_light_nodes_table.entrySet().iterator();
		while (iter1.hasNext()) {
			// ��ʮ��·����õ�״̬��Ŀ�꺯��ֵ
			double best_penalty = Integer.MAX_VALUE;
			best_status.clear();
			// ��ȡTrafficLightID,FromIDs
			Map.Entry<String, String[]> entry1 = (Map.Entry<String, String[]>) iter1.next();
			String traffic_light_id = (String) entry1.getKey();
			String[] from_ids = (String[]) entry1.getValue();
			
			//���
			current_traffic_status.clear();
			
			//Ŀ��·��
			String[] destinations;
			
			//��Դ·�ڸ�����T��Ϊ3��ʮ����Ϊ4
			int from_id_num = from_ids.length;
			if (from_id_num == 3) {
				for (int i1 = 0; i1 < 4; ++i1) {
					String key1 = traffic_light_id + "-" + from_ids[0];
					// ��ת����ת��ֱ��3��ת̬,0��ʾ��ƣ�1��ʾ�̵ƣ�-1��ʾ��·����
					Integer[] status1 = new Integer[3];
					// ��·�ڿ���ͨ����������򣬷ֱ�Ϊ��ת����ת��ֱ��
					destinations = traffic_light_topo_table.get(key1);
					for (int idx = 0; idx < 3; ++idx) {
						if ("#".equals(destinations[idx])) {
							status1[idx] = -1;
						}else{
							status1[idx] = candidate_status[i1][idx];
						}
					}
					// �洢��ǰ�õ���ת̬
					current_traffic_status.put(key1, status1);
					for (int i2 = 0; i2 < 4; ++i2) {
						String key2 = traffic_light_id + "-" + from_ids[1];
						// ��ת����ת��ֱ��3��ת̬,0��ʾ��ƣ�1��ʾ�̵ƣ�-1��ʾ��·����
						Integer[] status2 = new Integer[3];
						// ��·�ڿ���ͨ����������򣬷ֱ�Ϊ��ת����ת��ֱ��
						destinations = traffic_light_topo_table.get(key2);
						for (int idx = 0; idx < 3; ++idx) {
							if ("#".equals(destinations[idx])) {
								status2[idx] = -1;
							}else{
								status2[idx] = candidate_status[i2][idx];
							}
						}
						// �洢��ǰ�õ���ת̬
						current_traffic_status.put(key2, status2);
						for (int i3 = 0; i3 < 4; ++i3) {
							String key3 = traffic_light_id + "-" + from_ids[2];
							// ��ת����ת��ֱ��3��ת̬,0��ʾ��ƣ�1��ʾ�̵ƣ�-1��ʾ��·����
							Integer[] status3 = new Integer[3];
							// ��·�ڿ���ͨ����������򣬷ֱ�Ϊ��ת����ת��ֱ��
							destinations = traffic_light_topo_table.get(key3);
							for (int idx = 0; idx < 3; ++idx) {
								if ("#".equals(destinations[idx])) {
									status3[idx] = -1;
								}else{
									status3[idx] = candidate_status[i3][idx];
								}
							}
							// �洢��ǰ�õ���ת̬
							current_traffic_status.put(key3, status3);
							// ����״̬��
							setStatus(current_traffic_status);
							// �����ת̬�µ�Ŀ��ֵ
							double penalty = computePenalty(current_traffic_status);
							// ѡ��Ŀ��ֵ��С��ת̬
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
					// ��ת����ת��ֱ��3��ת̬,0��ʾ��ƣ�1��ʾ�̵ƣ�-1��ʾ��·����
					Integer[] status1 = new Integer[3];
					// ��·�ڿ���ͨ����������򣬷ֱ�Ϊ��ת����ת��ֱ��
					destinations = traffic_light_topo_table.get(key1);
					for (int idx = 0; idx < 3; ++idx) {
						if ("#".equals(destinations[idx])) {
							status1[idx] = -1;
						}else{
							status1[idx] = candidate_status[i1][idx];
						}
					}
					// �洢��ǰ�õ���ת̬
					current_traffic_status.put(key1, status1);
					for (int i2 = 0; i2 < 4; ++i2) {
						String key2 = traffic_light_id + "-" + from_ids[1];
						// ��ת����ת��ֱ��3��ת̬,0��ʾ��ƣ�1��ʾ�̵ƣ�-1��ʾ��·����
						Integer[] status2 = new Integer[3];
						// ��·�ڿ���ͨ����������򣬷ֱ�Ϊ��ת����ת��ֱ��
						destinations = traffic_light_topo_table.get(key2);
						for (int idx = 0; idx < 3; ++idx) {
							if ("#".equals(destinations[idx])) {
								status2[idx] = -1;
							}else{
								status2[idx] = candidate_status[i2][idx];
							}
						}
						// �洢��ǰ�õ���ת̬
						current_traffic_status.put(key2, status2);
						for (int i3 = 0; i3 < 4; ++i3) {
							String key3 = traffic_light_id + "-" + from_ids[2];
							// ��ת����ת��ֱ��3��ת̬,0��ʾ��ƣ�1��ʾ�̵ƣ�-1��ʾ��·����
							Integer[] status3 = new Integer[3];
							// ��·�ڿ���ͨ����������򣬷ֱ�Ϊ��ת����ת��ֱ��
							destinations = traffic_light_topo_table.get(key3);
							for (int idx = 0; idx < 3; ++idx) {
								if ("#".equals(destinations[idx])) {
									status3[idx] = -1;
								}else{
									status3[idx] = candidate_status[i3][idx];
								}
							}
							// �洢��ǰ�õ���ת̬
							current_traffic_status.put(key3, status3);
							for (int i4 = 0; i4 < 4; ++i4) {
								String key4 = traffic_light_id + "-"
										+ from_ids[3];
								// ��ת����ת��ֱ��3��ת̬,0��ʾ��ƣ�1��ʾ�̵ƣ�-1��ʾ��·����
								Integer[] status4 = new Integer[3];
								// ��·�ڿ���ͨ����������򣬷ֱ�Ϊ��ת����ת��ֱ��
								destinations = traffic_light_topo_table.get(key4);
								for (int idx = 0; idx < 3; ++idx) {
									if ("#".equals(destinations[idx])) {
										status4[idx] = -1;
									}else{
										status4[idx] = candidate_status[i4][idx];
									}
								}
								// �洢��ǰ�õ���ת̬
								current_traffic_status.put(key4, status4);
								// ����״̬��
								setStatus(current_traffic_status);
								// �����ת̬�µ�Ŀ��ֵ
								double penalty = computePenalty(current_traffic_status);
								// ѡ��Ŀ��ֵ��С��ת̬
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
			
			//����״̬��
			setStatus(best_status);
			
			Iterator<?> iter2 = best_status.entrySet().iterator();
			while(iter2.hasNext()){
				Map.Entry<String, Integer[]> entry2 = (Map.Entry<String, Integer[]>) iter2.next();
				all_road_best_status.put(entry2.getKey(), entry2.getValue());
			}
		}
			
		//���״̬
		printCurrentStatus();
		
	}
	
    //����status_history��all_road_current_flow����T(i)ʱ��ĳ·������������
	private static int computeStay(String key) {
  		
  		//��ת��תֱ��ʵ���ܹ�ͨ���ĳ�����
  		int left_through,right_through,straight_through;
  		left_through = right_through =  straight_through = 0;
  		
  		//�ж���ʮ��·�ڻ���T��·��,-1:�������򶼿����ߵ�·��,0:������ת��·��,1:������ת��·��,2:����ֱ�е�·��
  		int turn_flag = -1;
  	    //�̵�ʱͨ���ʲ���Ч
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
  		
  		//��ȡ��ǰʱ�θ�·�ڵ�����
  		int i_flow = all_road_current_flow.get(key);
  		
  		
  		
  		//��ת��תֱ�е���������
  		int left_stay,right_stay,straight_stay;
  		left_stay = right_stay = straight_stay = 0;
  		
  		//�����T��·�ڣ�����������������Ҫƽ�ֲ���ͨ�еķ����ת����
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

  		//��ȡ�����ܻᵼ����������һ��
  		left_stay = Math.max(0, (int)Math.ceil(i_flow*left_rate) - left_through);
  		right_stay = Math.max(0, (int)Math.ceil(i_flow*right_rate) - right_through);
  		straight_stay = Math.max(0,(int)Math.ceil(i_flow*straight_rate)-straight_through);
  		
  		//����T(i)ʱ������������
  		return (left_stay + right_stay + straight_stay);
  		
  	}
  	
    //����currentStatus����penalty,�÷���ֻ����ĳһ��ʮ��·�ڣ�����ͨ·�ڣ�
	private static double computePenalty(Map<String, Integer[]> current_traffic_status){
  	    //����
  	    double penalty = 0;
		// ÿ��·��T(i)ʱ��penalty: ��ת����+��ת����+ֱ������;Υ����ͨ����۷�;Υ����ƽ��ԭ��۷�
		for (String key : current_traffic_status.keySet()) {
			// ���£�������������
			penalty += computeStay(key);
			
			// ���£����Ϻ��̵�Υ����ͨ����ĳͷ� a:ֱ�д�ֱֱ�гͷ� b:ֱ�д�ֱ��ת�ͷ�
			double a, b;
			a = b = 0;
			
			String[] lights = key.split("-");
			String left_key = lights[0] + "-" + traffic_light_topo_table.get(key)[0];
			String right_key = lights[0] + "-" + traffic_light_topo_table.get(key)[1];

			// ��ֱ������ͬʱֱ��
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
			// ֱ��ʱ��ֱ�����Ҳ಻����ת
			if (current_traffic_status.get(key)[2] == 1&& current_traffic_status.containsKey(right_key) && current_traffic_status.get(right_key)[0] == 1) {
					b += ZETA* (all_road_current_flow.get(right_key) +all_road_current_flow.get(key));
			} 

			// Υ��۷�
			penalty += (0.5*a + b);

			// ���£�����Υ����ƽԭ��۷� v*sqrt(r-4)
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
  	//�����ǰ������ͨ״̬�ַ���
  	//��ʽΪ��TrafficLightID,FromID,LeftStatus,RightStatus,StraightStatus;TrafficLightID,FromID,LeftStatus,RightStatus,StraightStatus;...
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
  	
    
    //��ʼ����ǰʱ��ε�״̬
	private static void initCurrentStatus(){
		//��һ��Сʱ������ʷ���(��λʱ��Ϊ30��)
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
    
    //���õ�ǰʱ���ĳ����ͨ�ƣ���ͨʮ��·�ڻ���T��·�ڣ���״̬
	private static void setStatus(Map<String, Integer[]> aStatus) {
		for(String key :aStatus.keySet()){
			//��ת
			status_history.get(key).get(TIME_I)[0] = aStatus.get(key)[0];
			//��ת
			status_history.get(key).get(TIME_I)[1] = aStatus.get(key)[1];
			//ֱ��
			status_history.get(key).get(TIME_I)[2] = aStatus.get(key)[2];
		}
	}
    
}

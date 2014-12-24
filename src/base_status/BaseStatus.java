/**
 * 思想：
 * 定时翻转红绿灯状态思想
 * 
 * 1) 遍历红绿灯位置关系表将非丁字路口的红绿灯全部置为红灯，丁字路口的缺失方向置为-1
 * 2) 遍历1)中生成的红绿灯状态表10次  每次遍历尝试将每个非丁字路口的缺失路口置为绿灯
 * 如果不违反交通规则，则置为绿灯，如果违反交通规则重新置为红灯，将10次遍历结束后的
 * 状态作为每个小时的初始状态
 * 3) 每个小时的T(i+1)时刻红绿灯状态由T(i)时刻状态决定，如果T(i)为红灯则T(i+1)翻转为绿灯
 * 如果T(i)为绿灯则T(i+1)翻转为红灯，如果为-1 则不变
 * 
 * created by C.L.Wang
 */

package base_status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseStatus {

	// 拓扑关系文件转化成的字符串
	private static String tupoString = "tl44,tl42,tl43,#,tl19;tl44,tl43,tl19,tl42,#;tl44,tl19,#,tl43,tl42;tl43,tl44,tl41,tl18,#;tl43,tl41,#,tl44,tl18;tl43,tl18,tl44,#,tl41;tl42,tl26,tl41,#,tl44;tl42,tl41,tl44,tl26,#;tl42,tl44,#,tl41,tl26;tl41,tl42,tl25,tl43,tl40;tl41,tl25,tl40,tl42,tl43;tl41,tl40,tl43,tl25,tl42;tl41,tl43,tl42,tl40,tl25;tl40,tl41,tl24,tl17,tl39;tl40,tl24,tl39,tl41,tl17;tl40,tl39,tl17,tl24,tl41;tl40,tl17,tl41,tl39,tl24;tl39,tl40,tl23,tl16,#;tl39,tl23,#,tl40,tl16;tl39,tl16,tl40,#,tl23;tl38,tl12,tl37,#,tl5;tl38,tl37,tl5,tl12,#;tl38,tl5,#,tl37,tl12;tl37,tl38,tl11,tl4,tl36;tl37,tl11,tl36,tl38,tl4;tl37,tl36,tl4,tl11,tl38;tl37,tl4,tl38,tl36,tl11;tl36,tl37,tl10,tl3,#;tl36,tl10,#,tl37,tl3;tl36,tl3,tl37,#,tl10;tl35,tl52,tl58,tl28,tl34;tl35,tl58,tl34,tl52,tl28;tl35,tl34,tl28,tl58,tl52;tl35,tl28,tl52,tl34,tl58;tl34,tl35,tl57,tl27,tl33;tl34,tl57,tl33,tl35,tl27;tl34,tl33,tl27,tl57,tl35;tl34,tl27,tl35,tl33,tl57;tl33,tl34,tl56,tl26,tl32;tl33,tl56,tl32,tl34,tl26;tl33,tl32,tl26,tl56,tl34;tl33,tl26,tl34,tl32,tl56;tl32,tl33,tl55,tl25,tl31;tl32,tl55,tl31,tl33,tl25;tl32,tl31,tl25,tl55,tl33;tl32,tl25,tl33,tl31,tl55;tl31,tl32,#,tl24,tl30;tl31,tl30,tl24,#,tl32;tl31,tl24,tl32,tl30,#;tl30,tl31,tl54,tl23,tl29;tl30,tl54,tl29,tl31,tl23;tl30,tl29,tl23,tl54,tl31;tl30,tl23,tl31,tl29,tl54;tl29,tl30,tl53,tl22,tl51;tl29,tl53,tl51,tl30,tl22;tl29,tl51,tl22,tl53,tl30;tl29,tl22,tl30,tl51,tl53;tl28,tl35,tl27,#,tl21;tl28,tl27,tl21,tl35,#;tl28,tl21,#,tl27,tl35;tl27,tl28,tl34,tl20,tl26;tl27,tl34,tl26,tl28,tl20;tl27,tl26,tl20,tl34,tl28;tl27,tl20,tl28,tl26,tl34;tl26,tl27,tl33,tl42,tl25;tl26,tl33,tl25,tl27,tl42;tl26,tl25,tl42,tl33,tl27;tl26,tl42,tl27,tl25,tl33;tl25,tl26,tl32,tl41,tl24;tl25,tl32,tl24,tl26,tl41;tl25,tl24,tl41,tl32,tl26;tl25,tl41,tl26,tl24,tl32;tl24,tl25,tl31,tl40,tl23;tl24,tl31,tl23,tl25,tl40;tl24,tl23,tl40,tl31,tl25;tl24,tl40,tl25,tl23,tl31;tl23,tl24,tl30,tl39,tl22;tl23,tl30,tl22,tl24,tl39;tl23,tl22,tl39,tl30,tl24;tl23,tl39,tl24,tl22,tl30;tl22,tl23,tl29,tl14,#;tl22,tl29,#,tl23,tl14;tl22,tl14,tl23,#,tl29;tl21,tl28,tl20,#,tl6;tl21,tl20,tl6,tl28,#;tl21,tl6,#,tl20,tl28;tl20,tl21,tl27,#,tl19;tl20,tl27,tl19,tl21,#;tl20,tl19,#,tl27,tl21;tl19,tl20,tl44,tl12,tl18;tl19,tl44,tl18,tl20,tl12;tl19,tl18,tl12,tl44,tl20;tl19,tl12,tl20,tl18,tl44;tl18,tl19,tl43,tl11,tl17;tl18,tl43,tl17,tl19,tl11;tl18,tl17,tl11,tl43,tl19;tl18,tl11,tl19,tl17,tl43;tl17,tl18,tl40,tl10,tl16;tl17,tl40,tl16,tl18,tl10;tl17,tl16,tl10,tl40,tl18;tl17,tl10,tl18,tl16,tl40;tl16,tl17,tl39,tl9,tl15;tl16,tl39,tl15,tl17,tl9;tl16,tl15,tl9,tl39,tl17;tl16,tl9,tl17,tl15,tl39;tl15,tl16,#,tl8,tl14;tl15,tl14,tl8,#,tl16;tl15,tl8,tl16,tl14,#;tl14,tl15,tl22,tl7,#;tl14,tl22,#,tl15,tl7;tl14,tl7,tl15,#,tl22;tl12,tl19,tl11,#,tl38;tl12,tl11,tl38,tl19,#;tl12,tl38,#,tl11,tl19;tl11,tl12,tl18,tl37,tl10;tl11,tl18,tl10,tl12,tl37;tl11,tl10,tl37,tl18,tl12;tl11,tl37,tl12,tl10,tl18;tl10,tl11,tl17,tl36,tl9;tl10,tl17,tl9,tl11,tl36;tl10,tl9,tl36,tl17,tl11;tl10,tl36,tl11,tl9,tl17;tl9,tl10,tl16,tl2,tl8;tl9,tl16,tl8,tl10,tl2;tl9,tl8,tl2,tl16,tl10;tl9,tl2,tl10,tl8,tl16;tl8,tl9,tl15,#,tl7;tl8,tl15,tl7,tl9,#;tl8,tl7,#,tl15,tl9;tl7,tl8,tl14,tl1,tl13;tl7,tl14,tl13,tl8,tl1;tl7,tl13,tl1,tl14,tl8;tl7,tl1,tl8,tl13,tl14;tl1,tl2,tl7,#,tl45;tl1,tl7,tl45,tl2,#;tl1,tl45,#,tl7,tl2;tl2,tl47,tl3,tl1,tl9;tl2,tl3,tl9,tl47,tl1;tl2,tl9,tl1,tl3,tl47;tl2,tl1,tl47,tl9,tl3;tl3,tl4,tl36,#,tl2;tl3,tl36,tl2,tl4,#;tl3,tl2,#,tl36,tl4;tl4,tl48,tl5,tl3,tl37;tl4,tl5,tl37,tl48,tl3;tl4,tl37,tl3,tl5,tl48;tl4,tl3,tl48,tl37,tl5;tl5,tl49,tl6,tl4,tl38;tl5,tl6,tl38,tl49,tl4;tl5,tl38,tl4,tl6,tl49;tl5,tl4,tl49,tl38,tl6;tl6,tl50,tl46,tl5,tl21;tl6,tl46,tl21,tl50,tl5;tl6,tl21,tl5,tl46,tl50;tl6,tl5,tl50,tl21,tl46";
	private static Map<String, String[]> trafficLightMap = new HashMap<String, String[]>();
	private static Map<String, Integer[]> trafficStatus = new HashMap<String, Integer[]>();
	private static Map<String, Integer[]> currentStatus = new HashMap<String, Integer[]>();

	public static void main(String[] args) throws NumberFormatException,
			IOException {

		int count = 0;
		initTrafficLightMap();
		initStatus();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String flows_str = br.readLine();

		while (!"end".equalsIgnoreCase(flows_str)) {

			if (count % 120 == 0) {
				// 每个小时红绿灯状态复位
				currentStatus = trafficStatus;
			} else {
				// 每个新的时刻，翻转红绿灯状态
				for (String aKey : currentStatus.keySet()) {
					for (int i = 0; i < 3; i++) {
						if (currentStatus.get(aKey)[i] == 0) {
							currentStatus.get(aKey)[i] = 1;
						} else if (currentStatus.get(aKey)[i] == 1) {
							currentStatus.get(aKey)[i] = 0;
						}
					}
				}
			}

			String statusString = geneStatusString(currentStatus);
			System.out.println(statusString);
			flows_str = br.readLine();
			count += 1;
		}

	}

	// 初始化拓扑关系表
	private static void initTrafficLightMap() {
		String[] pathStrings = tupoString.split(";");
		for (String aPathString : pathStrings) {
			String[] aStrings = aPathString.split(",");
			String aKey = aStrings[0] + "-" + aStrings[1];
			String[] aValue = { aStrings[2], aStrings[3], aStrings[4] };
			trafficLightMap.put(aKey, aValue);
		}
	}

	// 通过多次扫描，在不违反交通规则的前提下，最大化绿灯个数，作为红绿灯初始状态表
	private static void initStatus() {
		for (String aKey : trafficLightMap.keySet()) {

			Integer[] aStatus = { 0, 0, 0 };
			if ("#".equals(trafficLightMap.get(aKey)[0])) {
				aStatus[0] = -1;
			} else if ("#".equals(trafficLightMap.get(aKey)[1])) {
				aStatus[1] = -1;
			} else if ("#".equals(trafficLightMap.get(aKey)[2])) {
				aStatus[2] = -1;
			}

			trafficStatus.put(aKey, aStatus);
		}

		// 遍历 trafficStatus 10次，最大化红灯个数设置红绿灯初始状态表
		int refreshCount = 10;
		for (int i = 0; i < refreshCount; i++) {
			for (String aKey : trafficStatus.keySet()) {
				for (int j = 0; j < 3; j++) {
					if (trafficStatus.get(aKey)[j] == 0) {
						trafficStatus.get(aKey)[j] = 1;
						if (!isLegal(trafficStatus)) {
							trafficStatus.get(aKey)[j] = 0;
						}
					}
				}
			}
		}

	}

	// 检查红绿灯设置状态是否合法
	private static boolean isLegal(Map<String, Integer[]> aStatus) {
		boolean flag = true;
		for (String aKey : aStatus.keySet()) {
			for (int i = 0; i < 3; i++) {
				if (aStatus.get(aKey)[i] == 1) {
					String targetLight = aKey.split("-")[0];
					String leftLight = trafficLightMap.get(aKey)[0];
					String rightLight = trafficLightMap.get(aKey)[1];
					String leftKey = targetLight + "-" + leftLight;
					String rightKey = targetLight + "-" + rightLight;

					// 违规情况：1）垂直方向不能同时直行2）垂直方向不可左转
					if ((aStatus.containsKey(leftKey) && aStatus.get(leftKey)[2] == 1)
							|| (aStatus.containsKey(rightKey) && (aStatus
									.get(rightKey)[2] == 1 || aStatus
									.get(rightKey)[0] == 1))) {
						flag = false;
						return flag;
					}
				}
			}
		}

		return flag;
	}

	// 由aStatus 生成 statusString
	private static String geneStatusString(Map<String, Integer[]> aStatus) {

		ArrayList<String> records = new ArrayList<String>();
		for (String aKey : aStatus.keySet()) {
			String[] keyString = aKey.split("-");
			String aRecordString = keyString[0] + "," + keyString[1] + ","
					+ aStatus.get(aKey)[0] + "," + aStatus.get(aKey)[1] + ","
					+ aStatus.get(aKey)[2];
			records.add(aRecordString);
		}
		String statusString = join(";", records);
		return statusString;
	}

	// 字符串数组拼接
	public static String join(String join, ArrayList<String> strAry) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strAry.size(); i++) {
			if (i == (strAry.size() - 1)) {
				sb.append(strAry.get(i));
			} else {
				sb.append(strAry.get(i)).append(join);
			}
		}

		return new String(sb);
	}

}

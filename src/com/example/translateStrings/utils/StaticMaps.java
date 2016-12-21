package com.example.translateStrings.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class StaticMaps {

	public static Map<String, Integer> map1 = new HashMap<>();

	public static Map<String, Integer> map2 = new HashMap<>();

	public static void printMap(int mapNum) {
		Map<String, Integer> map = new HashMap<>();
		if (mapNum != 1 && mapNum != 2) {
			return;
		}

		if (mapNum == 1) {
			map = map1;
		} else {
			map = map2;
		}

		System.out.println("map" + mapNum + " size: " + map.entrySet().size());

		// for (Map.Entry<String, Integer> e : map.entrySet()) {
		// System.out.println("name: " + e.getKey() + ", lineNumber: " +
		// e.getValue());
		// }
	}

	public void readFileToMap(String filePath, int mapNum) {
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader reader = new InputStreamReader(fis, encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(reader);

				long current = System.currentTimeMillis();
				FileOutputStream fos = new FileOutputStream("D:\\test\\test_" + String.valueOf(current) + ".xml");
				OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
				BufferedWriter bufferedWriter = new BufferedWriter(writer);

				int lineNumber = 0;
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					lineNumber++;
					// System.out.println(lineTxt);

					while (lineTxt.contains("string") && !lineTxt.endsWith("</string>")) {
						lineTxt += bufferedReader.readLine();
						lineNumber++;
					}

					writeToMap(mapNum, lineTxt, lineNumber);

					int index1 = lineTxt.indexOf("\">");
					int index2 = lineTxt.indexOf("</");
					String value = null;
					if (index1 != -1 && index2 != -1) {
						value = lineTxt.substring(index1 + 2, index2);
					} else {
						value = lineTxt;
					}

					bufferedWriter.write(value);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}

				fos.close();
				// bufferedReader.close();
				// reader.close();
				fis.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读写文件内容出错");
			e.printStackTrace();
		}
	}

	public void readFileToMap(String filePath) {
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader reader = new InputStreamReader(fis, encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(reader);

				int lineNumber = 0;
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					lineNumber++;
					// System.out.println(lineTxt);

					while (lineTxt.contains("string") && !lineTxt.endsWith("</string>")) {
						lineTxt += bufferedReader.readLine();
						lineNumber++;
					}

					writeToMap(1, lineTxt, lineNumber);

					int index1 = lineTxt.indexOf("\">");
					int index2 = lineTxt.indexOf("</");
					String value = null;
					if (index1 != -1 && index2 != -1) {
						value = lineTxt.substring(index1 + 2, index2);
					} else {
						value = lineTxt;
					}

				}

				// bufferedReader.close();
				// reader.close();
				fis.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读写文件内容出错");
			e.printStackTrace();
		}
	}

	public void writeToMap(int mapNum, String line, int lineNumber) {
		if (mapNum != 1 && mapNum != 2) {
			return;
		}
		// Map<String, Integer> map = new HashMap<>();
		int indexFirst = 0;
		String newLine = null;
		if ((indexFirst = line.indexOf("name")) == -1) {
			return;
		} else {
			newLine = line.substring(indexFirst + 6);
			newLine = newLine.split("\"")[0];
		}
		if (mapNum == 1) {
			map1.put(newLine, new Integer(lineNumber));
		} else {
			map2.put(newLine, new Integer(lineNumber));
		}
	}

	public void compareTwoMaps() {
		// Map<String, Integer> map3 = map1;
		for (Map.Entry<String, Integer> e : map1.entrySet()) {
			String mapString1 = e.getKey();

			if (map2.containsKey(mapString1)) {
				map1.put(mapString1, 0);
			}
			// System.out.println(map2.get(mapString1));
		}

		int count = 0;
		for (Map.Entry<String, Integer> e : map1.entrySet()) {
			if (e.getValue() != 0) {
				count++;
				System.out.println("name: " + e.getKey() + ", lineNumber: " + e.getValue());
			}
		}

		System.out.println("相差：" + count + "行");
	}

}

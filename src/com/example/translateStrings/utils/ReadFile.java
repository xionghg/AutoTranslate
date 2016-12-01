package com.example.translateStrings.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.translateStrings.bean.StringLine;

public class ReadFile {

	public static ArrayList<StringLine> readFileToArray(String filePath) {
		String encoding = "utf-8";
		File file = new File(filePath);
		if (!(file.isFile() && file.exists())) {
			System.out.println("找不到指定的文件");
			return null;
		}

		List<StringLine> strLines = new ArrayList<>();

		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis, encoding);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(reader);

			int currentLine = 0;
			String lineTxt = null;
			String key = null;
			String value = null;
			int lineNumber = 0;
			boolean isTranslated = false;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				currentLine++;
				if (lineTxt.equals("")) {
					continue;
				}
				int temp = 0;
				while (lineTxt.contains("string") && !lineTxt.contains("</string>")) {
					// System.out.println(lineTxt);
					lineTxt += bufferedReader.readLine();
					currentLine++;
					temp++;
				}

				if (!(lineTxt.contains("string") && lineTxt.contains("name"))) {
					key = StringLine.DEFAULT_KEY;
					value = lineTxt;
					lineNumber = currentLine;
					isTranslated = true;
					StringLine strLine = new StringLine(key, value, lineNumber, isTranslated);
					strLines.add(strLine);
				} else {
					int firstIndex = 0;
					firstIndex = lineTxt.indexOf("name");
					key = lineTxt.substring(firstIndex + 6).split("\"")[0];

					int index1 = lineTxt.indexOf("\">");
					int index2 = lineTxt.indexOf("</string>");
					if (index1 != -1 && index2 != -1) {
						value = lineTxt.substring(index1 + 2, index2);
					} else {
						value = lineTxt;
					}
					lineNumber = currentLine - temp;
					isTranslated = false;
					StringLine strLine = new StringLine(key, value, lineNumber, isTranslated);
					strLines.add(strLine);
				}
			}

			fis.close();
		} catch (Exception e) {
			System.out.println("读写文件内容出错");
			e.printStackTrace();
		}
		return (ArrayList<StringLine>) strLines;
	}

	public static HashMap<String, String> readFileToMap(String filePath) {
		String encoding = "utf-8";
		File file = new File(filePath);
		if (!(file.isFile() && file.exists())) {
			System.out.println("找不到指定的文件");
			return null;
		}

		Map<String, String> map = new HashMap<>();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis, encoding);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(reader);

			int lineNumber = 0;
			String key = null;
			String value = null;
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				lineNumber++;
				// System.out.println(lineTxt);

				while (lineTxt.contains("string") && !lineTxt.contains("</string>")) {
					lineTxt += bufferedReader.readLine();
					lineNumber++;
				}

				if (lineTxt.contains("string") && lineTxt.contains("name")) {
					int firstIndex = 0;
					firstIndex = lineTxt.indexOf("name");
					key = lineTxt.substring(firstIndex + 6).split("\"")[0];

					int index1 = lineTxt.indexOf("\">");
					int index2 = lineTxt.indexOf("</string>");
					if (index1 != -1 && index2 != -1) {
						value = lineTxt.substring(index1 + 2, index2);
					} else {
						value = lineTxt;
					}
					map.put(key, value);
				}
			}
			fis.close();
		} catch (Exception e) {
			System.out.println("读写文件内容出错");
			e.printStackTrace();
		}

		return (HashMap<String, String>) map;
	}

}

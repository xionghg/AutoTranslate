package com.example.translateStrings.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.translate.demo.TransApi;
import com.example.translateStrings.bean.StringLine;

public class Translater {

	private static final String APP_ID = "20151113000005349";
	private static final String SECURITY_KEY = "osubCEzlGjzvw8qdQc41";

	// private static final String APP_ID = "20161126000032760";
	// private static final String SECURITY_KEY = "XbKOyIgCIAEvLbADm3oE";

	public Translater() {

	}

	public static ArrayList<StringLine> transArrayUseMap(ArrayList<StringLine> array, HashMap<String, String> map) {
		ArrayList<StringLine> strLines = array;
		int index = 0;
		for (StringLine line : strLines) {
			String key = line.getKey();
			String mapValue = null;
			if (key == StringLine.DEFAULT_KEY) {
				index++;
				continue;
			} else {
				if ((mapValue = map.get(key)) != null) {
					int lineNumber = line.getLineNumber();
					boolean isTranslated = true;

					StringLine temp = new StringLine(key, mapValue, lineNumber, isTranslated);
					strLines.set(index, temp);
				}

			}
			index++;
		}

		return strLines;
	}

	public static ArrayList<StringLine> translateByBaidu(ArrayList<StringLine> array) {
		ArrayList<StringLine> strLines = array;
		int index = 0;
		int count = 0;
		ArrayList<String> transStrings = new ArrayList<>();
		for (StringLine line : strLines) {
			// boolean isTranslated = line.isTranslated();
			if (line.isTranslated()) {
				index++;
				continue;
			}

			// String key = line.getKey();
			String value = line.getValue();
			// int lineNumber = line.getLineNumber();

			String temp = String.valueOf(index) + "@" + value;
			transStrings.add(temp);
			count++;
			index++;
		}
		System.out.println("待翻译: " + transStrings.size());

		TransApi api = new TransApi(APP_ID, SECURITY_KEY);
		int transTimes = count / 33 + (count % 33 == 0 ? 0 : 1);

		int newIndex = 0;
		while (transTimes > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 32 && newIndex < transStrings.size() - 1; i++) {
				sb.append(transStrings.get(newIndex++) + "\n");
			}
			sb.append(transStrings.get(newIndex++));
			String query = sb.toString();
			String result = api.getTransResult(query, "auto", "cht");
			ArrayList<String> results = dealResult(result);
			// for(String s:results){
			// System.out.println(s);
			// }
			for (String str : results) {
				if (!str.contains("@")) {
					continue;
				}
				int returnIndex = 0;
				// System.out.println("0: "+str);

				if (str.endsWith("@")) {
					System.out.println("1: " + str);
					String oneStr = str.substring(0, str.length() - 1);
					try {
						returnIndex = Integer.parseInt(oneStr.trim());
					} catch (Exception e) {
						System.out.println("读取下标失败");
					}
					strLines.get(returnIndex).setValue("");
					strLines.get(returnIndex).setTranslated(true);
					continue;
				}

				// System.out.println("2: "+str);
				String[] strs = str.split("@");
				try {
					returnIndex = Integer.parseInt(strs[0].trim());
				} catch (Exception e) {
					System.out.println("读取下标失败");
				}
				// System.out.println(returnIndex);
				String returnString = strs[1].replaceAll("\\\\n", "\\n").replaceAll("<\\/", "</").replaceAll("“", "\"")
						.replaceAll("”", "\"");

				strLines.get(returnIndex).setValue(returnString);
				strLines.get(returnIndex).setTranslated(true);
			}
			transTimes--;
		}

		// String query = "74@卸载SD卡\n76@格式化SD卡\n82@SD卡\n127@某个应用想让其他蓝牙设备在"
		// + " <xliff:g id=\"TIMEOUT\">%1$d</xliff:g> 秒内可检测到您的手机。"
		// + "\n129@某个应用想让其他蓝牙设备检测到您的手机。之后，您可以在“蓝牙”设置中更改此设置。";
		// String result = api.getTransResult(query, "anto", "cht");
		// System.out.println(result);
		//
		// ArrayList<String> results = dealResult(result);
		// for(String s:results){
		// System.out.println(s);
		// }

		return strLines;
	}

	public static ArrayList<String> dealResult(String result) {
		ArrayList<String> translated = new ArrayList<>();
		String regexName2 = "\"dst\"\\s*:\\s*\"(?<value>[^\\}]*)\"";
		Matcher mt = Pattern.compile(regexName2).matcher(result);
		while (mt.find()) {
			// System.out.println(mt.group("value"));
			translated.add(decodePartUnicode(mt.group("value")));
		}
		return translated;
	}

	// 半Unicode转中文
	public static String decodePartUnicode(String dataStr) {
		int start = 0;
		boolean hasUnicode = false;
		StringBuilder sb = new StringBuilder();
		while (start < dataStr.length() - 5) {
			hasUnicode = dataStr.substring(start, start + 2).equals("\\u");
			String charStr = "";
			char letter = 0;
			if (hasUnicode) {
				start += 6;
				charStr = dataStr.substring(start - 4, start);
				letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			} else {
				letter = dataStr.charAt(start++);
			}

			sb.append(letter);
		}

		while (start < dataStr.length()) {
			sb.append(dataStr.charAt(start++));
		}

		return sb.toString();
	}

}

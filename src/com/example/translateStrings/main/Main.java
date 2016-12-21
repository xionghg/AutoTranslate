package com.example.translateStrings.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.translateStrings.bean.StringLine;
import com.example.translateStrings.utils.ReadFile;
import com.example.translateStrings.utils.Translater;
import com.example.translateStrings.utils.WriteFile;

public class Main {

	public static final String BASEPATH2 = "/home/xionghg/test/translate/";

	public static final String BASEPATH = System.getProperty("user.home") + "/test/translate/";

	public static void transArrayUseMap(String filePath1, String filePath2) {
		transArrayUseMap(filePath1, filePath2, BASEPATH);
	}

	public static void main(String[] args) {
		// long current = System.currentTimeMillis();
		// System.out.println(current);
		// startProgram(args);

		// startProgram(new String[] {
		// "D:\\gn_strings.xml","D:\\gn_strings2.xml", "1" });
		// List<StringLine> str = new ArrayList<>();

		int choice = 1;
		if (choice == 1) {
			startProgram(new String[] { BASEPATH + "strings.xml", BASEPATH + "strings2.xml", BASEPATH });
		} else if (choice == 2) {
			transArray(BASEPATH + "strings2.xml", BASEPATH);
		}
	}

	public static void transArray(String filePath1, String outFolder) {
		System.out.println("as follows: ");
		ArrayList<StringLine> strLines = new ArrayList<>();
		strLines = ReadFile.readFileToArray(filePath1);
		strLines = Translater.translateByBaidu(strLines);
		WriteFile.writeFileToPath(outFolder, strLines);
		for (StringLine line : strLines) {
			System.out.println(
					"line: " + line.getLineNumber() + "    key: " + line.getKey() + "    value: " + line.getValue());
		}
	}

	public static void transArrayUseMap(String filePath1, String filePath2, String outFolder) {
		System.out.println("as follows: ");

		ArrayList<StringLine> strLines = new ArrayList<>();
		strLines = ReadFile.readFileToArray(filePath1);
		HashMap<String, String> map = new HashMap<>();
		map = ReadFile.readFileToMap(filePath2);

		int index = 0;
		for (StringLine line : strLines) {
			String key = line.getKey();
			String mapValue = null;
			if (key == StringLine.DEFAULT_KEY) {
				index++;
				continue;
			} else {
				if ((mapValue = map.get(key)) != null) {
					strLines.get(index).setKey(StringLine.DEFAULT_KEY);
					strLines.get(index).setValue(mapValue);
					strLines.get(index).setTranslated(true);
					map.remove(key);
				}
			}
			index++;
		}

		StringLine temp = strLines.get(strLines.size() - 1);
		int addLineNumber = temp.getLineNumber();
		for (Map.Entry<String, String> e : map.entrySet()) {
			StringLine sl = new StringLine(e.getKey(), e.getValue(), addLineNumber++, true);
			strLines.add(strLines.size() - 1, sl);
		}
		strLines.get(strLines.size() - 1).setLineNumber(addLineNumber);

		strLines = Translater.translateByBaidu(strLines);
		int show = 0;
		if (show == 1) {
			for (StringLine line : strLines) {
				System.out.println("line: " + line.getLineNumber());
				// System.out.println("key: " + line.getKey());
				System.out.println("value: " + line.getValue());

			}
		} else if (show == 2) {
			for (Map.Entry<String, String> e : map.entrySet()) {
				System.out.println("key: " + e.getKey());
				System.out.println("value: " + e.getValue());
			}
		}

		WriteFile.writeFileToPath(outFolder, strLines);

		for (StringLine line : strLines) {
			System.out.println(
					"line: " + line.getLineNumber() + "    key: " + line.getKey() + "    value: " + line.getValue());
		}
	}

	public static void startProgram(String[] args) {
		int argsLength = args.length;
		// int b = new String[] { "", "" }.length; //2
		switch (argsLength) {
		case 2:
			transArrayUseMap(args[0], args[1]);
			break;
		case 3:
			transArrayUseMap(args[0], args[1], args[2]);
			break;
		default:
			System.out.println("输入三个参数，依次为原始文件、待翻译文件、输出路径：");
			boolean isParametersRight = false;
			Scanner sc = new Scanner(System.in);
			while (!isParametersRight) {
				isParametersRight = getParameters(sc);
			}
			sc.close();
			break;
		}
	}

	public static boolean getParameters(Scanner sc) {
		String inputLine = sc.nextLine();
		String[] parameters = inputLine.split(" ");
		int parametersAmount = parameters.length;
		if (parametersAmount == 2) {
			transArrayUseMap(parameters[0], parameters[1]);
			return true;
		} else if (parametersAmount == 3) {
			transArrayUseMap(parameters[0], parameters[1], parameters[2]);
			return true;
		}

		System.out.println("输入不合法，请重新输入：");
		return false;
	}

}

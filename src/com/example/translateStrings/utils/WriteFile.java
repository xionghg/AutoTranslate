package com.example.translateStrings.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.example.translateStrings.bean.StringLine;

public class WriteFile {

	public static void writeFileToPath(String basePath, ArrayList<StringLine> strLines) {
		String encoding = "utf-8";
		try {
			long current = System.currentTimeMillis();
			FileOutputStream fos = new FileOutputStream(basePath + "strings_" + String.valueOf(current) + ".xml");
			OutputStreamWriter writer = new OutputStreamWriter(fos, encoding);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			int lineNumber = 0;
			int cha = 0;
			String lineTxt = null;
			for (StringLine line : strLines) {
				if (line.getKey() == StringLine.DEFAULT_KEY) {
					cha = line.getLineNumber() - lineNumber;
					while (cha > 1) {
						bufferedWriter.newLine();
						cha--;
					}

					bufferedWriter.write(line.getValue());
					bufferedWriter.newLine();
					bufferedWriter.flush();
					lineNumber = line.getLineNumber();
				} else {
					cha = line.getLineNumber() - lineNumber;
					while (cha > 1) {
						bufferedWriter.newLine();
						cha--;
					}

					lineTxt = "    <string name=\"" + line.getKey() + "\">" + line.getValue() + "</string>";
					bufferedWriter.write(lineTxt);
					bufferedWriter.newLine();
					bufferedWriter.flush();

					lineNumber = line.getLineNumber();
				}
			}

			fos.close();
		} catch (Exception e) {
			System.out.println("读写文件内容出错");
			e.printStackTrace();
		}
	}

}

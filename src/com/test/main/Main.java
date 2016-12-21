package com.test.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		testRegex();
	}

	public static void testRegex() {
		String dst = "String name=\"gn_fingerprint_note_tip#￥\" product=\"erty\" name=\"erty\"";
		// \\s*等号两边任意多的空白符，[^\"]+匹配一次以上的字母、数字、下划线，除'"'以外的所有字符
		String regexName = "name\\s*=\\s*\"(?<name>[^\"]+)\"\\s*product\\s*=\\s*\"(?<product>[^\"]+)\""
				+ "|name\\s*=\\s*\"(?<name2>[^\"]+)\"";
		String regex2 = "product=\"(\\w*)\"";

		StringBuffer sb = new StringBuffer();
		char c;
		String regexName2 = "name\\s*=\\s*\"(?<name>[^\"]+(\"\\s*product\\s*=\\s*\"[^\"]+)?)\"";
		Matcher mt = Pattern.compile(regexName2).matcher(dst);
		while (mt.find()) {
			System.out.println(mt.group(0));
			System.out.println(mt.group(1));
			System.out.println(mt.group(2));
			System.out.println(mt.group("name"));

		}
	}

	public static String decodeUnicodes(String dst) {
		// \是转移，查找\本身，需要两个\，而java要输出两个\时要输入\\\\,如下
		String regex = "\\\\u([0-9a-f]{4})";

		Matcher mt = Pattern.compile(regex).matcher(dst);
		StringBuffer sb = new StringBuffer();
		char c;
		while (mt.find()) {
			c = (char) Integer.parseInt((mt.group(1)), 16);
			sb.append(c);
		}
		return sb.toString();
	}

	public static void printSystemInfo() {
		// String os = System.getProperty("os.name");
		// System.out.println(os);
		// if (os.toLowerCase().startsWith("win")) {
		// System.out.println(os + " can't gunzip");
		// }
		String[] props = new String[] { "java.version", "java.vendor", "java.vendor.url", "java.home",
				"java.vm.version", "java.vm.vendor", "java.vm.name", "java.class.path", "os.name", "os.arch",
				"os.version", "user.name", "user.home", "user.dir" };
		int index = 0;
		for (String str : props) {
			System.out.println(String.valueOf(++index) + "、 " + str + ": " + System.getProperty(str));
		}
	}
}

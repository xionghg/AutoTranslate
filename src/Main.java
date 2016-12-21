import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.translate.demo.TransApi;

public class Main {

	// 在平台申请的APP_ID 详见
	// http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
	private static final String APP_ID = "20161126000032760";
	private static final String SECURITY_KEY = "XbKOyIgCIAEvLbADm3oE";

	public static void main(String[] args) {
		TransApi api = new TransApi(APP_ID, SECURITY_KEY);

		String query = "74@\"卸载SD卡\"\n76@格式化“SD卡”<xliff:g id=\"PERCENTAGE\">%s%%</xliff:g>\n127@某个应用想让其他蓝牙设备\n129@某个应用想让其他蓝牙设备检测到您的手机。";
		String result = api.getTransResult(query, "anto", "cht");
		System.out.println(result);

		ArrayList<String> results = dealResult(result);
		for (String s : results) {
			System.out.println(s);
		}
		// String dst = result.split("\"dst\":\"")[1];
		// dst = dst.substring(0, dst.length() - 4);
		// System.out.println(dst);

		// 只能转纯unicode
		// String regex="\\\\u([0-9a-f]{4})";
		// Matcher mt= Pattern.compile(regex).matcher(dst);
		// StringBuffer sb=new StringBuffer();
		// char c;
		// while(mt.find()){
		// c = (char)Integer.parseInt((mt.group(1)), 16);
		// sb.append(c);
		// }
		// System.out.println("sb: "+ sb.toString());

		// System.out.println();
		// String ss = decodePartUnicode(dst);
		// System.out.println(ss);

		String b = "\u9ad8\u5ea6600\u7c73\uff0c\u5982\u679c\u60a8\u7121\u6cd5\u78ba\u8a8d\u81ea\u5df1\u751f\u6210\u7c3d\u540d\u7684\u7d50\u679c\u662f\u5426\u6b63\u78ba\uff0c\u53ef\u4ee5\u5c07\u60a8\u751f\u6210\u7684\u7c3d\u540d\u7d50\u679c\u548c\u5728\u4e2d\u751f\u6210\u7684\u5e38\u898fmd5\u52a0\u5bc6-32\u6bd4\u7279\u5c0f\u5beb\u7c3d\u540d\u7d50\u679c\u5c0d\u6bd4\u3002";
		System.out.println(b);

	}

	public static ArrayList<String> dealResult(String result) {
		ArrayList<String> translated = new ArrayList<>();
		String regexName2 = "\"dst\"\\s*:\\s*\"(?<value>[^\\}]*)\"";
		Matcher mt = Pattern.compile(regexName2).matcher(result);
		while (mt.find()) {
			System.out.println(mt.group("value"));
			translated.add(decodePartUnicode(mt.group("value")));
		}

		// ArrayList<String> translated = new ArrayList<>();
		// String[] results = result.split("\"dst\":\"");
		// String temp = null;
		// for (int i = 1; i < results.length; i++) {
		// System.out.println(i + ": " + results[i]);
		// temp = results[i].substring(0, results[i].indexOf("\"}"));
		// System.out.println(i + ": " + temp);
		// temp = decodePartUnicode(temp);
		// translated.add(temp);
		// }

		return translated;

	}

	// Unicode转中文,只能转纯unicode
	public static String decodeUnicode(String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	// 半Unicode转中文
	public static String decodePartUnicode(String dataStr) {
		int start = 0;
		// int end = 0;
		boolean hasUnicode = false;
		StringBuilder sb = new StringBuilder();
		while (start < dataStr.length() - 5) {
			hasUnicode = dataStr.substring(start, start + 2).equals("\\u");
			// start = dataStr.indexOf("\\u", end);
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
			// start = end;
		}

		while (start < dataStr.length()) {
			sb.append(dataStr.charAt(start++));
		}

		return sb.toString();
	}

}

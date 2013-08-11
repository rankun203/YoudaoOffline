package com.mindfine.youdaooffline;

import java.io.BufferedReader;
import java.io.FileReader;

public class DicReader {
	public static void main(String[] args) throws Exception {
		int start = 286161;
		int length = 22;
		getLinesFrom(start, length);
	}

	private static void getLinesFrom(int start, int length) throws Exception {
		long startt = System.currentTimeMillis();
		FileReader fr = new FileReader(System.getProperty("user.dir") + "/youdaocollins.exp");
		BufferedReader br = new BufferedReader(fr);
		for(int i = 0; i < start - 1; i++) {
			br.readLine();
		}
		StringBuilder explain = new StringBuilder();
		for(int i = 0; i < length; i++) {
			String tpr = br.readLine();
			if (tpr != null && !tpr.equals("")) {
				explain.append(tpr + "\r\n");
			}
		}
		
		long endt = System.currentTimeMillis();
		
		System.out.println(explain.toString());
		System.out.println("expend mills = " + (endt - startt));
	}
	
}

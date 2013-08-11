package com.mindfine.youdaooffline;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DicReader {
	public static void main(String[] args) throws Exception {
		int start = 286113;
		int length = 6;
//		getLinesFrom(start, length);
		getExplainFrom(14700135, 335);
	}

	/**
	 * 按行读取解释
	 * @param start 读取起始行数
	 * @param length 读取行数
	 * @throws Exception 抛出Exception
	 */
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
		br.close();
		fr.close();
	}
	
	public static void getExplainFrom(long skip, int length) throws Exception{
		long startt = System.currentTimeMillis();
		FileReader fr = new FileReader(System.getProperty("user.dir") + "/youdaocollins.exp");
		fr.skip(skip);
		char[] temp = new char[length];
		fr.read(temp, 0, length);

		long endt = System.currentTimeMillis();
		System.out.println(new String(temp));
		System.out.println("expend mills = " + (endt - startt));
		
		fr.close();
	}
	
}

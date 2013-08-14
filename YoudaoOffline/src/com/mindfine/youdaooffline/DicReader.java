package com.mindfine.youdaooffline;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class DicReader {
	HashMap<String, Indexer> idxMap = new HashMap<String, Indexer>();
	
	protected class Indexer {
		String wordName;
		String type;
		long bytePos;
		int byteCount;
		public Indexer(String wordName, String type, String bytePos, String byteCount){
			this.wordName = wordName;
			this.type = type;
			this.bytePos = Long.parseLong(bytePos);
			this.byteCount = Integer.parseInt(byteCount);
		}
	}

	public static void main(String[] args) throws Exception {
		int start = 286113;
		int length = 6;
//		getLinesFrom(start, length);
//		getExplainFrom(14700135, 335);
//		getExplainFrom2(32320382, 5000);
		new DicReader().getExplainBytesFromWord("keep");
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
		fr.read(temp);

		long endt = System.currentTimeMillis();
		System.out.println(new String(temp));
		System.out.println("expend mills = " + (endt - startt));
		
		fr.close();
	}
	public static void getExplainFrom2(long skip, int length){
		RandomAccessFile raf = null;
		try {
			long startt = System.currentTimeMillis();
			raf = new RandomAccessFile(System.getProperty("user.dir") + "/youdaocollins.exp", "r");
			raf.skipBytes((int) skip);
			byte[] temp = new byte[length];
			raf.read(temp);

			long endt = System.currentTimeMillis();
			System.out.println(new String(temp));
			System.out.println("expend mills = " + (endt - startt));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public String getExplainBytesFromWord(String word) {
		FileReader fr = null;
		try {
			fr = new FileReader(System.getProperty("user.dir") + "/youdaocollins.idx");
			BufferedReader br = new BufferedReader(fr);
			String bufTpr = null;//缓存着每一条索引
			while((bufTpr = br.readLine()) != null) {
				String [] units = bufTpr.split(":");
				if(units.length >= 4) {
					String wordName = units[0];
					String type = units[1];
					String bytePos = units[2];
					String byteCount = units[3];
					idxMap.put(wordName, new Indexer(wordName, type, bytePos, byteCount));
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("词典索引未找到");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("索引读取错误");
			e.printStackTrace();
		} finally {
			if(fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//从map中获取单词索引，调用getLinesFrom方法获取单词解释
		Indexer indexer = idxMap.get(word);
		if(indexer != null) {
System.err.println(indexer.bytePos + ":" + indexer.byteCount);
			getExplainFrom2(indexer.bytePos, indexer.byteCount);
			return null;
		} else {
			return null;
		}
	}
	
}

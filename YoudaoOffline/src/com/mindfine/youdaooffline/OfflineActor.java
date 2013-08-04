package com.mindfine.youdaooffline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import com.mindfine.youdaodict.fetcher.Fetcher;
import com.mindfine.youdaodict.fetcher.YoudaoCollinsFetcher;


public class OfflineActor {
	public static void main(String[] args) throws Exception {
		OfflineActor oa = new OfflineActor();
		String [] words = oa.parseWords("words.txt");
		
		FileWriter fw = new FileWriter(System.getProperty("user.dir") + "collinsWords.txt", true);
		PrintWriter pw = new PrintWriter(fw);
		
		oa.saveWords(pw, words);
		pw.close();
	}
	public void saveWords(PrintWriter pw, String[] words) {
		for(int i = 0; i < words.length; i++) {
			String word = words[i];
			YoudaoCollinsFetcher ycf = new YoudaoCollinsFetcher();
			ycf.setStyleType(Fetcher.StyleType.plain);
			String exp = ycf.jsoupFetcher(word);
			printToFile(word, exp, pw);
		}
	}
	/**
	 * 字典格式：<br>
	 * wordName``wordExplain```wordName``wordExplain```wordName``wordExplain```wordName``wordExplain```<br>
	 * 这只是临时把数据取下来，以后转的时候直接先用```把单词之间分开，再用``把词汇和释义分开，再做索引、之类的处理<br>
	 * 即：```分隔不同的单词，``分隔单词内的词汇和解释
	 * @param exp 要打印的单词及释义
	 * @param word 当前打印的单词
	 */
	public void printToFile(String word, String exp, PrintWriter pw) {
		File collinsWords = new File(System.getProperty("user.dir") + "collinsWords.txt");
		try {
			if(!collinsWords.exists()) {
					if(!collinsWords.createNewFile()) {
						System.err.println("输出数据的文件(user.dir.collinsWords.txt)不存在，且无法创建！");
					}
			}
			pw.print(word + "``");
			pw.println(exp);
			pw.print(word + "```");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 警告！该方法有副作用，会将单词文件写入一个临时文件中<br>
	 * 该临时文件位于当前工作路径中，名称为：words-tmp.txt
	 * @param filePath 要解析哪个文件的单词？
	 * @return 单词们和他们是否已经获取释义完成的标记
	 * @throws Exception 
	 */
	public String[] parseWords(String filePath) throws Exception{
		File wf = new File(filePath);
		String [] wds = null;
		if(wf.exists()) {
			FileInputStream fis = new FileInputStream(wf);
			StringBuffer sb = new StringBuffer();
			int ch;
			while((ch = fis.read()) != -1) {
				sb.append((char)ch);
			}
			String tmpString = new String(sb);
			String words = new String(tmpString.getBytes("ISO8859-1"), "utf-8");
			wds = words.split("[^a-zA-z]");

			//去重
			HashSet<String> noDupli = new HashSet<String>();
			for(int i = 0; i < wds.length; i++) {
				noDupli.add(wds[i]);
			}
			
			PrintWriter pw = new PrintWriter(new FileWriter(System.getProperty("user.dir") + "/words-tmp.txt"));
			String tpr = null;
			Iterator<String> noDupIterator = noDupli.iterator();
			while(noDupIterator.hasNext()) {
				tpr = noDupIterator.next();
				if(!tpr.equals("")) {
					pw.println(tpr);
				}
			}
			
			pw.close();
		} else {
			System.out.println("File not exists.");
		}
		return wds;
	}
}

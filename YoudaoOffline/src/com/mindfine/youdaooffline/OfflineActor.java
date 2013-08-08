package com.mindfine.youdaooffline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.mindfine.youdaodict.fetcher.Fetcher;
import com.mindfine.youdaodict.fetcher.YoudaoCollinsFetcher;
import com.mindfine.youdaodict.pronouncer.YoudaoPronouncer;


public class OfflineActor {
	public static boolean debug = true;
	public static String baseDir = System.getProperty("user.dir") + "/";
	public static int failedCount = 0;
	
	public static void main(String[] args) throws Exception {
		
		OfflineActor oa = new OfflineActor();
		oa.parseAndSaveWords("words.txt");
		
	}
	/**
	 * @param word
	 */
	public void saveAudio(String word) {
		String saveTo = baseDir + "speech/";
		new YoudaoPronouncer().download(word, saveTo);
	}
	/**
	 * 字典格式：<br>
	 * wordName``wordExplain```wordName``wordExplain```wordName``wordExplain```wordName``wordExplain```<br>
	 * 这只是临时把数据取下来，以后转的时候直接先用```把单词之间分开，再用``把词汇和释义分开，再做索引、之类的处理<br>
	 * 即：```分隔不同的单词，``分隔单词内的词汇和解释
	 * @param exp 要打印的单词及释义
	 * @param word 当前打印的单词
	 * @param pw 往哪儿打印？
	 */
	public void printToFile(String word, String exp, PrintWriter pw) {
		File collinsWords = new File(baseDir + "collinsWords.txt");
		try {
			if(!collinsWords.exists()) {
					if(!collinsWords.createNewFile()) {
						System.err.println("输出数据的文件(user.dir/collinsWords.txt)不存在，且无法创建！");
					}
			}
			pw.print(word + "``");
			pw.println(exp);
			pw.print("```");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param filePath 要解析哪个文件的单词？
	 * @return 单词们和他们是否已经获取释义完成的标记
	 * @throws Exception 
	 */
	public String[] parseAndSaveWords(String filePath) throws Exception{
		File wf = new File(filePath);
		String [] wds = null;
		if(wf.exists()) {
			FileInputStream fis = new FileInputStream(wf);
			StringBuffer sb = new StringBuffer();
			int ch;
			while((ch = fis.read()) != -1) {
				sb.append((char)ch);
			}
			fis.close();
			String tmpString = new String(sb);
			String words = new String(tmpString.getBytes("ISO8859-1"), "utf-8");
			wds = words.split("[^a-zA-z]");			
			
			//读取已经完成的单词
			File finishedFile = new File(System.getProperty("user.dir") + "/words-finished.txt");
			String finishedWords = null;
			if(finishedFile.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/words-finished.txt"));
				StringBuilder finishedSB = new StringBuilder();
				String bufLine = null;
				while((bufLine = br.readLine()) != null) {
					finishedSB.append(bufLine);
				}
				finishedWords = new String(finishedSB);
				br.close();
			}
			
			//已完成单词文件，以追加方式打开
			PrintWriter finishedLogPw = new PrintWriter(new FileWriter(System.getProperty("user.dir") + "/words-finished.txt", true));
			String tprWord = null;

			//存储单词释义的文件
			FileWriter fw = new FileWriter(baseDir + "collinsWords.txt", true);
			PrintWriter expOutPw = new PrintWriter(fw);
			
			for(int i = 0; i < wds.length; i++) {
				tprWord = wds[i];
				if(debug == true) System.out.print("当前正在下载:" + tprWord + "...");
				if(finishedWords != null && !finishedWords.equals("") && finishedWords.contains(tprWord)) {
if(debug == true) System.out.println("已经存在");
					continue;
				}
				//先获取释义及存储释义音频
				//然后存储进度
				if(!tprWord.equals("")) {
					YoudaoCollinsFetcher ycf = new YoudaoCollinsFetcher();
					ycf.setStyleType(Fetcher.StyleType.plain);
					String exp = ycf.jsoupFetcher(tprWord);
					if(null == exp || exp.equals("")) {
if(debug == true) System.out.println("未找到释义，下一个");
						if(failedCount > 5) {
							Thread.sleep(failedCount * 1000);
						} else if (failedCount > 50) {
							System.exit(0);
						}
						continue;
					} else {
						failedCount = 0;
					}
					printToFile(tprWord, exp, expOutPw);
					saveAudio(tprWord);

					finishedLogPw.println(tprWord);
				}
				expOutPw.flush();
				finishedLogPw.flush();
if(debug == true) System.out.println("done");
			}			
			expOutPw.close();
			finishedLogPw.close();
		} else {
			System.out.println("File " + filePath + " not exists.");
		}
		return wds;
	}
}

package com.mindfine.youdaooffline.categorise;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CETTEMCategorise {
	
	public static void main(String[] args) {
		String wordsLoc = System.getProperty("user.dir") + "/collinsWords.txt";

		CETTEMCategorise cts = new CETTEMCategorise();
		String [][] words = cts.readFile(wordsLoc);
//		生成词典文件
		cts.generateDicFile(words);
	}
	
	public String[][] readFile(String filePath){
		String [][] words = null;
		
		File wordsFile = new File(filePath);
		//文件读取器
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(wordsFile));
			StringBuffer sb = new StringBuffer();
			String tpr = null;
			while((tpr = br.readLine()) != null) {
				sb.append(tpr + "\r\n");
			}
			br.close();

			String wordsString = new String(sb);
			String [] wordsUnit = wordsString.split("```");
			int wordsSize = wordsUnit.length;
			words = new String[wordsSize][2];
			
			///拆分单词数组，提取里面的单词和解释
			//每个单词，包括单词和解释
			String wordUnit = null;
			//每个单词的两个部分，组成的数组
			String[] tpr2 = null;
			for(int i = 0; i < wordsSize; i++) {
				wordUnit = wordsUnit[i];
				tpr2 = wordUnit.split("``");
				if(tpr2.length > 1) {
					if(tpr2[0] != null && !tpr2[0].equals("") && tpr2[1] != null && !tpr2[1].equals("")) {
						String tpr20 = tpr2[0];
						String tpr21 = tpr2[1];
						words[i][0] = tpr20;
						words[i][1] = tpr21;
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("错误：无法找到文件！");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return words;
	}

	/**
	 * @param words 要保存的单词数据：<br>
	 * 参数内容格式：<br>
	 * word  explain<br>
	 * word2 explain2<br>
	 * ...<br>
	 */
	private void generateDicFile(String[][] words) {
		DicOperator dor = new DicOperator();
		for(int i = 0; i < words.length; i++) {
			String explain = words[i][1];
			String typeStr = "";
			//如果解释不为空则进行判断类型和保存单词
			if(explain != null) {
				if(explain.contains("CET4")){
					typeStr += "CET4";
					if(explain.contains("CET6")){
						typeStr += ",CET6";
						if(explain.contains("TEM8")){
							typeStr += ",TEM8";
						}
					}
				} else if (explain.contains("CET6")) {
					typeStr += "CET6";
					if(explain.contains("TEM8")){
						typeStr += ",TEM8";
					}
				} else if (explain.contains("TEM8")) {
					typeStr += "TEM8";
				}
				dor.saveWordForByte(words[i][0], words[i][1], typeStr);
			}
		}
	}

}

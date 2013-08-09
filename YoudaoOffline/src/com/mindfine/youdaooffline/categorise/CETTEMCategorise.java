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
		cts.readFile(wordsLoc);
	}
	
	public String[][] readFile(String filePath){
		String [][] words = null;
		
		File wordsFile = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(wordsFile));
			StringBuffer sb = new StringBuffer();
			String tpr = null;
			while((tpr = br.readLine()) != null) {
				sb.append(tpr);
			}
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
				words[i][0] = tpr2[0];
				words[i][1] = tpr2[1];
			}
			
			//TODO 如何存储单词数据！
			
			
		} catch (FileNotFoundException e) {
			System.err.println("错误：无法找到文件！");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return words;
	}

}

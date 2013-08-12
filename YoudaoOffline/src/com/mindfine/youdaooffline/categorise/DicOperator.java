package com.mindfine.youdaooffline.categorise;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 词库有两个文件：youdaocollins.idx、youdaocollins.exp<br>
 * 先生成.exp词库及解释文件，然后再根据这份文件生成.idx索引文件<br>
 * 词库文件内容描述了单词的位置：<br>
 * 单词名称:单词类型 :解释位置:解释长度<br>
 * name:CET4,TEM4:1300:30<br>
 * what:TEM8:1330:54<br>
 * 
 * 定义一系列操作单词索引表和解释表的方法
 * 
 * @author mindfine
 */
public class DicOperator {

	private static PrintWriter idxout = null;
	private static PrintWriter expout = null;
	private static int ecur = 1;

	static {
		try {
			idxout = new PrintWriter(new FileWriter(
					System.getProperty("user.dir") + "/youdaocollins.idx"));
			expout = new PrintWriter(new FileWriter(
					System.getProperty("user.dir") + "/youdaocollins.exp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单词名称:单词类型 :解释位置:解释长度<br>
	 * @param word 要存储的单词
	 * @param explain 解释
	 * @param type 类型
	 */
	public void saveWord(String word, String explain, String type) {
		explain = explain.trim();
		int explainLines = explain.split("\r\n").length;

		idxout.print(word + ":" + type + ":" + ecur + ":" + explainLines);
		idxout.println();
		expout.println(explain);
		ecur += explainLines;
		idxout.flush();
		expout.flush();
	}

}

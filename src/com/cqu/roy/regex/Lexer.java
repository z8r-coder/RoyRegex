package com.cqu.roy.regex;

import java.util.Stack;

/**
 * 词法分析
 * @author Roy
 * @date: 2017年3月26日  上午11:09:50
 * version:
 */
public class Lexer {
	private Stack<Token> symbolStack;//符号栈
	private Stack<Token> astNodeStack;//语法树结点栈
	private String message;//需要分析的串
	public Lexer(String message) {
		// TODO Auto-generated constructor stub
		this.message = message;
		symbolStack = new Stack<>();
		astNodeStack = new Stack<>();
	}
	/**
	 * 先构建语法分析树
	 * 表示连接符号时候,如message中ab,我们需要将b压入栈中前,先将$符号压入符号栈
	 * 自底向上构建语法分析树
	 */
	public void buildASTree() {
		for(int i = 0; i < message.length();i++){
			if (message.charAt(i) == '(' || message.charAt(i) == ')'
					|| message.charAt(i) == '|' || message.charAt(i) == '*'
					|| message.charAt(i) == '?' || message.charAt(i) == '+') {
				
			}
		}
	}
}

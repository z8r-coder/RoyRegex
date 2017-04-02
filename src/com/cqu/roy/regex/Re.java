package com.cqu.roy.regex;

/**
 * 解析正则表达式，构建nfa 及nfa转dfa
 * @author Roy
 * @date: 2017年3月26日  上午10:25:45
 * version:
 */
public class Re {
	private String regex;
	public Re() {
		// TODO Auto-generated constructor stub
	}
	public Re(String regex) {
		// TODO Auto-generated constructor stub
		this.regex = regex;
	}
	/**
	 * 非传参编译
	 */
	public void compile() {
		
	}
	/**
	 * 传参编译
	 * @param re
	 */
	public void compile(String re) {
		
	}
	public boolean match() {
		return true;
	}
	/**
	 * 辅助编译
	 * @param ss
	 */
	private void com(String message){
		Parser parser = new Parser(message);
		//解析字符串，得到语法分析树
		ASTNode root = parser.resolveAST();
	}
	
	private void generateNFA(){
		
	}
}

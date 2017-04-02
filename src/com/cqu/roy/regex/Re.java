package com.cqu.roy.regex;

/**
 * 解析正则表达式，构建nfa 及nfa转dfa
 * @author Roy
 * @date: 2017年3月26日  上午10:25:45
 * version:
 */
public class Re {
	private String regex;
	private ASTNode node;
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
		com(this.regex);
	}
	/**
	 * 传参编译
	 * @param re
	 */
	public void compile(String re) {
		com(re);
	}
	public boolean match(String target) {
		return node.getNfa().match_(target);
	}
	/**
	 * 辅助编译
	 * @param ss
	 */
	private void com(String message){
		Parser parser = new Parser(message);
		//解析字符串，得到语法分析树
		ASTNode root = parser.resolveAST();
		generateNFA(root);
		node = root;
	}
	
	private void generateNFA(ASTNode root){
		if (root == null) {
			return;
		}
		generateNFA(root.getLeftChild());
		generateNFA(root.getRightChild());
		if (!root.getIsLeaf()) {
			if (root.getToken().getCharacter() == '$') {
				Nfa nfa = root.getLeftChild().getNfa();
				nfa.connect(root.getRightChild().getNfa());
				root.setNfa(nfa);
			}else if (root.getToken().getCharacter() == '|') {
				Nfa nfa = root.getLeftChild().getNfa();
				nfa.and(root.getRightChild().getNfa());
				root.setNfa(nfa);
			}
		}
	}
}

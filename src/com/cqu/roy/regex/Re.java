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
		//Dfa dfa = new Dfa(node.getNfa());
		
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
				//使用拷贝构造函数生成新的nfa，这用在进行构造时候不会改变 结点中的nfa
				Nfa nfa_left = new Nfa(root.getLeftChild().getNfa());
				Nfa nfa_right = new Nfa(root.getRightChild().getNfa());
				nfa_left.connect(nfa_right);
				
				root.setNfa(nfa_left);
			}else if (root.getToken().getCharacter() == '|') {
				//使用拷贝构造函数生成新的nfa，这用在进行构造时候不会改变 结点中的nfa
				Nfa nfa_left = new Nfa(root.getLeftChild().getNfa());
				Nfa nfa_right = new Nfa(root.getRightChild().getNfa());
				nfa_left.and(nfa_right);
		
				root.setNfa(nfa_left);
			}
		}
	}

	/**
	 * 翻转字符串
	 * @param message
	 * @return
	 */
//	private String reverse(String message){
//		Stack<Character> stack = new Stack<>();
//		for(int i = 0; i < message.length();i++){
//			stack.add(message.charAt(i));
//		}
//		StringBuilder sb = new StringBuilder();
//		while(!stack.isEmpty()){
//			sb.append(stack.pop());
//		}
//		return sb.toString();
//	}
}

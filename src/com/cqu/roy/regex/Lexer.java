package com.cqu.roy.regex;

import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Stack;

import com.cqu.roy.exception.BracketsNotMatchedException;
import com.cqu.roy.exception.NodeExistException;
import com.cqu.roy.exception.UncertainException;

/**
 * 词法分析
 * @author Roy
 * @date: 2017年3月26日  上午11:09:50
 * version:
 */
public class Lexer {
	private Stack<ASTNode> symbolStack;//符号栈
	private Stack<ASTNode> astNodeStack;//语法树结点栈
	private String message;//需要分析的串
	private final static int CHARSTATE = 1;
	private final static int SYMBOLSTATE = 2;
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
	 * 符号优先级*=+=?>()>$>|
	 * 栈中的数量永远不超过三个
	 */
	public void buildASTree() throws NodeExistException,
	BracketsNotMatchedException{
		//栈中是否有左括号
		boolean left_parenthese = false;
		for(int i = 0; i < message.length();i++){
			if (message.charAt(i) == '(') {
				//栈中存在左括号
				left_parenthese = true;
				symbolStack.push(new ASTNode(new Token('(', true, false), null, null));
			}else if (message.charAt(i) == ')') {
				if (left_parenthese) {//若存在左括号，
					/**
					 * 括号不加入语法树，仅做优先级判断
					 * 括号内存在三种情况
					 * ()
					 * (N)
					 * (N-M)-代表二元运算符
					 */
					ASTNode astNode = symbolStack.pop();
					Character c = astNode.getToken().getCharacter();
					if (c == '(') {
						//表明()或(N)两种情况，这两种情况都不用做操作
					}else {
						//(N-M)情况
						//符号打包成结点
						ASTNode an = new ASTNode(new Token(c, true, false), null, null);
						//M和N打包成结点
						if (astNodeStack.size() != 2) {
							throw new NodeExistException(getClass().toString());
						}else {
							ASTNode leftNode = astNodeStack.pop();
							ASTNode rightNode = astNodeStack.pop();
							an.setLeftChild(leftNode);
							an.setRightChild(rightNode);
							//然后将整颗树包裹成一个ASTNode结点
							ASTNode newTree = new ASTNode(null, null, null);
							//设置root根结点，以便后面能够获取整颗树的信息
							newTree.setRootNode(an);
							//然后将新生成的节点树压入
							astNodeStack.push(newTree);
							//弹出')'
							symbolStack.pop();
						}
					}
					//将左括号进栈标识符置为false,因为其已经匹配到相应右括号了
					left_parenthese = false;
				}else {
					//括号匹配异常
					throw new BracketsNotMatchedException(getClass().toString());
				}
			}else if (message.charAt(i) == '|') {
				//若此时符号栈中元素为空，则直接压栈，不直接后面的内容
				if (symbolStack.size() == 0) {
					ASTNode astNode = new ASTNode(new Token('|', true, false)
							, null, null);
					symbolStack.push(astNode);
					continue;
				}
				//压入栈时需要比较优先级
				ASTNode node = symbolStack.peek();
				char c = node.getToken().getCharacter();
				if (c == '|' || c == '$') {
					//说明M|N或MN后又遇到了|，此时需要先构建M|N或MN的语法树
					symbolStack.pop();//弹出| $
					if (astNodeStack.size() != 2) {
						throw new NodeExistException(getClass().toString());
					}else {
						ASTNode leftNode = astNodeStack.pop();
						ASTNode rightNode = astNodeStack.pop();
						node.setLeftChild(leftNode);
						node.setRightChild(rightNode);
						//将整棵树包装成一个node结点
						ASTNode newTree = new ASTNode(null, null, null);
						//设置root结点
						newTree.setRootNode(node);
						//压入新生成的结点栈
						astNodeStack.push(newTree);
						//再向符号栈中压入|
						ASTNode andNode = new ASTNode(new Token('|', true, false)
								, null, null);
						symbolStack.push(andNode);
					}
				}
				else if (c == '(') {
					//若栈中只有(则直接压入|就行了
					ASTNode node2 = new ASTNode(new Token('|', true, false)
							, null, null);
					symbolStack.push(node2);
				}
			}else if (message.charAt(i) == '$') {
				//符号栈中只可能出现'|'和'*'或为空
				if (symbolStack.size() == 0) {
					//符号栈为空，则将'$'压栈，继续解析下一个字符
					ASTNode astNode = new ASTNode(new Token('$', true, false)
							, null, null);
					symbolStack.push(astNode);
					continue;
				}
				ASTNode symNode = symbolStack.peek();
				char c = symNode.getToken().getCharacter();
				if (c == '$') {
					//栈中存在的是$
					if (astNodeStack.size() != 2) {
						throw new NodeExistException(getClass().toString());
					}
					//将栈中存在的$构建为M$N的语法树
					ASTNode sNode = symbolStack.pop();
					ASTNode leftNode = astNodeStack.pop();
					ASTNode rightNode = astNodeStack.pop();
					sNode.setLeftChild(leftNode);
					sNode.setRightChild(rightNode);
					//将这颗语法树，包裹成ASTNode
					ASTNode newTree = new ASTNode(null, null, null);
					newTree.setRootNode(sNode);
					//压入结点栈
					astNodeStack.push(newTree);
				}else if (c == '|') {
					//栈中存在是|
					if (astNodeStack.size() != 2) {
						
					}
				}else {
					throw new NodeExistException(getClass().toString());
				}
			}else if (message.charAt(i) == '*' || message.charAt(i) == '+' 
					|| message.charAt(i) == '?') {
				//这三个符号为优先级最高，所以不用进栈，直接弹出
				//结构如   *
				//       |
				//       N
				if (astNodeStack.size() == 0) {
					throw new NodeExistException(getClass().toString());
				}
				ASTNode charNode = astNodeStack.pop();
				ASTNode symbolNode = new ASTNode(new Token(message.charAt(i),
						true, false), charNode, null);
				//将整颗树包装成一个node结点
				ASTNode newTree = new ASTNode(null, null, null);
				newTree.setRootNode(symbolNode);
				//压入结点栈
				astNodeStack.push(newTree);
			}	
			else {
				//字符[a-zA-Z0-9]若ab连接，中间需要添加$
				ASTNode astNode = new ASTNode(new Token(message.charAt(i), false, true)
						, null, null);
				//第i个元素是字符，判断第i + 1个元素是不是字符
				try {
					if (i + 1 < message.length() && 
							getState(message.charAt(i + 1)) == CHARSTATE) {
						//说明连续i和i+1都是字符，则需要朝symbolStack中push $ ASTNode
						ASTNode symNode = new ASTNode(new Token('$', true, false),
								null, null);
						symbolStack.push(symNode); 
					}
				} catch (UncertainException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				astNodeStack.push(astNode);
			}
		}
		//栈中有可能剩余元素,由于*?+三个符号根本不会进栈，
		//可能的情况为MN,M|N
		//ASTNode sym		
	}
	/**
	 * 解析被包裹在ASTNode中的整颗语法树
	 * @return
	 * @throws NodeExistException
	 */
	public ASTNode getTree() throws NodeExistException{
		if (astNodeStack.size() != 1 || symbolStack.size() != 0) {
			throw new NodeExistException(getClass().toString());
		}
		return astNodeStack.pop();
	}
	
	/**
	 * 判断该字符是符号还是数字字符
	 * @param c
	 * @return
	 */
	public int getState(char c) throws UncertainException{
		String charSeq = "123456789qwertyuiopasdfghjklzxcvbnm";
		String symbolSeq = "$*?+|";
		for(int i = 0; i < charSeq.length();i++){
			if (charSeq.charAt(i) == c) {
				return CHARSTATE;
			}
		}
		for(int i = 0; i < symbolSeq.length();i++){
			if (symbolSeq.charAt(i) == c) {
				return SYMBOLSTATE;
			}
		}
		throw new UncertainException(getClass().toString());
	}
}

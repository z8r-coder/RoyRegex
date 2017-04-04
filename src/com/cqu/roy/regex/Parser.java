package com.cqu.roy.regex;

import java.util.Stack;

import com.cqu.roy.exception.BracketsNotMatchedException;
import com.cqu.roy.exception.NodeExistException;
import com.cqu.roy.exception.UncertainException;

/**
 * 解析，生成语法树
 * @author Roy
 * @date: 2017年3月26日  上午11:09:50
 * version:
 */
public class Parser {
	private Stack<ASTNode> symbolStack;//符号栈
	//*+?这三个符号优先级最高，不会进栈，而|和$后进栈的一定大于等于先进栈的优先级
	private Stack<ASTNode> astNodeStack;//语法树结点栈
	private String message;//需要分析的串
	private final static String SS_STRING = "qwertyuioplkjhgfdsazxcvbnm"
			+ "QWERTYUIOPLKJHGFDSAZXCVBNM1234567890";
	private final static int CHARSTATE = 1;
	private final static int SYMBOLSTATE = 2;
	public Parser(String message) {
		// TODO Auto-generated constructor stub
		this.message = message;
		symbolStack = new Stack<>();
		astNodeStack = new Stack<>();
	}
	
	/**
	 * 解析语法树,并返回这颗树
	 */
	public ASTNode resolveAST() {
		try {
			buildASTree();
		} catch (NodeExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BracketsNotMatchedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (astNodeStack.size() == 0) {
			return null;
		}
		ASTNode root = astNodeStack.pop().getRootNode();
		TreeOpen(root);
		PreTravel(root);
		System.out.println();
		InTravel(root);
		return root;
	}
	/**
	 * 将被包裹在ASTNode中的tree展开
	 * @param root
	 */
	private void TreeOpen(ASTNode root) {
		if (root == null) {
			return;
		}
		if (root.getRightChild() != null && root.getRightChild().getRootNode() != null) {
			root.setRightChild(root.getRightChild().getRootNode());
			TreeOpen(root.getRightChild());
		}
		if (root.getLeftChild() != null && root.getLeftChild().getRootNode() != null) {
			root.setLeftChild(root.getLeftChild().getRootNode());
			TreeOpen(root.getLeftChild());
		}
	}
	/**
	 * 先序遍历
	 * @param root
	 */
	private void PreTravel(ASTNode root){
		if (root == null) {
			return;
		}
		System.out.print(root.getToken().getCharacter());
		PreTravel(root.getLeftChild());
		PreTravel(root.getRightChild());
	}
	/**
	 * 中序遍历
	 * @param root
	 */
	private void InTravel(ASTNode root) {
		if (root == null) {
			return;
		}
		InTravel(root.getLeftChild());
		//System.out.println(root.getLeftChild());
		System.out.print(root.getToken().getCharacter());
		InTravel(root.getRightChild());
	}
	/**
	 * 先构建语法分析树
	 * 表示连接符号时候,如message中ab,我们需要将b压入栈中前,先将$符号压入符号栈
	 * 自底向上构建语法分析树
	 * 符号优先级*=+=?>()>$>|
	 * ?+*)这四个符号是不可能在栈中出现的,符号栈中只可能出现 | $ (，
	 * 并且栈中最多可能连续出现| $再来任意一个|和$都会导致左结合
	 * 并切符号栈中的元素从底到顶，优先级不断升高，右结合需谨慎
	 */
	public void buildASTree() throws NodeExistException,
	BracketsNotMatchedException{
		//栈中有多少个左括号待匹配
		int left_parenthese = 0;
		for(int i = 0; i < message.length();i++){
			if (message.charAt(i) == '(') {
				//栈中存在左括号
				left_parenthese++;
				symbolStack.push(new ASTNode(new Token('(', true, false), null, null,false));
			}else if (message.charAt(i) == ')') {
				if (left_parenthese > 0) {//若存在左括号，
					/**
					 * 括号不加入语法树，仅做优先级判断
					 * 括号内存在三种情况
					 * ()
					 * (N)
					 * (N-M)-代表二元运算符
					 */
					//System.out.println(symbolStack.size());
					ASTNode symNode = symbolStack.pop();
					Character c = symNode.getToken().getCharacter();
					if (c == '(') {
						//表明()或(N)两种情况，这两种情况都不用做操作
					}else {
						//c != (  
						//(N-M)情况

						//M和N打包成结点
						if (astNodeStack.size() < 2) {
							throw new NodeExistException(getClass().toString());
						}else {
							
							while(c != '('){
								ASTNode leftNode = astNodeStack.pop();
								ASTNode rightNode = astNodeStack.pop();
								//符号打包成结点
								ASTNode an = new ASTNode(new Token(c, 
										true, false), null, null,false);
								//包裹结点
								ASTNode newTree = PackASTNode(an, leftNode, rightNode);
								//然后将新生成的节点树压入
								astNodeStack.push(newTree);
								ASTNode tmp = symbolStack.pop();
								c =  tmp.getToken().getCharacter();
							}
						}
					}
					//匹配一个右括号，减一
					left_parenthese--;
				}else {
					//括号匹配异常
					throw new BracketsNotMatchedException(getClass().toString());
				}
			}else if (message.charAt(i) == '|') {
				//若此时符号栈中元素为空，则直接压栈，不直接后面的内容,|优先级最低，底部结合
				if (symbolStack.size() == 0) {
					ASTNode symNode = new ASTNode(new Token('|', true, false)
							, null, null,false);
					symbolStack.push(symNode);
					continue;
				}
				//压入栈时需要比较优先级
				ASTNode node = symbolStack.pop();//由于这个操作，若node是'('，后面需要再将该结点压栈
				char c = node.getToken().getCharacter();
				if (c == '|' || c == '$') {
					//说明M|N或MN后又遇到了|，此时需要先构建M|N或MN的语法树
					if (astNodeStack.size() < 2) {
						throw new NodeExistException(getClass().toString());
					}else {
						ASTNode leftNode = astNodeStack.pop();
						ASTNode rightNode = astNodeStack.pop();
						ASTNode newTree = PackASTNode(node, leftNode, rightNode);
						//压入新生成的结点栈
						astNodeStack.push(newTree);
						//再向符号栈中压入|
						ASTNode andNode = new ASTNode(new Token(message.charAt(i)
								, true, false), null, null,false);
						symbolStack.push(andNode);
					}
				}
				else if (c == '(') {
					//若栈中只有(则直接压入|就行了
					ASTNode node2 = new ASTNode(new Token(message.charAt(i)
							, true, false), null, null,false);
					ASTNode leftBra = new ASTNode(new Token('(',
							true, false), null, null,false);
					//将'('压栈
					symbolStack.push(leftBra);
					symbolStack.push(node2);
				}else {
					throw new NodeExistException(getClass().toString());
				}
			}else if (message.charAt(i) == '$') {
				//符号栈中只可能出现'|'和'*'或为空
				if (symbolStack.size() == 0) {
					//符号栈为空，则将'$'压栈，继续解析下一个字符
					ASTNode astNode = new ASTNode(new Token('$', true, false)
							, null, null,false);
					symbolStack.push(astNode);
					continue;
				}
				ASTNode symNode = symbolStack.pop();
				char c = symNode.getToken().getCharacter();
				if (c == '$') {
					//栈中存在的是$,因为$是二元操作符，栈中不可能少于2个元素
					if (astNodeStack.size() < 2) {
						throw new NodeExistException(getClass().toString());
					}
					//将栈中存在的$构建为M$N的语法树
					ASTNode leftNode = astNodeStack.pop();
					ASTNode rightNode = astNodeStack.pop();
					//将语法树包裹成ASTNode
					ASTNode newTree = PackASTNode(symNode, leftNode, rightNode);
					//压入结点栈
					astNodeStack.push(newTree);
					//并将message.charAt(i)打包成符号结点放入符号栈中
					ASTNode sNode = new ASTNode(new Token(message.charAt(i)
							, true, false), null, null,false);
					symbolStack.push(sNode);
				}else if (c == '|' || c == '(') {
					//栈中存在是|,此时不能右结合，因为不知后面的操作符优先级情况
					//所以只能将$打包，压入symStack中
					//若只存在左括号，也是直接打包压入就OK了
					ASTNode sNode = new ASTNode(new Token(message.charAt(i)
							, true, false), null, null,false);
					if (c == '(') {
						//由于'('被弹出来了，还要重新压回去
						ASTNode leftBra = new ASTNode(new Token('(', true, false),
								null, null,false);
						symbolStack.push(leftBra);
					}else {
						//由于'|'被弹出来了，需要重新压回去
						ASTNode and = new ASTNode(new Token('|', true, false),
								null, null,false);
						symbolStack.push(and);
					}
					symbolStack.push(sNode);
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
						true, false), null, null,false);
				//将整颗树包装成一个node结点
				ASTNode newTree = PackASTNode(symbolNode, charNode);
				//压入结点栈
				astNodeStack.push(newTree);
			}	
			else {
				//字符[a-zA-Z0-9]若ab连接，中间需要添加$
				ASTNode astNode = new ASTNode(new Token(message.charAt(i)
						, false, true), null, null,true);
				//必须提前加入栈中，因为若栈中存在$会与该字符结合
				astNodeStack.push(astNode);
				//第i个元素是字符，判断第i + 1个元素是不是字符
				try {
					if (i + 1 < message.length() && 
							getState(message.charAt(i + 1)) == CHARSTATE) {
						//说明连续i和i+1都是字符，则需要朝symbolStack中push $ ASTNode
						//在push之前，需要检查其stack中符号的优先级
						if (symbolStack.size() == 0) {
							//若符号栈中为空，则直接压入
							ASTNode symNode = new ASTNode(new Token('$', true, false),
									null, null,false);
							symbolStack.push(symNode); 
						}else {
							ASTNode sNode = symbolStack.pop();
							char c = sNode.getToken().getCharacter();
							if (c == '$') {
								//若栈中的是连接符号，则需要左结合
								if (astNodeStack.size() < 2) {
									throw new NodeExistException(getClass().toString());
								}
								//生成左结合的结点，并将其压入栈中
								ASTNode leftNode = astNodeStack.pop();
								ASTNode rightNode = astNodeStack.pop();
								ASTNode newTree = PackASTNode(sNode, leftNode, rightNode);
								astNodeStack.push(newTree);
								
								//将$压入栈中
								ASTNode node = new ASTNode(new Token('$',
										true, false), null, null, false);
								symbolStack.push(node);
							}else if (c == '|' || c == '(') {
								//将sNode压回栈中
								symbolStack.push(sNode);
								
								//若栈中的是| 或 (符号，则直接压入符号
								ASTNode node = new ASTNode(new Token('$',
										true, false), null, null, false);
								symbolStack.push(node);
							}
						}
					}
				} catch (UncertainException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//栈中有可能剩余元素,由于*?+三个符号根本不会进栈，
		if (left_parenthese > 0) {
			//判断左括号匹配完没有，若未匹配完，则抛出异常
			throw new BracketsNotMatchedException(getClass().toString());
		}else {
			while(symbolStack.size() != 0){
				//栈中，顶的优先级一定高于底部的优先级
				ASTNode newTree = PackASTNode(symbolStack.pop()
						, astNodeStack.pop(), astNodeStack.pop());
				astNodeStack.push(newTree);
			}
		}
		//最终在结点栈中只会剩下一颗完整被ASTNode包裹起来的树
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
		String charSeq = "qwertyuioplkjhgfdsazxcvbnm"
			+ "QWERTYUIOPLKJHGFDSAZXCVBNM1234567890";
		String symbolSeq = "$*?+|()";
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
	
	/**
	 * | $ 二元符的打包函数，将一颗简单的语法树打包成一个ASTNode
	 * @param symNode 
	 * @param left
	 * @param right
	 */
	private ASTNode PackASTNode(ASTNode symNode,ASTNode left,ASTNode right){
		symNode.setLeftChild(left);
		symNode.setRightChild(right);
		ASTNode newTree = new ASTNode(null, null, null,false);
		newTree.setRootNode(symNode);
		return newTree;
	}
	/**
	 * *?+一元符打包函数，将一颗简单的语法树打包成一个ASTNode
	 * @param symNode
	 * @param left
	 * @return
	 */
	private ASTNode PackASTNode(ASTNode symNode, ASTNode left){
		symNode.setLeftChild(left);
		ASTNode newTree = new ASTNode(null, null, null,false);
		newTree.setRootNode(symNode);
		return newTree;
	}
	/**
	 * 判断c是否属于[0-9A-Za-z]
	 * @param c
	 */
	private boolean checkChar(char c){
		for(int i = 0; i < SS_STRING.length();i++){
			if (c == SS_STRING.charAt(i)) {
				return true;
			}
		}
		return false;
	}
}

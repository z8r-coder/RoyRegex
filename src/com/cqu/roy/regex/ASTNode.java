package com.cqu.roy.regex;

/**
 * 一个ASTNode中包含这一颗AST，在构建NFA的时候再展开，
 * @author Roy
 * @date: 2017年3月26日  下午8:27:57
 * version:
 */
public class ASTNode {
	private ASTNode rootNode;//每个ASTNode包含一个rootNode，要么返回一个树，或返回一个结点
	private Token token;
	private ASTNode leftChild;
	private ASTNode rightChild;
	public ASTNode(Token token, ASTNode leftChild, ASTNode rightChild) {
		// TODO Auto-generated constructor stub
		this.token = token;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	public void setLeftChild(ASTNode leftNode) {
		this.leftChild = leftNode;
	}
	public void setRightChild(ASTNode rightNode) {
		this.rightChild = rightNode;
	}
	
	public ASTNode getLeftChild() {
		return leftChild;
	}
	
	public ASTNode getRightChild() {
		return rightChild;
	}
	
	public void setRootNode(ASTNode rootNode) {
		this.rootNode = rootNode;
	}
	public ASTNode getRootNode() {
		return rootNode;
	}
	public void setToken(Token token) {
		this.token = token;
	}
	public Token getToken() {
		return token;
	}
}

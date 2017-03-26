package com.cqu.roy.regex;

import java.util.ArrayList;
import java.util.HashMap;

import com.cqu.roy.exception.UncertainException;
/**
 * Dfa的node
 * @author Roy
 * @date: 2017年3月22日  下午3:36:19
 * version:1.0
 */
public class DfaNode {
	private String state = null;
	//每个结点存着状态表,由于是确定状态，每个输入对应一条边
	private HashMap<Character, DfaNode> stateMoveTable;
	public boolean start;
	public boolean end;
	private ArrayList<NfaNode> nfaNodes;//子集构造法，dfa单个结点存储的nfa结点
	
	public DfaNode() {
		// TODO Auto-generated constructor stub
		stateMoveTable = new HashMap<>();
		nfaNodes = new ArrayList<>();
		start = false;
		end = false;
	}
	/**
	 * 拷贝构造函数
	 * @param node
	 */
	public DfaNode(DfaNode node){
		this.state = node.state;
		this.stateMoveTable = node.stateMoveTable;
		this.start = node.start;
		this.end = node.end;
		this.nfaNodes = node.nfaNodes;
	}
	/**
	 * 状态转移表添加input 字符和转移后状态结点
	 * @param c
	 * @param node
	 * @throws UncertainException
	 */
	public void addMoveTable (Character c, DfaNode node) throws UncertainException {
		/**
		 * 防止一条边出现多个对应状态，dfa每个结点的一个输入只应该对应一条边
		 */
		if (stateMoveTable.get(c) != null) {
				throw new UncertainException(getClass().toString());
		}
		stateMoveTable.put(c, node);
	}
	public HashMap<Character, DfaNode> getMoveTable() {
		return stateMoveTable;
	}
	
	//判定nfa集合中是否有end状态，若有则将该dfa结点的状态设置为end
	public void checkEnd() {
		for(NfaNode node : nfaNodes){
			if (node.end) {
				end = true;
			}
		}
	}
	
	public ArrayList<NfaNode> getNfaNodesSet() {
		return nfaNodes;
	}
	
	public void setNfaNodesSet(ArrayList<NfaNode> nodes) {
		this.nfaNodes = nodes;
	}
}

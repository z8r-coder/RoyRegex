package com.cqu.roy.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NfaNode {
	String state;// 当前节点状态名
	// 输入字符状态转移表,'\0'转移多个状态，不确定表现
	private Map<Character, ArrayList<NfaNode>> stateMoveTable;
	boolean start;
	boolean end;

	public NfaNode(String state) {
		// TODO Auto-generated constructor stub
		stateMoveTable = new HashMap<>();
		this.state = state;
		start = false;
		end = false;
	}

	/*
	 * @param s 接收的字符
	 * 
	 * @param newNode 转移到状态
	 */
	public void addMoveState(Character s, NfaNode newNode) {
		if (stateMoveTable.get(s) == null) {
			// 下一状态节点集合
			ArrayList<NfaNode> desNode = new ArrayList<>();
			desNode.add(newNode);
			stateMoveTable.put(s, desNode);
		} else {
			ArrayList<NfaNode> desNode = stateMoveTable.get(s);
			desNode.add(newNode);
			stateMoveTable.put(s, desNode);
		}
	}
	
	public HashMap<Character, ArrayList<NfaNode>> getStateTable() {
		return (HashMap<Character, ArrayList<NfaNode>>) stateMoveTable;
	}
}

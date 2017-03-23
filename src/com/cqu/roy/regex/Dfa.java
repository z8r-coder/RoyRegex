package com.cqu.roy.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.cqu.roy.exception.UncertainException;
/**
 * 通过子集构造法将nfa构造成dfa
 * @author Roy
 * @date: 2017年3月23日  下午4:33:47
 * version:
 */
public class Dfa {
	private ArrayList<DfaNode> dfa;
	private Nfa nfa;

	public Dfa(Nfa nfa) {
		// TODO Auto-generated constructor stub
		dfa = new ArrayList<>();
		this.nfa = nfa;
	}

	public DfaNode getStart() {
		for (DfaNode node : dfa) {
			if (node.start) {
				return node;
			}
		}
		return null;
	}

	public DfaNode getEnd() {
		for (DfaNode node : dfa) {
			if (node.end) {
				return node;
			}
		}
		return null;
	}

	public void addNodeToNfa(DfaNode node) {
		if (dfa == null) {
			return;
		}
		dfa.add(node);
	}

	public ArrayList<DfaNode> getNodeSet() {
		return dfa;
	}

	/**
	 * 生成dfa
	 */
	public void generaterDfa() {
		NfaNode nfaNode = nfa.getStart();
		// 第一次通过'\0'所构成的子集为第一个状态
		DfaNode dfaStartNode = new DfaNode();
		dfaStartNode.start = true;// start状态
		/**
		 * nfaNodes是dfa的子结点集合，每个能通过一次字符转移就能到达的状态
		 * 均需添加，组成dfanode 一个状态结点
		 */
		ArrayList<NfaNode> nfaNodesSet = dfaStartNode.getNfaNodesSet();
		
		/**
		 * 深搜查找dfa的子集状态结点
		 */
		nfaNodesSet.add(nfaNode);//dfsState()不能放入初始结点
		dfsState('\0', nfaNode, nfaNodesSet);
		
		/**
		 * 是否包含终结结点
		 */
		dfaStartNode.checkEnd();
		dfa.add(dfaStartNode);
		
		/**
		 * inputNode是只转移表不只含有'\0'边的结点，是用来接收输入的
		 */
		ArrayList<NfaNode> inputNode = new ArrayList<>();
		dfsInput('\0', nfaNode, inputNode);

		SubsetConstruction(inputNode, dfaStartNode);
	}

	/**
	 * 通过子集构造法，构造dfa状态集合
	 * @param nfaNodesSet 当前能接受input的nfa结点的状态的集合
	 * @param dfaNode     当前dfaNode,传递来以便建立边
	 */
	public void SubsetConstruction(ArrayList<NfaNode> nfaNodesSet, DfaNode dfaNode) {
		/**
		 * 将需要的字符缓存起来
		 */
		HashSet<Character> charCache = new HashSet<>();
		for (NfaNode node : nfaNodesSet) {
			HashMap<Character, ArrayList<NfaNode>> stateMoveTable = node.getStateTable();
			for (Character c : stateMoveTable.keySet()) {
				/**
				 * 不能有'\0'边的转移结点
				 */
				if (c != '\0') {
					charCache.add(c);
				}
			}
		}

		/**
		 * 这个地方应该可以优化 nn用来存字符转移后的ndoe，
		 * 以便后来进行'\0'深搜，同样会处于相同的集合
		 */
		ArrayList<NfaNode> nn = new ArrayList<>();
		for (Character c : charCache) {
			// 每一个dfanode中所包含的nfanode集合
			ArrayList<NfaNode> arr = new ArrayList<>();
			for (NfaNode node : nfaNodesSet) {
				HashMap<Character, ArrayList<NfaNode>> hm = node.getStateTable();
				/**
				 * 存在路径
				 */
				if (hm.get(c) != null) {
					ArrayList<NfaNode> nno = hm.get(c);
					for(NfaNode node2 : nno){
						arr.add(node2);
						dfsState('\0', node2, arr);
					}
				}
			}
			/**
			 * 生成dfa结点
			 */
			DfaNode dNode = new DfaNode();
			dNode.setNfaNodesSet(arr);
			/**
			 * 设置边关系
			 */
			try {
				dfaNode.addMoveTable(c, dNode);
			} catch (UncertainException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *
	 * 该深搜是用来获得dfa的状态子集结点
	 */
	public void dfsState(char c, NfaNode node, ArrayList<NfaNode> nfaNodeSet) {
		if (node == null) {
			return;
		}
		ArrayList<NfaNode> nfaNodes = node.getStateTable().get('\0');
		if (nfaNodes == null) {
			nfaNodeSet.add(node);
			return;
		}
		for (NfaNode node2 : nfaNodes) {
			dfsState(c, node2, nfaNodeSet);
			if (node2 != null) {
				nfaNodeSet.add(node2);
			}
		}
	}
	/**
	 * 该深搜是用来获得input的结点
	 * @param c的值一般为'\0'
	 */
	
	public void dfsInput(char c, NfaNode node,ArrayList<NfaNode> inputNodes) {
		if (node == null) {
			return;
		}
		
		ArrayList<NfaNode> nn = node.getStateTable().get(c);
		//深搜的尽头
		if (nn == null) {
			inputNodes.add(node);
			return;
		}
		
		HashMap<Character, ArrayList<NfaNode>> hm = node.getStateTable();
		/*
		 * 转移表可能会带一边'\0'，另多边为character
		 */
		if (hm.size() >= 2) {
			inputNodes.add(node);
		}
		
		for(NfaNode node2 : inputNodes){
			dfsInput(c, node2, inputNodes);
		}
	}
}

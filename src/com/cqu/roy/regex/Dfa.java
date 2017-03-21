package com.cqu.roy.regex;

import java.util.ArrayList;

public class Dfa {
	private ArrayList<DfaNode> dfa;
	private Nfa nfa;
	
	public Dfa(Nfa nfa) {
		// TODO Auto-generated constructor stub
		dfa = new ArrayList<>();
		this.nfa = nfa;
	}
	
	public DfaNode getStart() {
		for(DfaNode node : dfa){
			if (node.start) {
				return node;
			}
		}
		return null;
	}
	
	public DfaNode getEnd() {
		for(DfaNode node : dfa){
			if (node.end) {
				return node;
			}
		}
		return null;
	}
	
	public void addNodeToNfa(String state) {
		if (dfa == null) {
			return;
		}
		dfa.add(new DfaNode(state));
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
	
	//通过子集构造法，构造dfa状态集合
	public void SubsetConstruction() {
		NfaNode node = nfa.getStart();
	}
	
	public void dfs() {
		
	}
}

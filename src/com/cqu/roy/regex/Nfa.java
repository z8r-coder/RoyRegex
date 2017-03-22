package com.cqu.roy.regex;

import java.util.ArrayList;
import java.util.HashMap;

import com.cqu.roy.exception.EndNodeException;
import com.cqu.roy.exception.StartNodeException;

/**
 * 正则表现的nfa仅有一个end结点和一个start结点
 * @author Roy
 * @date: 2017年3月22日  下午3:48:25
 * version:1.0
 */
public class Nfa {
	private ArrayList<NfaNode> nfa;

	public Nfa() {
		// TODO Auto-generated constructor stub
		nfa = new ArrayList<>();
	}

	/*
	 * @s 将状态s的节点设置为初始节点
	 */
	public void addStart(String s) {
		if (nfa == null) {
			return;
		}
		getNode(s).start = true;
	}

	// 将状态s的节点设置为终止节点
	public void addEnd(String s) {
		if (nfa == null) {
			return;
		}
		getNode(s).end = true;
	}

	// 获取开始状态
	public NfaNode getStart() {
		for (NfaNode node : nfa) {
			if (node.start) {
				return node;
			}
		}
		return null;
	}

	// 获取终结状态
	public NfaNode getEnd() {
		for (NfaNode node : nfa) {
			if (node.end) {
				return node;
			}
		}
		return null;
	}

	// 更改状态名
	public void updateState() {
		for (int i = 0; i < nfa.size(); i++) {
			nfa.get(i).state = String.valueOf(i);
		}
	}

	public void addNodeToNfa(String state) {
		if (nfa == null) {
			return;
		}
		nfa.add(new NfaNode(state));
	}

	private void addNodeToNfa(NfaNode node) {
		if (nfa == null) {
			return;
		}
		nfa.add(node);
	}

	public ArrayList<NfaNode> getNfaSet() {
		return nfa;
	}
	/*
	 * like this a* *闭包运算，出现任意次数
	 */

	public void closure_1() {
		if (nfa.size() == 0) {
			return;
		}
		NfaNode node_start = getStart();
		NfaNode node_end = getEnd();
		node_start.start = false;
		node_end.end = false;

		addNodeToNfa(new NfaNode("S"));
		addNodeToNfa(new NfaNode("E"));

		addStart("S");
		addEnd("E");

		NfaNode new_Start = getStart();
		NfaNode new_end = getEnd();

		new_Start.addMoveState('\0', node_start);
		new_Start.addMoveState('\0', new_end);

		node_end.addMoveState('\0', node_start);
		node_end.addMoveState('\0', new_end);

		updateState();
		
		try {
			checkStartNode();
			checkEndNode();
		} catch (StartNodeException | EndNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * like this a+ 闭包运算 出现1个或多个
	 */
	public void closure_2() {
		if (nfa.size() == 0) {
			return;
		}
		NfaNode node_start = getStart();
		NfaNode node_end = getEnd();

		node_start.start = false;
		node_end.end = false;

		addNodeToNfa(new NfaNode("S"));
		addNodeToNfa(new NfaNode("E"));
		addStart("S");
		addEnd("E");

		NfaNode new_Start = getStart();
		NfaNode new_end = getEnd();

		new_Start.addMoveState('\0', node_start);
		node_end.addMoveState('\0', new_end);
		node_end.addMoveState('\0', node_start);

		updateState();
		
		try {
			checkEndNode();
			checkStartNode();
		} catch (EndNodeException | StartNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * like this a? 闭包运算 出现1个或0个
	 */
	public void closure_3() {
		if (nfa.size() == 0) {
			return;
		}
		NfaNode node_Start = getStart();
		NfaNode node_End = getEnd();
		node_Start.start = false;
		node_End.end = false;

		addNodeToNfa(new NfaNode("S"));
		addNodeToNfa(new NfaNode("E"));

		addStart("S");
		addEnd("E");

		NfaNode new_Start = getStart();
		NfaNode new_End = getEnd();

		new_Start.addMoveState('\0', node_Start);
		new_Start.addMoveState('\0', node_End);
		node_End.addMoveState('\0', new_End);

		updateState();
		
		try {
			checkStartNode();
			checkEndNode();
		} catch (StartNodeException | EndNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * @param nfa like this ab 顺序为 self->nfa_tmp 连接运算
	 */
	public void connect(Nfa nfa_tmp) {
		if (nfa_tmp.getNfaSet().size() == 0) {
			return;
		}
		if (nfa.size() == 0) {
			nfa = nfa_tmp.getNfaSet();
			return;
		}

		NfaNode s1 = nfa_tmp.getStart();
		s1.start = false;

		NfaNode e0 = getEnd();
		e0.end = false;

		e0.addMoveState('\0', s1);

		for (NfaNode node : nfa_tmp.getNfaSet()) {
			nfa.add(node);
		}
		updateState();
		
		try {
			checkStartNode();
			checkEndNode();
		} catch (StartNodeException | EndNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * @param nfa like this a|b 并运算,由两个小的nfa进行|合成
	 */
	public void and(Nfa nfa_tmp) {
		Nfa new_nfa = new Nfa();
		// 为新的nfa创建一个start节点
		new_nfa.addNodeToNfa(new NfaNode("S"));
		new_nfa.addStart("S");
		// 为新的nfa创建一个end节点
		new_nfa.addNodeToNfa(new NfaNode("E"));
		new_nfa.addEnd("E");

		NfaNode s0 = getStart();
		NfaNode s1 = nfa_tmp.getStart();
		s0.start = false;
		s1.start = false;
		NfaNode new_start = new_nfa.getStart();
		new_start.addMoveState('\0', s0);
		new_start.addMoveState('\0', s1);

		NfaNode e0 = getEnd();
		NfaNode e1 = nfa_tmp.getEnd();
		e0.end = false;
		e1.end = false;
		NfaNode new_end = new_nfa.getEnd();
		e0.addMoveState('\0', new_end);
		e1.addMoveState('\0', new_end);

		// 以上构建关系，现在将所有的节点放入新的nfa
		for (NfaNode node : nfa) {
			new_nfa.addNodeToNfa(node);
		}
		for (NfaNode node : nfa_tmp.getNfaSet()) {
			new_nfa.addNodeToNfa(node);
		}
		new_nfa.updateState();
		nfa = new_nfa.getNfaSet();// 刷新当前nfa
		//改变了start结点和end结点后，check
		try {
			checkStartNode();
			checkEndNode();
		} catch (StartNodeException | EndNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NfaNode getNode(String state) {
		if (nfa == null) {
			return null;
		}
		for (NfaNode node : nfa) {
			if (node.state.equals(state)) {
				return node;
			}
		}
		return null;
	}

	// nfa匹配，主要测试用例
	public boolean match_(String target) {
		NfaNode start_node = getStart();
		ArrayList<NfaNode> nodeSet = new ArrayList<>();
		// 获取第一次没有'\0'运算的结点,由于第一次只有一个结点，可以直接深搜获取全部不带'\0'边的结点
		dfs(start_node, nodeSet, '\0');

		ArrayList<NfaNode> res_set = new ArrayList<>();
		for (int i = 0; i < target.length(); i++) {
			//状态转移
			nodeSet = moveState(nodeSet, target.charAt(i));
			// 消除含有'\0'的边
			ArrayList<NfaNode> arr = new ArrayList<>();
			for (NfaNode node : nodeSet) {
				dfs(node, arr, '\0');
			}
			nodeSet = new ArrayList<>(arr);
			for (NfaNode node : nodeSet) {
				if (node.end) {
					return true;
				}
			}
		}
		ArrayList<NfaNode> tmp_nn = (ArrayList<NfaNode>) nodeSet.clone();
		nodeSet.clear();
		for (NfaNode node : tmp_nn) {
			dfs(node, nodeSet, '\0');
		}

		for (NfaNode node : nodeSet) {
			if (node.end) {
				return true;
			}
		}
		return false;
	}
	//该深搜主要用于消除'\0'边
	private void dfs(NfaNode node, ArrayList<NfaNode> nodeSet, char c) {
		if (node == null) {
			return;
		}
		ArrayList<NfaNode> nn = node.getStateTable().get(c);
		if (nn == null) {
			nodeSet.add(node);
			return;
		}
		//可能出现一条边为'\0'而另一条边为'char'，则该结点也应该加入集合。接收输入
		HashMap<Character, ArrayList<NfaNode>> hm = node.getStateTable();
		if (hm.keySet().size() >= 2) {
			nodeSet.add(node);
		}
		for (NfaNode node2 : nn) {
			dfs(node2, nodeSet, c);
		}
	}
	
	private ArrayList<NfaNode> moveState(ArrayList<NfaNode> nodeSet, char c){
		ArrayList<NfaNode> arr = new ArrayList<>();
		for(NfaNode node : nodeSet){
			ArrayList<NfaNode> tmp = node.getStateTable().get(c);
			if (tmp == null) {
				//若没有与c匹配的字符，则将start结点add，但保证集合中只有一个start结点
				if (!arr.contains(getStart())) {
					arr.add(getStart());
				}
			}else {
				for(NfaNode node2 : tmp){
					arr.add(node2);
				}
			}
		}
		return arr;
	}

	public int size() {
		return nfa.size();
	}
	
	private void checkStartNode() throws StartNodeException{
		int count = 0;
		for(NfaNode node : nfa){
			if (node.start) {
				count++;
				if (count >= 2) {
					throw new StartNodeException(getClass().toString());
				}
			}
		}
	}
	
	private void checkEndNode() throws EndNodeException{
		int count = 0;
		for(NfaNode node : nfa){
			if (node.end) {
				count++;
				if (count >= 2) {
					throw new EndNodeException(getClass().toString());
				}
			}
		}
	}
}

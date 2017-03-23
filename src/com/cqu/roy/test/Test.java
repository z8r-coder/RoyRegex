package com.cqu.roy.test;

import com.cqu.roy.regex.Dfa;
import com.cqu.roy.regex.Nfa;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Nfa nfa = test_case();
		Dfa dfa = new Dfa(nfa);
		System.out.println(nfa.match_("ab"));
		dfa.generaterDfa();
	}
	
	/*test a|b
	 * 单个匹配由两个状态组成*/
	public static Nfa test_case() {
		Nfa nfa1 = new Nfa();
		Nfa nfa2 = new Nfa();
		
		nfa1.addNodeToNfa("start");
		nfa1.addStart("start");
		nfa1.addNodeToNfa("end");
		nfa1.addEnd("end");
		
		nfa1.getStart().addMoveState('a', nfa1.getNode("end"));
		
		nfa2.addNodeToNfa("start");
		nfa2.addStart("start");
		nfa2.addNodeToNfa("end");
		nfa2.addEnd("end");
		
		nfa2.getStart().addMoveState('b', nfa2.getNode("end"));
		
		nfa1.connect(nfa2);
//		nfa1.and(nfa2);
		nfa1.closure_2();
		return nfa1;
	}
}

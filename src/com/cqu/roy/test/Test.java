package com.cqu.roy.test;

import com.cqu.roy.regex.Nfa;
import com.cqu.roy.regex.Re;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Nfa nfa = test_case();
//		Dfa dfa = new Dfa(nfa);
//		System.out.println(nfa.match_("ab"));
//		dfa.generaterDfa();
//		Parser p = new Parser("a|bd|wa");
//		p.resolveAST();
//		Nfa nfa = test_case();
//		System.out.println(nfa.match_("ab"));
		Re re = new Re();
		//联接和或运算符
		//re.compile("addsa|dwqqd|dwqqqdw|qwqe|ewq|qq");
		//测试括号运算符
//		re.compile("a(addsadsa)+a|Dwq|a(dqwa)aa|dsa*");
		re.compile("a(addsa)a");
		//测试闭包运算*+?
		re.compile("a");
		System.out.println();
		System.out.println(re.match("d"));
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
//		nfa1.closure_2();
		return nfa1;
	}
}

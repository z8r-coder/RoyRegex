package com.cqu.roy.regex;

import com.cqu.roy.exception.SymbolCollision;

/**
 * token包含字符[1-9a-zA-Z]
 * 符号包含()|+?*[]\
 * @author Roy
 * @date: 2017年3月26日  上午11:11:10
 * version:
 */
public class Token {
	private char c;//所含字符
	private boolean symbol;//是否是符号
	private boolean cc;
	public Token(Character c, boolean symbol, boolean cc) {
		// TODO Auto-generated constructor stub
		this.c = c;
		this.symbol = symbol;
		this.cc = cc;
	}
	
	private void check() throws SymbolCollision{
		if ((cc && symbol) || ((!cc) &&(!symbol))) {
			throw new SymbolCollision(getClass().toString());
		}
	}
	
}

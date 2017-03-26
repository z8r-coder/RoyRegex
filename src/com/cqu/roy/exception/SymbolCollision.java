package com.cqu.roy.exception;

/**
 * 符号和字符选项不能 同时为真
 * @author Roy
 * @date: 2017年3月26日  上午11:18:05
 * version:1.0.0
 */
public class SymbolCollision extends Exception{
	public SymbolCollision(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		System.out.println("Symbol and character options conflict");
	}
}

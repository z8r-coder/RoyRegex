package com.cqu.roy.exception;
/**
 * 
 * @author Roy
 * @date: 2017年3月21日  下午4:55:34
 * 确定化dfa，每接收输入，仅存在一条边
 */
public class UncertainException extends Exception{
	
	public UncertainException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		System.err.println("There multiple edge map input!");
		
	}
}

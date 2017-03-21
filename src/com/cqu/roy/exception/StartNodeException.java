package com.cqu.roy.exception;

/**
 * 
 * @author Roy
 * @date: 2017年3月21日  下午4:55:23
 * 检查每个集合中是否存在多个start结点
 */

public class StartNodeException extends Exception{
	
	public StartNodeException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		System.err.println("There are multiple start nodes");
	}
}

package com.cqu.roy.exception;
/**
 * 
 * @author Roy
 * @date: 2017年3月21日  下午4:55:05
 * 检查每个集合中是否存在多个end结点
 */

public class EndNodeException extends Exception{
	public EndNodeException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		System.err.println("There are multiple start nodes");
	}
}

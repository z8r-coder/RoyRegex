package com.cqu.roy.exception;

/**
 * 结点存在异常。如二元符需要匹配两个结点，但只存在一个结点的时候，抛出
 * 该异常！
 * @author Roy
 * @date: 2017年3月30日  下午8:57:45
 * version:
 */
public class NodeExistException extends Exception{
	public NodeExistException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		System.out.println("There are nodes exception!");
	}
}

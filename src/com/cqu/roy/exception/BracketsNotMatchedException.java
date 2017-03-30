package com.cqu.roy.exception;

public class BracketsNotMatchedException extends Exception{
	public BracketsNotMatchedException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		System.err.println("The bracket does not match");
	}
}

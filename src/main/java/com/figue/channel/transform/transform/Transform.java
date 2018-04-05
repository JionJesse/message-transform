package com.figue.channel.transform.transform;

public interface Transform<T> {
	/**
	 * 报文字符串转成报文对象
	 * @param input  报文字符串
	 * @param clazz  报文对象class
	 * @return 报文对象
	 */
	abstract T trans2Bean(String input,Class<T> clazz);
	
	/**
	 * 报文对象转成报文字符串
	 * @param input 报文对象
	 * @return 报文字符串
	 */
	abstract String trans2String(T input);
	
}

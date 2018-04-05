package com.figue.channel.transform.validate;

import java.util.ArrayList;
import java.util.List;

public class Errors {
	private List<String> errorMsgs = new ArrayList<String>();
	/**
	 * 添加错误消息
	 * @param errorMsg
	 */
	public void addErrorMsg(String errorMsg) {
		errorMsgs.add(errorMsg);
	}

	/**
	 * 是否存在错误
	 * @return
	 */
	public boolean hasError() {
		return errorMsgs.size() > 0;
	}

	/**
	 * 获取一条错误消息
	 * 多条错误小时时返回第一条
	 * 不存在时返回null
	 * @return
	 */
	public String getErrorMsg() {
		if (errorMsgs.size() > 0) {
			return errorMsgs.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取所有的错误消息集合
	 * 不存在时返回空结合
	 * @return
	 */
	public List<String> getErrorMsgs() {
		return errorMsgs;
	}
}

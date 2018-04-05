package com.figue.channel.transform.transform;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.figue.channel.transform.validate.Errors;
import com.figue.channel.transform.validate.Validator;


public abstract class TransformSupport<T> implements Transform<T>{
	protected List<Validator> validators = new ArrayList<Validator>();
	private String encoding = "UTF-8";
	/**
	 * 检验不通过是否抛出异常
	 */
	private boolean throwsException = false;
	
	public List<Validator> getValidators() {
		return validators;
	}

	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}
	
	public void addValidator(Validator validator) {
		this.validators.add(validator);
	}

	public boolean isThrowsException() {
		return throwsException;
	}

	public void setThrowsException(boolean throwsException) {
		this.throwsException = throwsException;
	}
	
	
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 字段校验
	 * @param annotations 字段的注解
	 * @param fieldName 字段名称
	 * @param fieldValue 字段值
	 * @param errors 存放校验异常结果
	 */
	protected void validateField(Field field, Object fieldValue, Errors errors) {
		if(validators!=null){
			for (Validator validator : validators) {
				validator.validate(field, fieldValue, errors);
			}
		}
	}
	
}

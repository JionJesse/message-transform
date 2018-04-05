package com.figue.channel.transform.validate.ext;

import java.lang.reflect.Field;

import com.figue.channel.transform.annotation.validate.Length;
import com.figue.channel.transform.annotation.validate.NotNull;
import com.figue.channel.transform.util.AccessBeanUtil;
import com.figue.channel.transform.validate.Errors;
import com.figue.channel.transform.validate.Validator;


public class NotNullAndLengthValidator implements Validator{

	@Override
	public void validate(Field field, Object fieldValue, Errors errors) {
		if(null!=field){
			if(field.isAnnotationPresent(NotNull.class)){
				if(null==fieldValue){
					errors.addErrorMsg("字段["+field.getName()+"]值为null");
				}
				
			}
			if(field.isAnnotationPresent(Length.class)){
				Length length = (Length) field.getAnnotation(Length.class);
				String value = AccessBeanUtil.convertFieldValueToStr(field, fieldValue);
				if(!(length.min()<=value.length()&&value.length()<=length.max())){
					if(value.length()<length.min()){
						errors.addErrorMsg("字段["+field.getName()+"]值的长度"+value.length()+"小于设置的最小长度"+length.min());
					}else if (value.length()>length.max()){
						errors.addErrorMsg("字段["+field.getName()+"]值长度"+value.length()+"大于设置的最大长度"+length.max());
					}
					
				}
				
			}
		}
	}


}

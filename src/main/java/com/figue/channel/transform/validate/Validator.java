package com.figue.channel.transform.validate;

import java.lang.reflect.Field;

public interface Validator {
	void validate( Field fieldName, Object fieldValue, Errors errors);
}

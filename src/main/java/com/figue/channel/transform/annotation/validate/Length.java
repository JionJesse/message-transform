package com.figue.channel.transform.annotation.validate;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Length {
	long max() default -1L;
	long min() default -1L;
}

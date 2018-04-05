/**
 * 
 */
package com.figue.channel.transform.annotation.parse;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BeanClass {
	Class<?> value();
}

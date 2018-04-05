/**
 * 
 */
package com.figue.channel.transform.annotation.parse.json;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <h2>简述</h2>
 *     <ol>Json组包解包配置.</ol>
 * <h2>功能描述</h2>
 *     <ol>1. 可以在类或者属性中使用注解.</ol>
 *     <ol>2. 使用"/"分割不同层级的节点.</ol>
 * <h2>修改历史</h2>
 *     <ol>无.</ol>
 * </p>
 * @author figue
 * @time 2017-10-31
 * @version 1.0
 */
@Target({TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JsonPath {
	String value() default "";
}

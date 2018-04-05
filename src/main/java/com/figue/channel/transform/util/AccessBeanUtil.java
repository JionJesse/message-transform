package com.figue.channel.transform.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessBeanUtil {

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>类实例化.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param clazz
	 * @return 
	 */
	public static <T> T instantiateClass(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new AccessBeanException("类实例化异常[" + clazz + "]", e);
		}
	}

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>获取类的所有属性.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param clazz
	 * @return 
	 */
	public static <T> List<Field> findAllFields(Class<T> clazz) {
		//Field[] fields = clazz.getFields();
		Field[] declaredFields = clazz.getDeclaredFields();

		//List<Field> fieldList = Arrays.asList(fields);
		List<Field> declaredFieldList = Arrays.asList(declaredFields);

		List<Field> allFieldList = new ArrayList<Field>();
		//allFieldList.addAll(fieldList);
		allFieldList.addAll(declaredFieldList);

		return allFieldList;
	}

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>根据名称获取属性.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>可以获取公有也可以获取私有属性，不存在返回null.</ol>
	 * </p>
	 * @param clazz
	 * @param fieldName
	 * @return 
	 */
	public static <T> Field findField(Class<T> clazz, String fieldName) {
		Field field = null;
		try {
			field = clazz.getField(fieldName);
		} catch (Exception e) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e1) {
				field = null;
			}
		}

		return field;
	}

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>获取类注解.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param clazz
	 * @return 
	 */
	public static List<Annotation> findAnnotations(Class<?> clazz) {
		Annotation[] annotations = clazz.getAnnotations();
		return Arrays.asList(annotations);
	}

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>获取字段注解.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param field
	 * @return 
	 */
	public static List<Annotation> findAnnotations(Field field) {
		Annotation[] annotations = field.getAnnotations();
		return Arrays.asList(annotations);
	}

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>获取类指定类型的注解.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param clazz
	 * @param annotationClass
	 * @return 
	 */
	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationClass) {
		return clazz.getAnnotation(annotationClass);
	}

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>获取字段指定类型的注解.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param field
	 * @param annotationClass
	 * @return 
	 */
	public static <A extends Annotation> A findAnnotation(Field field, Class<A> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>设置字段值.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param target
	 * @param field
	 * @param value 
	 */
	public static <T> void setFieldValue(T target, Field field, Object value) {
		try {
			String fieldName = field.getName();
			Method method = null;
			try {
				method = target.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
			} catch (NoSuchMethodException e) {
				method = null;
			} catch (SecurityException e) {
				method = null;
			}
			Object setValue = convertFieldValue(field, value);
			if (method == null) {
				if (Modifier.isPublic(field.getModifiers())) {
					field.set(target, setValue);
				} else {
					throw new AccessBeanException("属性[" + field + "]为非共有或者不存在共有setter方法");
				}
			} else {
				if (setValue == null && field.getType().isPrimitive()) {
					// nothing
				} else {
					method.invoke(target, setValue);
				}
			}
		} catch (Exception e) {
			throw new AccessBeanException("属性赋值出错", e);
		}
	}

	public static Object getFieldValue(Field field, Object object) {
		try {
			String fieldName = field.getName();
			Method method = null;
			try {
				method = object.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
			} catch (NoSuchMethodException e) {
				method = null;
			} catch (SecurityException e) {
				method = null;
			}
			if (method == null) {
				if (Modifier.isPublic(field.getModifiers())) {
					return field.get(object);
				} else {
					throw new AccessBeanException("属性[" + field + "]为非共有或者不存在共有getter方法");
				}
			} else {
					Object value = method.invoke(object, null);
					return value;				
			}
		} catch (Exception e) {
			throw new AccessBeanException("属性获取值出错", e);
		}
	}

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 *     <ol>将value值转换为属性可接收的类型.</ol>
	 * <h2>功能描述</h2>
	 *     <ol>无.</ol>
	 * </p>
	 * @param field
	 * @param value
	 * @return 
	 */
	public static Object convertFieldValue(Field field, Object value) {
		Class<?> targetClass = field.getType();
		if (value == null) {
			return value;
		} else if (targetClass.isInstance(value)) {
			return value;
		} else if (String.class == targetClass) {
			return value.toString();
		} else if ("".equals(value.toString().trim())) {
			// 如果不是字符串类型但是传入的数据为空字符串直接返回null
			return null;
		} else if (Boolean.class == targetClass || targetClass.getName().equalsIgnoreCase("boolean")) {
			return Boolean.valueOf(value.toString());
		} else if (Character.class == targetClass || targetClass.getName().equalsIgnoreCase("char")) {
			return Character.valueOf(value.toString().charAt(0));
		} else if (Byte.class == targetClass || targetClass.getName().equalsIgnoreCase("Byte")) {
			return Byte.valueOf(value.toString());
		} else if (Short.class == targetClass || targetClass.getName().equalsIgnoreCase("Short")) {
			return Short.valueOf(value.toString());
		} else if (Integer.class == targetClass || targetClass.getName().equalsIgnoreCase("Integer")) {
			return Integer.valueOf(value.toString());
		} else if (Long.class == targetClass || targetClass.getName().equalsIgnoreCase("Long")) {
			return Long.valueOf(value.toString());
		} else if (BigInteger.class == targetClass) {
			return BigInteger.valueOf(Long.valueOf(value.toString()));
		} else if (Float.class == targetClass || targetClass.getName().equalsIgnoreCase("Float")) {
			return Float.valueOf(value.toString());
		} else if (Double.class == targetClass || targetClass.getName().equalsIgnoreCase("Double")) {
			return Double.valueOf(value.toString());
		} else if (BigDecimal.class == targetClass) {
			return new BigDecimal(value.toString());
		} else {
			throw new AccessBeanException(value.getClass() + "转换为" + targetClass + "不被支持");
		}
	}

	
	public static String convertFieldValueToStr(Field field, Object value) {
		Class<?> targetClass = field.getType();
		if (value == null) {
			return "";
		} else if (String.class == targetClass) {
			return value.toString();
		} else if ("".equals(value.toString().trim())) {
			return "";
		} else if (Boolean.class == targetClass || targetClass.getName().equalsIgnoreCase("boolean")) {
			return String.valueOf(value);
		} else if (Character.class == targetClass || targetClass.getName().equalsIgnoreCase("char")) {
			return String.valueOf(value);
		} else if (Byte.class == targetClass || targetClass.getName().equalsIgnoreCase("Byte")) {
			byte valueT[] = (byte[])value;
			return new String(valueT);
		} else if (Short.class == targetClass || targetClass.getName().equalsIgnoreCase("Short")) {
			return String.valueOf(value);
		} else if (Integer.class == targetClass || targetClass.getName().equalsIgnoreCase("Integer")) {
			return String.valueOf(value);
		} else if (Long.class == targetClass || targetClass.getName().equalsIgnoreCase("Long")) {
			return String.valueOf(value);
		} else if (BigInteger.class == targetClass) {
			return ((BigInteger) value).toString(10);
		} else if (Float.class == targetClass || targetClass.getName().equalsIgnoreCase("Float")) {
			return String.valueOf(value);
		} else if (Double.class == targetClass || targetClass.getName().equalsIgnoreCase("Double")) {
			return String.valueOf(value);
		} else if (BigDecimal.class == targetClass) {
			return value.toString();
		} else {
			throw new AccessBeanException(value.getClass() + "转换为" + targetClass + "不被支持");
		}
	}

}

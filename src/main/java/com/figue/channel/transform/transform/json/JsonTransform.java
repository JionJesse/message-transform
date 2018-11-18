package com.figue.channel.transform.transform.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.json.JsonPath;
import com.figue.channel.transform.transform.TransformException;
import com.figue.channel.transform.transform.TransformSupport;
import com.figue.channel.transform.util.AccessBeanUtil;
import com.figue.channel.transform.validate.Errors;


/**
 * <p>
 * <h2>简述</h2>
 *     <ol>Json报文的格式化与反格式化</ol>
 * <h2>功能描述</h2>
 *     <ol>将对象转成Json报文；将Json报文转成对象</ol>
 * <h2>修改历史</h2>
 *     <ol>无.</ol>
 * </p>
 * @author wangyf10
 * @time 2017-11-02
 * @version 1.0
 */
public class JsonTransform<T> extends TransformSupport<T>{
	protected final Logger logger = Logger.getLogger("JsonTransform");

	@Override
	public T trans2Bean(String input, Class<T> clazz) {
		logger.info("json报文转对象开始...");
		logger.info("输入报文:\n" + input);
		
		//添加字段验证
		Errors errors = new Errors();	
		
		T bean = null;
		try {
			bean = clazz.newInstance();
			JSONObject parentjSONObject = JSON.parseObject(input);
			jSONObjectToBean( parentjSONObject,true, bean, errors);
			if(!errors.getErrorMsgs().isEmpty()){
				for(String errorStr:errors.getErrorMsgs()){
					logger.info("校验结果:\n" + errorStr);
				}
				if(isThrowsException()){
					throw new TransformException("报文格式过程中部分字段校验不通过");
				}
			}
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("报文格式化类" + clazz.getName() + "实例化报异常");
			throw new TransformException("报文格式化类" + clazz.getName() + "实例化报异常", e);
		}

	}

	
	@Override
	public String trans2String(T input) {
		logger.info("对象转报文开始...");
	    logger.info("输入对象:\n" + input);
		// 格式化对象得到报文字符串
		try {
			JSONObject root = new JSONObject();
			beanToStr(input, true,root);
			
			String xmlStr = JSON.toJSONString(root);
			logger.info("对象格式化成报文如下:\n" + xmlStr);
			return xmlStr;
		} catch (Exception e) {
			throw new TransformException("对象格式化成报文异常",e);
		}
	}
	
	/**
	 * 递归遍历json对象生成报文对象
	 * @param currentJSONObject 当前json对象
	 * @param isRoot 是否是根节点对象
	 * @param currentobject 当前报文对象
	 * @param errors 异常信息收集
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void jSONObjectToBean(Object currentJSONObject, boolean isRoot, Object currentobject,Errors errors) throws InstantiationException, IllegalAccessException {

		// 模板对象必须有JsonPath注解,或者是根对象则不需要注解
		if (currentobject.getClass().isAnnotationPresent(JsonPath.class) || isRoot) {
			// 遍历对象内部的变量
			List<Field> fields = AccessBeanUtil.findAllFields(currentobject.getClass());
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				// 只解析含有注解的字段属性
				if (field.isAnnotationPresent(JsonPath.class) || field.isAnnotationPresent(BeanClass.class)) {
					Class fieldClass = field.getType();
					if (fieldClass == java.util.List.class) {// 集合字段
						BeanClass beanClass = null;
						String jsonPathValueList = null;
						if (field.isAnnotationPresent(BeanClass.class) && field.isAnnotationPresent(JsonPath.class)) {// 集合字段必须有JsonPath
																														// BeanClass注解才解析
							beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							JsonPath jsonPathT = (JsonPath) field.getAnnotation(JsonPath.class);
							if (null == beanClass.value()) {
								throw new TransformException(field.getName() + "未指定BeanClass注解的value");
							}
							jsonPathValueList = jsonPathT.value();
							if ("".equals(jsonPathValueList)) {
								throw new TransformException(field.getName() + "未指定JsonPath注解的value");
							}
						} else {
							throw new TransformException(field.getName() + " 集合字段必须有JsonPath BeanClass注解才解析");
						}

						Map parent = (Map) currentJSONObject;
						Object object = parent.get(jsonPathValueList);
						if (null != object) {
							if (!(object instanceof List)) {// json对象中的数据类型必须是list
								throw new TransformException(field.getName() + " 集合字段在json对象中不是list类型");
							}
							List listT = (List) object;
							List listObj = new ArrayList();
							for (int m = 0; m < listT.size(); m++) {
								Object subBean = beanClass.value().newInstance();
								listObj.add(subBean);
								jSONObjectToBean(listT.get(m), false, subBean, errors);
							}
							AccessBeanUtil.setFieldValue(currentobject, field, listObj);
						}
						

					} else {// 非集合字段
						if (field.isAnnotationPresent(BeanClass.class)) {// 非集合字段中的javabean字段

							BeanClass beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							if (null == beanClass.value()) {
								throw new TransformException(field.getName() + "未指定BeanClass注解的value");
							}
							Object subBean = beanClass.value().newInstance();

							JsonPath subBeanJsonPath = (JsonPath) subBean.getClass().getAnnotation(JsonPath.class);
							if ("".equals(subBeanJsonPath.value())) {
								throw new TransformException(subBean.getClass() + "未指定JsonPath注解的value");
							}

							Map parent = (Map) currentJSONObject;
							jSONObjectToBean(parent.get(subBeanJsonPath.value()), false, subBean, errors);
							AccessBeanUtil.setFieldValue(currentobject, field, subBean);

						} else {// 非集合字段中的普通字段
							JsonPath fieldJsonPath = (JsonPath) field.getAnnotation(JsonPath.class);
							String key = fieldJsonPath.value();
							if ("".equals(key)) {
								throw new TransformException(currentobject.getClass().getName() + "." + field.getName() + "未指定JsonPath注解的value");
							}
							if (currentJSONObject instanceof Map) {
								Map parent = (Map) currentJSONObject;
								Object value = parent.get(key);
											
								//赋值
								AccessBeanUtil.setFieldValue(currentobject, field, value);
								
								//校验
								validateField( field, AccessBeanUtil.getFieldValue(field, currentobject),  errors);
									logger.info("解析完成-->" + currentobject.getClass().getName() + "." + field.getName()
											+ ",JsonPath:" + key + ",值:" + value + ";");
							}
						}
					}
				}
			}
		}
	}
	
	private void beanToStr(Object object, boolean isRoot,JSONObject currentJSONObject) throws InstantiationException, IllegalAccessException {
		//object对象必须有XmlPath注解，否则不解析
		
		if ((object!=null)&&(object.getClass().isAnnotationPresent(JsonPath.class) || isRoot)) {

			//遍历object对象内部的字段
			List<Field> fields = AccessBeanUtil.findAllFields(object.getClass());
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				// 只解析含有注解的字段属性
				if (field.isAnnotationPresent(JsonPath.class) || field.isAnnotationPresent(BeanClass.class)) {
					Class fieldClass = field.getType();
					if (fieldClass == java.util.List.class) {// 集合字段
						BeanClass beanClass = null;
						String jsonPathValueList = null;
						if (field.isAnnotationPresent(BeanClass.class) && field.isAnnotationPresent(JsonPath.class)) {// 集合字段必须有JsonPath BeanClass注解才解析
							beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							JsonPath jsonPathT = (JsonPath) field.getAnnotation(JsonPath.class);
							if (null == beanClass.value()) {
								throw new TransformException(field.getName() + "未指定BeanClass注解的value");
							}
							jsonPathValueList = jsonPathT.value();
							if ("".equals(jsonPathValueList)) {
								throw new TransformException(field.getName() + "未指定JsonPath注解的value");
							}
						} else {
							throw new TransformException(field.getName() + " 集合字段必须有JsonPath BeanClass注解才解析");
						}

						Object listT =  AccessBeanUtil.getFieldValue(field, object);
						
						if(null!=listT){						
							//System.out.println(listT.getClass());
							List listBean = (List) listT;
							JSONArray listBeanObject = new JSONArray();
							currentJSONObject.put(jsonPathValueList,listBeanObject);
							
							//获取list属性中的对象，遍历乡下解析
							
							if(null!=listBean&&!listBean.isEmpty()){
								for (int m = 0; m < listBean.size(); m++) {	
									JSONObject jSONObjectT = new JSONObject();
									listBeanObject.add(jSONObjectT);
									beanToStr(listBean.get(m),false,jSONObjectT);
								}
							}
						}
						
					} else {// 非集合字段
						if (field.isAnnotationPresent(BeanClass.class)) {//javabean字段
							BeanClass beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							if (null == beanClass.value()) {
								throw new TransformException(field.getName() + "未指定BeanClass注解的value");
							}
							Object subBean = beanClass.value().newInstance();

							JsonPath subBeanJsonPath = (JsonPath) subBean.getClass().getAnnotation(JsonPath.class);
							if (subBeanJsonPath==null||"".equals(subBeanJsonPath.value())) {
								throw new TransformException(subBean.getClass().getName() + "未指定JsonPath注解的value");
							}

							JSONObject beanObject = new JSONObject();
							
							Object beanValue =  AccessBeanUtil.getFieldValue(field, object);
							if(null!=beanValue){
								currentJSONObject.put(subBeanJsonPath.value(),beanObject);
								beanToStr(beanValue,false,beanObject);								
							}
							
						} else {//普通字段
							
							JsonPath fieldJsonPath = (JsonPath) field.getAnnotation(JsonPath.class);
							String key = fieldJsonPath.value();
							if ("".equals(key)) {
								throw new TransformException(object.getClass().getName() + "." + field.getName() + "未指定JsonPath注解的value");
							}
							Object value =  AccessBeanUtil.getFieldValue(field, object);
							currentJSONObject.put(key,value);
							
							logger.info("格式化完成-->" + object.getClass().getName() + "." + field.getName()
										+ ",JsonPath:" + key + ",值:" + value + ";");

						}
					}

				}

			}

		}

	}
}

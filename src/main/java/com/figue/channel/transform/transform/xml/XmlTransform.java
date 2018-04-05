package com.figue.channel.transform.transform.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.xml.XmlPath;
import com.figue.channel.transform.transform.TransformException;
import com.figue.channel.transform.transform.TransformSupport;
import com.figue.channel.transform.util.AccessBeanUtil;
import com.figue.channel.transform.util.Cons;
import com.figue.channel.transform.validate.Errors;

/**
 * <p>
 * <h2>简述</h2>
 *     <ol>XML报文的格式化与反格式化</ol>
 * <h2>功能描述</h2>
 *     <ol>将对象转成xml报文；将xml报文转成对象</ol>
 * <h2>修改历史</h2>
 *     <ol>无.</ol>
 * </p>
 * @author figue
 * @time 2017-10-31
 * @version 1.0
 */
public class XmlTransform<T> extends TransformSupport<T> {
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public T trans2Bean(String input,Class<T> clazz) {
		logger.debug("xml报文转对象开始...");
		if (logger.isDebugEnabled()) {
			logger.debug("输入报文:\n" + input);
		}
		// 解析
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(new ByteArrayInputStream(input.getBytes())));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("xml字符串创建Document文档异常,字符串如下:/n" + input, e);
			throw new TransformException("xml字符串创建Document文档异常,字符串如下:/n" + input, e);
		}
		
		//添加字段验证
		Errors errors = new Errors();
			
		T bean = null;
		try {
			bean = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报文格式化类" + clazz.getName() + "实例化报异常", e);
			throw new TransformException("报文格式化类" + clazz.getName() + "实例化报异常", e);
		}

		try {
			docToBean(doc,bean,null,false,errors);
		} catch (Exception e) {
			throw new TransformException("报文格式类" + clazz.getName() + "实例化报异常", e);
		}
		
		if(!errors.getErrorMsgs().isEmpty()){
			for(String errorStr:errors.getErrorMsgs()){
				logger.error("校验结果:\n" + errorStr);
			}
			if(isThrowsException()){
				throw new TransformException("报文格式过程中部分字段校验不通过");
			}
		}
		return bean;
		
	}
	
	public T trans2Bean(File file,Class<T> clazz) {
		logger.debug("报文转对象开始...");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new FileInputStream(file));
			if (logger.isDebugEnabled()) {
				logger.debug("输入报文:\n" + file.getAbsolutePath());
			}
		} catch (Exception e) {
			throw new TransformException("xml字符串创建Document文档异常," + file.getAbsolutePath(), e);
		}

		T bean = null;
		try {
			bean = clazz.newInstance();
		} catch (Exception e) {
			throw new TransformException("报文格式化类" + clazz.getName() + "实例化报异常", e);
		}
		Errors errors = new Errors();
		try {
			
			docToBean(doc,bean,null,false,errors);
		} catch (Exception e) {
			throw new TransformException("报文格式类" + clazz.getName() + "实例化报异常", e);
		}
		
		if(!errors.getErrorMsgs().isEmpty()){
			for(String errorStr:errors.getErrorMsgs()){
				logger.error("校验结果:\n" + errorStr);
			}
			if(isThrowsException()){
				throw new TransformException("报文格式过程中部分字段校验不通过");
			}
		}
		return bean;
		
	}
	
	/**
	 * 递归遍历Document文本对象生成报文对象
	 * @param doc 文本对象
	 * @param object 报文对象
	 * @param parentXpath 报文对象xpath
	 * @param beanOfList 是否是list中的对象
	 * @param errors 存放校验信息异常
	 * @throws XPathExpressionException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void docToBean(Document doc, Object object,String parentXpath ,boolean beanOfList,Errors errors) throws XPathExpressionException, InstantiationException, IllegalAccessException {
		
		//模板对象必须有XmlPath注解
		if(object.getClass().isAnnotationPresent(XmlPath.class)){
					
			String parentXpathT = "";//父节点xpath
			if(null==parentXpath||"".equals(parentXpath)){
				parentXpathT = "";
			}else{
				parentXpathT = (parentXpath);
			}
			
			String selfXpath = "";//节点自身xpath
			XmlPath xmlPath = (XmlPath) object.getClass().getAnnotation(XmlPath.class);
			String xmlPathValue = fixPath(xmlPath.value());
			if(null==xmlPathValue||"".equals(xmlPathValue)){
				throw new TransformException(object.getClass()+"未指定XmlPath注解的value");
			}else{
				if(!beanOfList){//list的子对象，xpath已经提前传入
					selfXpath = parentXpathT +Cons.sprit+ xmlPathValue;
				}else{
					selfXpath = parentXpathT ;
				}
			}
			
			//遍历对象内部的变量
			List<Field> fields =  AccessBeanUtil.findAllFields(object.getClass());
			for (int i = 0; i < fields.size(); i++) {			
				Field field = fields.get(i);
				//只解析含有注解的字段属性
				if (field.isAnnotationPresent(XmlPath.class)||field.isAnnotationPresent(BeanClass.class)) {
					Class fieldClass = field.getType();
					if(fieldClass == java.util.List.class){//集合字段
						System.out.println(fieldClass);
						BeanClass beanClass = null;
						if(field.isAnnotationPresent(BeanClass.class)){//集合字段并且有BeanClass注解才解析							
							beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							if(null==beanClass.value()){
								throw new TransformException(field.getName()+"未指定BeanClass注解的value");
							}
						}else{
							throw new TransformException(field.getName()+"未指定BeanClass注解的value");
						}
						
						//list的xpath
						String fieldXmlPathRel = selfXpath;
						XmlPath fieldXmlPath = (XmlPath) field.getAnnotation(XmlPath.class);
						if(null!=fieldXmlPath){
							String fieldXmlPathT = fixPath(fieldXmlPath.value());						
							if(!"".equals(fieldXmlPathT)){
								fieldXmlPathRel = fieldXmlPathRel + Cons.sprit + fieldXmlPathT;
							}						
						}
						
						//list中的javabean的xpath
						Class subBeanClass = beanClass.value();
						XmlPath subBeanXmlPath = (XmlPath) subBeanClass.getAnnotation(XmlPath.class);
						String subBeanXmlPathT = fixPath(subBeanXmlPath.value());
						if(!"".equals(subBeanXmlPathT)){
							fieldXmlPathRel = fieldXmlPathRel + Cons.sprit + subBeanXmlPathT;
						}
												
						NodeList nodeList = selectNodes(fieldXmlPathRel, doc);
						//System.out.println(">>>>"+nodeList.getLength());
						
						List listT = new ArrayList();
						for(int m=0;m<nodeList.getLength();m++){
							Object subBean = subBeanClass.newInstance();
							listT.add(subBean);
							docToBean(doc,subBean,fieldXmlPathRel+"["+(m+1)+"]" ,true,errors);							
						}
						AccessBeanUtil.setFieldValue(object, field, listT);
						
					}else{//非集合字段
						if (field.isAnnotationPresent(BeanClass.class)) {//非集合字段中的javabean字段
							String parentbeanXmlPath = selfXpath;
							
							XmlPath fieldXmlPath = (XmlPath) field.getAnnotation(XmlPath.class);
							if(null!=fieldXmlPath){
								String beanXmlPath = fixPath(fieldXmlPath.value());							
								if(!"".equals(beanXmlPath)){
									parentbeanXmlPath = parentbeanXmlPath + Cons.sprit + beanXmlPath;
								}
							}
							BeanClass beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							if(null==beanClass.value()){
								throw new TransformException(field.getName()+"未指定BeanClass注解的value");
							}
							Object subBean = beanClass.value().newInstance();
							docToBean( doc, subBean,parentbeanXmlPath ,false,errors);
							AccessBeanUtil.setFieldValue(object, field, subBean);
							
						}else{//非集合字段中的普通字段
							XmlPath fieldXmlPath = (XmlPath) field.getAnnotation(XmlPath.class);
							String fieldXmlPathT = fixPath(fieldXmlPath.value());						
							
							String fieldXmlPathRel = selfXpath ;
							
							if(!"".equals(fieldXmlPathT)){
								fieldXmlPathRel = fieldXmlPathRel + Cons.sprit + fieldXmlPathT;
							}
							
									
							String valueAttr = fieldXmlPath.valueAttr();
							if (logger.isDebugEnabled()) {
								logger.debug("解析到-->" + fieldXmlPathRel+ " " + valueAttr);
							}
							Node node = selectSingleNode(fieldXmlPathRel, doc);
							if(null==node){
								throw new TransformException(fieldXmlPathRel+"在xml中无对应的节点数据");
							}
							if(valueAttr.startsWith(Cons.ATTR_IDENT)){//通过属性获取值
								Element nodeT = (Element) node;
								Attr attr = nodeT.getAttributeNode(valueAttr.substring(1, valueAttr.length()));
								String value = null;
								if(null!=attr){
									value = attr.getValue();
								}
								
								if (logger.isDebugEnabled()) {
									logger.debug("属性:"+field.getName() + ",值:" + value + ",路径:" + fieldXmlPathRel + " " + valueAttr);
								}
								
								AccessBeanUtil.setFieldValue(object, field, value);
								//校验
								validateField(field, AccessBeanUtil.getFieldValue(field, object),  errors);
							}else{//通过xpath直接获取值	
								Element nodeT = (Element) node;
								Node childrenText = nodeT.getFirstChild();
								String value = null;
								if(null!=childrenText){
									value = node.getTextContent().trim();
								}
								if (logger.isDebugEnabled()) {
									logger.debug("属性:"+field.getName() + ",值:" + value + ",路径:" + fieldXmlPathRel + " " + valueAttr);
								}
								
								AccessBeanUtil.setFieldValue(object, field, value);
								//校验
								validateField(field, AccessBeanUtil.getFieldValue(field, object),  errors);		
							}
													
						}
					}
					
				}

			}

		}
	}

	@Override
	public String trans2String(T input) {
		logger.debug("对象转报文开始...");
		if (logger.isDebugEnabled()) {
			logger.debug("输入对象:\n" + input);
		}
		//格式化对象得到报文字符串
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			beanToStr(input, doc, doc, null, false, null);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty("encoding", getEncoding());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			t.transform(new DOMSource(doc), new StreamResult(bos));
			
			String xmlStr = bos.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("对象格式化成报文如下:\n" + xmlStr);
			}
			return xmlStr;
		} catch (Exception e) {
			throw new TransformException("对象格式化成报文异常",e);
		}
	}
	
	/**
	 * 递归遍历报文对象生成Document文本对象，用于生成报文字符串
	 * @param object 报文对象
	 * @param doc Document文本对象
	 * @param parent Document文本对象的父节点
	 * @param parentXpath 父节点的xpath
	 * @param beanOfList 是否是list中的对象
	 * @param errors 存放校验信息异常
	 * @throws XPathExpressionException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void beanToStr(Object object, Node doc, Node parent, String parentXpath, boolean beanOfList,
			Errors errors) throws XPathExpressionException, InstantiationException, IllegalAccessException {
		//object对象必须有XmlPath注解，否则不解析
		if (null!=object&&object.getClass().isAnnotationPresent(XmlPath.class)) {

			String parentXpathT =(null == parentXpath || "".equals(parentXpath))? "":parentXpath;//父节点xpath
			
			String selfXpath = "";//节点自身xpath
			XmlPath xmlPath = (XmlPath) object.getClass().getAnnotation(XmlPath.class);
			String xmlPathValue = fixPath(xmlPath.value());
			if (null == xmlPathValue || "".equals(xmlPathValue)) {
				throw new TransformException(object.getClass() + "未指定XmlPath注解的value");
			} else {
				if (!beanOfList) {
					selfXpath = parentXpathT + Cons.sprit + xmlPathValue;
				} else {//如果object参数是list的子对象，xpath已经提前传入
					selfXpath = parentXpathT;
				}
			}

			//新增object对象自身xpath对应的节点
			Element selfNode = createNode( doc, parent,xmlPathValue);

			//遍历object对象内部的字段
			List<Field> fields = AccessBeanUtil.findAllFields(object.getClass());
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				// 只解析含有注解的字段属性
				if (field.isAnnotationPresent(XmlPath.class) || field.isAnnotationPresent(BeanClass.class)) {
					Class fieldClass = field.getType();
					if (fieldClass == java.util.List.class) {// 集合字段
						BeanClass beanClass = null;
						if (field.isAnnotationPresent(BeanClass.class)) {// 集合字段并且有BeanClass注解才解析
							beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							if (null == beanClass.value()) {
								throw new TransformException(field.getName() + "未指定BeanClass注解的value");
							}
						} else {
							throw new TransformException(field.getName() + "未指定BeanClass注解的value");
						}

						Element listNode = null;
						//list属性的xpath						
						String fieldXmlPathRel = selfXpath;
						XmlPath fieldXmlPath = (XmlPath) field.getAnnotation(XmlPath.class);
						if (null != fieldXmlPath) {
							String fieldXmlPathT = fixPath(fieldXmlPath.value());
							if (!"".equals(fieldXmlPathT)) {
								fieldXmlPathRel = fieldXmlPathRel + Cons.sprit + fieldXmlPathT;
								listNode = createNode( doc, selfNode,fieldXmlPathT);//创建list属性自身xpath对应的节点
							}
						}
						if(null==listNode){//无xpath则默认父类的节点为自身节点，便于乡下遍历
							listNode = selfNode;
						}
						
						
						Class subBeanClass = beanClass.value();
						XmlPath subBeanXmlPath = (XmlPath) subBeanClass.getAnnotation(XmlPath.class);
						//获取list属性中的javabean对应的xpath
						String subBeanXmlPathT = fixPath(subBeanXmlPath.value());
						if(!"".equals(subBeanXmlPathT)){
							fieldXmlPathRel = fieldXmlPathRel + Cons.sprit + subBeanXmlPathT;
						}
						
						//获取list属性中的对象，遍历乡下解析
						List listBean = (List) AccessBeanUtil.getFieldValue(field, object);						
						if(null!=listBean&&!listBean.isEmpty()){
							for (int m = 0; m < listBean.size(); m++) {								
								beanToStr( listBean.get(m),  doc, listNode, fieldXmlPathRel + "[" + (m + 1) + "]",true,errors);
							}
						}
						
					} else {// 非集合字段
						if (field.isAnnotationPresent(BeanClass.class)) {//javabean字段
							String parentbeanXmlPath = selfXpath;
							//javabean字段的xpath
							XmlPath fieldXmlPath = (XmlPath) field.getAnnotation(XmlPath.class);
							Element beanNode = null;
							if (null != fieldXmlPath) {
								String beanXmlPath = fixPath(fieldXmlPath.value());
								if (!"".equals(beanXmlPath)) {
									parentbeanXmlPath = parentbeanXmlPath + Cons.sprit + beanXmlPath;
									beanNode = createNode( doc, selfNode,beanXmlPath);
								}
							}
							BeanClass beanClass = (BeanClass) field.getAnnotation(BeanClass.class);
							if (null == beanClass.value()) {
								throw new TransformException(field.getName() + "未指定BeanClass注解的value");
							}
							if(null==beanNode){
								beanNode = selfNode;
							}
							beanToStr( AccessBeanUtil.getFieldValue(field, object),  doc, beanNode, parentbeanXmlPath,false,errors);
						} else {//普通字段
							XmlPath fieldXmlPath = (XmlPath) field.getAnnotation(XmlPath.class);
							//普通字段自身xpath
							String fieldXmlPathT = fixPath(fieldXmlPath.value());
							Element fieldNode = createNode( doc, selfNode,fieldXmlPathT);
							if(null==fieldNode){
								fieldNode = selfNode;
							}
							
							//普通字段绝对xpath，便于输出日志
							String fieldXmlPathRel = selfXpath;

							if (!"".equals(fieldXmlPathT)) {
								fieldXmlPathRel = fieldXmlPathRel + Cons.sprit + fieldXmlPathT;
							}

							//普通字段的属性配置
							String valueAttr = fieldXmlPath.valueAttr();
							if (logger.isDebugEnabled()) {
								logger.debug("格式化到-->" + fieldXmlPathRel+ " " + valueAttr);
							}
							
							Object value = AccessBeanUtil.getFieldValue(field, object);
							if(null!=value){
								if (valueAttr.startsWith(Cons.ATTR_IDENT)) {// 通过属性赋值							
									fieldNode.setAttribute(valueAttr.substring(1),AccessBeanUtil.convertFieldValueToStr(field,value));
								} else {// 通过节点赋值
									fieldNode.setTextContent(AccessBeanUtil.convertFieldValueToStr(field,value));
								}							
							}
							if (logger.isDebugEnabled()) {
								logger.debug(
										"属性:"+field.getName() + ",值:" + value + ",路径:" + fieldXmlPathRel + " " + valueAttr);
							}
						}
					}

				}

			}

		}

	}

	/**
	 * 根据xpath获取节点
	 * @param express xpath表达式
	 * @param doc xml文本对象
	 * @return 目标节点
	 * @throws XPathExpressionException
	 */
	private Node selectSingleNode(String express, Document doc) throws XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Node node = (Node) xpath.evaluate(express, doc, XPathConstants.NODE);
		return node;
	}
	
	/**
	 * 根据xpath获取节点集合
	 * @param express xpath表达式
	 * @param doc xml文本对象
	 * @return 目标节点
	 * @throws XPathExpressionException
	 */
	private NodeList selectNodes(String express,Document doc) throws XPathExpressionException {		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList  nodeList = (NodeList) xpath.evaluate(express, doc, XPathConstants.NODESET);
		return nodeList;
	}
	
	/**
	 * 格式化节点路径
	 * @param xpath 节点路径
	 * @return
	 */
	private String fixPath(String xpath){
		if(null==xpath){
			return "";
		}
		if(xpath.startsWith(Cons.sprit)){//去除xpath前后的/
			xpath = xpath.substring(1, xpath.length());
		}
		if(xpath.endsWith(Cons.sprit)){
			xpath = xpath.substring(0, xpath.length()-1);
		}
		return xpath;
	}
	
	/**
	 * 分割节点路径
	 * @param xpath 节点路径
	 * @return
	 */
	private String[] splitPath(String xpath){
		if(null==xpath){
			return null;
		}		
		return xpath.split(Cons.sprit);
	}
	
	/**
	 * 根据节点路径创建xml节点
	 * @param doc 文本对象
	 * @param parent 父节点
	 * @param xpath 节点路径
	 * @return 节点
	 * @throws XPathExpressionException
	 */
	protected Element createNode(Node doc,Node parent,String xpath) throws XPathExpressionException{
		
		Node parentT = parent;
		if(null==xpath||"".equals(xpath)){
			return null;
		}
		String[] paths = splitPath(xpath);
		if(null==paths){
			return null;
		}
		Element node = null;
		for (int i = 0; i < paths.length; i++) {
			
			if (paths[i].contains("[@")) {// 含有属性的路径，创建节点后，存放属性
				String pathRel = paths[i].substring(0, paths[i].indexOf("[@"));
				node = ((Document) doc).createElement(pathRel);
				parentT.appendChild(node);
				parentT = node;
				// 获取属性键值对，存入节点
				Map<String, String> KV = getAttrKeyValue(paths[i]);
				if (!KV.isEmpty()) {
					for (String key : KV.keySet()) {
						node.setAttribute(key, KV.get(key));
					}
				}
			} else {// 直接创建节点
				node = ((Document) doc).createElement(paths[i]);
				parentT.appendChild(node);
				parentT = node;
			}

		}
		return node;
	}
	
	/**
	 * 节点属性中的键值对
	 * @param path 节点路径
	 * @return 属性键值对
	 */
	protected Map<String,String> getAttrKeyValue(String path){
		Map<String,String> kv = new LinkedHashMap<String,String>();
		if(null!=path){
			if(path.contains("[@")&&path.endsWith("]")){
				int start = path.lastIndexOf("[@");
				int end = path.lastIndexOf("]");
				if(start<end){
					String kvStr = path.substring(start+2, end);
					String[] kvArray = kvStr.split("=");
					if(kvArray.length==2){
						kv.put(kvArray[0], kvArray[1].replace("'", "").replace("\"", ""));
					}
				}
			}
		}
		return kv;		
	}
}

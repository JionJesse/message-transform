package com.figue.channel.transform.xml.demo.other;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;
import com.figue.channel.transform.transform.xml.XmlTransform;

@XmlPath(value="field[@key='info1']")
public class Info1 {
	@XmlPath(value="field[@key='jobNo']")
	public String jobNo;
	
	@XmlPath(value="field[@key='compName']",valueAttr="@value")
	public String compName;
	
	@XmlPath(value="field[@key='compLocal']/value")
	public String compLocal;
	
	public static void main(String[] args) {
		Info1 Info1 = new Info1();
		Info1.compLocal="北京";
		Info1.compName="宇信";
		//Info1.jobNo="014397";
		
		XmlTransform<Info1> format = new XmlTransform<Info1>();
		// 对象转报文字符串
  		String msg  = format.trans2String(Info1);
		
		// 报文字符串格式化成对象
		Info1 object = format.trans2Bean(msg, Info1.class);
	}
}

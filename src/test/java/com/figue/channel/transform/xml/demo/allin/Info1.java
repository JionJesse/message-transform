package com.figue.channel.transform.xml.demo.allin;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("field[@key='info1']")
public class Info1 {
	@XmlPath("field[@key='jobNo']")
	public String jobNo;
	
	@XmlPath(value="field[@key='compName']",valueAttr="@value")
	public String compName;
	
	@XmlPath("field[@key='compLocal']/value")
	public String compLocal;

}

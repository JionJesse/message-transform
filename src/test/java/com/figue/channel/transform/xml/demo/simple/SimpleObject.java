package com.figue.channel.transform.xml.demo.simple;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;
import com.figue.channel.transform.annotation.validate.Length;
import com.figue.channel.transform.annotation.validate.NotNull;
import com.figue.channel.transform.transform.xml.XmlTransform;

@XmlPath("msgbody")
public class SimpleObject {
	@NotNull
	@Length(min=1,max=2)
	@XmlPath("CO_AMT")
	public Integer amt;
	
	@NotNull
	@Length(min=1,max=2)
	@XmlPath("APP_USER_ID")
	public String userId;
	@XmlPath("APP_USER_NAME")
	public String userName;
	@XmlPath("APP_USER_ORG_NO")
	public String userOrg;	


	public static void main(String args[]){
		XmlTransform<SimpleObject> format = new XmlTransform<SimpleObject>();
		//format.addValidator(new NotNullAndLengthValidator());
		//format.setEncoding("gbk");
		
		//对象格式换成报文	
		SimpleObject simpleObject = new SimpleObject();
		simpleObject.amt=8000;
		//simpleObject.userId="01009";
		simpleObject.userName="林寻";
		simpleObject.userOrg="HBWORK";
		String msg = format.trans2String(simpleObject);
		
		//报文格式化成对象
		SimpleObject object = format.trans2Bean(msg, SimpleObject.class);
	}
}

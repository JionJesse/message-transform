package com.figue.channel.transform.xml.demo.multi;

import java.math.BigDecimal;
import com.figue.channel.transform.transform.xml.XmlTransform;
public class Test2 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XmlTransform<MultiObject> format = new XmlTransform<MultiObject>();
		//format.setEncoding("gbk");
		//对象格式换成报文		
		MultiObject MultiObject = new MultiObject();
		SimpleObject1 SimpleObject1 = new SimpleObject1();
		SimpleObject1.brchNo="018701";
		SimpleObject1.seqNum= new BigDecimal("141221000000019165");
		SimpleObject1.userId="0187001";
		SimpleObject2 SimpleObject2 = new SimpleObject2();
		SimpleObject2.acctNo="802000031192588";
		MultiObject.object1=SimpleObject1;
		MultiObject.object2=SimpleObject2;
		
		// 对象转换成报文字符串
		String msg = format.trans2String(MultiObject);
		
		// 报文字符串转化成对象
		MultiObject object = format.trans2Bean(msg, MultiObject.class);
	}
}

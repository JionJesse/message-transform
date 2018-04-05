package com.figue.channel.transform.xml.demo.listandobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.figue.channel.transform.transform.xml.XmlTransform;
public class Test2 {
	public static void main(String[] args) {
		XmlTransform<ListObject> format = new XmlTransform<ListObject>();
	
		ListObject ListObject = new ListObject();
		
		List<SimpleObject> realTimeAmtList = new ArrayList<SimpleObject>();
		ListObject.realTimeAmtList = realTimeAmtList;
		
		SimpleObject SimpleObject = new SimpleObject();
		SimpleObject.acctNo="02000031192588";
		SimpleObject.amntCd='a';
		SimpleObject.ioshtg="L";
		SimpleObject.tranAm=960000.00;
		realTimeAmtList.add(SimpleObject);
		
		SimpleObject SimpleObject2 = new SimpleObject();
		SimpleObject2.acctNo="02000031192589";
		SimpleObject2.amntCd='b';
		SimpleObject2.ioshtg="M";
		SimpleObject2.tranAm=960000.02;
		realTimeAmtList.add(SimpleObject2);
		
		SimpleObject1 SimpleObject1 = new SimpleObject1();
		SimpleObject1.brchNo="018701";
		SimpleObject1.seqNum= new BigDecimal("141221000000019165");
		SimpleObject1.userId="0187001";		
		ListObject.simpleObject1=SimpleObject1;
		
		// 对象格式换成报文字符串
		String msg = format.trans2String(ListObject);
				
		// 字符串报文格式化成对象
		ListObject object = format.trans2Bean(msg, ListObject.class);
	}
}

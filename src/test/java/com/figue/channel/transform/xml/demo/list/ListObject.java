package com.figue.channel.transform.xml.demo.list;

import java.util.ArrayList;
import java.util.List;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.xml.XmlPath;
import com.figue.channel.transform.transform.xml.XmlTransform;

/**
 * list对象yu
 * @author figue
 *
 */
@XmlPath("Packet/Data/Req")
public class ListObject {
	@XmlPath("RealTimeAmtList")
	@BeanClass(SimpleObject.class)
	public List<SimpleObject> realTimeAmtList;
	
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
		
		//对象格式换成报文		
		String msg = format.trans2String(ListObject);
		
		//报文格式化成对象
		ListObject object = format.trans2Bean(msg, ListObject.class);
	}

}

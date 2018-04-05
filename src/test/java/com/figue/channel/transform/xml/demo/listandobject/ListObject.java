package com.figue.channel.transform.xml.demo.listandobject;


import java.util.List;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.xml.XmlPath;


@XmlPath("Packet/Data")
public class ListObject {
	@XmlPath("Req/RealTimeAmtList")
	@BeanClass(SimpleObject.class)
	public List<SimpleObject> realTimeAmtList;
	
	@BeanClass(SimpleObject1.class)
	public SimpleObject1 simpleObject1;

}

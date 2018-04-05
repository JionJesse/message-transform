package com.figue.channel.transform.xml.demo.multi;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("Packet/Data")
public class MultiObject {
	@BeanClass(SimpleObject1.class)
	public SimpleObject1 object1;
	
	@BeanClass(SimpleObject2.class)
	public SimpleObject2 object2;

}

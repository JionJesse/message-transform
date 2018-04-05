package com.figue.channel.transform.xml.demo.allin;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("house")
public class House {
	
	@XmlPath(valueAttr="@id")
	public String id;
	
	@XmlPath("size")
	public Integer size;

}

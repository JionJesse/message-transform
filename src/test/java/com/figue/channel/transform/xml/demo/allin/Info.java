package com.figue.channel.transform.xml.demo.allin;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath(value="info")
public class Info {

	@XmlPath("jobNo")
	public String jobNo;

	@BeanClass(House.class)
	public House house ;
	
}

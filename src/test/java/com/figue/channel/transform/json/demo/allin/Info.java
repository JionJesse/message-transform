package com.figue.channel.transform.json.demo.allin;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.json.JsonPath;

@JsonPath("info")
public class Info {

	@JsonPath(value="jobNo")
	public String jobNo;

	@BeanClass(House.class)
	public House house = new House();
	
}

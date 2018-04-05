package com.figue.channel.transform.xml.demo.allin;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("car")
public class Car {
	@XmlPath("carId")
	public String carId;
	@XmlPath("carName")
	public String carName;

}

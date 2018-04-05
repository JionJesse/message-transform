package com.figue.channel.transform.json.demo.allin;

import com.figue.channel.transform.annotation.parse.json.JsonPath;

@JsonPath("car")
public class Car {
	@JsonPath("carId")
	public String carId;
	@JsonPath("carName")
	public String carName;
	
}

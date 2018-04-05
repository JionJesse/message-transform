package com.figue.channel.transform.json.demo.multi;

import com.figue.channel.transform.annotation.parse.json.JsonPath;

@JsonPath("CAR")
public class Car {
	@JsonPath("carId")
	public String carId;
	@JsonPath("carName")
	public String carName;
}

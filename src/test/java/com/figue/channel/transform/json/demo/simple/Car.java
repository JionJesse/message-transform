package com.figue.channel.transform.json.demo.simple;

import com.alibaba.fastjson.JSON;
import com.figue.channel.transform.annotation.parse.json.JsonPath;
import com.figue.channel.transform.transform.json.JsonTransform;

public class Car {
	@JsonPath("carId")
	public String carId;
	@JsonPath("carName")
	public String carName;
	
	
	public static void main(String[] args) {
		Car car = new Car();
		car.carId="S0001";
		car.carName="凯美瑞";
		JsonTransform<Car> format = new JsonTransform<Car>();
		String str = format.trans2String(car);
		Car pp = format.trans2Bean(str, Car.class);
		System.out.println(JSON.toJSONString(pp));	
		
	}
}

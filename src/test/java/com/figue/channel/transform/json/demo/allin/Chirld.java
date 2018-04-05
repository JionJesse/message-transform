package com.figue.channel.transform.json.demo.allin;

import java.math.BigDecimal;
import java.util.List;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.json.JsonPath;

@JsonPath(value="chirld")
public class Chirld {
	
	@JsonPath(value="name")
	public String name;
	 
	@JsonPath(value="age")
	public BigDecimal age;
	
	@JsonPath(value="cars")
	@BeanClass(Car.class)
	public List<Car> cars;

	@BeanClass(House.class)
	public House house;
	
}

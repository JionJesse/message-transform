package com.figue.channel.transform.xml.demo.allin;

import java.math.BigDecimal;
import java.util.List;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("chirld")
public class Chirld {
	
	@XmlPath(valueAttr="@name")
	public String name;
	
	@XmlPath("age")
	public BigDecimal age;

	@BeanClass(House.class)
	public House house;
	
	@XmlPath("cars")
	@BeanClass(Car.class)
	public List<Car> cars;
	
	
}

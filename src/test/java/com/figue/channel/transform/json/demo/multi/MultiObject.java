package com.figue.channel.transform.json.demo.multi;

import com.figue.channel.transform.annotation.parse.BeanClass;

public class MultiObject {
	@BeanClass(Car.class)
	public Car car;
	
	@BeanClass(House.class)
	public House house;
}

package com.figue.channel.transform.json.demo.listandobject;

import java.util.List;
import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.json.JsonPath;

public class ListObject {

	@BeanClass(House.class)
	public House house;
	
	@JsonPath("LIST")
	@BeanClass(Car.class)
	public List<Car> list;
	
}

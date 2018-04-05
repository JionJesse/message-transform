package com.figue.channel.transform.json.demo.list;

import java.util.List;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.json.JsonPath;


public class ListObject {

	public String name;
	
	@JsonPath("LIST")
	@BeanClass(Car.class)
	public List<Car> list;

}

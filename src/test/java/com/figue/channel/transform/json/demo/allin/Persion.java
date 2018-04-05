package com.figue.channel.transform.json.demo.allin;

import java.util.ArrayList;
import java.util.List;

import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.json.JsonPath;


public class Persion {
	@JsonPath("name")
	public String name;
	
	@JsonPath("age")
	public Integer age;
	
	@JsonPath("phone")
	public Integer phone;
	
	@BeanClass(Info.class)
	public Info info = new Info();

	@JsonPath("houses")
	@BeanClass(House.class)
	public List<House> houses;
	
	@JsonPath("chirlds")
	@BeanClass(Chirld.class)
	public List<Chirld> chirlds = new ArrayList<Chirld>();
	
}

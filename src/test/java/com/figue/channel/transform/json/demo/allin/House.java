package com.figue.channel.transform.json.demo.allin;

import com.figue.channel.transform.annotation.parse.json.JsonPath;

@JsonPath("house")
public class House {
	
	@JsonPath("id")
	public String id;
	
	@JsonPath(value="size")
	public Integer size;
	
}

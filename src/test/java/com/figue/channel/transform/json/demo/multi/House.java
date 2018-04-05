package com.figue.channel.transform.json.demo.multi;

import com.figue.channel.transform.annotation.parse.json.JsonPath;

@JsonPath("HOUSE")
public class House {
	@JsonPath("address")
	public String address;
	@JsonPath("size")
	public Integer size;
}

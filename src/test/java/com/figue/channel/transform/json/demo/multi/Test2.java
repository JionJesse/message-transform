package com.figue.channel.transform.json.demo.multi;

import com.alibaba.fastjson.JSON;
import com.figue.channel.transform.transform.json.JsonTransform;
public class Test2 {
	
	public static void main(String[] args) {
		MultiObject MultiObject = new MultiObject();
		Car car = new Car();
		car.carId="S0001";
		car.carName="凯美瑞";
		MultiObject.car = car;
		
		House house = new House();
		MultiObject.house=house;
		house.address="江苏";
		house.size=100;
		
		JsonTransform<MultiObject> format = new JsonTransform<MultiObject>();
		String str = format.trans2String(MultiObject);
		MultiObject pp = format.trans2Bean(str, MultiObject.class);
		System.out.println(JSON.toJSONString(pp));	
		
	}


}

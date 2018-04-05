package com.figue.channel.transform.json.demo.list;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.figue.channel.transform.transform.json.JsonTransform;
public class Test2 {
	public static void main(String[] args) {
		ListObject listObject = new ListObject();
		List<Car> lists = new ArrayList<Car>();
		
		Car car = new Car();
		car.carId="S0001";
		car.carName="凯美瑞";
		
		Car car2 = new Car();
		car2.carId="S0002";
		car2.carName="凯美瑞2";
		
		lists.add(car);
		lists.add(car2);
		
		listObject.list = lists;
		JsonTransform<ListObject> format = new JsonTransform<ListObject>();
		
		// 对象转json字符串
		String str = format.trans2String(listObject);
		
		// json字符串转对象
		ListObject pp = format.trans2Bean(str, ListObject.class);
				
		System.out.println(JSON.toJSONString(pp));	

	}

}

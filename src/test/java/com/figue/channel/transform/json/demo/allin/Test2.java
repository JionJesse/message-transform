package com.figue.channel.transform.json.demo.allin;

import com.alibaba.fastjson.JSON;
import com.figue.channel.transform.transform.json.JsonTransform;
public class Test2 {

	public static void main(String[] args) {
		JsonTransform<Persion> pe = new JsonTransform<Persion>();
		
		//字符串转对象
		Persion pp = pe.trans2Bean("{\"age\":37,\"chirlds\":[{\"age\":1,\"house\":{\"id\":\"cTP001\",\"size\":11},\"name\":\"json1\"},{\"age\":2,\"cars\":[{\"carId\":\"DDDDD2\",\"carName\":\"凯美瑞\"}],\"house\":{\"id\":\"TP002\",\"size\":2},\"name\":\"json2\"}],\"houses\":[{\"id\":\"TP003\",\"size\":3}],\"info\":{\"house\":{\"id\":\"TP004\",\"size\":4},\"jobNo\":\"014397\"},\"name\":\"周杰伦\",\"phone\":1328974033}", Persion.class);
		System.out.println(JSON.toJSONString(pp));
		
		//对象转字符串
		System.out.println(pe.trans2String(pp));
		
		
	}

}

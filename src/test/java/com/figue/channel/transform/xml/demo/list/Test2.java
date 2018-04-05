package com.figue.channel.transform.xml.demo.list;

import java.io.File;

import com.figue.channel.transform.transform.xml.XmlTransform;
import com.figue.channel.transform.xml.demo.allin.Persion;
public class Test2 {
	public static void main(String args[]){
		XmlTransform<Persion> hh = new XmlTransform<Persion>();
		//xml报文转对象
		Persion kk = hh.trans2Bean(new File("F:\\workspacecmis4cmismaven4.6-channel\\nemp\\nemp-access\\src\\test\\java\\com\\yucheng\\emp\\access\\test\\xml\\4.xml"),Persion.class);
		
		//对象转xml报文
		String gg = hh.trans2String(kk);
		System.out.println(gg);
	}

}

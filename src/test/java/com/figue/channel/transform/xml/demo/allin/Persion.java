package com.figue.channel.transform.xml.demo.allin;

import java.util.List;
import com.figue.channel.transform.annotation.parse.BeanClass;
import com.figue.channel.transform.annotation.parse.xml.XmlPath;

/**
 * xml报文与复杂结构对象互转
 * @author figue
 *
 */
@XmlPath("root")
public class Persion {
	@XmlPath("name")
	public String name;
	
	@XmlPath("age")
	public Integer age;
	
	@XmlPath("phone")
	public Integer phone;
	
	@BeanClass(Info.class)
	public Info info;
	
	
	@XmlPath("houses")
	@BeanClass(House.class)
	public List<House> houses;
	
	@BeanClass(Chirld.class)
	public List<Chirld> chirlds;
	
	@BeanClass(Info1.class)
	public Info1 info1;
	
}

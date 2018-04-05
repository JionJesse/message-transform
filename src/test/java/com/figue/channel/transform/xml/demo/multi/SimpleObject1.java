package com.figue.channel.transform.xml.demo.multi;

import java.math.BigDecimal;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("PUB")
public class SimpleObject1 {
	@XmlPath("seqnum")
	public BigDecimal seqNum;
	@XmlPath("brchno")
	public String brchNo;
	@XmlPath("userid")
	public String userId;
	
}

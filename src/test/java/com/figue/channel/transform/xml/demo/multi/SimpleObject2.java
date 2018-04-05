package com.figue.channel.transform.xml.demo.multi;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("Req")
public class SimpleObject2 {
	@XmlPath("acctno")
	public String acctNo;
}

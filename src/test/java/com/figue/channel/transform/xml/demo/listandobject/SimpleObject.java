package com.figue.channel.transform.xml.demo.listandobject;

import com.figue.channel.transform.annotation.parse.xml.XmlPath;

@XmlPath("MX")
public class SimpleObject {
	@XmlPath("acctno")
	public String acctNo;
	@XmlPath("amntcd")
	public char amntCd;
	@XmlPath("tranam")
	public double tranAm;
	@XmlPath("ioshtg")
	public String ioshtg;
	
}

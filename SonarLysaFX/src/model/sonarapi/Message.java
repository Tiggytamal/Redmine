package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message
{
	private String msg;
	
	@XmlAttribute (name = "msg")
	public String getMsg()
	{
		return msg;
	}
}

package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatusPeriode
{
	private int index;
	private String mode;
	private String date;
	private String parameter;
	
	
	@XmlAttribute
	public int getIndex()
	{
		return index;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	@XmlAttribute
	public String getMode()
	{
		return mode;
	}
	
	public void setMode(String mode)
	{
		this.mode = mode;
	}
			
	@XmlAttribute
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
			
	@XmlAttribute
	public String getParameter()
	{
		return parameter;
	}	
	
	public void setParameter(String parameter)
	{
		this.parameter = parameter;
	}
}

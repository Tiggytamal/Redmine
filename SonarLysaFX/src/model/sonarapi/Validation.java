package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Validation implements ModeleSonar
{
	/*Attibuts*/

	private boolean valid;


	/* Accesseurs */

	@XmlAttribute (name = "valid", required = true)
	public boolean isValid()
	{
		return valid;
	}

	public void setValid(final boolean valid)
	{
		this.valid = valid;
	}
}

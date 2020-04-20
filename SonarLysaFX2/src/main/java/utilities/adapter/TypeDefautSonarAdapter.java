package utilities.adapter;

import model.enums.TypeDefautSonar;

public class TypeDefautSonarAdapter extends AbstractXmlAdapter<String, TypeDefautSonar>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TypeDefautSonarAdapter()
    {
        super(TypeDefautSonar::valueOf, TypeDefautSonar::toString);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

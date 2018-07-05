package junit.control.mail;

import org.junit.Before;
import org.junit.Test;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.mail.ControlMail;
import control.rtc.ControlRTC;
import junit.JunitBase;
import model.enums.TypeMail;

public class TestControlMail extends JunitBase
{
    private ControlMail handler;
    
    @Before
    public void init()
    {
        ControlRTC.INSTANCE.connexion();
    }

    @Test
    public void testEnvoyer() throws TeamRepositoryException
    {
        handler = new ControlMail();
        handler.envoyerMail(TypeMail.SUIVIJAVA);
    }
}
package control.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

public class ControlMail
{
    
    public void createMail()
    {
        Email email = EmailBuilder.startingBlank()
                .from("lollypop", "lolly.pop@pretzelfun.com")
                .to("C. Cane", "candycane@candyshop.org")
                .cc("Alain Prudent <chocobo@candyshop.org>")
                .withSubject("hey")
                .withPlainText("We should meet up! ;)")
                .buildEmail();
        
        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.host.com", 587, "user@host.com", "password")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withProxy("socksproxy.host.com", 1080, "proxy user", "proxy password")
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria() // turns off email validation
                .withProperty("mail.smtp.sendpartial", "true")
                .withDebugLogging(true)
                .buildMailer();

            mailer.sendMail(email);
    }
}

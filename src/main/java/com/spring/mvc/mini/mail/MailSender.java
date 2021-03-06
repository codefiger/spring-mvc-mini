package com.spring.mvc.mini.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.mvc.mini.properties.PropertiesBean;

@Component
public class MailSender {
	
	static Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

    @Autowired
    private PropertiesBean propertiesBean;
	
    public void sendMail(final String username,final String password, String fromAddress, Address[] toAddress, String subject, String text) throws AddressException, MessagingException {
    	
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable",propertiesBean.getStarttlsEnable());
        props.put("mail.smtp.auth",propertiesBean.getAuth());
        props.put("mail.smtp.host",propertiesBean.getHost());
        props.put("mail.smtp.port",propertiesBean.getPort());
        
            Authenticator au =  new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
              };
              
            Session session = Session.getInstance(props, au);
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setSubject(subject);
            message.setText(text);
            message.setRecipients(Message.RecipientType.TO, toAddress);
            Transport.send(message);
            
            LOGGER.info("Send Mail Done: "+fromAddress+" to" + toAddress);

    }

}

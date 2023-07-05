package mailclient.com.EmailAPI.send;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Base64;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import mailclient.com.connectionData.MessageData;

public class SendMessage {
    private String smtpHost;
    private int smtpPort;
    private String senderEmail;
    private String senderPassword;
    private String receiverEmail;

    public SendMessage(String smtpHost, int smtpPort,
            String senderEmail, String senderPassword, String receiverEmail) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
        this.receiverEmail = receiverEmail;
    }

    public void sendEmail(String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                String encodedUsername = base64Encode(senderEmail);
                String encodedPassword = base64Encode(senderPassword);
                return new PasswordAuthentication(encodedUsername, encodedPassword);
            }
        };
        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String encodedUsername = base64Encode(senderEmail);
                    String encodedPassword = base64Encode(senderPassword);
                    return new PasswordAuthentication(encodedUsername, encodedPassword);
                }
            });

            MimeMessage message = EmailMessageBuilder.createMessage(session, senderEmail, receiverEmail, subject,
                    content);

            EmailTransportBuilder emailTransportBuilder = new EmailTransportBuilder(session, smtpHost, smtpPort);
            Transport transport = emailTransportBuilder.createTransport(session, smtpHost, smtpPort, senderEmail,
                    senderPassword);
            EmailSender emailSender = new EmailSender(transport, message);
            emailSender.sendMessage(transport, message);

            transport.close();

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}

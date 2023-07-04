package mailclient.com.EmailAPI.receive;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import mailclient.com.EmailAPI.receive.model.Pop3Model;

public class receiveMessages extends Pop3Model {
    private Session emailSession;
    private Folder inbox;
    private Store store;

    public receiveMessages(String host, int port, String user, String password) {
        super(host, port, user, password);
    }

    public void buildEmailConnection() {
        Properties props = new Properties(); // a list for holding the configurations for the connection
        try {
            props.put("mail.pop3.host", host);
            props.put("mail.pop3.port", port);
            if (port == 995) {
                props.put("mail.pop3.ssl.enable", "true"); // enables an encryption for the POP3 connection
            }
            emailSession = Session.getDefaultInstance(props); // returns a new Session object, which is initialized with
                                                              // props
            if (port == 995) {
                store = emailSession.getStore("pop3s"); // returns a message store (repository for storing email
                                                        // messages and related metadata) for the protocol pop3s
            } else {
                store = emailSession.getStore("pop3"); // returns a message store (repository for storing email messages
            }
            store.connect(host, user, password); // establishes the connection to the mail server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getInbox() {
        try {
            inbox = store.getFolder("INBOX"); // returns inbox folder
            inbox.open(Folder.READ_ONLY); // opens the inbox folder in read only mode
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countMessages() { // returns the quantity of the messages in the inbox folder
        try {
            getInbox();
            return inbox.getMessageCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting message count from POP3 server");
        }
    }

    public void showMessage(int index) {
        try {
            if (inbox == null) {
                getInbox();
            }
            Message currentMessage = inbox.getMessage(index);
            String sender = getSender(currentMessage);
            String date = getDate(currentMessage);
            System.out.println("---------------------------------");
            System.out.println("Email Number " + (index));
            System.out.println("Subject: " + currentMessage.getSubject());
            System.out.println("From: " + sender);
            System.out.println("Date: " + date);
            System.out.println("Text: " + currentMessage.getContent().toString());
            System.out.println("---------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeEmailConnection() {
        try {
            inbox.close(false); // closes the inbox folder. The argument false assures that any changes which
                                // were made, will not be saved
            store.close(); // closes the connetion to the mail server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSender(Message message) {
        try {
            String sender = message.getFrom()[0].toString();
            Pattern pattern = Pattern.compile("<(.*?)>");
            Matcher matcher = pattern.matcher(sender); // extracts the email address from the sender's name
            return matcher.find() ? matcher.group(1) : sender;
        } catch (Exception e) {
            System.out.println("Error getting sender");
            throw new RuntimeException(e);
        }
    }

    private String getDate(Message message) {
        String date = "";
        try {
            String[] dateHeaders = message.getHeader("Date");
            if (dateHeaders != null && dateHeaders.length > 0) {
                // Use the first date header
                date = dateHeaders[0];
            } else {
                System.out.println("Sent Date header not found");
                date = "unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}

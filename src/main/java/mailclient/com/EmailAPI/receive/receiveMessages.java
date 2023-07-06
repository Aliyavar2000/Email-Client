package mailclient.com.EmailAPI.receive;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import mailclient.com.EmailAPI.receive.model.Pop3Model;
import mailclient.com.EmailAPI.receive.model.ReceivedMessageModel;
import mailclient.com.connectionData.ConnectionInfo;
import mailclient.com.credentials.UserCredentials;

public class ReceiveMessages {
    // private Session emailSession;
    private Folder inbox;
    private Store store;
    private List<ReceivedMessageModel> messages;

    // private String host;
    // private int port;
    // private String user;
    // private String password;
    public ReceiveMessages(Store store) {
        messages = new ArrayList<ReceivedMessageModel>();
        this.store = store;
    }

    public int getMessageCount() {
        if (inbox == null) {
            getInbox();
        }
        try {
            return inbox.getMessageCount();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get message count");
        }
    }

    public List<ReceivedMessageModel> getMessages() {
        String sender;
        Address[] receiversAddresses;
        String subject;
        Object content;
        String receivedDateString;
        String sentDateString;

        try {
            if (inbox == null) {
                getInbox();
            }
            for (int i = 1; i <= inbox.getMessageCount(); i++) {
                // for (int i = 1; i <= 20; i++) {
                Message currentMessage = inbox.getMessage(i);
                sender = getSender(currentMessage);
                receiversAddresses = currentMessage.getAllRecipients();
                String[] receivers = { UserCredentials.getUserMail() };
                if (receiversAddresses != null) {
                    receivers = new String[receiversAddresses.length];
                    for (int j = 0; j < receiversAddresses.length; j++) {
                        receivers[j] = receiversAddresses[j].toString();
                    }
                }
                subject = currentMessage.getSubject();
                content = currentMessage.getContent();
                Date receivedDate = currentMessage.getReceivedDate();
                receivedDateString = receivedDate != null ? receivedDate.toString() : "N/A";
                Date sentDate = currentMessage.getSentDate();
                sentDateString = sentDate != null ? sentDate.toString() : "N/A";
                ReceivedMessageModel message = new ReceivedMessageModel(sender, receivers, subject, content,
                        receivedDateString, sentDateString);
                messages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<ReceivedMessageModel> getMessages(int count) {
        String sender;
        Address[] receiversAddresses;
        String subject;
        Object content;
        String receivedDateString;
        String sentDateString;

        try {
            if (inbox == null) {
                getInbox();
            }
            for (int i = inbox.getMessageCount() - count; i < inbox.getMessageCount(); i++) {
                // for (int i = 1; i <= 20; i++) {
                Message currentMessage = inbox.getMessage(i + 1);
                sender = getSender(currentMessage);
                receiversAddresses = currentMessage.getAllRecipients();
                String[] receivers = { UserCredentials.getUserMail() };
                if (receiversAddresses != null) {
                    receivers = new String[receiversAddresses.length];
                    for (int j = 0; j < receiversAddresses.length; j++) {
                        receivers[j] = receiversAddresses[j].toString();
                    }
                }
                subject = currentMessage.getSubject();
                content = currentMessage.getContent();
                Date receivedDate = currentMessage.getReceivedDate();
                receivedDateString = receivedDate != null ? receivedDate.toString() : "N/A";
                Date sentDate = currentMessage.getSentDate();
                sentDateString = sentDate != null ? sentDate.toString() : "N/A";
                ReceivedMessageModel message = new ReceivedMessageModel(sender, receivers, subject, content,
                        receivedDateString, sentDateString);
                messages.add(message);
            }
            System.out.println("messageCount: " + inbox.getMessageCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
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

    private String getSender(Message message) {
        try {
            String sender = message.getFrom()[0].toString();
            Pattern pattern = Pattern.compile("\\\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}\\\\b");
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

    public void saveMessagesToFile(List<ReceivedMessageModel> messages, String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToJsonFile(List<ReceivedMessageModel> messages, String filePath) {
        JSONArray jsonArray = new JSONArray();

        for (ReceivedMessageModel message : messages) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Sender", message.getFrom());
            String receivers = extractReceivers(message.getTo());
            jsonObject.put("Receivers", receivers);
            jsonObject.put("Subject", message.getSubject());
            jsonObject.put("Content", getTextContent(message.getContent()));
            jsonObject.put("ReceivedDate", message.getReceivedDate());
            jsonObject.put("SentDate", message.getSentDate());

            jsonArray.put(jsonObject);
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toString(4)); // Indent with 4 spaces for better readability
            fileWriter.flush();
            System.out.println("Messages written to JSON file successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String extractReceivers(String[] to) {
        String receivers = "";
        for (String receiver : to) {
            receivers += receiver + ", ";
        }
        String receiversFormatted = getFormattedReceivers(receivers);
        if (receiversFormatted == "") {
            return receivers;
        }
        return receiversFormatted;
    }

    private String getFormattedReceivers(String receivers) {
        try {
            String[] receiverArray = receivers.split(",");
            Pattern pattern = Pattern.compile("\\\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}\\\\b");
            StringBuilder formattedReceivers = new StringBuilder();

            for (String receiver : receiverArray) {
                Matcher matcher = pattern.matcher(receiver);
                if (matcher.find()) {
                    String extractedReceiver = matcher.group(1);
                    formattedReceivers.append(extractedReceiver).append(", ");
                }
            }

            // Remove trailing comma and space
            if (formattedReceivers.length() > 0) {
                formattedReceivers.setLength(formattedReceivers.length() - 2);
            }

            return formattedReceivers.toString();
        } catch (Exception e) {
            System.out.println("Error getting formatted receivers");
            throw new RuntimeException(e);
        }
    }

    private String getTextContent(Object content) {
        String textContent = "";
        if (content instanceof MimeMessage) {
            MimeMessage mimeMessage = (MimeMessage) content;
            try {
                if (mimeMessage.isMimeType("text/plain")) {
                    textContent = (String) mimeMessage.getContent();
                } else if (mimeMessage.isMimeType("text/html")) {
                    textContent = (String) mimeMessage.getContent();
                } else if (mimeMessage.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) mimeMessage.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            textContent = (String) bodyPart.getContent();
                            break;
                        } else if (bodyPart.isMimeType("text/html")) {
                            textContent = (String) bodyPart.getContent();
                            break;
                        } else if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                            // handle attachment
                        }
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            try {
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.isMimeType("text/plain")) {
                        textContent = (String) bodyPart.getContent();
                        break;
                    } else if (bodyPart.isMimeType("text/html")) {
                        textContent = (String) bodyPart.getContent();
                        break;
                    } else if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        // handle attachment
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (content instanceof String) {
            textContent = (String) content;
        }
        return textContent;
    }

}

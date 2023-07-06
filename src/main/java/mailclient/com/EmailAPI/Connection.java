package mailclient.com.EmailAPI;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import mailclient.com.connectionData.ConnectionInfo;
import mailclient.com.credentials.UserCredentials;

public class Connection {
    private Session emailSession;
    private Store store;
    private String host;
    private int port;
    private String user;
    private String password;
    private Folder inbox;

    public Connection() {
        this.host = ConnectionInfo.getHostPop3();
        this.port = ConnectionInfo.getPortPop3();
        this.user = UserCredentials.getUsername();
        this.password = UserCredentials.getPassword();
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
            throw new RuntimeException("Connection failed");
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

    public Store getStore() {
        return store;
    }
}

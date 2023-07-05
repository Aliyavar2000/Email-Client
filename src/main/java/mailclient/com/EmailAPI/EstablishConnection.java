package mailclient.com.EmailAPI;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import mailclient.com.connectionData.ConnectionInfo;

public class EstablishConnection {
    private Session emailSession;
    private Folder inbox;
    private Store store;

    // public EstablishConnection(String host, int port, String user, String
    // password) {

    // }

    public void buildEmailConnection() {
        Properties props = new Properties(); // a list for holding the configurations for the connection
        try {
            props.put("mail.pop3.host", ConnectionInfo.getHostPop3());
            props.put("mail.pop3.port", ConnectionInfo.getPortPop3());
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
}

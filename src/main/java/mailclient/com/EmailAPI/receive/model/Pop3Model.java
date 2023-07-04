package mailclient.com.EmailAPI.receive.model;

public abstract class Pop3Model {
    protected String host;
    protected int port;
    protected String user;
    protected String password;

    public Pop3Model(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public abstract void buildEmailConnection();

    public abstract void closeEmailConnection();

    public abstract int countMessages();

    public abstract void showMessage(int index);
}

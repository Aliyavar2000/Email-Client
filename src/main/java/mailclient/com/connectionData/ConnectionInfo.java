package mailclient.com.connectionData;

import mailclient.com.credentials.UserCredentials;

public class ConnectionInfo {
    private static String hostPop3;
    private static String hostSmtp;
    private static int portPop3;
    private static int portSmtp;
    // private static String userMail;
    // private static String userPassword;

    public static void initialize(String hostPop3, int portPop3, String hostSmtp, int portSmtp) {
        ConnectionInfo.hostPop3 = hostPop3;
        ConnectionInfo.hostSmtp = hostSmtp;
        ConnectionInfo.portPop3 = portPop3;
        ConnectionInfo.portSmtp = portSmtp;
        // ServerData.userMail = UserCredentials.getUserMail();
        // ServerData.userPassword = UserCredentials.getPassword();
    }

    public static String getHostPop3() {
        return hostPop3;
    }

    public static String getHostSmtp() {
        return hostSmtp;
    }

    public static int getPortPop3() {
        return portPop3;
    }

    public static int getPortSmtp() {
        return portSmtp;
    }
}

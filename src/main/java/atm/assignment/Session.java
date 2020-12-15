package atm.assignment;

import com.opencsv.exceptions.CsvException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

class Session {
    private final String DURATION = "session.timeout";
    private final String GLOBAL_VALUES_FILE_PATH = "src/main/resources/machine/global.properties";
    private final static Logger logger = Logger.getLogger(Session.class);
    private long activatedAt = Long.MAX_VALUE;
    private boolean isLoggedIn = false;

    private int getSessionTimeoutValue() throws IOException {
        Properties prop = new Properties();
        FileInputStream input = new FileInputStream(GLOBAL_VALUES_FILE_PATH);
        prop.load(input);
        return Integer.parseInt(prop.getProperty(DURATION));
    }

    void activate() {
        activatedAt = System.currentTimeMillis();
    }

    boolean isActive() throws IOException {
        long activeFor = System.currentTimeMillis() - activatedAt;
        return isLoggedIn && activeFor >= 0 && activeFor <= getSessionTimeoutValue();
    }

    void logout() throws IOException {
        if (isActive()) {
            isLoggedIn = false;
            System.out.println("Account " + Users.currentUser + " logged out.");
        } else {
            System.err.println("No account is currently authorized.");
        }
    }

    void authorize(String acctId, String pin) throws IOException, CsvException {
        if (Users.isUserPresent(acctId, pin)) {
            isLoggedIn = true;
            System.out.println(acctId + " successfully authorized.");
        } else {
            System.err.println("Authorization failed.");
        }
    }
}

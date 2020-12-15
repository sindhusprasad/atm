package atm.assignment.utils;

import atm.assignment.tests.WithdrawTests;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestUtil {
    private final static Logger logger = Logger.getLogger(WithdrawTests.class);
    private final static String USERS_FILE_PATH = "src/main/resources/users/CSVDemo.csv";
    private final String ATM_BALANCE_FILE_PATH = "src/main/resources/machine/global.properties";
    private final String GLOBAL_VALUES_FILE_PATH = "machine.balance";
    private Properties prop = new Properties();

    private List<String[]> readUsers() throws IOException, CsvException {
        File inputFile = new File(USERS_FILE_PATH);
        CSVReader reader = new CSVReader(new FileReader(inputFile));
        return reader.readAll();
    }

    public void setup(String acctId, String pin, String balance) throws IOException, InterruptedException, CsvException {
        List<String[]> users = new ArrayList<>();
        String[] user = new String[3];
        CSVWriter writer = new CSVWriter(new FileWriter(USERS_FILE_PATH, true));
        user[0] = acctId;
        user[1] = pin;
        user[2] = balance;
        logger.debug("acctId: " + acctId);
        logger.debug("pin: " + pin);
        logger.debug("balance: " + balance);
        users.add(user);
        writer.writeAll(users);
        writer.flush();
        writer.close();
    }

    public double roundUp(double amount) {
        return Double.parseDouble(String.format("%.2f", amount));
    }

    public void tearDown(String acctId) throws IOException, CsvException {
        int currentUserIndex = 0;
        File inputFile = new File(USERS_FILE_PATH);
        CSVReader reader = new CSVReader(new FileReader(inputFile));
        List<String[]> users = reader.readAll();
        for (int i = 0; i < users.size(); i++) {
            String[] list = users.get(i);
            if (list[0].equals(String.valueOf(acctId))) {
                currentUserIndex = i;
                break;
            }
        }
        users.remove(currentUserIndex);
        CSVWriter writer = new CSVWriter(new FileWriter(USERS_FILE_PATH));
        writer.writeAll(users);
        writer.close();
    }

    public String[] getLatestUserTransaction(String acctId) throws IOException, CsvException {
        File inputFile = new File("src/main/resources/transactions/" + acctId + ".csv");
        CSVReader reader = new CSVReader(new FileReader(inputFile));
        List<String[]> transactions = reader.readAll();
        return transactions.get(0);
    }

    public double getCurrentMachineBalance() throws IOException {
        FileInputStream input = new FileInputStream(ATM_BALANCE_FILE_PATH);
        prop.load(input);
        return Double.parseDouble(prop.getProperty(GLOBAL_VALUES_FILE_PATH));
    }

    public void updateMachineBalance(double amount) throws IOException {
        FileInputStream input = new FileInputStream(ATM_BALANCE_FILE_PATH);
        prop.load(input);
        input.close();
        FileOutputStream out = new FileOutputStream(ATM_BALANCE_FILE_PATH);
        prop.setProperty(GLOBAL_VALUES_FILE_PATH, String.valueOf(amount));
        prop.store(out, null);
        out.close();
    }
}

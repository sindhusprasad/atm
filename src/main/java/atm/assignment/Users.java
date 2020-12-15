package atm.assignment;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * <p><code>Users</code> is meant to store and manage users of the bank
 */
public class Users {
    private final static String USERS_FILE_PATH = "src/main/resources/users/CSVDemo.csv";
    private static int currentUserIndex;
    private static List<String[]> users;
    static String currentUser;
    public static double currentUserBalance;

    /**
     * Rounds up to 2 decimals
     * @param amount round up amount
     */
    private static double roundUp(double amount) {
        return Double.parseDouble(String.format("%.2f", amount));
    }

    /**
     * Read the info of all users from the csv file into a List<String[]>
     * @throws IOException when csv file doesn't exist
     * @throws CsvException when there is an issue reading csv file
     */
    private static void readUsers() throws IOException, CsvException {
        File inputFile = new File(USERS_FILE_PATH);
        CSVReader reader = new CSVReader(new FileReader(inputFile));
        users = reader.readAll();
    }

    /**
     * Updates user balance
     * @param balance update the latest balance
     * @throws IOException
     */
    private static void updateUserBalance(double balance) throws IOException {
        File inputFile = new File(USERS_FILE_PATH);
        currentUserBalance = balance;
        users.get(currentUserIndex)[2] = String.valueOf(roundUp(currentUserBalance));
        CSVWriter writer = new CSVWriter(new FileWriter(inputFile));
        writer.writeAll(users);
        writer.flush();
        writer.close();
    }

    /**
     * @return current user's balance
     */
    private static double getCurrentUserBalance() {
        return Double.parseDouble(users.get(currentUserIndex)[2]);
    }

    /**
     * checks to see if the user is present
     * @param acctId account id of the user
     * @param pin pin of the user
     * @return returns boolean true if the user is present and false if not
     * @throws IOException when csv file doesn't exist
     * @throws CsvException when there is an issue reading csv file
     */
    static boolean isUserPresent(String acctId, String pin) throws IOException, CsvException {
        readUsers();
        boolean found = false;
        List<String[]> allValues = users;
        for (int i = 0; i < allValues.size(); i++) {
            String[] list = allValues.get(i);
            if (list[0].equals(acctId) && list[1].equals(pin)) {
                currentUser = list[0];
                currentUserBalance = Double.parseDouble(list[2]);
                currentUserIndex = i;
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * withdraw from the machine and account
     * @param value withdrawal amount
     * @throws IOException when csv file doesn't exist
     */
    static void withdraw(double value) throws IOException {
        updateUserBalance(getCurrentUserBalance() - value);
    }

    /**
     * deposit money to the account
     * @param value deposit amount
     * @throws IOException when csv file doesn't exist
     */
    static void deposit(double value) throws IOException {
        updateUserBalance(getCurrentUserBalance() + value);
    }
}

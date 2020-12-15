package atm.assignment;

import com.opencsv.exceptions.CsvException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

/**
 * <p><code>Users</code> is meant to read the users input and perform appropriate action
 */
public class ATMInput {
    private final static Logger logger = Logger.getLogger(ATMInput.class);
    private Machine machine;
    private Session session;
    private TransactionHistory transactionHistory;
    private String[] params;

    public ATMInput() {
        machine = new Machine();
        session = new Session();
        transactionHistory = new TransactionHistory();
    }

    /**
     * authorize a user
     * @param params command
     * @throws IOException when csv file doesn't exist
     * @throws CsvException when there is an issue reading csv file
     */
    public void authorize(String[] params) throws IOException, CsvException {
        this.params = params;
        try {
            if (params.length == 3 && !params[1].isEmpty() && !params[2].isEmpty()) {
                session.authorize(params[1], params[2]);
                session.activate();
            } else {
                System.err.println("Unrecognized params.");
                System.err.println("Usage: authorize <account_id> <pin>");
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Unrecognized params.");
            System.err.println("Usage: authorize <account_id> <pin>");
        }
    }

    /**
     * withdraw from the machine and account
     * @param params command
     * @throws IOException when csv file doesn't exist
     */
    public void withdraw(String[] params) throws IOException {
        if (session.isActive()) {
            if (params.length == 2) {
                machine.withdraw(Double.parseDouble(params[1]));
                session.activate();
            } else {
                System.err.println("Unrecognized params.");
                System.err.println("Usage: withdraw <value>");
            }
        } else {
            System.err.println("Authorization required.");
        }
    }

    /**
     * deposit to the account
     * @param params command
     * @throws IOException when csv file doesn't exist
     */
    public void deposit(String[] params) throws IOException {
        if (session.isActive()) {
            if (params.length == 2) {
                machine.deposit(Double.parseDouble(params[1]));
                transactionHistory.updateTransaction(Double.parseDouble(params[1]));
                session.activate();
            } else {
                System.err.println("Unrecognized params.");
                System.err.println("Usage: deposit <value>");
            }
        } else {
            System.err.println("Authorization required.");
        }
    }

    /**
     * show the balance
     * @param params command
     * @throws IOException when csv file doesn't exist
     */
    public void balance(String[] params) throws IOException {
        if (session.isActive()) {
            if (params.length == 1) {
                machine.balance();
                session.activate();
            } else {
                System.err.println("Unrecognized params.");
                System.err.println("Usage: balance");
            }
        } else {
            System.err.println("Authorization required.");
        }
    }

    /**
     * display history
     * @param params command
     * @throws IOException when csv file doesn't exist
     */
    public void history(String[] params) throws IOException, CsvException {
        if (session.isActive()) {
            if (params.length == 1) {
                transactionHistory.displayTransactions();
                session.activate();
            } else {
                System.err.println("Unrecognized params.");
                System.err.println("Usage: history");
            }
        } else {
            System.err.println("Authorization required.");
        }
    }

    /**
     * logout of the machine
     * @param params command
     * @throws IOException when csv file doesn't exist
     */
    public void logout(String[] params) throws IOException {
        if (session.isActive()) {
            if (params.length == 1) {
                session.logout();
            } else {
                System.err.println("Unrecognized params.");
                System.err.println("Usage: history");
            }
        } else {
            System.err.println("Authorization required.");
        }
    }

    /**
     * end the session
     * @param params command
     * @throws IOException when csv file doesn't exist
     */
    public void end(String[] params) throws IOException {
        if (params.length == 1) {
            logout(new String[]{"logout"});
            System.exit(0);
        } else {
            System.err.println("Unrecognized params");
            System.err.println("Usage: history");
        }
    }

    /**
     * withdraw from the machine and account
     * @throws IOException when csv file doesn't exist
     * @throws CsvException when there is an issue reading csv file
     */
    public static void main(String[] args) throws IOException, CsvException {
        ATMInput atmInput = new ATMInput();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the command: ");
            String input = scanner.nextLine();
            logger.trace("Command is: " + input);
            String[] params = input.split(" ");
            String command = params[0];

            switch (command) {
                case "authorize":
                    atmInput.authorize(params);
                    break;
                case "withdraw":
                    atmInput.withdraw(params);
                    break;
                case "deposit":
                    atmInput.deposit(params);
                    break;
                case "balance":
                    atmInput.balance(params);
                    break;
                case "history":
                    atmInput.history(params);
                    break;
                case "logout":
                    atmInput.logout(params);
                    break;
                case "end":
                    atmInput.end(params);
                    break;
                default:
                    System.err.println("Unrecognized command");
            }
        }
    }
}

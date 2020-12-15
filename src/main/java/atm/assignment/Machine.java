package atm.assignment;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

class Machine {
    private final String ATM_BALANCE_FILE_PATH = "src/main/resources/machine/global.properties";
    private final String GLOBAL_VALUES_FILE_PATH = "machine.balance";
    private final int OVERDRAFT_FEE = 5;
    private Properties prop = new Properties();
    private TransactionHistory transactionHistory = new TransactionHistory();

    private void updateMachineBalance(double withdrawnAmount) throws IOException {
        FileInputStream input = new FileInputStream(ATM_BALANCE_FILE_PATH);
        prop.load(input);
        input.close();
        FileOutputStream out = new FileOutputStream(ATM_BALANCE_FILE_PATH);
        prop.setProperty(GLOBAL_VALUES_FILE_PATH, String.valueOf(getCurrentMachineBalance() - withdrawnAmount));
        prop.store(out, null);
        out.close();
    }

    private double getCurrentMachineBalance() throws IOException {
        FileInputStream input = new FileInputStream(ATM_BALANCE_FILE_PATH);
        prop.load(input);
        return Double.parseDouble(prop.getProperty(GLOBAL_VALUES_FILE_PATH));
    }

    private double roundUp(double amount) {
        return Double.parseDouble(String.format("%.2f", amount));
    }

    void withdraw(double amount) throws IOException {
        if (amount % 20 == 0) {
            if (Users.currentUserBalance > 0) {
                if (getCurrentMachineBalance() >= 20) { //if there is enough money in the machine
                    if (amount <= Users.currentUserBalance) {
                        if (amount <= getCurrentMachineBalance()) {
                            Users.withdraw(roundUp(amount));
                            updateMachineBalance(roundUp(amount));
                            transactionHistory.updateTransaction(Double.parseDouble("-" + roundUp(amount)));
                            System.out.println("Amount dispensed: $" + roundUp(amount));
                            System.out.println("Current balance: $" + roundUp(Users.currentUserBalance));
                        } else {
                            double correctedAmount = getCurrentMachineBalance();
                            updateMachineBalance(correctedAmount);
                            Users.withdraw(correctedAmount);
                            transactionHistory.updateTransaction(Double.parseDouble("-" + correctedAmount));
                            System.err.println("Unable to dispense full amount requested at this time.");
                            System.out.println("Amount dispensed: $" + correctedAmount);
                            System.out.println("Current balance: $" + Users.currentUserBalance);
                        }
                    } else {
                        Users.withdraw(OVERDRAFT_FEE);
                        transactionHistory.updateTransaction(Double.parseDouble("-" + OVERDRAFT_FEE));
                        System.err.println("Amount dispensed: $0");
                        System.err.println("You have been charged an overdraft fee of $5. Current balance: $" + roundUp(Users.currentUserBalance));
                    }
                } else {
                    System.err.println("Unable to process your withdrawal at this time.");
                }
            } else {
                System.err.println("Your account is overdrawn! You may not make withdrawals at this time.");
            }
        } else {
            System.err.println("Enter the amount in the multiples of 20");
        }
    }

    void deposit(double amount) throws IOException {
        Users.deposit(roundUp(amount));
        System.out.println("Current balance: $" + roundUp(Users.currentUserBalance));
    }

    void balance() {
        System.out.println("Current balance: $" + roundUp(Users.currentUserBalance));
    }
}

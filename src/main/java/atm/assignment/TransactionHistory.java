package atm.assignment;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class TransactionHistory {
    private double roundUp(double amount) {
        return Double.parseDouble(String.format("%.2f", amount));
    }

    void updateTransaction(double transactionAmount) throws IOException {
        List<String[]> transactions = new ArrayList<>();
        String[] transaction = new String[4];
        CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/transactions/" + Users.currentUser + ".csv", true));
        transaction[0] = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        transaction[1] = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        transaction[2] = String.valueOf(transactionAmount); // todo casting can be improved
        transaction[3] = String.valueOf(roundUp(Users.currentUserBalance));
        transactions.add(transaction);
        writer.writeAll(transactions);
        writer.flush();
        writer.close();
    }

    void displayTransactions() throws IOException, CsvException {
        List<String[]> transactions;
        try {
            File inputFile = new File("src/main/resources/transactions/" + Users.currentUser + ".csv");
            CSVReader reader = new CSVReader(new FileReader(inputFile));
            transactions = reader.readAll();
            if (transactions.size() > 0) {
                System.out.println("Transaction history:");
                for (int i = transactions.size() - 1; i >= 0; i--) {
                    System.out.println(transactions.get(i)[0] + " " + transactions.get(i)[1] + " " + transactions.get(i)[2] + " " + transactions.get(i)[3]);
                }
            } else {
                System.err.println("No history found");
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("No history found");
        }
    }
}

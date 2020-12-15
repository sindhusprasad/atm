package atm.assignment.tests;

import atm.assignment.base.BaseTest;
import com.opencsv.exceptions.CsvException;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.testng.Assert.assertEquals;

public class TransactionHistoryTests extends BaseTest {
    private final static Logger logger = Logger.getLogger(TransactionHistoryTests.class);

    @Test
    public void latestDepositTransactionOnTopTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.deposit(new String[]{"deposit", "10.45"});
        atmInput.history(new String[]{"history"});
        logger.debug(outContent);
        logger.debug(errContent);
        String[] outputLines = outContent.toString().split("\n");
        assertEquals(outputLines[0], acctId + " successfully authorized.");
        assertEquals(outputLines[1], "Current balance: $" + (Double.parseDouble(balance) + 10.45));
        assertEquals(outputLines[2], "Transaction history:");
        String[] trans = outputLines[3].split(" ");
        assertEquals(trans[0], new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        assertEquals(trans[2], "10.45");
        assertEquals(Double.parseDouble(trans[3]), Double.parseDouble(balance) + 10.45);
    }

    @Test
    public void latestWithdrawalTransactionOnTopTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.withdraw(new String[]{"withdraw", "20"});
        atmInput.history(new String[]{"history"});
        logger.debug(outContent);
        logger.debug(errContent);
        String[] outputLines = outContent.toString().split("\n");
        assertEquals(outputLines[0], acctId + " successfully authorized.");
        assertEquals(outputLines[1], "Amount dispensed: $20.0");
        assertEquals(outputLines[2], "Current balance: $" + (Double.parseDouble(balance) - 20));
        assertEquals(outputLines[3], "Transaction history:");
        String[] trans = outputLines[4].split(" ");
        assertEquals(trans[0], new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        assertEquals(trans[2], "-20.0");
        assertEquals(Double.parseDouble(trans[3]), Double.parseDouble(balance) - 20);
    }

    @Test
    public void noHistoryTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.history(new String[]{"history"});
        assertEquals(outContent.toString(), acctId + " successfully authorized.\n");
        assertEquals(errContent.toString(), "No history found\n");
    }
}

package atm.assignment.tests;

import atm.assignment.base.BaseTest;
import com.opencsv.exceptions.CsvException;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class BalanceTests extends BaseTest {
    private final static Logger logger = Logger.getLogger(BalanceTests.class);
    @Test
    public void currentBalanceTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.balance(new String[]{"balance"});
        assertEquals(outContent.toString(), acctId + " successfully authorized.\nCurrent balance: $" + testUtil.roundUp(Double.parseDouble(balance)) + "\n");
    }
}

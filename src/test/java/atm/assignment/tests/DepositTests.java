package atm.assignment.tests;

import atm.assignment.Users;
import atm.assignment.base.BaseTest;
import com.opencsv.exceptions.CsvException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class DepositTests extends BaseTest {
    @Test
    public void updateUserBalanceOnDepositTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        double balance = Users.currentUserBalance;
        atmInput.deposit(new String[]{"deposit", "10.45"});
        logger.debug(outContent);
        logger.debug(errContent);
        assertEquals(outContent.toString(), acctId + " successfully authorized.\nCurrent balance: $" + (balance + 10.45) + "\n");
    }
}

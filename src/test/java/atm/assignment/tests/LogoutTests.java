package atm.assignment.tests;

import atm.assignment.base.BaseTest;
import com.opencsv.exceptions.CsvException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class LogoutTests extends BaseTest {

    @Test
    public void logoutTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.logout(new String[]{"logout"});
        atmInput.balance(new String[]{"balance"});
        assertEquals(outContent.toString(), acctId + " successfully authorized.\nAccount " + acctId + " logged out.\n");
        assertEquals(errContent.toString(), "Authorization required.\n");
    }
}

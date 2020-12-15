package atm.assignment.tests;

import atm.assignment.base.BaseTest;
import com.opencsv.exceptions.CsvException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class SessionTests extends BaseTest {
    @Test
    public void validAcctIdTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        assertEquals(outContent.toString(), acctId + " successfully authorized.\n");
    }

    @Test
    public void invalidPinTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, "1234"});
        assertEquals(errContent.toString(), "Authorization failed.\n");
    }

    @Test
    public void emptyPinTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, ""});
        assertEquals(errContent.toString(), "Unrecognized params.\nUsage: authorize <account_id> <pin>\n");
    }

    @Test
    public void incompatibleAcctIdTypeTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", "abcd", pin});
        assertEquals(errContent.toString(), "Authorization failed.\n");
    }

//    @Test
    public void sessionTimeoutTest() throws IOException, CsvException, InterruptedException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.balance(new String[]{"balance"});
        Thread.sleep(2001);
        atmInput.balance(new String[]{"balance"});
        logger.trace(outContent);
        logger.trace(errContent);
        assertEquals(errContent.toString(), "Authorization required.\n");
    }
}

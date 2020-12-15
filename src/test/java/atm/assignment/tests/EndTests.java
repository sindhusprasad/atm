package atm.assignment.tests;

import atm.assignment.base.BaseTest;
import com.opencsv.exceptions.CsvException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class EndTests extends BaseTest {
//    @Test
    public void validAcctIdTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        assertEquals(outContent.toString(), acctId + " successfully authorized.\n");
        atmInput.end(new String[]{"end"});
        atmInput.balance(new String[]{"balance"});
    }
}

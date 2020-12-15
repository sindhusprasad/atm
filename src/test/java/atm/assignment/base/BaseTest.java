package atm.assignment.base;

import atm.assignment.ATMInput;
import atm.assignment.tests.DepositTests;
import atm.assignment.utils.TestUtil;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class BaseTest {
    protected final static Logger logger = Logger.getLogger(BaseTest.class);
    protected TestUtil testUtil = new TestUtil();
    protected ATMInput atmInput = new ATMInput();
    protected ByteArrayOutputStream outContent;
    protected ByteArrayOutputStream errContent;
    protected final PrintStream originalOut = System.out;
    protected final PrintStream originalErr = System.err;

    protected String acctId;
    protected String pin;
    protected String balance;

    @BeforeMethod
    public void setupStreams() throws InterruptedException, IOException, CsvException {
        acctId = getRandomAcctId();
        pin = getRandomPin();
        balance = getRandomAcctBalance();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        testUtil.setup(acctId, pin, balance);
    }

    @AfterMethod
    public void tearDown() throws IOException, CsvException {
        testUtil.tearDown(acctId);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    protected String getRandomAcctId() {
        return "99999" + RandomStringUtils.randomNumeric(5); // starting with a prefix so that it can be deleted if not deleted by the tests
    }

    protected String getRandomPin() {
        return RandomStringUtils.randomNumeric(4);
    }

    protected String getRandomAcctBalance() {
        return RandomStringUtils.randomNumeric(3, 5);
    }

    public ByteArrayOutputStream getOutContent() {
        return outContent;
    }

    public ByteArrayOutputStream getErrContent() {
        return errContent;
    }
}

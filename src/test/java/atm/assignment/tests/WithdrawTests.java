package atm.assignment.tests;

import atm.assignment.base.BaseTest;
import com.opencsv.exceptions.CsvException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.testng.Assert.assertEquals;

public class WithdrawTests extends BaseTest {
    @Test
    public void withdrawTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.withdraw(new String[]{"withdraw", "40"});
        assertEquals(outContent.toString(), acctId + " successfully authorized.\nAmount dispensed: $40.0\nCurrent balance: $" + (Long.parseLong(balance) - 40.0) + "\n");
    }

    @Test
    public void withdrawExcessThanAcctBalanceTest() throws IOException, CsvException, InterruptedException {
        String acctId = getRandomAcctId();
        String pin = getRandomPin();
        testUtil.setup(acctId, pin, "100");
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        atmInput.withdraw(new String[]{"withdraw", "120"});
        String[] trans = testUtil.getLatestUserTransaction(acctId);
        assertEquals(errContent.toString(), "Amount dispensed: $0\nYou have been charged an overdraft fee of $5. Current balance: $95.0\n");
        assertEquals(trans[0], new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())); //todo: not verifying the transaction time due to lack of time
        assertEquals(trans[2], "-5.0");
        assertEquals(trans[3], "95.0");
        testUtil.tearDown(acctId);
    }

    @Test
    public void withdrawExcessThanMachineBalanceTest() throws IOException, CsvException, InterruptedException {
        String newacctId = getRandomAcctId();
        String newPin = getRandomPin();
        double machineBalance = testUtil.getCurrentMachineBalance();
        testUtil.setup(newacctId, newPin, String.valueOf(machineBalance + 200));
        atmInput.authorize(new String[]{"authorize", newacctId, newPin});
        atmInput.withdraw(new String[]{"withdraw", String.valueOf(machineBalance + 20)}); // todo: assuming the machine balance is always in the multiples of 20s due to the lack of time
        testUtil.updateMachineBalance(machineBalance);
        assertEquals(errContent.toString(), "Unable to dispense full amount requested at this time.\n");
    }

    @Test
    public void withdrawNonMultiplesOfTwenty() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        String[] withdrawTestData = {"withdraw", "19"};
        atmInput.withdraw(withdrawTestData);
        assertEquals(errContent.toString(), "Enter the amount in the multiples of 20\n");
    }

    @Test
    public void withdrawOverdrawTest() throws IOException, CsvException, InterruptedException {
        String newacctId = getRandomAcctId();
        String newPin = getRandomPin();
        testUtil.setup(newacctId, newPin, "-10");
        atmInput.authorize(new String[]{"authorize", newacctId, newPin});
        atmInput.withdraw(new String[]{"withdraw", "20"});
        assertEquals(errContent.toString(), "Your account is overdrawn! You may not make withdrawals at this time.\n");
        testUtil.tearDown(acctId);
    }

    @Test
    public void machineBalanceDeductionTest() throws IOException, CsvException {
        atmInput.authorize(new String[]{"authorize", acctId, pin});
        double machineBalance = testUtil.getCurrentMachineBalance();
        atmInput.withdraw(new String[]{"withdraw", "20"});
        assertEquals(testUtil.getCurrentMachineBalance(), machineBalance - 20);
    }
}

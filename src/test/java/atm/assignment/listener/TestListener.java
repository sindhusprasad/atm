package atm.assignment.listener;

import atm.assignment.base.BaseTest;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {
    private final static Logger logger = Logger.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("*****************************************************************************");
        logger.error("Test " + result.getName().trim() + " Failed! Output: ");
        logger.debug(((BaseTest) result.getInstance()).getErrContent());
        logger.error("*****************************************************************************");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.debug("-------------------------------------------------------------------------------");
        logger.debug("Starting test -> " + result.getName().trim());
        logger.debug("-------------------------------------------------------------------------------");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.debug("Test: " + result.getName().trim() + " completed successfully. Output is: ");
        logger.debug(((BaseTest) result.getInstance()).getOutContent());
        logger.debug("###############################################################################\n\n\n");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        logger.warn("Test " + result.getName().trim() + " Skipped!");
        logger.warn("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }
}

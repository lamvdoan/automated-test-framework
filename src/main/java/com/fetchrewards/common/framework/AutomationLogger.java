package com.fetchrewards.common.framework;

import com.fetchrewards.common.structure.JsonObjectBase;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AutomationLogger {
    private static final Logger log = Logger.getLogger("");
    private static boolean passed = true;
    private static boolean exception = false;
    private static boolean suitePassed = true;
    private static Date timeStarted = null;
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static int testsSkipped = 0;
    private static PrintStream systemOut = System.out;
    private static int stepNumber = 1;
    private static int testCaseNumber = 1;
    private String logLevel;

    private static AutomationLogger instance = null;

    public static AutomationLogger getInstance() {
        if(instance == null) {
            instance = new AutomationLogger();
        }

        return instance;
    }

    private AutomationLogger() {
        TestConfiguration testConfiguration = TestConfiguration.getInstance();
        this.logLevel = testConfiguration.getLogLevel();
        setLevel(Level.toLevel(logLevel, Level.INFO));
    }

    public void incrementTestCaseCount() {
        testCaseNumber++;
    }


    //**********************************
    //    Methods that mirror Logger
    //**********************************
    public void info(Object message) {
        log.info(message);
    }

    public void debug(Object message) {
        log.debug(message);
    }

    public void trace(Object message) {
        log.trace(message);
    }

    public void error(String message) {
        log.error(message);
        passed = false;
    }

    public void fatal(String message) {
        passed = false;
        log.fatal(message);
        Assert.fail(message);
    }

    public void comment(String message) {
        log.info("");
        log.info("--------------------------------------------------------");
        log.info(message);
        log.info("--------------------------------------------------------");

    }

    public void warning(Object message) {
        log.warn(message);
    }

    public void exception(Throwable except) {
        String[] exceptLines = except.toString().split("\\n");

        this.error("! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! !");
        this.error("!              EXCEPTION RAISED               !");
        this.error(exceptLines[0]);
        this.error("! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! !");

        exception = true;
    }

    public void setLevel(Level level) {
        log.setLevel(level);
    }


    // *********************************
    //    New methods to extend logger
    // *********************************
    public void startTest(String testName, String description) {
        log.info("======================================================================================");
        log.info("                       Test Case #" + testCaseNumber);
        log.info("");
        log.info("    Starting test:  " + testName);
        log.info("    Description:    " + description);
        log.info("======================================================================================");
        log.info("");

        passed = true;
        exception = false;
        timeStarted = new Date();
        stepNumber = 1;
    }

    public void endTest() {
        if (timeStarted != null) {
            Date duration = new Date((new Date()).getTime() - timeStarted.getTime());
            Format formatted = new SimpleDateFormat("mm:ss");
            String passFail = passed ? "PASS" : "FAIL";
            timeStarted = null;
            suitePassed = suitePassed && passed;

            log.info("");
            log.info("======================================================================================");
            log.info("     Test Result:      (" + passFail + ")        Duration: " + formatted.format(duration));
            log.info("======================================================================================");

            if (passed)
                testsPassed++;
            else
                testsFailed++;

            if (!exception)
                Assert.assertTrue("Test failed with 1 or more ERRORs", passed);
        }
    }

    public void startSuite() {
        //  Reset to defaults in case we're running multiple suites back-to-back
        testsPassed = 0;
        testsFailed = 0;
        testsSkipped = 0;
        suitePassed = true;

        log.info("//////////////////////////////////////////////////////////////////////////////////////");
        log.info("//                         STARTING SUITE");
        log.info("//////////////////////////////////////////////////////////////////////////////////////");
    }

    public void endSuite() {
        log.info("//////////////////////////////////////////////////////////////////////////////////////");
        log.info("//    ENDING SUITE");
        log.info("//    ------------");
        log.info("//    Tests Passed:  " + testsPassed);
        log.info("//    Tests Failed:  " + testsFailed);
        log.info("//    Tests Skipped: " + testsSkipped);
        log.info("//////////////////////////////////////////////////////////////////////////////////////");
    }

    public void step(String message) {
        if (!message.toLowerCase().startsWith("step") && !message.toLowerCase().startsWith("setup - ") && !message.toLowerCase().startsWith("teardown - ")) {
            message = "Step " + stepNumber + " - " + message;
            stepNumber++;
        }

        log.info("");
        log.info("**********************************************************************");
        log.info(message);
        log.info("**********************************************************************");
    }

    public void pass(String message) {
        log.info("    (PASS)  " + message);
    }

    public void fail(String message) {
        this.error("    (FAIL)  " + message);
    }

    public void skip(String message) {
        log.info("");
        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.info(message);
        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        testsSkipped++;
    }

    public void resultWithAssert(String message, Object expected, Object actual) {
        if (!verify(message, expected, actual))
            Assert.fail(message);
    }

    public boolean verify(String message, Object expected, Object actual) {
        log.info("Verifying: " + message);

        boolean pass = compareExpectedAndActualValues(expected, actual);

        if (pass) {
            this.pass("Got: " + actual);
        } else {
            this.fail("Exp: " + expected);
            this.error("            Got: " + actual);
            pass = false;
        }

        return pass;
    }

    public boolean verifyWithNoLogs(String message, Object expected, Object actual) {
        boolean pass = compareExpectedAndActualValues(expected, actual);

        if (!pass) {
            log.info("Verifying: " + message);
            this.fail("Exp: " + expected);
            this.error("            Got: " + actual);
            pass = false;
        } else {
            this.pass(message);
        }

        return pass;
    }

    public boolean verifyForJsonObjects(String message, JsonObjectBase expected, JsonObjectBase actual) {
        log.info("Verifying: " + message);

        expected.resetEqualsInfo();
        verifyWithNoLogs(message, expected, actual);

        if (expected.getEqualsFlag()) {
            this.pass("Json objects are the same!");
        }

        return expected.getEqualsFlag();
    }

    public boolean verifyNotEquals(String message, Object expected, Object actual) {
        boolean pass = true;

        log.info("Verifying Not Equals: " + message);

        if (expected.equals(actual)) {
            this.fail("Got and Exp: " + actual.toString());
            pass = false;
        } else {
            this.pass("Got: " + actual.toString());
        }

        return pass;
    }

    public boolean verifyNotEqualsWithNoLogs(String message, Object expected, Object actual) {
        boolean pass = true;

        if (expected.equals(actual)) {
            this.fail(message + expected);
            this.error("Got and Exp: " + expected);
            pass = false;
        } else {
            this.pass(message);
        }

        return pass;
    }

    public boolean verifyContains(String message, String expectedToLookFor, String actualToSearch) {
        boolean pass = true;

        log.info("Verifying: " + message);

        if (actualToSearch.contains(expectedToLookFor)) {
            this.pass("Contains: " + expectedToLookFor);
        } else {
            this.fail("Actual : " + actualToSearch);
            this.error("            Contain: " + expectedToLookFor);
            pass = false;
        }

        return pass;
    }

    public boolean verifyMatches(String message, String expectedToLookFor, String actualToSearch) {
        return this.verify(message, true, actualToSearch.matches(expectedToLookFor));
    }

    public boolean verifyForListContainsValues(String message, List expectedList, List actualList) {
        int i;
        int actualCount = actualList.size();
        List expectedCopy = new ArrayList(expectedList);
        List actualNotInExpected = new ArrayList(actualList);
        actualNotInExpected.clear();
        boolean pass = false;

        log.info("Verifying: " + message);

        for (i = 0; i < actualCount; i++) {
            if (expectedCopy.contains(actualList.get(i))) {
                // if in both expected and actual then remove them from the expected list
                expectedCopy.remove(expectedCopy.indexOf(actualList.get(i)));
            } else {
                actualNotInExpected.add(actualList.get(i));
            }
        }

        if (expectedCopy.size() == 0 && actualNotInExpected.size() == 0) {
            pass = true;
        }
        if (pass) {
            this.pass("Got: " + actualList.toString());
        } else {
            this.info("Actual: " + actualList.toString());
            this.info("Expect: " + expectedList.toString());

            if (actualNotInExpected.size() > 0)
                this.fail("Actual not in Expected: " + actualNotInExpected.toString());
            if (expectedCopy.size() > 0)
                this.fail("Expected not in Actual: " + expectedCopy.toString());
        }

        return pass;

    }

    public boolean verifyForListContains(String message, List expectedList, List actualList) {
        return this.doListCompare(message, expectedList, actualList, "contains");
    }

    public boolean verifyForListContains(String message, String expectedItem, List actualList) {
        boolean pass = true;

        log.info("Verifying: " + message);

        if (actualList.contains(expectedItem)) {
            this.pass("Contains: " + expectedItem);
        } else {
            this.fail("Actual : " + actualList);
            this.error("            Contain: " + expectedItem);
            pass = false;
        }

        return pass;
    }

    public boolean verifyForListEquals(String message, List expectedList, List actualList) {
        return this.doListCompare(message, expectedList, actualList, "equals");
    }

    public boolean verifyContainsAnyValue(String message, Object expected) {
        boolean pass = true;

        log.info("Verifying: " + message);

        if((expected == null) || expected.toString().equals("") || expected.toString().equals("null")) {
            this.fail("Actual : "+expected);
            pass = false;
        }
        else {
            this.pass("Got: " + expected.toString());
        }

        return pass;
    }

    public boolean verifyForDateTimeIgnoringSeconds(String message, DateTime expected, DateTime actual) {
        String format = "MM/dd/yyyy HH:mm";

        return this.verify(message, expected.toString(format), actual.toString(format));
    }

    private boolean doListCompare(String message, List expectedList, List actualList, String compareType) {
        int i;
        int expectedCount = expectedList.size();
        int actualCount = actualList.size();
        boolean pass = (expectedCount == actualCount);
        boolean result = false;

        log.info("Verifying: " + message);

        for (i = 0; i < Math.max(expectedCount, actualCount); i++) {
            Object expected = (i >= expectedCount) ? "<null>" : expectedList.get(i);
            Object actual = (i >= actualCount) ? "<null>" : actualList.get(i);

            switch (compareType) {
                case "equals":
                    result = expected.equals(actual);
                    break;
                case "contains":
                    result = actual.toString().contains(expected.toString());
                    break;
            }

            if (result) {
                this.pass("Got: " + actual.toString());
            } else {
                this.fail("Exp: " + expected.toString());
                this.error("            Got: " + actual.toString());
                pass = false;
            }
        }

        return pass;

    }

    public boolean verifyForArray(String message, String[] expected, String[] actual) {
        return this.doArrayCompare(message, expected, actual, "equals");
    }

    private boolean doArrayCompare(String message, String[] expected, String[] actual, String compareType) {
        int i;
        int expectedCount = expected.length;
        int actualCount = actual.length;
        int totalToCompare = Math.max(expectedCount, actualCount);
        boolean pass = (expectedCount == actualCount);
        boolean result = false;

        log.info("Verifying: " + message);

        if (totalToCompare == 0) {
            this.pass("Got: Empty!");
            return pass;
        }

        for (i = 0; i < totalToCompare; i++) {
            String expectedItem = (i >= expectedCount) ? "<null>" : expected[i];
            String actualItem = (i >= actualCount) ? "<null>" : actual[i];

            switch (compareType) {
                case "equals":
                    result = expectedItem.equals(actualItem);
                    break;
                case "contains":
                    result = actualItem.contains(expectedItem);
                    break;
            }

            if (result) {
                this.pass("Got: " + actualItem);
            } else {
                this.fail("Exp: " + expectedItem);
                this.error("            Got: " + actualItem);
                pass = false;
            }
        }

        return pass;
    }

    private boolean compareExpectedAndActualValues(Object expected, Object actual) {
        boolean pass = false;

        if (expected==null && actual==null) {
            pass = true;
        } else if (expected instanceof BigDecimal) {
            BigDecimal biggExp = (BigDecimal) expected;

            if ((expected != null) && (actual != null) && (biggExp.compareTo((BigDecimal) actual) == 0)) {
                pass = true;
            }
        } else {
            if ((expected != null) && (actual != null) && (expected.equals(actual))) {
                pass = true;
            }
        }
        return pass;
    }


    // *********************************
    //    Toggle system out printing
    // *********************************
    public void disableSystemOut() {
        try {
            System.setOut(new PrintStream(new NullOutputStream()));
        } catch (Exception e) {
            log.warn(e.toString());
        }
    }
    public void enableSystemOut() {
        System.setOut(systemOut);
    }

}

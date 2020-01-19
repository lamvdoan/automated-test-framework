package com.fetchrewards.common.framework;

import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestBase extends AutomationBase {
    private String currentTestDescription = null;
    private static boolean firstTestInSuite = true;
    private static String currentTestName = null;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            currentTestName = description.getMethodName();
            TestDescription info = description.getAnnotation(TestDescription.class);
            currentTestDescription = (info == null) ? "<No Description>" : info.description();
        }

        protected void finished(Description description) {
            log.endTest();
        }

        protected void failed(Throwable e, Description description) {
            log.debug("!!!!!  In TestWatcher 'failed': " + description);
            log.exception(e);
        }
    };

    // Executes before any test cases start
    @BeforeClass
    public static void beforeClass() {
        firstTestInSuite = true;
    }

    // Executes after all test cases are finished
    @AfterClass
    public static void afterClass() {
        log.trace("");

        log.endSuite();
        log.trace("");
    }

    @Before
    public void beforeEachTest() {
        if (firstTestInSuite) {
            log.startSuite();
        }

        log.startTest(currentTestName, currentTestDescription);
    }

    @After
    public void afterEachTest() {
        log.incrementTestCaseCount();

        if (firstTestInSuite) {
            firstTestInSuite = false;
        }
    }
}

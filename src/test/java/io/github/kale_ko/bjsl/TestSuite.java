package io.github.kale_ko.bjsl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import io.github.kale_ko.bjsl.TestResult.Status;
import io.github.kale_ko.bjsl.elements.ParsedElement;

public class TestSuite {
    static {
        try {
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream("handlers=java.util.logging.ConsoleHandler\njava.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter\njava.util.logging.SimpleFormatter.format=[%3$s] %5$s %n".getBytes()));
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private static Logger logger = Logger.getLogger("Test Suite");

    public static void main(String[] args) {
        BJSL.getLogger().setLevel(Level.OFF);

        logger.info("----- Finding tests -----");

        List<Test> tests = new ArrayList<Test>();

        int i = 1;
        while (i > 0) {
            try {
                Class<?> clazz = Class.forName(TestSuite.class.getPackageName() + ".tests.Test" + i);

                i++;

                try {
                    tests.add((Test) clazz.getDeclaredConstructors()[0].newInstance());
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                i = -1;
            }
        }

        logger.info("Found: " + tests.size());

        logger.info("----- Running tests -----");

        List<Test> succeeded = new ArrayList<Test>();
        List<Test> failed = new ArrayList<Test>();

        for (Test test : tests) {
            TestResult result = null;

            try {
                result = test.run();
            } catch (Exception e) {
                result = new TestResult(Status.FAILED, e);
            }

            if (result.status == Status.SUCCEEDED) {
                succeeded.add(test);

                logger.info(test.getName() + " (" + test.getClass().getSimpleName() + "): Succeeded");
            } else {
                failed.add(test);

                if (result.getResult() instanceof Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    logger.warning(test.getName() + " (" + test.getClass().getSimpleName() + "): Failed with exception:\n" + writer.toString());
                } else if (result.getResult() instanceof ParsedElement element) {
                    logger.warning(test.getName() + " (" + test.getClass().getSimpleName() + "): Failed with result:\n" + BJSL.stringifyJson(element, false));
                } else if (!result.getResult().equals(false)) {
                    logger.warning(test.getName() + " (" + test.getClass().getSimpleName() + "): Failed with result:\n" + result.getResult());
                } else {
                    logger.warning(test.getName() + " (" + test.getClass().getSimpleName() + "): Failed");
                }
            }
        }

        logger.info("------- Finished --------");
        logger.info("Total: " + tests.size());
        logger.info("Succeeded: " + succeeded.size());
        logger.info("Failed: " + failed.size());
        logger.info("-------------------------");
    }
}
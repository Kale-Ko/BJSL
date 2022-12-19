package io.github.kale_ko.bjsl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TestSuite {
    public static void main(String[] args) {
        List<Test> tests = new ArrayList<Test>();
        List<String> succeeded = new ArrayList<String>();
        List<String> failed = new ArrayList<String>();

        BJSL.setLogger(null);

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

        System.out.println("----Running tests----");

        for (Test test : tests) {
            Object result = null;
            boolean success = false;

            try {
                result = test.run();

                if (result.equals(true)) {
                    success = true;
                } else {
                    success = false;
                }
            } catch (Exception e) {
                result = e;

                success = false;
            }

            if (success) {
                succeeded.add(test.getName());

                System.out.println(test.getName() + ": Succeeded");
            } else {
                failed.add(test.getName());

                if (result instanceof Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    System.out.println(test.getName() + ": Failed with exception:\n" + writer.toString());
                } else if (!result.equals(false)) {
                    System.out.println(test.getName() + ": Failed with result:\n" + result.toString());
                } else {
                    System.out.println(test.getName() + ": Failed");
                }
            }
        }

        System.out.println("------Finished-------");
        System.out.println("Total: " + tests.size());
        System.out.println("Succeeded: " + succeeded.size());
        System.out.println("Failed: " + failed.size());
        System.out.println("---------------------");
    }
}
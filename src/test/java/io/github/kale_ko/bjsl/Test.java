package io.github.kale_ko.bjsl;

public abstract class Test {
    protected final String name;

    protected Test(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract TestResult run();
}
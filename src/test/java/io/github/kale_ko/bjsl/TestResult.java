package io.github.kale_ko.bjsl;

public class TestResult {
    public enum Status {
        SUCCEEDED, FAILED
    }

    protected Status status;
    protected Object result;

    public TestResult(Status status) {
        this(status, null);
    }

    public TestResult(Status status, Object result) {
        this.status = status;
        this.result = result;
    }

    public Status getStatus() {
        return this.status;
    }

    public Object getResult() {
        return this.result;
    }
}
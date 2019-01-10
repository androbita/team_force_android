package com.app.salesapp.common;

public class SalesAppEnvironment {
    public static final String TEST = "test";
    private final String environment;

    public SalesAppEnvironment(String environment) {
        this.environment = environment;
    }

    public boolean isTestEnvironment() {
        return TEST.equals(environment);
    }
}

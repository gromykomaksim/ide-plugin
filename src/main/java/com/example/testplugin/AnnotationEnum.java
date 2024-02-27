package com.example.testplugin;

public enum AnnotationEnum {
    HELLO_WORLD("org.example.HelloWorld"),
    LOG_IT("org.example.LogIt");

    private final String fqn;

    AnnotationEnum(String fqn) {
        this.fqn = fqn;
    }

    public String getFqn() {
        return fqn;
    }
}

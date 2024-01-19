package com.example.testplugin;

public enum AnnotationEnum {
    HELLO_WORLD("org.example.HelloWorld");

    private final String fqn;

    private AnnotationEnum(String fqn) {
        this.fqn = fqn;
    }

    public String getFqn() {
        return fqn;
    }
}

package log.it.plugin;

public enum AnnotationEnum {
    LOG_IT("org.example.annotations.LogIt");

    private final String fqn;

    AnnotationEnum(String fqn) {
        this.fqn = fqn;
    }

    public String getFqn() {
        return fqn;
    }
}

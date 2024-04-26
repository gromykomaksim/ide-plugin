package log.it.plugin;

public enum AnnotationEnum {
    LOG_IT("log.it.annotations.LogIt");

    private final String fqn;

    AnnotationEnum(String fqn) {
        this.fqn = fqn;
    }

    public String getFqn() {
        return fqn;
    }
}

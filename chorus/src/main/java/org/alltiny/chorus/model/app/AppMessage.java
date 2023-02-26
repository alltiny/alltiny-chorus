package org.alltiny.chorus.model.app;

public class AppMessage {

    public enum Type { NEUTRAL, SUCCESS, ERROR }

    private final Type type;
    private final String text;

    public AppMessage(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}

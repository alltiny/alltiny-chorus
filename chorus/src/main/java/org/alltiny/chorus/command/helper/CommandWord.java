package org.alltiny.chorus.command.helper;

import java.util.Objects;

public class CommandWord {

    private final String word;
    private final boolean required;

    /**
     * Convenience constructor for required command words.
     */
    public CommandWord(String word) {
        this(word, true);
    }

    public CommandWord(String word, boolean required) {
        this.word = word;
        this.required = required;
    }

    public String getWord() {
        return word;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandWord that = (CommandWord) o;
        return required == that.required && Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, required);
    }

    @Override
    public String toString() {
        return "CommandWord{" +
            "word='" + word + '\'' +
            ", required=" + required +
            '}';
    }
}

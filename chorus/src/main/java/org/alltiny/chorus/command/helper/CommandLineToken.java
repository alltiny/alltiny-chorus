package org.alltiny.chorus.command.helper;

import java.util.Objects;

public class CommandLineToken {

    private String characters;
    private int startPos;
    private int endPos;

    public String getCharacters() {
        return characters;
    }

    public CommandLineToken setCharacters(String characters) {
        this.characters = characters;
        return this;
    }

    public CommandLineToken addCharacter(char character) {
        if (this.characters == null) {
            this.characters = String.valueOf(character);
        } else {
            this.characters += character;
        }
        return this;
    }

    public int getStartPos() {
        return startPos;
    }

    public CommandLineToken setStartPos(int startPos) {
        this.startPos = startPos;
        return this;
    }

    public int getEndPos() {
        return endPos;
    }

    public CommandLineToken setEndPos(int endPos) {
        this.endPos = endPos;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandLineToken that = (CommandLineToken) o;
        return startPos == that.startPos && endPos == that.endPos && Objects.equals(characters, that.characters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characters, startPos, endPos);
    }

    @Override
    public String toString() {
        return "CommandLineToken{" +
            "characters='" + characters + '\'' +
            ", startPos=" + startPos +
            ", endPos=" + endPos +
            '}';
    }
}

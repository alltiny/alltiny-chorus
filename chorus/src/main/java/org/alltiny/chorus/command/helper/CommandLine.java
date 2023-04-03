package org.alltiny.chorus.command.helper;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {

    private enum Mode {
        IN_CMD_LINE,
        IN_TOKEN
    }

    private final String commandLine;
    private final List<CommandLineToken> tokens = new ArrayList<>();

    public CommandLine(String commandLine) {
        this.commandLine = commandLine;

        CommandLineToken token = null;
        Mode mode = Mode.IN_CMD_LINE;
        for (int i = 0; i < commandLine.length(); i++) {
            final char chr = commandLine.charAt(i);
            if (mode == Mode.IN_CMD_LINE) {
                if (!Character.isWhitespace(chr)) {
                    // switch into token mode
                    mode = Mode.IN_TOKEN;
                    token = new CommandLineToken().addCharacter(chr).setStartPos(i);
                }
            } else if (mode == Mode.IN_TOKEN) {
                if (Character.isWhitespace(chr)) {
                    // drop out IN_TOKEN mode
                    mode = Mode.IN_CMD_LINE;
                    token.setEndPos(i - 1);
                    tokens.add(token);
                    token = null;
                } else if (Character.isUpperCase(chr)) {
                    // end the current token and start a new one
                    token.setEndPos(i - 1);
                    tokens.add(token);
                    token = new CommandLineToken().addCharacter(chr).setStartPos(i);
                } else {
                    token.addCharacter(chr);
                }
            }
        }

        if (mode == Mode.IN_TOKEN) {
            mode = Mode.IN_CMD_LINE;
            token.setEndPos(commandLine.length() - 1);
            tokens.add(token);
            token = null;
        }
    }

    public String getCommandLine() {
        return commandLine;
    }

    public List<CommandLineToken> getTokens() {
        return tokens;
    }
}

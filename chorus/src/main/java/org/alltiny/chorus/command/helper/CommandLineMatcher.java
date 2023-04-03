package org.alltiny.chorus.command.helper;

import java.util.List;

public class CommandLineMatcher {

    private final CommandLine commandLine;
    private final List<CommandWord> words;

    private boolean isMatching = true;
    private int argumentStartPos;

    /**
     * Convenience constructor.
     */
    public CommandLineMatcher(List<CommandWord> words, String commandLine) {
        this(words, new CommandLine(commandLine));
    }

    public CommandLineMatcher(List<CommandWord> words, CommandLine commandLine) {
        this.words = words;
        this. commandLine = commandLine;

        int lastMatchingToken = -1;

        for (CommandWord cmdWord : words) {
            CommandLineToken token = (commandLine.getTokens().size() > lastMatchingToken + 1)
                ? commandLine.getTokens().get(lastMatchingToken + 1)
                : null;
            if (token == null) {
                if (cmdWord.isRequired()) {
                    isMatching = false;
                }
                break;
            }
            if (cmdWord.getWord().startsWith(token.getCharacters().toLowerCase())) {
                lastMatchingToken++;
            } else if (cmdWord.isRequired()) {
                isMatching = false;
                break;
            }
        }

        argumentStartPos = lastMatchingToken + 1;
    }

    public boolean isMatching() {
        return isMatching;
    }

    public List<CommandLineToken> getArguments() {
        return commandLine.getTokens().subList(argumentStartPos, commandLine.getTokens().size());
    }
}

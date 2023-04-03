package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.command.helper.CommandLineMatcher;
import org.alltiny.chorus.command.helper.CommandWord;
import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ClearCommandQueueCommand extends Command<ClearCommandQueueCommand> {

    private static final List<CommandWord> commandWords = Arrays.asList(
        new CommandWord("clear"), new CommandWord("commands")
    );

    public ClearCommandQueueCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return new CommandLineMatcher(commandWords, getAppModel().getCommandLine()).isMatching();
    }

    @Override
    public String getUsageOneLine() {
        return "clear commands";
    }

    @Override
    public String getDescriptionOneLine() {
        return "clears the command history";
    }

    @Override
    public Function<Void, ExecutedCommand<ClearCommandQueueCommand>> getExecutableFunction() {
        return unused -> {
            getAppModel().getCommandQueueDone().clear();
            getAppModel().getCommandQueueUndo().clear();
            return new ExecutedCommand<ClearCommandQueueCommand>()
                .setResolvedCommand("clear commands")
                .setSuccessful(true);
        };
    }
}

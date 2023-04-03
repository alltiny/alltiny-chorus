package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.command.helper.CommandLineMatcher;
import org.alltiny.chorus.command.helper.CommandWord;
import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ClearMessageQueueCommand extends Command<ClearMessageQueueCommand> {

    private static final List<CommandWord> commandWords = Arrays.asList(
        new CommandWord("clear"), new CommandWord("messages")
    );

    public ClearMessageQueueCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return new CommandLineMatcher(commandWords, getAppModel().getCommandLine()).isMatching();
    }

    @Override
    public String getUsageOneLine() {
        return "clear messages";
    }

    @Override
    public String getDescriptionOneLine() {
        return "clears all messages";
    }

    @Override
    public Function<Void, ExecutedCommand<ClearMessageQueueCommand>> getExecutableFunction() {
        return unused -> {
            getAppModel().getApplicationMessageQueue().clear();
            return new ExecutedCommand<ClearMessageQueueCommand>()
                .setResolvedCommand("clear messages")
                .setSuccessful(true);
        };
    }
}

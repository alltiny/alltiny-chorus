package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.function.Function;

public class ClearCommandQueueCommand extends Command<ClearCommandQueueCommand> {

    public ClearCommandQueueCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return "clear commands".startsWith(getAppModel().getCommandLine());
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

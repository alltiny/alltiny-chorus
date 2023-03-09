package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.model.app.AppMessage;
import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRegistry {

    private final ApplicationModel appModel;

    private final List<Command<?>> commands;

    public CommandRegistry(ApplicationModel appModel) {
        this.appModel = appModel;
        this.commands = Arrays.asList(
            new AddVoiceCommand(appModel),
            new ClearCommandQueueCommand(appModel),
            new ClearMessageQueueCommand(appModel),
            new HelpCommand(this),
            new OpenFileCommand(appModel),
            new ShowVoicesCommand(appModel)
        );
    }

    public ApplicationModel getApplicationModel() {
        return appModel;
    }

    public List<Command<?>> getCommands() {
        return commands;
    }

    public boolean execute() {
        List<Command<?>> responsibleCommands = commands.stream()
            .filter(command -> command.feelsResponsible())
            .collect(Collectors.toList());

        if (responsibleCommands.size() == 1) {
            ExecutedCommand<?> executedCommand = responsibleCommands.get(0).getExecutableFunction().apply(null);
            if (executedCommand != null && executedCommand.isSuccessful()) {
                appModel.getCommandQueueDone().add(executedCommand);
                appModel.setCommandLine("");
                return true;
            } else {
                return false;
            }
        } else if (responsibleCommands.isEmpty()) {
            appModel.getApplicationMessageQueue().add(
                new AppMessage(AppMessage.Type.ERROR, "no matching command found. try 'help'")
            );
        } else {
            appModel.getApplicationMessageQueue().add(
                new AppMessage(AppMessage.Type.ERROR, "which command? " +
                    responsibleCommands.stream()
                        .map(Command::getUsageOneLine)
                        .map(cmd -> "'" + cmd + "'")
                        .collect(Collectors.joining(" "))
                )
            );
        }
        return false;
    }

    public boolean execute(String command) {
        appModel.setCommandLine(command);
        return execute();
    }
}

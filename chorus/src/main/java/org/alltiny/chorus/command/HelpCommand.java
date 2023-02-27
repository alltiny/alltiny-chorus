package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.model.app.AppMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HelpCommand extends Command<HelpCommand> {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        super(registry.getApplicationModel());
        this.registry = registry;
    }

    @Override
    public boolean feelsResponsible() {
        return "help".startsWith(getAppModel().getCommandLine());
    }

    @Override
    public String getUsageOneLine() {
        return "help [topic]";
    }

    @Override
    public String getDescriptionOneLine() {
        return "prints help";
    }

    @Override
    public Function<Void, ExecutedCommand<HelpCommand>> getExecutableFunction() {
        return unused -> {
            getAppModel().getApplicationMessageQueue().add(
                new AppMessage(AppMessage.Type.NEUTRAL, getCommandOverview())
            );
            return new ExecutedCommand<HelpCommand>()
                .setResolvedCommand("help")
                .setSuccessful(true);
        };
    }

    public String getCommandOverview() {
        return registry.getCommands()
            .stream()
            .map(c -> "<tr><td>" + c.getUsageOneLine() + "</td><td>&nbsp;:&nbsp;</td><td>" + c.getDescriptionOneLine() + "</td></tr>")
            .collect(Collectors.joining("", "<html><table cellpadding=\"0\" cellspacing=\"0\">", "</table></html>"));
    }
}

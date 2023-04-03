package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.command.helper.CommandLineMatcher;
import org.alltiny.chorus.command.helper.CommandWord;
import org.alltiny.chorus.model.app.AppMessage;
import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ShowVoicesCommand extends Command<ShowVoicesCommand> {

    private static final List<CommandWord> commandWords = Arrays.asList(
        new CommandWord("show"), new CommandWord("voices")
    );

    private static final Pattern pattern = Pattern.compile("show voices");

    public ShowVoicesCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return new CommandLineMatcher(commandWords, getAppModel().getCommandLine()).isMatching();
    }

    @Override
    public String getUsageOneLine() {
        return "show voices";
    }

    @Override
    public String getDescriptionOneLine() {
        return "shows the currently existing voices";
    }

    @Override
    public Function<Void, ExecutedCommand<ShowVoicesCommand>> getExecutableFunction() {
        return unused -> {
            if (getAppModel().getCurrentSong() == null) {
                getAppModel().getApplicationMessageQueue().add(
                    new AppMessage(AppMessage.Type.NEUTRAL, "no song exists"));
            }

            if (getAppModel().getCurrentSong().getMusic() == null) {
                getAppModel().getApplicationMessageQueue().add(
                    new AppMessage(AppMessage.Type.NEUTRAL, "song has no music data"));
            }

            final AtomicInteger pos = new AtomicInteger(1);
            getAppModel().getApplicationMessageQueue().add(
                new AppMessage(AppMessage.Type.NEUTRAL,
                    getAppModel().getCurrentSong().getMusic().getVoices()
                        .stream()
                        .map(voice -> pos.getAndIncrement() + " : '" + voice.getName() + "' " + (voice.getMuted() ? "(muted)" : "(unmuted)"))
                        .collect(Collectors.joining("<br/>", "<html>", "</html>"))
                ));

            return new ExecutedCommand<ShowVoicesCommand>()
                .setResolvedCommand("show voices").setSuccessful(true);
        };
    }
}

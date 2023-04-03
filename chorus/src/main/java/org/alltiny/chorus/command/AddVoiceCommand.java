package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.command.helper.CommandLineMatcher;
import org.alltiny.chorus.command.helper.CommandLineToken;
import org.alltiny.chorus.command.helper.CommandWord;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.model.app.AppMessage;
import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddVoiceCommand extends Command<AddVoiceCommand> {

    private static final List<CommandWord> commandWords = Arrays.asList(
        new CommandWord("add"), new CommandWord("voice")
    );

    public AddVoiceCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return new CommandLineMatcher(commandWords, getAppModel().getCommandLine()).isMatching();
    }

    @Override
    public String getUsageOneLine() {
        return "add voice [pos]? [name]?";
    }

    @Override
    public String getDescriptionOneLine() {
        return "adds a new voice with the given [name] at the given [pos] (first position is 1)";
    }

    @Override
    public Function<Void, ExecutedCommand<AddVoiceCommand>> getExecutableFunction() {
        return unused -> {
            final CommandLineMatcher matcher = new CommandLineMatcher(commandWords, getAppModel().getCommandLine());
            if (!matcher.isMatching()) {
                return null;
            }
            final List<CommandLineToken> tokens = matcher.getArguments();

            final String pos = getPos(tokens);
            final String name = getName(tokens);

            if (getAppModel().getCurrentSong() == null) {
                getAppModel().setCurrentSong(new Song());
            }

            Voice voice = new Voice();

            if (name != null) {
                voice.setName(name);
            }

            if (pos != null) {
                int p = Integer.parseInt(pos);
                getAppModel().getCurrentSong().getMusic().addVoice(voice, p - 1);
            } else {
                getAppModel().getCurrentSong().getMusic().addVoice(voice);
            }

            getAppModel().getApplicationMessageQueue().add(
                new AppMessage(AppMessage.Type.SUCCESS, "added voice" +
                    (pos != null ? " " + pos : "") +
                    (name != null ? " " + name : "")
                    ));

            return new ExecutedCommand<AddVoiceCommand>()
                .setResolvedCommand("add voice" +
                    (pos != null ? " " + pos : "") +
                    (name != null ? " " + name : "")
                ).setSuccessful(true);
        };
    }

    private static String getPos(List<CommandLineToken> tokens) {
        if (tokens.size() > 0 && tokens.get(0).getCharacters().matches("\\d+")) {
            return tokens.get(0).getCharacters();
        } else {
            return null;
        }
    }

    private static String getName(List<CommandLineToken> tokens) {
        return tokens.subList((getPos(tokens) == null ? 0 : 1), tokens.size()).stream()
            .map(CommandLineToken::getCharacters)
            .collect(Collectors.joining(" "));
    }
}

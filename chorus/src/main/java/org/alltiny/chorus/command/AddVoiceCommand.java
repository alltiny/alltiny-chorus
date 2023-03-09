package org.alltiny.chorus.command;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.model.app.AppMessage;
import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddVoiceCommand extends Command<AddVoiceCommand> {

    private static final Pattern pattern = Pattern.compile("add voice(\\s+(?<pos>\\d+))?(\\s+(?<name>.*))?");

    public AddVoiceCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return pattern.matcher(getAppModel().getCommandLine()).matches();
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
            Matcher matcher = pattern.matcher(getAppModel().getCommandLine());
            if (!matcher.matches()) {
                return null;
            }
            final String pos = matcher.group("pos");
            final String name = matcher.group("name");

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
}

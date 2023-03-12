package org.alltiny.chorus.model.app;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.model.generic.DOMList;
import org.alltiny.chorus.model.generic.DOMMap;
import org.alltiny.chorus.model.generic.DOMOperation;

public class ApplicationModel extends DOMMap<ApplicationModel,Object> {

    public enum Property {
        APP_MODE,
        APP_MESSAGE_QUEUE,
        COMMAND_LINE,
        COMMAND_QUEUE_DONE,
        COMMAND_QUEUE_UNDO,
        CURRENT_SONG,
        TEMPO_FACTOR,
    }

    public ApplicationModel() {
        setApplicationMode(ApplicationMode.PLAY);
        put(Property.COMMAND_QUEUE_DONE.name(), new DOMList<DOMList<?, ExecutedCommand<?>>,ExecutedCommand<?>>());
        put(Property.COMMAND_QUEUE_UNDO.name(), new DOMList<DOMList<?,Command>,Command>());
        put(Property.APP_MESSAGE_QUEUE.name(), new DOMList<DOMList<?,AppMessage>,AppMessage>());
        put(Property.TEMPO_FACTOR.name(), 1f);
    }

    public ApplicationMode getApplicationMode() {
        return (ApplicationMode)get(Property.APP_MODE.name());
    }

    public ApplicationModel setApplicationMode(ApplicationMode mode) {
        put(Property.APP_MODE.name(), mode);
        return this;
    }

    public String getCommandLine() {
        return (String)get(Property.COMMAND_LINE.name());
    }

    public ApplicationModel setCommandLine(String commandLine) {
        put(Property.COMMAND_LINE.name(), commandLine);
        return this;
    }

    public DOMList<DOMList<?, ExecutedCommand<?>>,ExecutedCommand<?>> getCommandQueueDone() {
        return (DOMList<DOMList<?, ExecutedCommand<?>>,ExecutedCommand<?>>)get(Property.COMMAND_QUEUE_DONE.name());
    }

    public DOMList<DOMList<?,Command>,Command> getCommandQueueUndo() {
        return (DOMList<DOMList<?,Command>,Command>)get(Property.COMMAND_QUEUE_UNDO.name());
    }

    public DOMList<DOMList<?,AppMessage>,AppMessage> getApplicationMessageQueue() {
        return (DOMList<DOMList<?,AppMessage>,AppMessage>)get(Property.APP_MESSAGE_QUEUE.name());
    }

    public Song getCurrentSong() {
        return (Song)get(Property.CURRENT_SONG.name());
    }

    public ApplicationModel setCurrentSong(Song song) {
        put(Property.CURRENT_SONG.name(), song);
        return this;
    }

    public ApplicationModel setCurrentSong(Song song, DOMOperation operation) {
        put(Property.CURRENT_SONG.name(), song, operation);
        return this;
    }

    public float getTempoFactor() {
        return (float)get(Property.TEMPO_FACTOR.name());
    }

    public ApplicationModel setTempoFactor(float factor) {
        put(Property.TEMPO_FACTOR.name(), factor);
        return this;
    }


}

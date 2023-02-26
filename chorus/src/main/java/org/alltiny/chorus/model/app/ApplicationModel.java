package org.alltiny.chorus.model.app;

import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.generic.model.DOMList;
import org.alltiny.chorus.generic.model.DOMMap;

public class ApplicationModel extends DOMMap<ApplicationModel,Object> {

    public final static String PROP_APP_MODE = "applicationMode";
    public final static String PROP_COMMAND_LINE = "commandLine";
    public final static String PROP_COMMAND_QUEUE_DONE = "commandQueueDone";
    public final static String PROP_COMMAND_QUEUE_UNDO = "commandQueueUndo";
    public final static String PROP_APP_MESSAGE_QUEUE = "applicationMessageQueue";

    public ApplicationModel() {
        setApplicationMode(ApplicationMode.PLAY);
        put(PROP_COMMAND_QUEUE_DONE, new DOMList<DOMList<?, ExecutedCommand<?>>,ExecutedCommand<?>>());
        put(PROP_COMMAND_QUEUE_UNDO, new DOMList<DOMList<?,Command>,Command>());
        put(PROP_APP_MESSAGE_QUEUE, new DOMList<DOMList<?,AppMessage>,AppMessage>());
    }

    public ApplicationMode getApplicationMode() {
        return (ApplicationMode)get(PROP_APP_MODE);
    }

    public ApplicationModel setApplicationMode(ApplicationMode mode) {
        put(PROP_APP_MODE, mode);
        return this;
    }

    public String getCommandLine() {
        return (String)get(PROP_COMMAND_LINE);
    }

    public ApplicationModel setCommandLine(String commandLine) {
        put(PROP_COMMAND_LINE, commandLine);
        return this;
    }

    public DOMList<DOMList<?, ExecutedCommand<?>>,ExecutedCommand<?>> getCommandQueueDone() {
        return (DOMList<DOMList<?, ExecutedCommand<?>>,ExecutedCommand<?>>)get(PROP_COMMAND_QUEUE_DONE);
    }

    public DOMList<DOMList<?,Command>,Command> getCommandQueueUndo() {
        return (DOMList<DOMList<?,Command>,Command>)get(PROP_COMMAND_QUEUE_UNDO);
    }

    public DOMList<DOMList<?,AppMessage>,AppMessage> getApplicationMessageQueue() {
        return (DOMList<DOMList<?,AppMessage>,AppMessage>)get(PROP_APP_MESSAGE_QUEUE);
    }
}

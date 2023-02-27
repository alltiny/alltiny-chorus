package org.alltiny.chorus.command.generic;

import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.function.Function;

public abstract class Command<Self extends Command<?>> {

    private final ApplicationModel appModel;

    public Command(ApplicationModel appModel) {
        this.appModel = appModel;
    }

    public ApplicationModel getAppModel() {
        return appModel;
    }

    public abstract boolean feelsResponsible();

    public abstract String getUsageOneLine();

    public abstract String getDescriptionOneLine();

    public abstract Function<Void,ExecutedCommand<Self>> getExecutableFunction();
}

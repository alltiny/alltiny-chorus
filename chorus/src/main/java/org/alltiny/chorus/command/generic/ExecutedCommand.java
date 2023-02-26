package org.alltiny.chorus.command.generic;

import org.alltiny.chorus.model.app.ApplicationModel;

import java.util.function.Function;

public class ExecutedCommand<ExecutableCommand> {

    private String enteredCommand;
    private String resolvedCommand;
    private boolean successful;
    private String response;
    private Function<ApplicationModel,ExecutedCommand> revertFunction;

    public String getEnteredCommand() {
        return enteredCommand;
    }

    public ExecutedCommand<ExecutableCommand> setEnteredCommand(String enteredCommand) {
        this.enteredCommand = enteredCommand;
        return this;
    }

    public String getResolvedCommand() {
        return resolvedCommand;
    }

    public ExecutedCommand<ExecutableCommand> setResolvedCommand(String resolvedCommand) {
        this.resolvedCommand = resolvedCommand;
        return this;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public ExecutedCommand<ExecutableCommand> setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public ExecutedCommand<ExecutableCommand> setResponse(String response) {
        this.response = response;
        return this;
    }

    public Function<ApplicationModel, ExecutedCommand> getRevertFunction() {
        return revertFunction;
    }

    public ExecutedCommand<ExecutableCommand> setRevertFunction(Function<ApplicationModel, ExecutedCommand> revertFunction) {
        this.revertFunction = revertFunction;
        return this;
    }
}

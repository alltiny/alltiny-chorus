package org.alltiny.xml.handler;

/**
 * This interface defines a factory which creates {@link XMLHandler}-instances.
 * This factory is used by {@link XMLDocument} to create the top-most root
 * handler.
 * @param <Type> type of node for which the created handlers are.
 */
public interface XMLHandlerFactory<Type> {

    public XMLHandler<Type> createInstance(AssignHandler<Type> assignHandler);
}

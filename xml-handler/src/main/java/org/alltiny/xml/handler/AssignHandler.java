package org.alltiny.xml.handler;

/**
 * This interface describes an assign handler which is able to assign the given
 * {@param node} to itself. Each {@link XMLHandler} implementation will require
 * an special assign handler to which it can pass the created item.
 *
 * @param <Type> type of node which can be assigned.
 */
public interface AssignHandler<Type> {

    public void assignNode(Type node) throws AssignException;
}

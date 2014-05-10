package org.alltiny.xml.handler;

/**
 * This assign handler may be used to ignore the assignment procedure.
 * @param <Type> type of node which can be assigned.
 */
public class NullAssignHandler<Type> implements AssignHandler<Type> {

    public void assignNode(Type node) throws AssignException {
        /* do nothing in here */
    }
}

package org.alltiny.chorus.render;

/**
 * This class represents
 * @param <Type> type of elements this container can carry.
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.02.2009 19:09:34
 */
public interface Container<Type> {

    public void addChild(Type child);
}

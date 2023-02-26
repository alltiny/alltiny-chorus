package org.alltiny.chorus.command.generic;

/**
 * This is a marker interface for revertable commands.
 */
public interface HasRevert {

    void revert();
}

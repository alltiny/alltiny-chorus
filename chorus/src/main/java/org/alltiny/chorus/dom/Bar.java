package org.alltiny.chorus.dom;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 11:32:15
 */
public class Bar extends DurationElement {

    private boolean keepBeatDuration = false;

    public Bar(int duration, int division) {
        setDuration(duration);
        setDivision(division);
    }

    public boolean isKeepBeatDuration() {
        return keepBeatDuration;
    }

    public void setKeepBeatDuration(boolean keepBeatDuration) {
        this.keepBeatDuration = keepBeatDuration;
    }
}

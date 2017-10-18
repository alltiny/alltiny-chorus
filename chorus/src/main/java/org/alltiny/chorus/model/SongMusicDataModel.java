package org.alltiny.chorus.model;

import org.alltiny.chorus.dom.Element;
import org.alltiny.chorus.dom.DurationElement;
import org.alltiny.chorus.dom.Bar;
import org.alltiny.chorus.dom.Voice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.02.2009 18:37:10
 */
public class SongMusicDataModel implements MusicDataModel {

    private static final int NOTE = 192; // measure a 1/1 note with 192 ticks.

    private final SongModel model;
    private final ArrayList<Frame> frames = new ArrayList<Frame>();

    public SongMusicDataModel(SongModel model) {
        this.model = model;

        model.addPropertyChangeListener(SongModel.CURRENT_SONG, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
        // get into a valid initial state.
        update();
    }

    private void update() {
        // clear previous frames.
        frames.clear();

        if (model.getSong() == null) {
            return;
        }

        final int numVoice = getNumberOfVoices();

        long[] covered = new long[numVoice];
        long barOffset = 0;
        long barLength = Long.MAX_VALUE;
        Iterator<Element>[] iter = new Iterator[numVoice];
        for (int i = 0; i < numVoice; i++) {
            covered[i] = 0;
            iter[i] = model.getSong().getMusic().getVoices().get(i).getSequence().iterator();
        }
        boolean wasBarDefined = false;

        for (;;) { // forever
            // find the smallest tick count.
            long smallest = covered[0];
            for (long current : covered) {
                if (current < smallest) {
                    smallest = current;
                }
            }

            // if even the smallest tick count is MAX_VALUE, then break.
            if (smallest == Long.MAX_VALUE) {
                break;
            }

            Frame currentFrame = new Frame(numVoice, smallest);
            if (wasBarDefined) {
                currentFrame.setNewBarStarts(((smallest - barOffset) % barLength) == 0);
            }

            // involve each voice into rendering which is currently on the smallest tick count.
            for (int current = 0; current < numVoice; current++) {
                while (smallest == covered[current]) {
                    // render the next element of the found voice.
                    if (iter[current].hasNext()) {
                        Element element = iter[current].next();
                        if (element instanceof Bar) {
                            // refresh the voices bar-offset and bar-length.
                            barOffset = smallest;
                            barLength = Math.round(NOTE * ((Bar)element).getLength());
                            wasBarDefined = true;
                            if (smallest != 0) { // whenever a bar-element is found render a bar (unless it is still the beginning of the score)
                                currentFrame.setNewBarStarts(true);
                            }
                        } else if (element instanceof DurationElement) {
                            // recalculate the covered tick count of this voice.
                            final long currentLength = Math.round(NOTE * ((DurationElement)element).getLength());
                            covered[current] += currentLength;
                            // add the element to the cell.
                            currentFrame.add(current, element, currentLength);
                        }
                    } else { // this voice has no more elements.
                        covered[current] = Long.MAX_VALUE; // set the tick count on max to exclude it from further searching.
                    }
                }
            }
            frames.add(currentFrame);
        }
    }

    public int getNumberOfVoices() {
        return model.getSong() != null ? model.getSong().getMusic().getVoices().size() : 0;
    }

    public int getNumberOfFrames() {
        return frames.size();
    }

    public Voice getVoice(int index) {
        return model.getSong() != null ? model.getSong().getMusic().getVoices().get(index) : null;
    }

    public List<Frame> getFrames() {
        return frames;
    }
}

package org.alltiny.chorus.model;

import org.alltiny.chorus.dom.Element;
import org.alltiny.chorus.dom.DurationElement;
import org.alltiny.chorus.dom.Bar;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.generic.Context;
import org.alltiny.chorus.model.generic.DOMHierarchicalListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.02.2009 18:37:10
 */
public class SongMusicDataModel implements MusicDataModel {

    private static final int NOTE = 192; // measure a 1/1 note with 192 ticks.

    private final ApplicationModel model;
    private final ArrayList<Frame> frames = new ArrayList<Frame>();

    public SongMusicDataModel(ApplicationModel model) {
        this.model = model;

        model.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.CURRENT_SONG.name()),
                new DOMHierarchicalListener.Callback<Song,String>() {
                    @Override
                    public void added(Song song, String property, Context<?> context) {
                        update();
                    }

                    @Override
                    public void changed(Song song, String property, Context<?> context) {
                        update();
                    }

                    @Override
                    public void removed(String property, Context<?> context) {
                        update();
                    }
                }).setName(getClass().getSimpleName() + "@SONG"));
    }

    private void update() {
        // clear previous frames.
        frames.clear();

        if (model.getCurrentSong() == null) {
            return;
        }

        final int numVoice = getNumberOfVoices();

        long[] covered = new long[numVoice];
        long barOffset = 0;
        long barLength = Long.MAX_VALUE;
        Iterator<Element<?>>[] iter = new Iterator[numVoice];
        for (int i = 0; i < numVoice; i++) {
            covered[i] = 0;
            iter[i] = model.getCurrentSong().getMusic().getVoices().get(i).getSequence().getElements().iterator();
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
                            currentFrame.setNewBarStarts(true);
                            currentFrame.setBar((Bar)element);
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
        return model.getCurrentSong() != null ? model.getCurrentSong().getMusic().getVoices().size() : 0;
    }

    public int getNumberOfFrames() {
        return frames.size();
    }

    public Voice getVoice(int index) {
        return model.getCurrentSong() != null ? model.getCurrentSong().getMusic().getVoices().get(index) : null;
    }

    public List<Frame> getFrames() {
        return frames;
    }
}

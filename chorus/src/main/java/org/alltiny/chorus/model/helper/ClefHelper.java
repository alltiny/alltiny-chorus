package org.alltiny.chorus.model.helper;

import org.alltiny.chorus.base.type.Clef;
import org.alltiny.chorus.dom.Element;
import org.alltiny.chorus.dom.Music;
import org.alltiny.chorus.dom.Note;
import org.alltiny.chorus.dom.Sequence;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.generic.Context;
import org.alltiny.chorus.model.generic.DOMHierarchicalListener;
import org.alltiny.chorus.model.generic.DOMList;
import org.alltiny.chorus.model.generic.DOMOperation;

public class ClefHelper {

    private final ApplicationModel model;

    public ClefHelper(ApplicationModel model) {
        this.model = model;

        model.addListener(
            new DOMHierarchicalListener<ApplicationModel,Song,String>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.CURRENT_SONG.name()),
            new DOMHierarchicalListener<Song,Music,String>(
                new DOMHierarchicalListener.PropertyOnMap<>(Song.class, Song.Property.MUSIC.name()),
            new DOMHierarchicalListener<Music, DOMList<?,Voice>,String>(
                new DOMHierarchicalListener.PropertyOnMap<>(Music.class, Music.Property.VOICES.name()),
            new DOMHierarchicalListener<DOMList<?,Voice>,Voice,Integer>(
                new DOMHierarchicalListener.AnyItemInList<>(),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Voice.class, Voice.Property.MAIN_SEQUENCE.name()),
            new DOMHierarchicalListener<Sequence,Clef,String>(
                new DOMHierarchicalListener.PropertyOnMap<>(Sequence.class, Sequence.Property.CLEF.name()),
                new DOMHierarchicalListener.Callback<Clef,String>() {
                    @Override
                    public void added(Clef clef, String property, Context<?> context) {
                        updateElementsInVoice((Voice)context.getParent().getNode(), clef);
                    }

                    @Override
                    public void changed(Clef clef, String property, Context<?> context) {
                        updateElementsInVoice((Voice)context.getParent().getNode(), clef);
                    }

                    @Override
                    public void removed(Clef clef, String property, Context<?> context) {}
                })
        ))))).setName(ClefHelper.class.getSimpleName() + "@CLEF"));

        model.addListener(
            new DOMHierarchicalListener<ApplicationModel,Song,String>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.CURRENT_SONG.name()),
            new DOMHierarchicalListener<Song,Music,String>(
                new DOMHierarchicalListener.PropertyOnMap<>(Song.class, Song.Property.MUSIC.name()),
            new DOMHierarchicalListener<Music, DOMList<?,Voice>,String>(
                new DOMHierarchicalListener.PropertyOnMap<>(Music.class, Music.Property.VOICES.name()),
            new DOMHierarchicalListener<DOMList<?,Voice>,Voice,Integer>(
                new DOMHierarchicalListener.AnyItemInList<>(),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Voice.class, Voice.Property.MAIN_SEQUENCE.name()),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Sequence.class, Sequence.Property.ELEMENTS.name()),
            new DOMHierarchicalListener<DOMList<?,Element<?>>,Element<?>,Integer>(
                new DOMHierarchicalListener.AnyItemInList<>(),
                new DOMHierarchicalListener.Callback<Element<?>,Integer>() {
                    @Override
                    public void added(Element<?> element, Integer index, Context<?> context) {
                        if (element instanceof Note) {
                            Sequence sequence = (Sequence)context.getParent().getNode();
                            ((Note)element).setCurrentClef(sequence.getClef());
                        }
                    }

                    @Override
                    public void changed(Element<?> element, Integer index, Context<?> context) {}

                    @Override
                    public void removed(Element<?> element, Integer index, Context<?> context) {}
                })
        )))))).setName(ClefHelper.class.getSimpleName() + "@SEQUENCE-ADDED"));
    }

    /**
     * update is done on the voice because inline-sequences must also be changed.
     */
    public void updateElementsInVoice(Voice voice, Clef clef) {
        if (voice == null || clef == null) {
            return;
        }

        final DOMOperation operation = new DOMOperation("updating note clef");

        voice.getSequence().getElements().stream()
            .filter(element -> element instanceof Note)
            .map(e -> (Note)e)
            .forEach(note -> note.setCurrentClef(clef, operation));

        voice.getInlineSequences().stream()
            .flatMap(seq -> seq.getElements().stream())
            .filter(element -> element instanceof Note)
            .map(e -> (Note)e)
            .forEach(note -> note.setCurrentClef(clef, operation));
    }
}

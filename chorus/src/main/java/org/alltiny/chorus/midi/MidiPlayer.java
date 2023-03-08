package org.alltiny.chorus.midi;

import org.alltiny.chorus.dom.*;
import org.alltiny.chorus.dom.decoration.Decoration;
import org.alltiny.chorus.dom.decoration.Bound;

import javax.sound.midi.*;
import javax.sound.midi.Sequence;
import java.util.HashMap;
import java.util.function.Consumer;

import org.alltiny.base.model.PropertySupportBean;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.generic.Context;
import org.alltiny.chorus.model.generic.DOMHierarchicalListener;
import org.alltiny.chorus.model.generic.DOMOperation;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 17.12.2007 23:14:45
 */
public class MidiPlayer extends PropertySupportBean implements Consumer<DOMOperation> {

    public static final String PLAYABLE = "playable";
    public static final String RUNNING = "running";
    public static final String ABSOLUTE_LENGTH = "absoluteLength";
    public static final String CURRENT_POSITION = "currentPosition";
    public static final String ABSOLUTE_MILLISEC = "absoluteMillisec";
    public static final String CURRENT_MILLISEC = "currentMillisec";

    private static final int PPQ = 48; // one quarter note is 48 pulses.
    private static final int FULLNOTE = PPQ * 4; // ticks for one full note 1/1

    private final ApplicationModel model;

    private Sequencer sequencer;
    private SequencerObserverThread observer = null;

    public MidiPlayer(ApplicationModel model) {
        this.model = model;

        model.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.CURRENT_SONG.name()),
                new DOMHierarchicalListener.Callback<Song,String>() {
                    @Override
                    public void added(Song song, String property, Context<?> context) {
                        if (context.getOperation() != null) {
                            context.getOperation().addConclusionListener(MidiPlayer.this);
                        } else {
                            update();
                        }
                    }

                    @Override
                    public void changed(Song song, String property, Context<?> context) {
                        if (context.getOperation() != null) {
                            context.getOperation().addConclusionListener(MidiPlayer.this);
                        } else {
                            update();
                        }
                    }

                    @Override
                    public void removed(String property, Context<?> context) {
                        if (context.getOperation() != null) {
                            context.getOperation().addConclusionListener(MidiPlayer.this);
                        } else {
                            update();
                        }
                    }
                }).setName(getClass().getSimpleName() + "@SONG"));

        model.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.CURRENT_SONG.name()),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Song.class, Song.Property.MUSIC.name()),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Music.class, Music.Property.VOICES.name()),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.AnyItemInList<>(),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Voice.class, Voice.Property.MUTED.name()),
                new DOMHierarchicalListener.Callback<Boolean,String>() {
                    @Override
                    public void added(Boolean muted, String property, Context<?> context) {
                        MidiPlayer.this.setTrackMute((Integer)context.getParent().getIdentifier(), muted != null && muted);
                    }

                    @Override
                    public void changed(Boolean muted, String property, Context<?> context) {
                        MidiPlayer.this.setTrackMute((Integer)context.getParent().getIdentifier(), muted != null && muted);
                    }

                    @Override
                    public void removed(String property, Context<?> context) {
                        MidiPlayer.this.setTrackMute((Integer)context.getParent().getIdentifier(), false);
                    }
                })
            )))).setName(getClass().getSimpleName() + "@MUTED"));

        model.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.TEMPO_FACTOR.name()),
                new DOMHierarchicalListener.Callback<Float,String>() {
                    @Override
                    public void added(Float factor, String property, Context<?> context) {
                        MidiPlayer.this.setTempoFactor(factor != null ? factor : 1);
                    }

                    @Override
                    public void changed(Float factor, String property, Context<?> context) {
                        MidiPlayer.this.setTempoFactor(factor != null ? factor : 1);
                    }

                    @Override
                    public void removed(String property, Context<?> context) {
                        MidiPlayer.this.setTempoFactor(1);
                    }
                }).setName(getClass().getSimpleName() + "@TEMPO_FACTOR"));
    }

    @Override
    public void accept(DOMOperation operation) {
        update();
    }

    public void update() {
        if (sequencer != null) {
            sequencer.close();
        }

        pcs.firePropertyChange(PLAYABLE, null, false);
        pcs.firePropertyChange(RUNNING, null, false);

        if (model.getCurrentSong() == null) {
            return;
        }

        try {
            sequencer = MidiSystem.getSequencer();

            for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
                System.out.println("Info: " + info.getName() + " - " + info.getDescription());
            }

            Synthesizer synthesizer = MidiSystem.getSynthesizer();

            synthesizer.open();
            synthesizer.loadAllInstruments(synthesizer.getDefaultSoundbank());

            Sequence seq = new Sequence(Sequence.PPQ, PPQ);//song.getTempo()*FULLNOTE/480); // 4*4*60
            Track track = seq.createTrack();

            long usecPerQNote = Math.round(60000000d / model.getCurrentSong().getTempo());
            track.add(createTempoMessage(usecPerQNote, 0));

            Bar currentBar = null;

            int channel = 0;
            int currentVoice = 0;
            for (Voice voice : model.getCurrentSong().getMusic().getVoices()) {
                // clear/recreate the HashMap for caching the anchor ticks.
                HashMap<Integer,Anchor> anchors = new HashMap<Integer,Anchor>();

                // get the velocity for "mp".
                int velocity = model.getCurrentSong().getDynamic().getValue("mf");

                // define the instrument for that voice.
                track.add(createMidiMessage(ShortMessage.PROGRAM_CHANGE, channel, 53, 0, 0));

                long tick = 0;
                NoteBinding curBinding = null;
                for (Element element : voice.getSequence().getElements()) {
                    if (element instanceof Note) {
                        Note note = (Note)element;

                        NoteBinding binding = null;
                        // determine whether this note has a binding
                        for (Decoration decor : note.getDecorations()) {
                            if (decor instanceof Bound) {
                                binding = new NoteBinding(((Bound)decor).getRef(), note.getNoteValue().getMidiValue());
                            }
                        }

                        final int duration = (int)Math.round(FULLNOTE * note.getLength());

                        // check if a current binding is active.
                        if (curBinding != null) {
                            // if this note is unbound, then terminate the current binding.
                            if (binding == null) {
                                track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getNoteValue().getMidiValue(), velocity, tick));
                            } else { // if this note has a binding, but the note values and the ids differ, then do also stop the previous note.
                                if (curBinding.getNoteValue() != binding.getNoteValue() || curBinding.getId() != binding.getId()) {
                                    track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                    track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getNoteValue().getMidiValue(), velocity, tick));
                                }
                                // else the note values are the same, so do not stop the previous note, neighter do start this note.
                            }
                        } else { // no current binding is active, just play the note.
                            track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getNoteValue().getMidiValue(), velocity, tick));
                        }

                        tick += duration;

                        if (binding == null) {
                            track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, note.getNoteValue().getMidiValue(), 65, tick));
                        }

                        curBinding = binding;
                    }
                    if (element instanceof Rest) {
                        if (curBinding != null) { // if this is not a note, but binding is active.
                            // then terminate the current binding.
                            track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), 65, tick));
                            curBinding = null;
                        }

                        Rest rest = (Rest)element;
                        final int duration = (int)Math.round(FULLNOTE * rest.getLength());
                        tick += duration;
                    }
                    if (element instanceof Bar && currentVoice == 0) { // only process bars in the first track, which is the master track for timing information.
                        Bar bar = (Bar)element;
                        // check whether this bar should keep the duration. (works only if the currentBar is already set.)
                        if (currentBar != null && bar.isKeepBeatDuration()) {
                            usecPerQNote = Math.round(usecPerQNote * (currentBar.getLength() / bar.getLength()));
                            track.add(createTempoMessage(usecPerQNote, tick));
                        }
                        // this bar gets the now currentBar
                        currentBar = bar;

                    }
                    if (element instanceof DynamicElement) {
                        // if an dynamical element is found than tune the velocity corresponding.
                        velocity = model.getCurrentSong().getDynamic().getValue(((DynamicElement)element).getKey());
                    }
                    if (element instanceof Anchor) {
                        // if an anchor is found than cache the current tick count in the map.
                        Anchor anchor = (Anchor)element;
                        // cache the current values in the anchor.
                        anchor.setTick(tick);
                        anchor.setVelocity(velocity);
                        anchors.put(anchor.getRef(), anchor);
                    }
                    if (element instanceof AbsoluteTempoChange) {
                        AbsoluteTempoChange tempoChange = (AbsoluteTempoChange)element;
                        // recalculate the quarter note duration.
                        usecPerQNote = Math.round(60000000d / tempoChange.getNumberPerMinute() * tempoChange.getNote().getDivision() / 4 / tempoChange.getNote().getDuration());
                        track.add(createTempoMessage(usecPerQNote, tick));
                    }
                    if (element instanceof RelativeTempoChange) {
                        RelativeTempoChange tempoChange = (RelativeTempoChange)element;
                        // recalculate the quarter note duration.
                        usecPerQNote *= tempoChange.getDurationFactor();
                        track.add(createTempoMessage(usecPerQNote, tick));
                    }
                }

                if (curBinding != null) { // check if a binding note is still active.
                    // then terminate the current binding.
                    track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), 65, tick));
                    curBinding = null;
                }

                // now render the inlineSequences from this voice.
                for (InlineSequence inlineSeq : voice.getInlineSequences()) {
                    // set the tick count for this inline sequence onto the cached count for it's anchor.
                    Anchor anchor = anchors.get(inlineSeq.getAnchorRef());
                    // initialize the current value from the anchor.
                    tick = anchor.getTick();
                    velocity = anchor.getVelocity();

                    for (Element element : inlineSeq.getElements()) {
                        // an inline sequence may only have notes and rests.
                        if (element instanceof Note) {
                            Note note = (Note)element;

                            NoteBinding binding = null;
                            // determine whether this note has a binding
                            for (Decoration decor : note.getDecorations()) {
                                if (decor instanceof Bound) {
                                    binding = new NoteBinding(((Bound)decor).getRef(), note.getNoteValue().getMidiValue());
                                }
                            }

                            final long duration = Math.round(FULLNOTE * note.getLength());

                            // check if a current is active.
                            if (curBinding != null) {
                                // if this note in unboundand, then terminate the current binding.
                                if (binding == null) {
                                    track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                    track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getNoteValue().getMidiValue(), velocity, tick));
                                } else { // if this note has a binding, but the note values and the ids differ, then do also stop the previous note.
                                    if (curBinding.getNoteValue() != binding.getNoteValue() || curBinding.getId() != binding.getId()) {
                                        track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                        track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getNoteValue().getMidiValue(), velocity, tick));
                                    }
                                    // else the note values are the same, so do not stop the previous note, neither do start this note.
                                }
                            } else { // no current binding is active, just play the note.
                                track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getNoteValue().getMidiValue(), velocity, tick));
                            }

                            tick += duration;

                            if (binding == null) {
                                track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, note.getNoteValue().getMidiValue(), 65, tick));
                            }

                            curBinding = binding;
                        }
                        if (element instanceof Rest) {
                            if (curBinding != null) { // if this is not a note, but binding is active.
                                // then terminate the current binding.
                                track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), 65, tick));
                            }
                            Rest rest = (Rest)element;
                            final long duration = Math.round(FULLNOTE * rest.getLength());
                            tick += duration;
                        }
                        if (element instanceof DynamicElement) {
                            // if an dynamical element is found than tune the velocity corresponding.
                            velocity = model.getCurrentSong().getDynamic().getValue(((DynamicElement)element).getKey());
                        }
                    }

                    if (curBinding != null) { // check if a binding note is still active.
                        // then terminate the current binding.
                        track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), 65, tick));
                        curBinding = null;
                    }
                }

                track = seq.createTrack();
                //channel++;
                currentVoice++;
            }

            sequencer.setSequence(seq);
            sequencer.open();

            System.out.println("Sequencer-Receiver: " + sequencer.getReceiver());
            // NOTE: the tempo changes have only effect when the sequencer is already opened.
            //sequencer.setTempoInMPQ(60000000f/model.getSong().getTempo());
            //sequencer.setTempoFactor(1f);

            pcs.firePropertyChange(CURRENT_POSITION, null, 0l);
            pcs.firePropertyChange(ABSOLUTE_LENGTH, null, getAbsoluteLength());
            pcs.firePropertyChange(CURRENT_MILLISEC, null, 0l);
            pcs.firePropertyChange(ABSOLUTE_MILLISEC, null, getAbsoluteMillisec());
            pcs.firePropertyChange(PLAYABLE, null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isPlayable() {
        return sequencer != null && sequencer.isOpen();
    }

    public boolean isRunning() {
        return sequencer != null && sequencer.isRunning();
    }

    public void start() {
        if (!sequencer.isRunning()) {
            sequencer.start();

            // start the observer thread which observes the sequencer.
            observer = new SequencerObserverThread();
            observer.start();

            pcs.firePropertyChange(RUNNING, null, true);
        }
    }

    public void stop() {
        if (sequencer.isRunning()) {
            observer.terminate();
            sequencer.stop();
            pcs.firePropertyChange(RUNNING, null, false);
        }
    }

    public void setTickPosition(long position) {
        long old = sequencer.getTickPosition();
        long oldSec = sequencer.getMicrosecondPosition();
        sequencer.setTickPosition(position);
        pcs.firePropertyChange(CURRENT_POSITION, old, position);
        pcs.firePropertyChange(CURRENT_MILLISEC, oldSec, sequencer.getMicrosecondPosition());
    }

    public void setTrackMute(int index, boolean mute) {
        sequencer.setTrackMute(index, mute);
    }

    public void setTempoFactor(float factor) {
        if (sequencer != null) {
            sequencer.setTempoFactor(factor);
        }
    }

    public float getTempoFactor() {
        return sequencer.getTempoFactor();
    }

    public long getAbsoluteLength() {
        return sequencer.getTickLength();
    }

    public long getAbsoluteMillisec() {
        return sequencer.getMicrosecondLength();
    }

    public long getTickPosition() {
        return (sequencer != null) ? sequencer.getTickPosition() : 0;
    }

    public long getCurrentMicrosec() {
        return sequencer.getMicrosecondPosition();
    }

    public static MidiEvent createMidiMessage(int command, int channel, int data1, int data2, long tick) throws InvalidMidiDataException {
        ShortMessage message = new ShortMessage();
        message.setMessage(command, channel, data1, data2);
        return new MidiEvent(message, tick);
    }

    public static MidiEvent createTempoMessage(long usecPerQNote, long tick) throws InvalidMidiDataException {
        MetaMessage tempo = new MetaMessage();
        tempo.setMessage(0x51, new byte[]{(byte)((usecPerQNote & 0xff0000) >> 16), (byte)((usecPerQNote & 0xff00) >> 8), (byte)(usecPerQNote & 0xff)}, 3);
        return new MidiEvent(tempo, tick);
    }

    private class SequencerObserverThread extends Thread {

        private long lastPos = 0;
        private long lastSec = 0;
        private boolean running = true;

        public void run() {
            while (running) {
                // determine if the sequencer is still playing.
                if (!sequencer.isRunning()) {
                    // inform listeners.
                    pcs.firePropertyChange(RUNNING, true, false);
                    // cause this thread to terminate.
                    running = false;
                    // set the current position to the beginning.
                    setTickPosition(0);
                    return;
                } else { // since sequencer is still running update the positions.
                    // determine the new current position.
                    long newPos = sequencer.getTickPosition();
                    long newSec = sequencer.getMicrosecondPosition();
                    pcs.firePropertyChange(CURRENT_POSITION, lastPos, newPos);
                    pcs.firePropertyChange(CURRENT_MILLISEC, lastSec, newSec);
                    lastPos = newPos;
                    lastSec = newSec;
                }

                try {
                    sleep(20); // resolves in an update rate of 50Hz
                } catch (InterruptedException e) {
                    /* nothing to do on interruption. */
                }
            }
        }

        public void terminate() {
            running = false;
        }
    }
}

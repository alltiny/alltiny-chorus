package org.alltiny.chorus.midi;

import org.alltiny.chorus.dom.*;
import org.alltiny.chorus.dom.decoration.Decoration;
import org.alltiny.chorus.dom.decoration.Bound;
import org.alltiny.chorus.model.SongModel;

import javax.sound.midi.*;
import javax.sound.midi.Sequence;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;

import org.alltiny.base.model.PropertySupportBean;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 17.12.2007 23:14:45
 */
public class MidiPlayer extends PropertySupportBean {

    public static final String PLAYABLE = "playable";
    public static final String RUNNING = "running";
    public static final String ABSOLUTE_LENGTH = "absoluteLength";
    public static final String CURRENT_POSITION = "currentPosition";
    public static final String ABSOLUTE_MILLISEC = "absoluteMillisec";
    public static final String CURRENT_MILLISEC = "currentMillisec";

    private static final int PPQ = 48; // one quarter note is 48 pulses.
    private static final int FULLNOTE = PPQ * 4; // ticks for one full note 1/1

    private final SongModel model;

    private Sequencer sequencer;
    private SequencerObserverThread observer = null;

    public MidiPlayer(SongModel model) {
        this.model = model;

        model.addPropertyChangeListener(SongModel.CURRENT_SONG, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });

        // get this class in a valid state.
        update();
    }

    public void update() {
        if (sequencer != null) {
            sequencer.close();
        }

        pcs.firePropertyChange(PLAYABLE, null, false);
        pcs.firePropertyChange(RUNNING, null, false);

        if (model.getSong() == null) {
            return;
        }

        try {
            sequencer = MidiSystem.getSequencer();

            for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
                System.out.println("Info: " + info.getName() + " - " + info.getDescription());
            }

            Synthesizer synthesizer = MidiSystem.getSynthesizer();

            synthesizer.open();
            //synthesizer.loadInstrument(synthesizer.getAvailableInstruments()[0]);
            //System.out.println("Instrument 0: " + synthesizer.getAvailableInstruments()[0]);
//
//            // connect the synthesizer and the sequencer.
//            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
//
//            Instrument[] instruments = synthesizer.getLoadedInstruments();
//            for (MidiChannel channel : synthesizer.getChannels()) {
//                channel.programChange(instruments[0].getPatch().getBank(),instruments[0].getPatch().getProgram());
//            }

            Sequence seq = new Sequence(Sequence.PPQ, PPQ);//song.getTempo()*FULLNOTE/480); // 4*4*60
            Track track = seq.createTrack();

            long usecPerQNote = Math.round(60000000d / model.getSong().getTempo());
            track.add(createTempoMessage(usecPerQNote, 0));

            Bar currentBar = null;

            int channel = 0;
            int currentVoice = 0;
            for (Voice voice : model.getSong().getMusic().getVoices()) {
                // clear/recreate the HashMap for caching the anchor ticks.
                HashMap<Integer,Anchor> anchors = new HashMap<Integer,Anchor>();

                // get the velocity for "mp".
                int velocity = model.getSong().getDynamic().getValue("mf");

                // define the instrument for that voice.
                track.add(createMidiMessage(ShortMessage.PROGRAM_CHANGE, channel, 2, 0, 0));

                long tick = 0;
                NoteBinding curBinding = null;
                for (Element element : voice.getSequence()) {
                    if (element instanceof Note) {
                        Note note = (Note)element;

                        NoteBinding binding = null;
                        // determine whether this note has a binding
                        for (Decoration decor : note.getDecorations()) {
                            if (decor instanceof Bound) {
                                binding = new NoteBinding(((Bound)decor).getRef(), note.getMidiValue());
                            }
                        }

                        final int duration = (int)Math.round(FULLNOTE * note.getLength());

                        // check if a current binding is active.
                        if (curBinding != null) {
                            // if this note is unbound, then terminate the current binding.
                            if (binding == null) {
                                track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getMidiValue(), velocity, tick));
                            } else { // if this note has a binding, but the note values and the ids differ, then do also stop the previous note.
                                if (curBinding.getNoteValue() != binding.getNoteValue() || curBinding.getId() != binding.getId()) {
                                    track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                    track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getMidiValue(), velocity, tick));
                                }
                                // else the note values are the same, so do not stop the previous note, neighter do start this note.
                            }
                        } else { // no current binding is active, just play the note.
                            track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getMidiValue(), velocity, tick));
                        }

                        tick += duration;

                        if (binding == null) {
                            track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, note.getMidiValue(), 65, tick));
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
                        // check whether thsi bar should keep the duration. (works only if the currentBar is already set.)
                        if (currentBar != null && bar.isKeepBeatDuration()) {
                            track.add(createTempoMessage(Math.round(usecPerQNote * 4 * Math.round(FULLNOTE * currentBar.getLength()) * bar.getLength() / 4), tick));
                        }
                        // this bar gets the now currentBar
                        currentBar = bar;

                    }
                    if (element instanceof DynamicElement) {
                        // if an dynamical element is found than tune the velocity corresponing.
                        velocity = model.getSong().getDynamic().getValue(((DynamicElement)element).getKey());
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
                    Anchor anchor = anchors.get(inlineSeq.getAnchor().getRef());
                    // initialize the current value from the anchor.
                    tick = anchor.getTick();
                    velocity = anchor.getVelocity();

                    for (Element element : inlineSeq) {
                        // an inline sequence may only have notes and rests.
                        if (element instanceof Note) {
                            Note note = (Note)element;

                            NoteBinding binding = null;
                            // determine whether this note has a binding
                            for (Decoration decor : note.getDecorations()) {
                                if (decor instanceof Bound) {
                                    binding = new NoteBinding(((Bound)decor).getRef(), note.getMidiValue());
                                }
                            }

                            final long duration = Math.round(FULLNOTE * note.getLength());

                            // check if a current is active.
                            if (curBinding != null) {
                                // if this note in unboundand, then terminate the current binding.
                                if (binding == null) {
                                    track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                    track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getMidiValue(), velocity, tick));
                                } else { // if this note has a binding, but the note values and the ids differ, then do also stop the previous note.
                                    if (curBinding.getNoteValue() != binding.getNoteValue() || curBinding.getId() != binding.getId()) {
                                        track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, curBinding.getNoteValue(), velocity, tick));
                                        track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getMidiValue(), velocity, tick));
                                    }
                                    // else the note values are the same, so do not stop the previous note, neighter do start this note.
                                }
                            } else { // no current binding is active, just play the note.
                                track.add(createMidiMessage(ShortMessage.NOTE_ON, channel, note.getMidiValue(), velocity, tick));
                            }

                            tick += duration;

                            if (binding == null) {
                                track.add(createMidiMessage(ShortMessage.NOTE_OFF, channel, note.getMidiValue(), 65, tick));
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
                            // if an dynamical element is found than tune the velocity corresponing.
                            velocity = model.getSong().getDynamic().getValue(((DynamicElement)element).getKey());
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
        sequencer.setTempoFactor(factor);
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
                    sleep(15);
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

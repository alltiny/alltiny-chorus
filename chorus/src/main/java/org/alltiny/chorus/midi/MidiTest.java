package org.alltiny.chorus.midi;

import javax.sound.midi.*;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 17.12.2007 23:14:45
 */
public class MidiTest {

    public static void main(String[] args) {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();

            Sequence seq = new Sequence(Sequence.PPQ, 120);
            Track track = seq.createTrack();

            track.add(createMidiMessage(ShortMessage.NOTE_ON, 0, 60, 127, 0));
            track.add(createMidiMessage(ShortMessage.NOTE_OFF,0, 60, 127, 120));
            track.add(createMidiMessage(ShortMessage.NOTE_ON, 0, 65, 64, 60));
            track.add(createMidiMessage(ShortMessage.NOTE_OFF,0, 65, 64, 180));

            sequencer.setSequence(seq);
            sequencer.open();
            sequencer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static MidiEvent createMidiMessage(int command, int channel, int data1, int data2, long tick) throws InvalidMidiDataException {
        ShortMessage message = new ShortMessage();
        message.setMessage(command, channel, data1, data2);
        return new MidiEvent(message, tick);
    }
}

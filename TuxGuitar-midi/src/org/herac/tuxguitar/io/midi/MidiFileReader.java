package org.herac.tuxguitar.io.midi;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

public class MidiFileReader implements MidiFileHeader {

  public static boolean CANCEL_RUNNING_STATUS_ON_META_AND_SYSEX = true;

  public Sequence getSequence(InputStream stream) throws InvalidMidiDataException, IOException {
    try {
      MidiFileFormat fileFormat = MidiSystem.getMidiFileFormat(stream);
      int type = fileFormat.getType();
      if (type < 0 || type > 2) {
        throw new InvalidMidiDataException("corrupt MIDI file: illegal type");
      } else if (type == 2) {
        throw new InvalidMidiDataException("this implementation doesn't support type 2 MIDI files");
      }
      Sequence sequence = MidiSystem.getSequence(stream);
      return sequence;
    } finally {
      stream.close();
    }
  }
}
package org.herac.tuxguitar.io.midi;

import java.io.IOException;
import java.io.OutputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

public class MidiFileWriter {

  public void write(Sequence sequence, int type, OutputStream stream) throws IOException {
    MidiSystem.write(sequence, type, stream);
  }
}

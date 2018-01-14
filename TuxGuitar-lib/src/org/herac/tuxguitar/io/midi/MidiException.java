package org.herac.tuxguitar.io.midi;

/**
 * Indicates invalid or malformed MIDI data.
 * <p>
 * Serves mostly as an internal {@code RuntimeException} wrapper for
 * {@code javax.sound.midi.InvalidMidiDataException}.
 * 
 * @author FS
 */
public class MidiException extends RuntimeException {
  private static final long serialVersionUID = 6516668654747210797L;

  public MidiException(String message, Throwable cause) {
    super(message, cause);
  }

  public MidiException(String message) {
    super(message);
  }

  public MidiException(Throwable cause) {
    super(cause);
  }
}

package org.herac.tuxguitar.io.midi.base;

import java.nio.charset.Charset;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.ShortMessage;

import org.herac.tuxguitar.song.models.TGTimeSignature;

public class MidiMessageFactory {

  public static ShortMessage noteOn(int channel, int note, int velocity) throws InvalidMidiDataException {
    return MidiMessageFactory.shortMessage(ShortMessage.NOTE_ON, fixChannel(channel), fixValue(note),
        fixValue(velocity));
  }

  public static ShortMessage noteOff(int channel, int note, int velocity) throws InvalidMidiDataException {
    return MidiMessageFactory.shortMessage(ShortMessage.NOTE_OFF, fixChannel(channel), fixValue(note),
        fixValue(velocity));
  }

  public static ShortMessage controlChange(int channel, int controller, int value) throws InvalidMidiDataException {
    return MidiMessageFactory.shortMessage(ShortMessage.CONTROL_CHANGE, fixChannel(channel), fixValue(controller),
        fixValue(value));
  }

  public static ShortMessage programChange(int channel, int instrument) throws InvalidMidiDataException {
    return MidiMessageFactory.shortMessage(ShortMessage.PROGRAM_CHANGE, fixChannel(channel), fixValue(instrument));
  }

  public static ShortMessage pitchBend(int channel, int value) throws InvalidMidiDataException {
    return MidiMessageFactory.shortMessage(ShortMessage.PITCH_BEND, fixChannel(channel), 0, fixValue(value));
  }

  public static ShortMessage systemReset() throws InvalidMidiDataException {
    return MidiMessageFactory.shortMessage(ShortMessage.SYSTEM_RESET);
  }

  public static MetaMessage trackName(String trackName) throws InvalidMidiDataException {
    return MidiMessageFactory.metaMessage(MetaMessageTypes.TRACK_NAME, trackName.getBytes(Charset.forName("UTF-8")));
  }

  public static MetaMessage tempoInUSQ(int usq) throws InvalidMidiDataException {
    byte[] data = new byte[]{(byte) ((usq >> 16) & 0xff), (byte) ((usq >> 8) & 0xff), (byte) ((usq) & 0xff)};
    return MidiMessageFactory.metaMessage(MetaMessageTypes.TEMPO_CHANGE, data);
  }

  public static MetaMessage timeSignature(TGTimeSignature ts) throws InvalidMidiDataException {
    byte[] data = new byte[]{
      (byte) ts.getNumerator(), (byte) ts.getDenominator().getIndex(), (byte) (96 / ts.getDenominator().getValue()), 8};
    return MidiMessageFactory.metaMessage(MetaMessageTypes.TIME_SIGNATURE_CHANGE, data);
  }

  public static MetaMessage endOfTrack() throws InvalidMidiDataException {
    return MidiMessageFactory.metaMessage(MetaMessageTypes.END_OF_TRACK, new byte[0]);
  }

  //
  // Internal helpers
  //

  private static int fixValue(int value) {
    int fixedValue = value;
    fixedValue = Math.min(fixedValue, 127);
    fixedValue = Math.max(fixedValue, 0);
    return fixedValue;
  }

  private static int fixChannel(int channel) {
    int fixedChannel = channel;
    fixedChannel = Math.min(fixedChannel, 15);
    fixedChannel = Math.max(fixedChannel, 0);
    return fixedChannel;
  }

  private static ShortMessage shortMessage(int command, int channel, int data1, int data2)
      throws InvalidMidiDataException {
    return new ShortMessage(command, channel, data1, data2);
  }

  private static ShortMessage shortMessage(int command, int channel, int data) throws InvalidMidiDataException {
    return new ShortMessage(command, channel, data, 0x00);
  }

  private static ShortMessage shortMessage(int command) throws InvalidMidiDataException {
    return new ShortMessage(command, 0x00, 0x00, 0x00);
  }

  private static MetaMessage metaMessage(int command, byte[] data) throws InvalidMidiDataException {
    return new MetaMessage(command, data, data.length);
  }
}

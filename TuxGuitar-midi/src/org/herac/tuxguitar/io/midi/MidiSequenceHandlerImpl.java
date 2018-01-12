package org.herac.tuxguitar.io.midi;

import java.io.IOException;
import java.io.OutputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import org.herac.tuxguitar.gm.GMChannelRoute;
import org.herac.tuxguitar.gm.GMChannelRouter;
import org.herac.tuxguitar.io.midi.base.MidiMessageFactory;
import org.herac.tuxguitar.player.base.MidiSequenceHandler;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGTimeSignature;

public class MidiSequenceHandlerImpl extends MidiSequenceHandler{
	
	private OutputStream stream;
	private Sequence sequence;
	private GMChannelRouter router;
	
	public MidiSequenceHandlerImpl(int tracks, GMChannelRouter router, OutputStream stream) throws InvalidMidiDataException {
		super(tracks);
		this.router = router;
		this.stream = stream;
		this.init();
	}
	
	private void init() throws InvalidMidiDataException {
		this.sequence = new Sequence(Sequence.PPQ, (int)TGDuration.QUARTER_TIME);
		for (int i = 0; i < getTracks(); i++) {
			this.sequence.createTrack();
		}
	}
	
	public Sequence getSequence(){
		return this.sequence;
	}
	
	private int resolveChannel(GMChannelRoute gmChannel, boolean bendMode){
		return (bendMode ? gmChannel.getChannel2() : gmChannel.getChannel1());
	}
	
	public void addEvent(int track, MidiEvent event) {
		if(track >= 0 && track < getSequence().getTracks().length){
			getSequence().getTracks()[track].add(event);
		}
	}
	
	public void addNoteOff(long tick, int track, int channelId, int note, int velocity, int voice, boolean bendMode) throws InvalidMidiDataException {
		GMChannelRoute gmChannel = this.router.getRoute(channelId);
		if( gmChannel != null ){
			addEvent(track, new MidiEvent(MidiMessageFactory.noteOff(resolveChannel(gmChannel, bendMode), note, velocity), tick));
		}
	}
	
	public void addNoteOn(long tick,int track,int channelId, int note, int velocity, int voice, boolean bendMode) throws InvalidMidiDataException {
		GMChannelRoute gmChannel = this.router.getRoute(channelId);
		if( gmChannel != null ){
			addEvent(track,new MidiEvent(MidiMessageFactory.noteOn(resolveChannel(gmChannel, bendMode), note, velocity), tick ));
		}
	}
	
	public void addPitchBend(long tick,int track,int channelId, int value, int voice, boolean bendMode) throws InvalidMidiDataException {
		GMChannelRoute gmChannel = this.router.getRoute(channelId);
		if( gmChannel != null ){
			addEvent(track,new MidiEvent(MidiMessageFactory.pitchBend(resolveChannel(gmChannel,bendMode), value), tick ));
		}
	}
	
	public void addControlChange(long tick,int track,int channelId, int controller, int value) throws InvalidMidiDataException {
		GMChannelRoute gmChannel = this.router.getRoute(channelId);
		if( gmChannel != null ){
			addEvent(track,new MidiEvent(MidiMessageFactory.controlChange(gmChannel.getChannel1(), controller, value), tick ));
			if( gmChannel.getChannel1() != gmChannel.getChannel2() ){
				addEvent(track,new MidiEvent(MidiMessageFactory.controlChange(gmChannel.getChannel2(), controller, value), tick ));
			}
		}
	}
	
	public void addProgramChange(long tick,int track,int channelId, int instrument) throws InvalidMidiDataException {
		GMChannelRoute gmChannel = this.router.getRoute(channelId);
		if( gmChannel != null ){
			addEvent(track,new MidiEvent(MidiMessageFactory.programChange(gmChannel.getChannel1(), instrument), tick ));
			if( gmChannel.getChannel1() != gmChannel.getChannel2() ){
				addEvent(track,new MidiEvent(MidiMessageFactory.programChange(gmChannel.getChannel2(), instrument), tick ));
			}
		}
	}
	
	public void addTrackName(long tick, int track, String name) throws InvalidMidiDataException {
		addEvent(track, new MidiEvent(MidiMessageFactory.trackName(name), tick));
	}
	
	public void addTempoInUSQ(long tick,int track,int usq) throws InvalidMidiDataException {
		addEvent(track,new MidiEvent(MidiMessageFactory.tempoInUSQ(usq), tick ));
	}
	
	public void addTimeSignature(long tick,int track,TGTimeSignature ts) throws InvalidMidiDataException {
		addEvent(track,new MidiEvent(MidiMessageFactory.timeSignature(ts), tick ));
	}
	
	public void notifyFinish() {
		try {
			new MidiFileWriter().write(getSequence(), 1, this.stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

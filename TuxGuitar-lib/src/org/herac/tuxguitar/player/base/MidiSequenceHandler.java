package org.herac.tuxguitar.player.base;

import javax.sound.midi.InvalidMidiDataException;

import org.herac.tuxguitar.song.models.TGTimeSignature;

public abstract class MidiSequenceHandler {
	
	private int tracks;
	
	public MidiSequenceHandler(int tracks){
		this.tracks = tracks;
	}
	
	public int getTracks() {
		return this.tracks;
	}
	
	public abstract void addNoteOn(long tick,int track,int channel,int note,int velocity,int voice,boolean bendMode) throws InvalidMidiDataException;
	
	public abstract void addNoteOff(long tick,int track,int channel,int note,int velocity,int voice,boolean bendMode) throws InvalidMidiDataException;
	
	public abstract void addPitchBend(long tick,int track,int channel,int value,int voice,boolean bendMode) throws InvalidMidiDataException;
	
	public abstract void addControlChange(long tick,int track,int channel,int controller,int value) throws InvalidMidiDataException;
	
	public abstract void addProgramChange(long tick,int track,int channel,int instrument) throws InvalidMidiDataException;
	
	public abstract void addTrackName(long tick, int track, String name) throws InvalidMidiDataException;
	
	public abstract void addTempoInUSQ(long tick,int track,int usq) throws InvalidMidiDataException;
	
	public abstract void addTimeSignature(long tick,int track,TGTimeSignature ts) throws InvalidMidiDataException;
	
	public abstract void notifyFinish() throws InvalidMidiDataException;
}

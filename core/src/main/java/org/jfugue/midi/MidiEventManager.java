/*
 * JFugue, an Application Programming Interface (API) for Music Programming
 * http://www.jfugue.org
 *
 * Copyright (C) 2003-2014 David Koelle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jfugue.midi;

import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import org.jfugue.theory.Note;

/**
 * Places musical data into the MIDI sequence. Package scope, final class.
 *
 * @author David Koelle
 */
final class MidiEventManager extends TrackTimeManager {

  private final Logger logger = Logger.getLogger("org.jfugue");
  private Sequence sequence;
  private Track[] track;
  private float divisionType;
  private int resolutionTicksPerBeat;
  private int tempoBeatsPerMinute;
  private float mpqn;
  private byte metronomePulse;
  private byte thirtysecondNotesPer24MidiClockSignals;

  /**
   * <p>Constructor for MidiEventManager.</p>
   */
  public MidiEventManager() {
    super();
    setDefaults();
  }

  /**
   * <p>Constructor for MidiEventManager.</p>
   *
   * @param divisionType a float.
   * @param resolution a int.
   */
  public MidiEventManager(float divisionType, int resolution) {
    super();
    this.divisionType = divisionType;
    this.resolutionTicksPerBeat = resolution;
  }

  private void setDefaults() {
    sequence = null;
    track = new Track[MidiDefaults.TRACKS];
    divisionType = MidiDefaults.DEFAULT_DIVISION_TYPE;
    resolutionTicksPerBeat = MidiDefaults.DEFAULT_RESOLUTION_TICKS_PER_BEAT;
    tempoBeatsPerMinute = MidiDefaults.DEFAULT_TEMPO_BEATS_PER_MINUTE;
    mpqn = 60000000
      / MidiDefaults.DEFAULT_TEMPO_BEATS_PER_MINUTE; // MPQN = Milliseconds per quarter note
    metronomePulse = MidiDefaults.DEFAULT_METRONOME_PULSE;
    thirtysecondNotesPer24MidiClockSignals = MidiDefaults.DEFAULT_THIRTYSECOND_NOTES_PER_24_MIDI_CLOCK_SIGNALS; // Default value
  }

  /**
   * <p>reset.</p>
   *
   * @throws javax.sound.midi.InvalidMidiDataException if any.
   */
  public void reset() throws InvalidMidiDataException {
    setDefaults();
    this.sequence = new Sequence(divisionType, resolutionTicksPerBeat);
    createTrack((byte) 0);
    this.tempoBeatsPerMinute = MidiDefaults.DEFAULT_TEMPO_BEATS_PER_MINUTE;
  }

  @Override
  void createTrack(byte track) {
    super.createTrack(track);
    this.track[track] = sequence.createTrack();
  }

  /**
   * <p>Getter for the field <code>divisionType</code>.</p>
   *
   * @return a float.
   */
  public float getDivisionType() {
    return this.divisionType;
  }

  private void setDivisionType(float divisionType) {
    this.divisionType = divisionType;
  }

  private void setResolution(int resolution) {
    this.resolutionTicksPerBeat = resolution;
  }

  /**
   * <p>setTempo.</p>
   *
   * @param tempoBPM a int.
   */
  public void setTempo(int tempoBPM) {
    this.tempoBeatsPerMinute = tempoBPM;
    this.mpqn = 60000000 / tempoBPM; // MPQN = microseconds per minute / BPM

    // Tempo is set in terms of microseconds per quarter note (MPQN), encoded in three big-endian bytes.
    byte[] bytes = new byte[3];
    bytes[0] = (byte) ((int) mpqn >> 16);
    bytes[1] = (byte) ((int) mpqn >> 8);
    bytes[2] = (byte) ((int) mpqn);
    this.addMetaMessage(MidiDefaults.META_TEMPO, bytes);
  }

  /**
   * <p>setTimeSignature.</p>
   *
   * @param beatsPerMeasure a byte.
   * @param durationForBeat a byte.
   */
  public void setTimeSignature(byte beatsPerMeasure, byte durationForBeat) {
    // Denominator passed to meta message is actually the power by which 2 must be raised to equal the
    // duration of a beat. Example: Given a time signature of 5/8, we must pass 3, because 2^3 = 8.
    byte d2 = (byte) (Math.log(durationForBeat) / Math.log(2));
    this.addMetaMessage(MidiDefaults.META_TIMESIG,
      new byte[]{beatsPerMeasure, d2, getMetronomePulse(), get32ndNotesPer24MidiClockSignals()});
  }

  /* MIDI-Specific Settings */

  private byte getMetronomePulse() {
    return this.metronomePulse;
  }

  /**
   * <p>Setter for the field <code>metronomePulse</code>.</p>
   *
   * @param metronomePulse a byte.
   */
  public void setMetronomePulse(byte metronomePulse) {
    this.metronomePulse = metronomePulse;
  }

  private byte get32ndNotesPer24MidiClockSignals() {
    return this.thirtysecondNotesPer24MidiClockSignals;
  }

  /**
   * <p>set32ndNotesPer24MidiClockSignals.</p>
   *
   * @param t a byte.
   */
  public void set32ndNotesPer24MidiClockSignals(byte t) {
    this.thirtysecondNotesPer24MidiClockSignals = t;
  }

  /**
   * <p>setSequenceResolution.</p>
   *
   * @param divisionType a float.
   * @param resolution a int.
   */
  public void setSequenceResolution(float divisionType, int resolution) {
    this.setDivisionType(divisionType);
    this.setResolution(resolution);
  }

  /**
   * <p>getSequenceDivisionType.</p>
   *
   * @return a float.
   */
  public float getSequenceDivisionType() {
    return this.divisionType;
  }

  /**
   * <p>getSequenceResolution.</p>
   *
   * @return a int.
   */
  public int getSequenceResolution() {
    return this.resolutionTicksPerBeat;
  }

  /**
   * Finishes the sequence by adding an End of Track meta message (0x2F) to each track that has been
   * used in this sequence.
   */
  public void finishSequence() {
    MetaMessage message = new MetaMessage();
    try {
      message.setMessage(0x2F, null, 0);
      for (byte i = 0; i < getLastCreatedTrackNumber(); i++) {
        if (track[i] != null) {
          track[i].add(new MidiEvent(message, convertBeatsToTicks(getLatestTrackBeatTime(i))));
        }
      }
    } catch (InvalidMidiDataException e) {
      // We know what's going into this message.  This exception won't happen.
      logger.warning(e.getMessage());
    }
  }

  /**
   * Returns the track indicated by getCurrentTrackNumber(), and creates the track if it does not
   * already exist.
   */
  private Track getCurrentTrack() {
    if (track[getCurrentTrackNumber()] == null) {
      track[getCurrentTrackNumber()] = sequence.createTrack();
    }
    return track[getCurrentTrackNumber()];
  }

  /**
   * Adds a MetaMessage to the current track.
   *
   * @param type the type of the MetaMessage
   * @param bytes the data of the MetaMessage
   */
  public void addMetaMessage(int type, byte[] bytes) {
    try {
      MetaMessage message = new MetaMessage();
      message.setMessage(type, bytes, bytes.length);
      MidiEvent event = new MidiEvent(message, convertBeatsToTicks(getTrackBeatTime()));
      getCurrentTrack().add(event);
    } catch (InvalidMidiDataException e) {
      // We've kept a good eye on the data.  This exception won't happen.
      logger.warning(e.getMessage());
    }
  }

  /**
   * Adds a SysexMessage to the current track.
   *
   * @param bytes the data of the SysexMessage
   */
  public void addSystemExclusiveEvent(byte[] bytes) {
    try {
      SysexMessage message = new SysexMessage();
      message.setMessage(bytes, bytes.length);
      MidiEvent event = new MidiEvent(message, convertBeatsToTicks(getTrackBeatTime()));
      getCurrentTrack().add(event);
    } catch (InvalidMidiDataException e) {
      // We've kept a good eye on the data.  This exception won't happen.
      logger.warning(e.getMessage());
    }
  }

  /**
   * Adds a MIDI event to the current track.
   *
   * @param command the MIDI command represented by this message
   * @param data1 the first data byte
   */
  public void addEvent(int command, int data1) {
    try {
      ShortMessage message = new ShortMessage();
      message.setMessage(command, getCurrentTrackNumber(), data1);
      getCurrentTrack().add(new MidiEvent(message, convertBeatsToTicks(getTrackBeatTime())));
    } catch (InvalidMidiDataException e) {
      // We've kept a good eye on the data.  This exception won't happen.
      logger.warning(e.getMessage());
    }
  }

  /**
   * Adds a MIDI event to the current track.
   *
   * @param command the MIDI command represented by this message
   * @param data1 the first data byte
   * @param data2 the second data byte
   */
  public void addEvent(int command, int data1, int data2) {
    try {
      getCurrentTrack().add(new MidiEvent(createShortMessage(command, data1, data2),
        convertBeatsToTicks(getTrackBeatTime())));
    } catch (InvalidMidiDataException e) {
      // We've kept a good eye on the data.  This exception won't happen.
      logger.warning(e.getMessage());
    }
  }

  private ShortMessage createShortMessage(int status, int data1, int data2)
    throws InvalidMidiDataException {
    ShortMessage message = new ShortMessage();
    message.setMessage(status, getCurrentTrackNumber(), data1, data2);
    return message;
  }

  /**
   * <p>addNote.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  public void addNote(Note note) {
    if (note.getDuration() == 0.0) {
      note.useDefaultDuration();
    }

    // If this is the first note in a sequence of harmonic or melodic notes, remember what time it is.
    if (note.isFirstNote()) {
      setInitialNoteBeatTimeForHarmonicNotes(getTrackBeatTime());
    }

    // If we're going to the next sequence in a parallel note situation, roll back the time to the beginning of the first note.
    // A note will never be a parallel note if a first note has not happened first.
    if (note.isHarmonicNote()) {
      setTrackBeatTime(getInitialNoteBeatTimeForHarmonicNotes());
    }

    // If the note is a rest, simply advance the track time and get outta here
    if (note.isRest()) {
      advanceTrackBeatTime(note.getDuration());
      return;
    }

    // Add a NOTE_ON event.
    // If the note is continuing a tie, it is already sounding, and there is not need to turn the note on
    if (!note.isEndOfTie()) {
      addEvent(ShortMessage.NOTE_ON, note.getValue(), note.getOnVelocity());
    }

    // Advance the track timer
    advanceTrackBeatTime(note.getDuration());

    // Add a NOTE_OFF event.
    // If this note is the start of a tie, the note will continue to sound, so we don't want to turn it off.
    if (!note.isStartOfTie()) {
      addEvent(ShortMessage.NOTE_OFF, note.getValue(), note.getOffVelocity());
    }
  }

  private long convertBeatsToTicks(double beats) {
    return (long) (resolutionTicksPerBeat * beats * MidiDefaults.DEFAULT_TEMPO_BEATS_PER_WHOLE);
  }

  /**
   * Returns the current sequence, which is a collection of tracks. If your goal is to add events to
   * the sequence, you don't want to use this method to get the sequence; instead, use the addEvent
   * methods to add your events.
   *
   * @return the current sequence
   */
  public Sequence getSequence() {
    return this.sequence;
  }

}

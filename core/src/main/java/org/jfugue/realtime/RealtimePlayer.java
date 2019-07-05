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

package org.jfugue.realtime;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import org.jfugue.midi.MidiDictionary;
import org.jfugue.midi.MidiTools;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.player.SynthesizerManager;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;
import org.staccato.StaccatoParser;

/**
 * This player sends messages directly to the MIDI Synthesizer, rather than creating a sequence with
 * the MIDI Sequencer.
 *
 * There are two ways that you can send messages to RealTimePlayer, and you can freely intermix
 * these: 1. Pass any Staccato string to the play() method. In this case, start notes should be
 * indicated as the start of a tie (e.g., "C4s-") and stop notes should be indicated as the end of a
 * tie (e.g., "C4-s") 2. Call specific methods, like startNote or changeInstrument
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class RealtimePlayer {

  private MidiChannel[] channels;
  private int currentChannel;
  private StaccatoParser staccatoParser;
  private RealtimeMidiParserListener rtMidiParserListener;

  /**
   * <p>Constructor for RealtimePlayer.</p>
   *
   * @throws javax.sound.midi.MidiUnavailableException if any.
   */
  public RealtimePlayer() throws MidiUnavailableException {
    Synthesizer synth = SynthesizerManager.getInstance().getSynthesizer();
    synth.open();
    this.channels = synth.getChannels();

    staccatoParser = new StaccatoParser();
    rtMidiParserListener = new RealtimeMidiParserListener(this);
    staccatoParser.addParserListener(rtMidiParserListener);
  }

  /**
   * <p>play.</p>
   *
   * @param pattern a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public void play(PatternProducer pattern) {
    staccatoParser.parse(pattern);
  }

  /**
   * <p>play.</p>
   *
   * @param pattern a {@link java.lang.String} object.
   */
  public void play(String pattern) {
    staccatoParser.parse(pattern);
  }

  private MidiChannel getCurrentChannel() {
    return this.channels[this.currentChannel];
  }

  /**
   * <p>getCurrentTime.</p>
   *
   * @return a long.
   */
  public long getCurrentTime() {
    return rtMidiParserListener.getCurrentTime();
  }

  /**
   * <p>schedule.</p>
   *
   * @param timeInMillis a long.
   * @param event a {@link org.jfugue.realtime.ScheduledEvent} object.
   */
  public void schedule(long timeInMillis, ScheduledEvent event) {
    rtMidiParserListener.onEventScheduled(timeInMillis, event);
  }

  /**
   * <p>unschedule.</p>
   *
   * @param timeInMillis a long.
   * @param event a {@link org.jfugue.realtime.ScheduledEvent} object.
   */
  public void unschedule(long timeInMillis, ScheduledEvent event) {
    rtMidiParserListener.onEventUnscheduled(timeInMillis, event);
  }

  /**
   * <p>startNote.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  public void startNote(Note note) {
    getCurrentChannel().noteOn(note.getValue(), note.getOnVelocity());
  }

  /**
   * <p>stopNote.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  public void stopNote(Note note) {
    getCurrentChannel().noteOff(note.getValue(), note.getOffVelocity());
  }

  /**
   * <p>startChord.</p>
   *
   * @param chord a {@link org.jfugue.theory.Chord} object.
   */
  public void startChord(Chord chord) {
    for (Note note : chord.getNotes()) {
      startNote(note);
    }
  }

  /**
   * <p>stopChord.</p>
   *
   * @param chord a {@link org.jfugue.theory.Chord} object.
   */
  public void stopChord(Chord chord) {
    for (Note note : chord.getNotes()) {
      stopNote(note);
    }
  }

  /**
   * <p>startInterpolator.</p>
   *
   * @param interpolator a {@link org.jfugue.realtime.RealtimeInterpolator} object.
   * @param durationInMillis a long.
   */
  public void startInterpolator(RealtimeInterpolator interpolator, long durationInMillis) {
    rtMidiParserListener.onInterpolatorStarted(interpolator, durationInMillis);
  }

  /**
   * <p>stopInterpolator.</p>
   *
   * @param interpolator a {@link org.jfugue.realtime.RealtimeInterpolator} object.
   */
  public void stopInterpolator(RealtimeInterpolator interpolator) {
    rtMidiParserListener.onInterpolatorStopping(interpolator);
  }

  /**
   * <p>changeInstrument.</p>
   *
   * @param newInstrument a int.
   */
  public void changeInstrument(int newInstrument) {
    getCurrentChannel().programChange(newInstrument);
  }

  /**
   * <p>changeInstrument.</p>
   *
   * @param newInstrument a {@link java.lang.String} object.
   */
  public void changeInstrument(String newInstrument) {
    getCurrentChannel()
      .programChange(MidiDictionary.INSTRUMENT_STRING_TO_BYTE.get(newInstrument.toUpperCase()));
  }

  /**
   * <p>changeTrack.</p>
   *
   * @param newTrack a int.
   */
  public void changeTrack(int newTrack) {
    this.currentChannel = newTrack;
  }

  /**
   * <p>setPitchBend.</p>
   *
   * @param pitch a int.
   */
  public void setPitchBend(int pitch) {
    setPitchBend(MidiTools.getLSB(pitch), MidiTools.getMSB(pitch));
  }

  private void setPitchBend(byte lsb, byte msb) {
    getCurrentChannel().setPitchBend(lsb + (msb << 7));
  }

  /**
   * <p>changeChannelPressure.</p>
   *
   * @param pressure a byte.
   */
  public void changeChannelPressure(byte pressure) {
    getCurrentChannel().setChannelPressure(pressure);
  }

  /**
   * <p>changePolyphonicPressure.</p>
   *
   * @param key a byte.
   * @param pressure a byte.
   */
  public void changePolyphonicPressure(byte key, byte pressure) {
    getCurrentChannel().setPolyPressure(key, pressure);
  }

  /**
   * <p>changeController.</p>
   *
   * @param controller a byte.
   * @param value a byte.
   */
  public void changeController(byte controller, byte value) {
    getCurrentChannel().controlChange(controller, value);
  }

  /**
   * <p>close.</p>
   */
  public void close() {
    for (MidiChannel channel : channels) {
      channel.allNotesOff();
    }

    rtMidiParserListener.finish();
  }
}



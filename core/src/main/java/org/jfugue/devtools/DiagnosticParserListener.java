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

package org.jfugue.devtools;

import java.util.Arrays;
import java.util.logging.Logger;
import org.jfugue.parser.ParserListener;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;

/**
 * This ParserListener simply logs, or prints to System.out, anything it hears from a parser. If you
 * build a new Parser, you can use DiagnosticParserListener to make sure it works!
 *
 * @author David Koelle
 * @version $Id: $Id
 */
public class DiagnosticParserListener implements ParserListener {

  private final Logger logger = Logger.getLogger("org.jfugue");

  /**
   * <p>Constructor for DiagnosticParserListener.</p>
   */
  public DiagnosticParserListener() {
  }

  private void print(String message) {
    System.out.println(message);
    logger.info(message);
  }

  /** {@inheritDoc} */
  @Override
  public void beforeParsingStarts() {
    print("Before parsing starts");
  }

  /** {@inheritDoc} */
  @Override
  public void afterParsingFinished() {
    print("After parsing finished");
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackChanged(byte track) {
    print("Track changed to " + track);
  }

  /** {@inheritDoc} */
  @Override
  public void onLayerChanged(byte layer) {
    print("Layer changed to " + layer);
  }

  /** {@inheritDoc} */
  @Override
  public void onInstrumentParsed(byte instrument) {
    print("Instrument parsed: " + instrument);
  }

  /** {@inheritDoc} */
  @Override
  public void onTempoChanged(int tempoBPM) {
    print("Tempo changed to " + tempoBPM + " BPM");
  }

  /** {@inheritDoc} */
  @Override
  public void onKeySignatureParsed(byte key, byte scale) {
    print("Key signature parsed: key = " + key + "  scale = " + scale);
  }

  /** {@inheritDoc} */
  @Override
  public void onTimeSignatureParsed(byte numerator, byte powerOfTwo) {
    print("Time signature parsed: " + numerator + "/" + (int) (Math.pow(2, powerOfTwo)));
  }

  /** {@inheritDoc} */
  @Override
  public void onBarLineParsed(long time) {
    print("Bar line parsed at time = " + time);
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarked(String timeBookmarkId) {
    print("Track time bookmarked into '" + timeBookmarkId + "'");
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarkRequested(String timeBookmarkId) {
    print("Track time bookmark looked up: '" + timeBookmarkId + "'");
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeRequested(double time) {
    print("Track time requested: " + time);
  }

  /** {@inheritDoc} */
  @Override
  public void onPitchWheelParsed(byte lsb, byte msb) {
    print("Pitch wheel parsed, lsb = " + lsb + "  msb = " + msb);
  }

  /** {@inheritDoc} */
  @Override
  public void onChannelPressureParsed(byte pressure) {
    print("Channel pressure parsed: " + pressure);
  }

  /** {@inheritDoc} */
  @Override
  public void onPolyphonicPressureParsed(byte key, byte pressure) {
    print("Polyphonic pressure parsed, key = " + key + "  pressure = " + pressure);
  }

  /** {@inheritDoc} */
  @Override
  public void onSystemExclusiveParsed(byte... bytes) {
    print("Sysex parsed, bytes = " + Arrays.toString(bytes));
  }

  /** {@inheritDoc} */
  @Override
  public void onControllerEventParsed(byte controller, byte value) {
    print("Controller event parsed, controller = " + controller + "  value = " + value);
  }

  /** {@inheritDoc} */
  @Override
  public void onLyricParsed(String lyric) {
    print("Lyric parsed: " + lyric);
  }

  /** {@inheritDoc} */
  @Override
  public void onMarkerParsed(String marker) {
    print("Marker parsed: " + marker);
  }

  /** {@inheritDoc} */
  @Override
  public void onFunctionParsed(String id, Object message) {
    print("User event parsed, id = " + id + "  message = " + message);
  }

  /** {@inheritDoc} */
  @Override
  public void onNotePressed(Note note) {
    print("Note pressed: value = " + note.getValue() + "  onVelocity = " + note.getOnVelocity());
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteReleased(Note note) {
    print("Note released: value = " + note.getValue() + "  offVelocity = " + note.getOffVelocity());
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteParsed(Note note) {
    print("Note parsed: value = " + note.getValue() + "  duration = " + note.getDuration()
      + "  onVelocity = " + note.getOnVelocity() + "  offVelocity = " + note.getOffVelocity());
  }

  /** {@inheritDoc} */
  @Override
  public void onChordParsed(Chord chord) {
    print("Chord parsed: rootnote = " + chord.getRoot().getValue() + "  intervals = " + chord
      .getIntervals().toString() + "  duration = " + chord.getRoot().getDuration()
      + "  onVelocity = " + chord.getRoot().getOnVelocity() + "  offVelocity = " + chord.getRoot()
      .getOffVelocity());
  }
}

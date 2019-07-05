/*
 * JFugue, an Application Programming Interface (API) for Music Programming
 * http://www.jfugue.org
 *
 * Copyright (C) 2003-2016 David Koelle
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

package org.staccato;

import org.jfugue.parser.ParserListener;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;

/**
 * <p>StaccatoParserListener class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class StaccatoParserListener implements ParserListener, PatternProducer {

  private Pattern pattern;
  private byte track;

  /**
   * <p>Constructor for StaccatoParserListener.</p>
   */
  public StaccatoParserListener() {
    pattern = new Pattern();
  }

  /**
   * <p>Getter for the field <code>pattern</code>.</p>
   *
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public Pattern getPattern() {
    return this.pattern;
  }

  /** {@inheritDoc} */
  @Override
  public void beforeParsingStarts() {
    pattern = new Pattern();
  }

  /** {@inheritDoc} */
  @Override
  public void afterParsingFinished() {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackChanged(byte track) {
    pattern.add(StaccatoUtil.createTrackElement(track));
    this.track = track;
  }

  /** {@inheritDoc} */
  @Override
  public void onLayerChanged(byte layer) {
    pattern.add(StaccatoUtil.createLayerElement(layer));
  }

  /** {@inheritDoc} */
  @Override
  public void onInstrumentParsed(byte instrument) {
    pattern.add(StaccatoUtil.createInstrumentElement(instrument));
  }

  /** {@inheritDoc} */
  @Override
  public void onTempoChanged(int tempoBPM) {
    pattern.add(StaccatoUtil.createTempoElement(tempoBPM));
  }

  /** {@inheritDoc} */
  @Override
  public void onKeySignatureParsed(byte key, byte scale) {
    pattern.add(StaccatoUtil.createKeySignatureElement(key, scale));
  }

  /** {@inheritDoc} */
  @Override
  public void onTimeSignatureParsed(byte numerator, byte powerOfTwo) {
    pattern.add(StaccatoUtil.createTimeSignatureElement(numerator, powerOfTwo));
  }

  /** {@inheritDoc} */
  @Override
  public void onBarLineParsed(long time) {
    pattern.add(StaccatoUtil.createBarLineElement(time));
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarked(String timeBookmarkId) {
    pattern.add(StaccatoUtil.createTrackBeatTimeBookmarkElement(timeBookmarkId));
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarkRequested(String timeBookmarkId) {
    pattern.add(StaccatoUtil.createTrackBeatTimeBookmarkRequestElement(timeBookmarkId));
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeRequested(double time) {
    pattern.add(StaccatoUtil.createTrackBeatTimeRequestElement(time));
  }

  /** {@inheritDoc} */
  @Override
  public void onPitchWheelParsed(byte lsb, byte msb) {
    pattern.add(StaccatoUtil.createPitchWheelElement(lsb, msb));
  }

  /** {@inheritDoc} */
  @Override
  public void onChannelPressureParsed(byte pressure) {
    pattern.add(StaccatoUtil.createChannelPressureElement(pressure));
  }

  /** {@inheritDoc} */
  @Override
  public void onPolyphonicPressureParsed(byte key, byte pressure) {
    pattern.add(StaccatoUtil.createPolyphonicPressureElement(key, pressure));
  }

  /** {@inheritDoc} */
  @Override
  public void onSystemExclusiveParsed(byte... bytes) {
    pattern.add(StaccatoUtil.createSystemExclusiveElement(bytes));
  }

  /** {@inheritDoc} */
  @Override
  public void onControllerEventParsed(byte controller, byte value) {
    pattern.add(StaccatoUtil.createControllerEventElement(controller, value));
  }

  /** {@inheritDoc} */
  @Override
  public void onLyricParsed(String lyric) {
    pattern.add(StaccatoUtil.createLyricElement(lyric));
  }

  /** {@inheritDoc} */
  @Override
  public void onMarkerParsed(String marker) {
    pattern.add(StaccatoUtil.createMarkerElement(marker));
  }

  /** {@inheritDoc} */
  @Override
  public void onFunctionParsed(String id, Object message) {
    pattern.add(StaccatoUtil.createFunctionElement(id, message));
  }

  /** {@inheritDoc} */
  @Override
  public void onNotePressed(Note note) {
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteReleased(Note note) {
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteParsed(Note note) {
    pattern.add(StaccatoUtil.createNoteElement(note, track));
  }

  /** {@inheritDoc} */
  @Override
  public void onChordParsed(Chord chord) {
    pattern.add(StaccatoUtil.createChordElement(chord));
  }
}

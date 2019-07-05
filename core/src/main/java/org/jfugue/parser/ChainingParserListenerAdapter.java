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

package org.jfugue.parser;

import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;

/**
 * This allows chaining of ParserListeners that enable each listener in the chain to alter the
 * events it passes to the other parsers it knows about. By default, it fires all events to its
 * listeners. You may want to override any of those to include your own behavior.
 *
 * @author dkoelle
 * @version $Id: $Id
 */
public class ChainingParserListenerAdapter extends Parser implements ParserListener {

  /**
   * <p>Constructor for ChainingParserListenerAdapter.</p>
   */
  protected ChainingParserListenerAdapter() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public void beforeParsingStarts() {
    fireBeforeParsingStarts();
  }

  /** {@inheritDoc} */
  @Override
  public void afterParsingFinished() {
    fireAfterParsingFinished();
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackChanged(byte track) {
    fireTrackChanged(track);
  }

  /** {@inheritDoc} */
  @Override
  public void onLayerChanged(byte layer) {
    fireLayerChanged(layer);
  }

  /** {@inheritDoc} */
  @Override
  public void onInstrumentParsed(byte instrument) {
    fireInstrumentParsed(instrument);
  }

  /** {@inheritDoc} */
  @Override
  public void onTempoChanged(int tempoBPM) {
    fireTempoChanged(tempoBPM);
  }

  /** {@inheritDoc} */
  @Override
  public void onKeySignatureParsed(byte key, byte scale) {
    fireKeySignatureParsed(key, scale);
  }

  /** {@inheritDoc} */
  @Override
  public void onTimeSignatureParsed(byte numerator, byte powerOfTwo) {
    fireTimeSignatureParsed(numerator, powerOfTwo);
  }

  /** {@inheritDoc} */
  @Override
  public void onBarLineParsed(long id) {
    fireBarLineParsed(id);
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarked(String timeBookmarkId) {
    fireTrackBeatTimeBookmarked(timeBookmarkId);
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarkRequested(String timeBookmarkId) {
    fireTrackBeatTimeBookmarkRequested(timeBookmarkId);
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeRequested(double time) {
    fireTrackBeatTimeRequested(time);
  }

  /** {@inheritDoc} */
  @Override
  public void onPitchWheelParsed(byte lsb, byte msb) {
    firePitchWheelParsed(lsb, msb);
  }

  /** {@inheritDoc} */
  @Override
  public void onChannelPressureParsed(byte pressure) {
    fireChannelPressureParsed(pressure);
  }

  /** {@inheritDoc} */
  @Override
  public void onPolyphonicPressureParsed(byte key, byte pressure) {
    firePolyphonicPressureParsed(key, pressure);
  }

  /** {@inheritDoc} */
  @Override
  public void onSystemExclusiveParsed(byte... bytes) {
    fireSystemExclusiveParsed(bytes);
  }

  /** {@inheritDoc} */
  @Override
  public void onControllerEventParsed(byte controller, byte value) {
    fireControllerEventParsed(controller, value);
  }

  /** {@inheritDoc} */
  @Override
  public void onLyricParsed(String lyric) {
    fireLyricParsed(lyric);
  }

  /** {@inheritDoc} */
  @Override
  public void onMarkerParsed(String marker) {
    fireMarkerParsed(marker);
  }

  /** {@inheritDoc} */
  @Override
  public void onFunctionParsed(String id, Object message) {
    fireFunctionParsed(id, message);
  }

  /** {@inheritDoc} */
  @Override
  public void onNotePressed(Note note) {
    fireNotePressed(note);
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteReleased(Note note) {
    fireNoteReleased(note);
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteParsed(Note note) {
    fireNoteParsed(note);
  }

  /** {@inheritDoc} */
  @Override
  public void onChordParsed(Chord chord) {
    fireChordParsed(chord);
  }
}

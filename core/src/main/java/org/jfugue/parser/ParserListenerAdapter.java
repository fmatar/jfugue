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
 * <p>ParserListenerAdapter class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class ParserListenerAdapter implements ParserListener {

  /** {@inheritDoc} */
  @Override
  public void beforeParsingStarts() {
  }

  /** {@inheritDoc} */
  @Override
  public void afterParsingFinished() {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackChanged(byte track) {
  }

  /** {@inheritDoc} */
  @Override
  public void onLayerChanged(byte layer) {
  }

  /** {@inheritDoc} */
  @Override
  public void onInstrumentParsed(byte instrument) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTempoChanged(int tempoBPM) {
  }

  /** {@inheritDoc} */
  @Override
  public void onKeySignatureParsed(byte key, byte scale) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTimeSignatureParsed(byte numerator, byte powerOfTwo) {
  }

  /** {@inheritDoc} */
  @Override
  public void onBarLineParsed(long id) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarked(String timeBookmarkId) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarkRequested(String timeBookmarkId) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeRequested(double time) {
  }

  /** {@inheritDoc} */
  @Override
  public void onPitchWheelParsed(byte lsb, byte msb) {
  }

  /** {@inheritDoc} */
  @Override
  public void onChannelPressureParsed(byte pressure) {
  }

  /** {@inheritDoc} */
  @Override
  public void onPolyphonicPressureParsed(byte key, byte pressure) {
  }

  /** {@inheritDoc} */
  @Override
  public void onSystemExclusiveParsed(byte... bytes) {
  }

  /** {@inheritDoc} */
  @Override
  public void onControllerEventParsed(byte controller, byte value) {
  }

  /** {@inheritDoc} */
  @Override
  public void onLyricParsed(String lyric) {
  }

  /** {@inheritDoc} */
  @Override
  public void onMarkerParsed(String marker) {
  }

  /** {@inheritDoc} */
  @Override
  public void onFunctionParsed(String id, Object message) {
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
  }

  /** {@inheritDoc} */
  @Override
  public void onChordParsed(Chord chord) {
  }
}

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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;

/**
 * <p>Parser class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Parser {

  private final CopyOnWriteArrayList<ParserListener> parserListeners;

  /**
   * <p>Constructor for Parser.</p>
   */
  protected Parser() {
    parserListeners = new CopyOnWriteArrayList<>();
  }

  /**
   * <p>addParserListener.</p>
   *
   * @param listener a {@link org.jfugue.parser.ParserListener} object.
   */
  public void addParserListener(ParserListener listener) {
    parserListeners.add(listener);
  }

  /**
   * <p>removeParserListener.</p>
   *
   * @param listener a {@link org.jfugue.parser.ParserListener} object.
   */
  public void removeParserListener(ParserListener listener) {
    parserListeners.remove(listener);
  }

  private List<ParserListener> getParserListeners() {
    return parserListeners;
  }

  /**
   * <p>clearParserListeners.</p>
   */
  public void clearParserListeners() {
    this.parserListeners.clear();
  }

  //
  // Event firing methods
  //

  /**
   * <p>fireBeforeParsingStarts.</p>
   */
  protected void fireBeforeParsingStarts() {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.beforeParsingStarts();
    }
  }

  /**
   * <p>fireAfterParsingFinished.</p>
   */
  protected void fireAfterParsingFinished() {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.afterParsingFinished();
    }
  }

  /**
   * <p>fireTrackChanged.</p>
   *
   * @param track a byte.
   */
  public void fireTrackChanged(byte track) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onTrackChanged(track);
    }
  }

  /**
   * <p>fireLayerChanged.</p>
   *
   * @param layer a byte.
   */
  public void fireLayerChanged(byte layer) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onLayerChanged(layer);
    }
  }

  /**
   * <p>fireInstrumentParsed.</p>
   *
   * @param instrument a byte.
   */
  public void fireInstrumentParsed(byte instrument) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onInstrumentParsed(instrument);
    }
  }

  /**
   * <p>fireTempoChanged.</p>
   *
   * @param tempoBPM a int.
   */
  public void fireTempoChanged(int tempoBPM) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onTempoChanged(tempoBPM);
    }
  }

  /**
   * <p>fireKeySignatureParsed.</p>
   *
   * @param key a byte.
   * @param scale a byte.
   */
  public void fireKeySignatureParsed(byte key, byte scale) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onKeySignatureParsed(key, scale);
    }
  }

  /**
   * <p>fireTimeSignatureParsed.</p>
   *
   * @param numerator a byte.
   * @param powerOfTwo a byte.
   */
  public void fireTimeSignatureParsed(byte numerator, byte powerOfTwo) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onTimeSignatureParsed(numerator, powerOfTwo);
    }
  }

  /**
   * <p>fireBarLineParsed.</p>
   *
   * @param id a long.
   */
  public void fireBarLineParsed(long id) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onBarLineParsed(id);
    }
  }

  /**
   * <p>fireTrackBeatTimeBookmarked.</p>
   *
   * @param timeBookmarkId a {@link java.lang.String} object.
   */
  public void fireTrackBeatTimeBookmarked(String timeBookmarkId) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onTrackBeatTimeBookmarked(timeBookmarkId);
    }
  }

  /**
   * <p>fireTrackBeatTimeBookmarkRequested.</p>
   *
   * @param timeBookmarkId a {@link java.lang.String} object.
   */
  public void fireTrackBeatTimeBookmarkRequested(String timeBookmarkId) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onTrackBeatTimeBookmarkRequested(timeBookmarkId);
    }
  }

  /**
   * <p>fireTrackBeatTimeRequested.</p>
   *
   * @param time a double.
   */
  public void fireTrackBeatTimeRequested(double time) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onTrackBeatTimeRequested(time);
    }
  }

  /**
   * <p>firePitchWheelParsed.</p>
   *
   * @param lsb a byte.
   * @param msb a byte.
   */
  public void firePitchWheelParsed(byte lsb, byte msb) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onPitchWheelParsed(lsb, msb);
    }
  }

  /**
   * <p>fireChannelPressureParsed.</p>
   *
   * @param pressure a byte.
   */
  public void fireChannelPressureParsed(byte pressure) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onChannelPressureParsed(pressure);
    }
  }

  /**
   * <p>firePolyphonicPressureParsed.</p>
   *
   * @param key a byte.
   * @param pressure a byte.
   */
  public void firePolyphonicPressureParsed(byte key, byte pressure) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onPolyphonicPressureParsed(key, pressure);
    }
  }

  /**
   * <p>fireSystemExclusiveParsed.</p>
   *
   * @param bytes a byte.
   */
  public void fireSystemExclusiveParsed(byte... bytes) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onSystemExclusiveParsed(bytes);
    }
  }

  /**
   * <p>fireControllerEventParsed.</p>
   *
   * @param controller a byte.
   * @param value a byte.
   */
  public void fireControllerEventParsed(byte controller, byte value) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onControllerEventParsed(controller, value);
    }
  }

  /**
   * <p>fireLyricParsed.</p>
   *
   * @param lyric a {@link java.lang.String} object.
   */
  public void fireLyricParsed(String lyric) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onLyricParsed(lyric);
    }
  }

  /**
   * <p>fireMarkerParsed.</p>
   *
   * @param marker a {@link java.lang.String} object.
   */
  public void fireMarkerParsed(String marker) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onMarkerParsed(marker);
    }
  }

  /**
   * <p>fireFunctionParsed.</p>
   *
   * @param id a {@link java.lang.String} object.
   * @param message a {@link java.lang.Object} object.
   */
  public void fireFunctionParsed(String id, Object message) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onFunctionParsed(id, message);
    }
  }

  /**
   * <p>fireNotePressed.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  protected void fireNotePressed(Note note) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onNotePressed(note);
    }
  }

  /**
   * <p>fireNoteReleased.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  protected void fireNoteReleased(Note note) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onNoteReleased(note);
    }
  }

  /**
   * <p>fireNoteParsed.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  public void fireNoteParsed(Note note) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onNoteParsed(note);
    }
  }

  /**
   * <p>fireChordParsed.</p>
   *
   * @param chord a {@link org.jfugue.theory.Chord} object.
   */
  public void fireChordParsed(Chord chord) {
    List<ParserListener> listeners = getParserListeners();
    for (ParserListener listener : listeners) {
      listener.onChordParsed(chord);
    }
  }

}

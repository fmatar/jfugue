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

package org.staccato;

import org.jfugue.midi.MidiDictionary;
import org.jfugue.pattern.Token.TokenType;

/**
 * Parses both Instrument and Layer tokens. Each has values that are parsed as bytes.
 *
 * @author dkoelle
 * @version $Id: $Id
 */
public class TempoSubparser implements Subparser {

  /** Constant <code>TEMPO='T'</code> */
  public static final char TEMPO = 'T';

  private static TempoSubparser instance;

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.staccato.TempoSubparser} object.
   */
  public static TempoSubparser getInstance() {
    if (instance == null) {
      instance = new TempoSubparser();
    }
    return instance;
  }

  /**
   * <p>populateContext.</p>
   *
   * @param context a {@link org.staccato.StaccatoParserContext} object.
   */
  public static void populateContext(StaccatoParserContext context) {
    // Instruments
    context.getDictionary().putAll(MidiDictionary.TEMPO_STRING_TO_INT);
  }

  /** {@inheritDoc} */
  @Override
  public boolean matches(String music) {
    return (music.charAt(0) == TEMPO);
  }

  /** {@inheritDoc} */
  @Override
  public TokenType getTokenType(String tokenString) {
    if (tokenString.charAt(0) == TEMPO) {
      return TokenType.TEMPO;
    }

    return TokenType.UNKNOWN_TOKEN;
  }

  /** {@inheritDoc} */
  @Override
  public int parse(String music, StaccatoParserContext context) {
    if (matches(music)) {
      int posNextSpace = StaccatoUtil.findNextOrEnd(music, ' ', 0);
      int tempo = -1;
      if (posNextSpace > 1) {
        String tempoId = music.substring(1, posNextSpace);
        if (tempoId.matches("\\d+")) {
          tempo = Integer.parseInt(tempoId);
        } else {
          if (tempoId.charAt(0) == '[') {
            tempoId = tempoId.substring(1, tempoId.length() - 1);
          }
          tempo = (Integer) context.getDictionary().get(tempoId);
        }
      }
      context.getParser().fireTempoChanged(tempo);
      return posNextSpace + 1;
    }
    return 0;
  }
}

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

package org.staccato.functions;

import org.jfugue.midi.MidiTools;
import org.staccato.StaccatoParserContext;
import org.staccato.SubparserFunction;

/**
 * <p>PitchWheelFunction class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class PitchWheelFunction implements SubparserFunction {

  private static final String[] NAMES = {"PW", "PITCHWHEEL", "PB", "PITCHBEND"};
  private static PitchWheelFunction instance;

  private PitchWheelFunction() {
  }

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.staccato.functions.PitchWheelFunction} object.
   */
  public static PitchWheelFunction getInstance() {
    if (instance == null) {
      instance = new PitchWheelFunction();
    }
    return instance;
  }

  /** {@inheritDoc} */
  @Override
  public String[] getNames() {
    return NAMES;
  }

  /** {@inheritDoc} */
  @Override
  public void apply(String parameters, StaccatoParserContext context) {
    String[] params = parameters.split(",");
    if (params.length == 2) {
      context.getParser()
        .firePitchWheelParsed(Byte.parseByte(params[0].trim()), Byte.parseByte(params[1].trim()));
    } else if (params.length == 1) {
      int pitch = Integer.parseInt(params[0]);
      context.getParser().firePitchWheelParsed(MidiTools.getLSB(pitch), MidiTools.getMSB(pitch));
    }
  }
}

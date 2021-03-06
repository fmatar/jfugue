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
 * <p>ControllerFunction class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class ControllerFunction implements SubparserFunction {

  private static final String[] NAMES = {"CE", "CON", "CONTROLLER", "CONTROLLEREVENT"};
  private static ControllerFunction instance;

  private ControllerFunction() {
  }

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.staccato.functions.ControllerFunction} object.
   */
  public static ControllerFunction getInstance() {
    if (instance == null) {
      instance = new ControllerFunction();
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
      int controllerNumber = 0;
      String controllerId = params[0].trim();
      if (controllerId.matches("\\d+")) {
        controllerNumber = Integer.parseInt(controllerId);
      } else {
        if (controllerId.charAt(0) == '[') {
          controllerId = controllerId.substring(1, controllerId.length() - 1);
        }
        controllerNumber = (Integer) context.getDictionary().get(controllerId);
      }
      if (controllerNumber > Byte.MAX_VALUE) {
        context.getParser().fireControllerEventParsed(MidiTools.getLSB(controllerNumber),
          MidiTools.getLSB(Integer.parseInt(params[1].trim())));
        context.getParser().fireControllerEventParsed(MidiTools.getMSB(controllerNumber),
          MidiTools.getMSB(Integer.parseInt(params[1].trim())));
      } else {
        context.getParser()
          .fireControllerEventParsed((byte) controllerNumber, Byte.parseByte(params[1].trim()));
      }
    }
  }
}

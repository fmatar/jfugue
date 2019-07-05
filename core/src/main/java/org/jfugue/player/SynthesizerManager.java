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

package org.jfugue.player;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 * <p>SynthesizerManager class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class SynthesizerManager {

  private static SynthesizerManager instance;
  private Synthesizer synth;

  private SynthesizerManager() throws MidiUnavailableException {
    this.synth = getDefaultSynthesizer();
  }

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.jfugue.player.SynthesizerManager} object.
   * @throws javax.sound.midi.MidiUnavailableException if any.
   */
  public static SynthesizerManager getInstance() throws MidiUnavailableException {
    if (instance == null) {
      instance = new SynthesizerManager();
    }
    return instance;
  }

  private Synthesizer getDefaultSynthesizer() throws MidiUnavailableException {
    return MidiSystem.getSynthesizer();
  }

  /**
   * <p>getSynthesizer.</p>
   *
   * @return a {@link javax.sound.midi.Synthesizer} object.
   */
  public Synthesizer getSynthesizer() {
    return this.synth;
  }

  /**
   * <p>setSynthesizer.</p>
   *
   * @param synth a {@link javax.sound.midi.Synthesizer} object.
   */
  public void setSynthesizer(Synthesizer synth) {
    this.synth = synth;
  }

}

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

package org.jfugue.devices;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import org.jfugue.midi.MidiParser;

/**
 * This class represents a MidiParser that is also a MIDI Receiver (javax.sound.midi.Receiver). As a
 * MidiParser, it can have ParserListeners As a Receiver, it overrides send() and sends the
 * resulting MidiMessage and timestamp to MidiParser's parseEvent method.
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class MidiParserReceiver implements Receiver {

  private final MidiParser parser;
  private Sequencer sequencer;
  private Receiver sequencerReceiver;

  /**
   * <p>Constructor for MidiParserReceiver.</p>
   *
   * @throws javax.sound.midi.MidiUnavailableException if any.
   */
  public MidiParserReceiver() throws MidiUnavailableException {
    this.parser = new MidiParser();
    this.sequencer = MidiSystem.getSequencer();
    this.sequencerReceiver = sequencer.getReceiver();
  }

  /**
   * <p>Getter for the field <code>parser</code>.</p>
   *
   * @return a {@link org.jfugue.midi.MidiParser} object.
   */
  public MidiParser getParser() {
    return this.parser;
  }

  /** {@inheritDoc} */
  @Override
  public void send(MidiMessage message, long timestamp) {
    parser.parseEvent(new MidiEvent(message, timestamp));
    sequencerReceiver.send(message, timestamp);
  }

  /**
   * <p>close.</p>
   */
  public void close() {
    sequencerReceiver.close();
  }

  /**
   * <p>getSequence.</p>
   *
   * @return a {@link javax.sound.midi.Sequence} object.
   */
  public Sequence getSequence() {
    return sequencer.getSequence();
  }
}

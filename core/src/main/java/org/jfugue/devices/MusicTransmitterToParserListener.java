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

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import org.jfugue.parser.ParserListener;

/**
 * Represents a device that will send music. For example, you can attach this to your external MIDI
 * keyboard and play music on the keyboard, which is then recorded here.
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class MusicTransmitterToParserListener {

  private final MidiDevice device;
  private final boolean isInitiated;
  private final List<ParserListener> listeners;
  private Transmitter transmitter;
  private MidiParserReceiver mrftd;

  /**
   * <p>Constructor for MusicTransmitterToParserListener.</p>
   *
   * @param device a {@link javax.sound.midi.MidiDevice} object.
   * @throws javax.sound.midi.MidiUnavailableException if any.
   */
  public MusicTransmitterToParserListener(MidiDevice device) throws MidiUnavailableException {
    this.device = device;
    this.isInitiated = false;
    this.listeners = new ArrayList<>();
    this.mrftd = new MidiParserReceiver();
  }

  private void init() throws MidiUnavailableException {
    if (!isInitiated) {
      try {
        if (!(device.isOpen())) {
          device.open();
        }
        this.transmitter = device.getTransmitter();
      } catch (MidiUnavailableException e) {
        device.close();
        throw e;
      }
    }

    this.mrftd.getParser().clearParserListeners();
    for (ParserListener listener : listeners) {
      this.mrftd.getParser().addParserListener(listener);
    }
  }

  /**
   * <p>addParserListener.</p>
   *
   * @param l a {@link org.jfugue.parser.ParserListener} object.
   */
  public void addParserListener(ParserListener l) {
    this.listeners.add(l);
  }

  /**
   * <p>getParserListeners.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<ParserListener> getParserListeners() {
    return this.listeners;
  }

  /**
   * <p>Getter for the field <code>transmitter</code>.</p>
   *
   * @return a {@link javax.sound.midi.Transmitter} object.
   */
  public Transmitter getTransmitter() {
    return this.transmitter;
  }

  /**
   * <p>getMidiParserReceiver.</p>
   *
   * @return a {@link org.jfugue.devices.MidiParserReceiver} object.
   */
  public MidiParserReceiver getMidiParserReceiver() {
    return this.mrftd;
  }

  private void startListening() throws MidiUnavailableException {
    init();
    mrftd.getParser().startParser();
    transmitter.setReceiver(this.mrftd);
  }

  private void stopListening() {
    mrftd.getParser().stopParser();
    close();
  }

  /**
   * Used instead of startListening() and stopListening() - listens for a pre-defined amount of
   * time.
   *
   * @param millis - the number of milliseconds to listen
   * @throws javax.sound.midi.MidiUnavailableException Midi is incorrect
   * @throws java.lang.InterruptedException A threading exception occurred
   */
  public void listenForMillis(long millis) throws MidiUnavailableException, InterruptedException {
    startListening();
    Thread.sleep(millis);
    stopListening();
  }

  /**
   * <p>close.</p>
   */
  public void close() {
    transmitter.close();
    device.close();
  }
}

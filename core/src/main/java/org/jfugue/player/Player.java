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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import org.jfugue.midi.MidiParserListener;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.staccato.StaccatoParser;

/**
 * This is a player that is optimized for defining and playing music in a program. It specifically
 * parses music with a StaccatoParser and converts the music to MIDI using a MidiParserListener.
 *
 * This Player uses a ManagedPlayer but does not expose any of the ManagedPlayer's ability to be
 * managed.
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Player {

  private final StaccatoParser staccatoParser;
  private final MidiParserListener midiParserListener;
  private final ManagedPlayer managedPlayer;

  /**
   * <p>Constructor for Player.</p>
   */
  public Player() {
    managedPlayer = new ManagedPlayer();
    staccatoParser = new StaccatoParser();
    midiParserListener = new MidiParserListener();
    staccatoParser.addParserListener(midiParserListener);
  }

  /**
   * <p>getSequence.</p>
   *
   * @param patternProducers a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link javax.sound.midi.Sequence} object.
   */
  public Sequence getSequence(PatternProducer... patternProducers) {
    return getSequence(new Pattern(patternProducers));
  }

  /**
   * <p>getSequence.</p>
   *
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link javax.sound.midi.Sequence} object.
   */
  public Sequence getSequence(PatternProducer patternProducer) {
    return getSequence(patternProducer.getPattern().toString());
  }

  /**
   * <p>getSequence.</p>
   *
   * @param strings a {@link java.lang.String} object.
   * @return a {@link javax.sound.midi.Sequence} object.
   */
  public Sequence getSequence(String... strings) {
    return getSequence(new Pattern(strings));
  }

  private Sequence getSequence(String string) {
    staccatoParser.parse(string);
    return midiParserListener.getSequence();
  }

  /**
   * <p>play.</p>
   *
   * @param patternProducers a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public void play(PatternProducer... patternProducers) {
    play(new Pattern(patternProducers));
  }

  /**
   * <p>play.</p>
   *
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public void play(PatternProducer patternProducer) {
    play(patternProducer.getPattern().toString());
  }

  /**
   * <p>play.</p>
   *
   * @param strings a {@link java.lang.String} object.
   */
  public void play(String... strings) {
    play(new Pattern(strings));
  }

  /**
   * <p>play.</p>
   *
   * @param string a {@link java.lang.String} object.
   */
  public void play(String string) {
    play(getSequence(string));
  }

  /**
   * This method plays a sequence by starting the sequence and waiting for the sequence to finish
   * before continuing. It also converts InvalidMidiDataException and MidiUnavailableException to
   * RuntimeExceptions for easier end-user programming. If you want to create an application where
   * you catch those exceptions, you may want to use ManagedPlayer directly.
   */
  private void play(Sequence sequence) {
    try {
      managedPlayer.start(sequence);
    } catch (InvalidMidiDataException | MidiUnavailableException e) {
      throw new RuntimeException(e);
    }

    // Wait for the sequence to finish playing
    while (!managedPlayer.isFinished()) {
      try {
        Thread.sleep(20); // don't hog all of the CPU
      } catch (InterruptedException e) {
        // Nothing to do here
      }
    }
  }

  /**
   * <p>delayPlay.</p>
   *
   * @param millisToDelay a long.
   * @param patternProducers a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public void delayPlay(final long millisToDelay, final PatternProducer... patternProducers) {
    delayPlay(millisToDelay, new Pattern(patternProducers));
  }

  /**
   * <p>delayPlay.</p>
   *
   * @param millisToDelay a long.
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public void delayPlay(final long millisToDelay, final PatternProducer patternProducer) {
    delayPlay(millisToDelay, patternProducer.getPattern().toString());
  }

  /**
   * <p>delayPlay.</p>
   *
   * @param millisToDelay a long.
   * @param strings a {@link java.lang.String} object.
   */
  public void delayPlay(final long millisToDelay, final String... strings) {
    delayPlay(millisToDelay, new Pattern(strings));
  }

  /**
   * <p>delayPlay.</p>
   *
   * @param millisToDelay a long.
   * @param string a {@link java.lang.String} object.
   */
  public void delayPlay(final long millisToDelay, final String string) {
    delayPlay(millisToDelay, getSequence(string));
  }

  private void delayPlay(final long millisToDelay, final Sequence sequence) {
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(millisToDelay);
      } catch (InterruptedException e) {
        // Get yourself an egg and beat it!
      }
      Player.this.play(sequence);
    });
    thread.start();
  }

  /**
   * Returns the ManagedPlayer behind this Player. You can start, pause, stop, resume, and seek a
   * ManagedPlayer.
   *
   * @return the ManagedPlayer instance
   * @see ManagedPlayer
   */
  public ManagedPlayer getManagedPlayer() {
    return this.managedPlayer;
  }

  /**
   * Returns the StaccatoParser used by this Player. The only thing you might want to do with this
   * is set whether the parser throws an exception if an unknown token is found.
   *
   * @return The StaccatoParseInstance
   * @see StaccatoParser
   */
  public StaccatoParser getStaccatoParser() {
    return this.staccatoParser;
  }

  /**
   * Returns the MidiParserListener used by this Player.
   *
   * @return The MidiParserListener instance
   * @see MidiParserListener
   */
  public MidiParserListener getMidiParserListener() {
    return this.midiParserListener;
  }
} 

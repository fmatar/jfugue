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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;

/**
 * This is player that can be "managed" - e.g., started, stopped, paused, resumed, seeked, and
 * finished. Additionally, the state of this player can be requested. If you want to create a player
 * that you can control like any standard media player, this is your class.
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class ManagedPlayer implements EndOfTrackListener {

  private final CopyOnWriteArrayList<ManagedPlayerListener> playerListeners;
  private SequencerManager common;
  private boolean started;
  private boolean finished;
  private boolean paused;

  /**
   * <p>Constructor for ManagedPlayer.</p>
   */
  public ManagedPlayer() {
    playerListeners = new CopyOnWriteArrayList<>();
    try {
      common = SequencerManager.getInstance();
    } catch (MidiUnavailableException e) {
      Logger.getLogger("org.jfugue").severe(e.getLocalizedMessage());
    }
  }

  /**
   * <p>addManagedPlayerListener.</p>
   *
   * @param listener a {@link org.jfugue.player.ManagedPlayerListener} object.
   */
  public void addManagedPlayerListener(ManagedPlayerListener listener) {
    playerListeners.add(listener);
  }

  /**
   * <p>removeManagedPlayerListener.</p>
   *
   * @param listener a {@link org.jfugue.player.ManagedPlayerListener} object.
   */
  public void removeManagedPlayerListener(ManagedPlayerListener listener) {
    playerListeners.add(listener);
  }

  private List<ManagedPlayerListener> getManagedPlayerListeners() {
    return playerListeners;
  }

  /**
   * This method opens the sequencer (if it is not already open - @see PlayerCommon), sets the
   * sequence, tells listeners that play is starting, and starts the sequence.
   *
   * @param sequence a {@link javax.sound.midi.Sequence} object.
   * @throws javax.sound.midi.InvalidMidiDataException if any.
   * @throws javax.sound.midi.MidiUnavailableException if any.
   */
  public void start(Sequence sequence) throws InvalidMidiDataException, MidiUnavailableException {
    common.openSequencer();
//		common.connectSequencerToSynthesizer(); // TODO - TEST connectSequencerToSynthesizer in ManagedPlayer // 2016-03-07 THIS IS CAUSING A PROBLEM WITH DOUBLE-HIT NOTES!!!
    common.addEndOfTrackListener(this);
    common.getSequencer().setSequence(sequence);
    fireOnStarted(sequence);
    this.started = true;
    this.paused = false;
    this.finished = false;
    common.getSequencer().start();
  }

  /**
   * To resume play, @see resume()
   */
  public void pause() {
    fireOnPaused();
    this.paused = true;
    common.getSequencer().stop();
  }

  /**
   * To pause play, @see pause()
   */
  public void resume() {
    fireOnResumed();
    this.paused = false;
    common.getSequencer().start();
  }

  /**
   * <p>seek.</p>
   *
   * @param tick a long.
   */
  public void seek(long tick) {
    fireOnSeek(tick);
    common.getSequencer().setTickPosition(tick);
  }

  private void finish() {
    common.close();
    this.finished = true;
    fireOnFinished();
  }

  /**
   * <p>reset.</p>
   */
  public void reset() {
    common.close();
    this.started = false;
    this.paused = false;
    this.finished = false;
    fireOnReset();
  }

  /**
   * <p>getTickLength.</p>
   *
   * @return a long.
   */
  public long getTickLength() {
    return common.getSequencer().getTickLength();
  }

  /**
   * <p>getTickPosition.</p>
   *
   * @return a long.
   */
  public long getTickPosition() {
    return common.getSequencer().getTickPosition();
  }

  /**
   * <p>isStarted.</p>
   *
   * @return a boolean.
   */
  public boolean isStarted() {
    return this.started;
  }

  /**
   * <p>isFinished.</p>
   *
   * @return a boolean.
   */
  public boolean isFinished() {
    return this.finished;
  }

  /**
   * <p>isPaused.</p>
   *
   * @return a boolean.
   */
  public boolean isPaused() {
    return this.paused;
  }

  /**
   * <p>isPlaying.</p>
   *
   * @return a boolean.
   */
  public boolean isPlaying() {
    return common.getSequencer().isRunning();
  }

  /** {@inheritDoc} */
  @Override
  public void onEndOfTrack() {
    finish();
  }

  private void fireOnStarted(Sequence sequence) {
    List<ManagedPlayerListener> listeners = getManagedPlayerListeners();
    for (ManagedPlayerListener listener : listeners) {
      listener.onStarted(sequence);
    }
  }

  private void fireOnFinished() {
    List<ManagedPlayerListener> listeners = getManagedPlayerListeners();
    for (ManagedPlayerListener listener : listeners) {
      listener.onFinished();
    }
  }

  private void fireOnPaused() {
    List<ManagedPlayerListener> listeners = getManagedPlayerListeners();
    for (ManagedPlayerListener listener : listeners) {
      listener.onPaused();
    }
  }

  private void fireOnResumed() {
    List<ManagedPlayerListener> listeners = getManagedPlayerListeners();
    for (ManagedPlayerListener listener : listeners) {
      listener.onResumed();
    }
  }

  private void fireOnSeek(long tick) {
    List<ManagedPlayerListener> listeners = getManagedPlayerListeners();
    for (ManagedPlayerListener listener : listeners) {
      listener.onSeek(tick);
    }
  }

  private void fireOnReset() {
    List<ManagedPlayerListener> listeners = getManagedPlayerListeners();
    for (ManagedPlayerListener listener : listeners) {
      listener.onReset();
    }
  }

} 

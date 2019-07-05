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

package org.jfugue.tools;

import org.jfugue.midi.MidiDefaults;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;

/**
 * <p>ComputeDurationForEachTrackTool class.</p>
 *
 * @author David Koelle
 * @version 5.0
 */
public class ComputeDurationForEachTrackTool extends ParserListenerAdapter {

  private final double[] durations;
  private int currentTrack;

  /**
   * <p>Constructor for ComputeDurationForEachTrackTool.</p>
   */
  public ComputeDurationForEachTrackTool() {
    durations = new double[MidiDefaults.TRACKS];
    currentTrack = 0;
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackChanged(byte track) {
    this.currentTrack = track;
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteParsed(Note note) {
    if (note.isFirstNote()) {
      double durationOfCurrentFirstNote = note.getDuration();
      durations[currentTrack] += note.getDuration();
    }
//        if (note.isHarmonicNote()) {
//
//        }
    durations[currentTrack] += note.getDuration();
  }

  /**
   * <p>Getter for the field <code>durations</code>.</p>
   *
   * @return an array of {@link double} objects.
   */
  public double[] getDurations() {
    return this.durations;
  }
}


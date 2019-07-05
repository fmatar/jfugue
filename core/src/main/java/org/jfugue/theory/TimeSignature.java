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

package org.jfugue.theory;

/**
 * <p>TimeSignature class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class TimeSignature {

  /** Constant <code>DEFAULT_TIMESIG</code> */
  public static final TimeSignature DEFAULT_TIMESIG = new TimeSignature(4, 4);
  private int beatsPerMeasure;
  private int durationForBeat;

  /**
   * <p>Constructor for TimeSignature.</p>
   *
   * @param beatsPerMeasure a int.
   * @param durationForBeat a int.
   */
  public TimeSignature(int beatsPerMeasure, int durationForBeat) {
    this.beatsPerMeasure = beatsPerMeasure;
    this.durationForBeat = durationForBeat;
  }

  /**
   * <p>Getter for the field <code>beatsPerMeasure</code>.</p>
   *
   * @return a int.
   */
  public int getBeatsPerMeasure() {
    return this.beatsPerMeasure;
  }

  /**
   * <p>Setter for the field <code>beatsPerMeasure</code>.</p>
   *
   * @param beatsPerMeasure a int.
   * @return a {@link org.jfugue.theory.TimeSignature} object.
   */
  public TimeSignature setBeatsPerMeasure(int beatsPerMeasure) {
    this.beatsPerMeasure = beatsPerMeasure;
    return this;
  }

  /**
   * <p>Getter for the field <code>durationForBeat</code>.</p>
   *
   * @return a int.
   */
  public int getDurationForBeat() {
    return this.durationForBeat;
  }

  /**
   * <p>Setter for the field <code>durationForBeat</code>.</p>
   *
   * @param durationForBeat a int.
   * @return a {@link org.jfugue.theory.TimeSignature} object.
   */
  public TimeSignature setDurationForBeat(int durationForBeat) {
    this.durationForBeat = durationForBeat;
    return this;
  }
}

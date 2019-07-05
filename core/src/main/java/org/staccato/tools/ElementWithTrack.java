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

package org.staccato.tools;

/**
 * This class wraps an element with the track and layer in which the element appears. For example, a
 * Note element (like "C5") is associated with information about its channel. Layers are frequently
 * used for notes in the MIDI percussion track (10th track, known in Staccato as V9)
 */
class ElementWithTrack {

  private final byte track;
  private final byte layer;
  private final String element;

  /**
   * <p>Constructor for ElementWithTrack.</p>
   *
   * @param track a byte.
   * @param layer a byte.
   * @param element a {@link java.lang.String} object.
   */
  public ElementWithTrack(byte track, byte layer, String element) {
    this.track = track;
    this.layer = layer;
    this.element = element;
  }

  /**
   * <p>Getter for the field <code>track</code>.</p>
   *
   * @return a byte.
   */
  public byte getTrack() {
    return this.track;
  }

  /**
   * <p>Getter for the field <code>layer</code>.</p>
   *
   * @return a byte.
   */
  public byte getLayer() {
    return this.layer;
  }

  /**
   * <p>Getter for the field <code>element</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getElement() {
    return this.element;
  }
}

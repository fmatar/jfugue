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

import org.jfugue.provider.KeyProviderFactory;

/**
 * <p>Key class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Key {

  /** Constant <code>DEFAULT_KEY</code> */
  public static final Key DEFAULT_KEY = new Key("C4maj");
  private final Note root;
  private Scale scale;

  /**
   * <p>Constructor for Key.</p>
   *
   * @param root a {@link org.jfugue.theory.Note} object.
   * @param scale a {@link org.jfugue.theory.Scale} object.
   */
  public Key(Note root, Scale scale) {
    this.root = root;
    this.scale = scale;
  }

  /**
   * <p>Constructor for Key.</p>
   *
   * @param chord a {@link org.jfugue.theory.Chord} object.
   */
  public Key(Chord chord) {
    this.root = chord.getRoot();
    if (chord.isMajor()) {
      this.scale = Scale.MAJOR;
    } else if (chord.isMinor()) {
      this.scale = Scale.MINOR;
    }
  }

  /**
   * This method requires a key signature represented by a chord name, like Cmaj, or 'K' followed by
   * sharps or flats, like "K####" for E Major
   *
   * @param keySignature a {@link java.lang.String} object.
   */
  public Key(String keySignature) {
    this(KeyProviderFactory.getKeyProvider().createKey(keySignature));
  }

  private Key(Key key) {
    this.root = key.root;
    this.scale = key.scale;
  }

  /**
   * <p>getKeySignature.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getKeySignature() {
    return this.root.toString() + this.scale.toString();
  }

  /**
   * <p>Getter for the field <code>root</code>.</p>
   *
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note getRoot() {
    return this.root;
  }

  /**
   * <p>Getter for the field <code>scale</code>.</p>
   *
   * @return a {@link org.jfugue.theory.Scale} object.
   */
  public Scale getScale() {
    return this.scale;
  }
}

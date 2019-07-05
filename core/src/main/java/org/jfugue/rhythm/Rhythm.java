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

package org.jfugue.rhythm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfugue.midi.MidiDefaults;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.staccato.StaccatoUtil;

/**
 * <p>Rhythm class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Rhythm implements PatternProducer {

  private static final Map<Character, String> DEFAULT_RHYTHM_KIT = new HashMap<Character, String>() {{
    put('.', "Ri");
    put('O', "[BASS_DRUM]i");
    put('o', "Rs [BASS_DRUM]s");
    put('S', "[ACOUSTIC_SNARE]i");
    put('s', "Rs [ACOUSTIC_SNARE]s");
    put('^', "[PEDAL_HI_HAT]i");
    put('`', "[PEDAL_HI_HAT]s Rs");
    put('*', "[CRASH_CYMBAL_1]i");
    put('+', "[CRASH_CYMBAL_1]s Rs");
    put('X', "[HAND_CLAP]i");
    put('x', "Rs [HAND_CLAP]s");
    put(' ', "Ri");
  }};
  private final Map<Integer, List<AltLayer>> altLayers;
  private List<String> layers;
  private Map<Character, String> rhythmKit;
  private int length = 1;

  /**
   * <p>Constructor for Rhythm.</p>
   */
  public Rhythm() {
    this(DEFAULT_RHYTHM_KIT);
  }

  /**
   * <p>Constructor for Rhythm.</p>
   *
   * @param layers a {@link java.lang.String} object.
   */
  public Rhythm(String... layers) {
    this(DEFAULT_RHYTHM_KIT, layers);
  }

  private Rhythm(Map<Character, String> rhythmKit) {
    layers = new ArrayList<>();
    altLayers = new HashMap<>();
    setRhythmKit(rhythmKit);
  }

  private Rhythm(Map<Character, String> rhythmKit, String... layers) {
    this(rhythmKit);
    for (String layer : layers) {
      this.addLayer(layer);
    }
  }

  /**
   * Combines rhythms into multiple layers. If there are more than MAX_LAYERS layers in the provided
   * rhythms, only the first MAX_LAYERS are used (for example, if you pass five rhythms that each
   * have four layers, the combined rhythm will only contain the layers from the first four
   * rhythms). This method also ensures that the Rhythm Kit for each of the provided Rhythms is
   * added to the return value's Rhythm Kit.
   *
   * @param rhythms the rhythms to combine
   * @return the combined rhythm
   */
  public static Rhythm combine(Rhythm... rhythms) {
    Rhythm retVal = new Rhythm();
    for (Rhythm rhythm : rhythms) {
      // Add the rhythm's Rhythm Kit to the return value's rhythm kit
      retVal.getRhythmKit().putAll(rhythm.getRhythmKit());

      // Add the rhythm data
      for (String layer : rhythm.getLayers()) {
        if (retVal.canAddLayer()) {
          retVal.addLayer(layer);
        } else {
          return retVal;
        }
      }

      // Add the alt layer into
      for (int key : rhythm.altLayers.keySet()) {
        retVal.getAltLayersForLayer(key).addAll(rhythm.getAltLayersForLayer(key));
      }

      // Figure out the length of the new rhythm
      if (retVal.getLength() < rhythm.getLength()) {
        retVal.setLength(rhythm.getLength());
      }
    }

    return retVal;
  }

  private Map<Character, String> getRhythmKit() {
    return this.rhythmKit;
  }

  /**
   * <p>Setter for the field <code>rhythmKit</code>.</p>
   *
   * @param rhythmKit a {@link java.util.Map} object.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm setRhythmKit(Map<Character, String> rhythmKit) {
    this.rhythmKit = rhythmKit;
    return this;
  }

  /**
   * Adds a layer to this Rhythm, but fails silently if the rhythm already has MAX_LAYERS layers.
   *
   * @param layer a {@link java.lang.String} object.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addLayer(String layer) {
    if (this.layers.size() < MidiDefaults.LAYERS) {
      this.layers.add(layer);
    }
    return this;
  }

  private String getLayer(int layer) {
    return this.layers.get(layer);
  }

  /**
   * Returns all layers that have been added with the traditional addLayer() method - but to truly
   * find out what the layer will sound like at a given segment, use getLayersForSegment(), which
   * takes alt layers into account.
   *
   * @see
   */
  private List<String> getLayers() {
    return this.layers;
  }

  /**
   * Sets all of the layers
   *
   * @param layers a {@link java.util.List} object.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm setLayers(List<String> layers) {
    if (layers.size() > MidiDefaults.LAYERS) {
      throw new RuntimeException(
        "Size of the List<String> provided to Rhythm.setLayers() is greater than "
          + MidiDefaults.LAYERS);
    }
    this.layers = layers;
    return this;
  }

  /**
   * Returns all layers, including altLayers, for the given segment
   *
   * @see getLayers
   */
  private String[] getLayersAt(int segment) {
    String[] retVal = new String[layers.size()];
    for (int layer = 0; layer < layers.size(); layer++) {
      List<AltLayer> altLayers = getSortedAltLayersForLayer(layer);
      // Start with the base layer
      retVal[layer] = getLayer(layer);

      // See if the base layer should be replaced by any of the alt layers
      for (AltLayer altLayer : altLayers) {
        if (altLayer.shouldProvideAltLayer(segment)) {
          // Remember that RhythmAltLayerProvider is allowed to return null if there is nothing to add
          String rhythmOrNull = altLayer.getAltLayer(segment);
          if (rhythmOrNull != null) {
            retVal[layer] = rhythmOrNull;
          }
        }
      }
    }
    return retVal;
  }

  /**
   * Returns true if the number of layers is less than MAX_LAYERS, which is limited to 16 by the
   * MIDI Specification
   */
  private boolean canAddLayer() {
    return (this.layers.size() < MidiDefaults.LAYERS);
  }

  /**
   * <p>clone.</p>
   *
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm clone() {
    return new Rhythm(this.rhythmKit, this.getLayers().toArray(new String[0]));
  }

  /**
   * Returns all AltLayers for the given layer; the resulting list is unsorted by z-order
   *
   * @see getSortedAltLatersForLayer
   */
  private List<AltLayer> getAltLayersForLayer(int layer) {
    altLayers.computeIfAbsent(layer, k -> new ArrayList<>());
    return altLayers.get(layer);
  }

  /**
   * Returns all AltLayers for the given layer sorted by each AltLayer's z-order
   */
  private List<AltLayer> getSortedAltLayersForLayer(int layer) {
    List<AltLayer> retVal = getAltLayersForLayer(layer);
    retVal.sort(Comparator.comparingInt(altLayer -> altLayer.zOrder));
    return retVal;
  }

  /**
   * Sets an alt layer that will recur every recurrence times *after* the start index is reached. If
   * the start index is 2 and the recurrence is 5, this alt layer will be used every time the
   * segment % recurrence == start. By default, this has a Z-Order of 1.
   *
   * @param layer a int.
   * @param start a int.
   * @param end a int.
   * @param recurrence a int.
   * @param rhythmString a {@link java.lang.String} object.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addRecurringAltLayer(int layer, int start, int end, int recurrence,
    String rhythmString) {
    return addRecurringAltLayer(layer, start, end, recurrence, rhythmString, 1);
  }

  /**
   * Sets an alt layer that will recur every recurrence times *after* the start index is reached. If
   * the start index is 2 and the recurrence is 5, this alt layer will be used every time the
   * segment % recurrence == start
   *
   * @param layer a int.
   * @param start a int.
   * @param end a int.
   * @param recurrence a int.
   * @param rhythmString a {@link java.lang.String} object.
   * @param zOrder a int.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addRecurringAltLayer(int layer, int start, int end, int recurrence,
    String rhythmString, int zOrder) {
    getAltLayersForLayer(layer)
      .add(new AltLayer(start, end, recurrence, rhythmString, null, zOrder));
    return this;
  }

  /**
   * Sets an alt layer that will play between and including the start and end indices. By default,
   * this has a Z-Order of 2.
   *
   * @param layer a int.
   * @param start a int.
   * @param end a int.
   * @param rhythmString a {@link java.lang.String} object.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addRangedAltLayer(int layer, int start, int end, String rhythmString) {
    return addRangedAltLayer(layer, start, end, rhythmString, 2);
  }

  /**
   * Sets an alt layer that will play between and including the start and end indices.
   *
   * @param layer a int.
   * @param start a int.
   * @param end a int.
   * @param rhythmString a {@link java.lang.String} object.
   * @param zOrder a int.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addRangedAltLayer(int layer, int start, int end, String rhythmString, int zOrder) {
    getAltLayersForLayer(layer).add(new AltLayer(start, end, -1, rhythmString, null, zOrder));
    return this;
  }

  /**
   * Sets an alt layer that will play one time, at the given segment. By default, this has a Z-Order
   * of 3.
   *
   * @param layer a int.
   * @param oneTime a int.
   * @param rhythmString a {@link java.lang.String} object.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addOneTimeAltLayer(int layer, int oneTime, String rhythmString) {
    return addOneTimeAltLayer(layer, oneTime, rhythmString, 3);
  }

  /**
   * Sets an alt layer that will play one time, at the given segment.
   *
   * @param layer a int.
   * @param oneTime a int.
   * @param rhythmString a {@link java.lang.String} object.
   * @param zOrder a int.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addOneTimeAltLayer(int layer, int oneTime, String rhythmString, int zOrder) {
    getAltLayersForLayer(layer).add(new AltLayer(oneTime, oneTime, -1, rhythmString, null, zOrder));
    return this;
  }

  /**
   * Gives a RhythmAltLayerProvider, which will make its own determination about what type of alt
   * layer to play, and when to play it. By default, this has a Z-Order of 4.
   *
   * @see RhythmAltLayerProvider
   * @param layer a int.
   * @param altLayerProvider a {@link org.jfugue.rhythm.RhythmAltLayerProvider} object.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm addAltLayerProvider(int layer, RhythmAltLayerProvider altLayerProvider) {
    return addAltLayerProvider(layer, altLayerProvider, 4);
  }

  /**
   * Gives a RhythmAltLayerProvider, which will make its own determination about what type of alt
   * layer to play, and when to play it.
   *
   * @see RhythmAltLayerProvider
   */
  private Rhythm addAltLayerProvider(int layer, RhythmAltLayerProvider altLayerProvider,
    int zOrder) {
    getAltLayersForLayer(layer)
      .add(new AltLayer(0, getLength(), -1, null, altLayerProvider, zOrder));
    return this;
  }

  /**
   * <p>Getter for the field <code>length</code>.</p>
   *
   * @return a int.
   */
  public int getLength() {
    return this.length;
  }

  /**
   * Sets the length of the rhythm, which is the number of times that a single pattern is repeated.
   * For example, creating a layer of "S...S...S...O..." and a length of 3 would result in a Rhythm
   * pattern of "S...S...S...O...S...S...S...O...S...S...S...O..."
   *
   * @param length a int.
   * @return a {@link org.jfugue.rhythm.Rhythm} object.
   */
  public Rhythm setLength(int length) {
    this.length = length;
    return this;
  }

  /**
   * Uses the RhythmKit to translate the given rhythm into a Staccato music string.
   *
   * @see getPattern
   */
  private String getStaccatoStringForRhythm(String rhythm) {
    StringBuilder buddy = new StringBuilder();
    for (char ch : rhythm.toCharArray()) {
      if (rhythmKit.get(ch) != null) {
        buddy.append(rhythmKit.get(ch));
        buddy.append(" ");
      } else {
        throw new RuntimeException("The character '" + ch + "' used in the rhythm layer \"" + rhythm
          + "\" is not associated with a Staccato music string in the RhythmKit " + rhythmKit);
      }
    }
    return buddy.toString().trim();
  }

  /**
   * <p>getPatternAt.</p>
   *
   * @param segment a int.
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public Pattern getPatternAt(int segment) {
    Pattern pattern = new Pattern(StaccatoUtil.createTrackElement((byte) 9));
    byte layerCounter = 0;
    for (String layer : getLayersAt(segment)) {
      pattern.add(StaccatoUtil.createLayerElement(layerCounter));
      layerCounter++;
      pattern.add(getStaccatoStringForRhythm(layer));
    }
    return pattern;
  }

  /** {@inheritDoc} */
  @Override
  public Pattern getPattern() {
    Pattern fullPattern = new Pattern();
    for (int segment = 0; segment < getLength(); segment++) {
      fullPattern.add(getPatternAt(segment));
    }
    return fullPattern;
  }

  /**
   * Returns the full rhythm, including alt layers, but not translated into Staccato music strings
   * by looking up rhythm entries into the RhythmKit
   *
   * @return an array of {@link java.lang.String} objects.
   */
  public String[] getRhythm() {
    // Create the full rhythm for each layer and each segment
    StringBuilder[] builders = new StringBuilder[this.layers.size()];
    for (int i = 0; i < layers.size(); i++) {
      builders[i] = new StringBuilder();
      for (int segment = 0; segment < getLength(); segment++) {
        builders[i].append(getLayersAt(segment)[i]);
      }
    }

    // Get strings from the builders
    String[] retVal = new String[this.layers.size()];
    for (int i = 0; i < layers.size(); i++) {
      retVal[i] = builders[i].toString();
    }

    return retVal;
  }

  class AltLayer {

    final String rhythmString;
    final RhythmAltLayerProvider altLayerProvider;
    final int startIndex;
    final int endIndex;
    final int recurrence;
    final int zOrder;

    AltLayer(int start, int end, int recurrence, String rhythmString,
      RhythmAltLayerProvider altLayerProvider, int zOrder) {
      this.startIndex = start;
      this.endIndex = end;
      this.recurrence = recurrence;
      this.rhythmString = rhythmString;
      this.altLayerProvider = altLayerProvider;
      this.zOrder = zOrder;
    }

    /**
     * Indicates whether this alt layer should be provided for the given segment
     */
    boolean shouldProvideAltLayer(int segment) {
      // ALways return true if there is an AltLayerProvider
      if (altLayerProvider != null) {
        return true;
      }

      // Check if we're in the right range of start and end indexes, and check the recurrence
      if ((segment >= startIndex) && (segment <= endIndex)) {
        if (recurrence == -1) {
          return true;
        }
        return segment % recurrence == startIndex;
      }

      return false;
    }

    /**
     * Returns this alt layer, assuming that shouldProvideAltLayer is true
     */
    String getAltLayer(int segment) {
      if (altLayerProvider != null) {
        return altLayerProvider.provideAltLayer(segment);
      } else {
        return this.rhythmString;
      }
    }
  }
}


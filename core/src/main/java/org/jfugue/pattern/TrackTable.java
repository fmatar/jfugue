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

package org.jfugue.pattern;

import java.util.ArrayList;
import java.util.List;
import org.jfugue.rhythm.Rhythm;

/**
 * <p>TrackTable class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class TrackTable implements PatternProducer {

  /** Constant <code>RHYTHM_TRACK=9</code> */
  public static final int RHYTHM_TRACK = 9;
  private static final int NUM_TRACKS = 16;
  private final int length;
  private final double cellDuration;
  private final List<List<PatternProducer>> tracks;
  private final List<PatternProducer> trackSettings;

  /**
   * <p>Constructor for TrackTable.</p>
   *
   * @param length a int.
   * @param cellDuration a double.
   */
  public TrackTable(int length, double cellDuration) {
    this.length = length;
    this.cellDuration = cellDuration;
    tracks = new ArrayList<>(TrackTable.NUM_TRACKS);
    trackSettings = new ArrayList<>(TrackTable.NUM_TRACKS);

    for (int i = 0; i < TrackTable.NUM_TRACKS; i++) {
      trackSettings.add(new Pattern(""));
      List<PatternProducer> list = new ArrayList<>(length);
      for (int u = 0; u < length; u++) {
        list.add(new Pattern("R/" + cellDuration));
      }
      tracks.add(list);
    }
  }

  /**
   * <p>getTrack.</p>
   *
   * @param track a int.
   * @return a {@link java.util.List} object.
   */
  public List<PatternProducer> getTrack(int track) {
    return tracks.get(track);
  }

  /**
   * <p>put.</p>
   *
   * @param track a int.
   * @param position a int.
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable put(int track, int position, PatternProducer patternProducer) {
    List<PatternProducer> trackList = this.tracks.get(track);
    if (trackList == null) {
      trackList = new ArrayList<>(getLength());
      this.tracks.add(track, trackList);
    }
    trackList.add(position, patternProducer.getPattern());
    return this;
  }

  /**
   * <p>put.</p>
   *
   * @param track a int.
   * @param start a int.
   * @param patternProducers a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable put(int track, int start, PatternProducer... patternProducers) {
    int counter = 0;
    for (PatternProducer producer : patternProducers) {
      this.put(track, start + counter, producer);
      counter++;
    }
    return this;
  }

  /**
   * Puts the given pattern in the track table at every 'nth' position
   *
   * @param track a int.
   * @param nth a int.
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable putAtIntervals(int track, int nth, PatternProducer patternProducer) {
    for (int position = 0; position < this.length; position += nth) {
      this.put(track, position, patternProducer);
    }
    return this;
  }

  /**
   * Puts the given pattern in the track table at every 'nth' position, starting with position
   * 'first' and ending with 'end'
   *
   * @param track a int.
   * @param first a int.
   * @param nth a int.
   * @param end a int.
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable putAtIntervals(int track, int first, int nth, int end,
    PatternProducer patternProducer) {
    for (int position = first; position < Math.min(this.length, end); position += nth) {
      this.put(track, position, patternProducer);
    }
    return this;
  }

  /**
   * As part of JFugue's fluent API, this method returns the instance of this class.
   *
   * @return The instance of this class
   * @param track a int.
   * @param start a int.
   * @param end a int.
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public TrackTable put(int track, int start, int end, PatternProducer patternProducer) {
    for (int i = start; i <= end; i++) {
      put(track, i, patternProducer);
    }
    return this;
  }

  /**
   * Lets you specify which cells in the TrackTable should be populated with the given
   * PatternProducer by using a String in which a period means "not in this cell" and any other
   * character means "in this cell". Example: put(1, pattern, "...XXXX..XX....XXXX..XX....");
   *
   * @param track a int.
   * @param periodMeansNo_DashMeansExtend_OtherMeansYes a {@link java.lang.String} object.
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable put(int track, String periodMeansNo_DashMeansExtend_OtherMeansYes,
    PatternProducer patternProducer) {
    for (int i = 0; i < periodMeansNo_DashMeansExtend_OtherMeansYes.length(); i++) {
      if (periodMeansNo_DashMeansExtend_OtherMeansYes.charAt(i) == '.') {
        // No op
      } else if (periodMeansNo_DashMeansExtend_OtherMeansYes.charAt(i) == '-') {
        put(track, i, new Pattern(""));
      } else {
        put(track, i, patternProducer);
      }
    }
    return this;
  }

  /**
   * <p>put.</p>
   *
   * @param rhythm a {@link org.jfugue.rhythm.Rhythm} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable put(Rhythm rhythm) {
    for (int i = 0; i < rhythm.getLength(); i++) {
      put(9, i, rhythm.getPatternAt(i));
    }
    return this;
  }

  /**
   * <p>get.</p>
   *
   * @param track a int.
   * @param position a int.
   * @return a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public PatternProducer get(int track, int position) {
    return tracks.get(track).get(position);
  }

  /**
   * <p>clear.</p>
   *
   * @param track a int.
   * @param position a int.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable clear(int track, int position) {
    put(track, position, new Pattern(""));
    return this;
  }

  /**
   * <p>reset.</p>
   *
   * @param track a int.
   * @param position a int.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable reset(int track, int position) {
    put(track, position, new Pattern("R/" + cellDuration));
    return this;
  }

  private int getLength() {
    return this.length;
  }

  /**
   * <p>Setter for the field <code>trackSettings</code>.</p>
   *
   * @param track a int.
   * @param p a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable setTrackSettings(int track, PatternProducer p) {
    this.trackSettings.add(track, p);
    return this;
  }

  /**
   * <p>Setter for the field <code>trackSettings</code>.</p>
   *
   * @param track a int.
   * @param s a {@link java.lang.String} object.
   * @return a {@link org.jfugue.pattern.TrackTable} object.
   */
  public TrackTable setTrackSettings(int track, String s) {
    this.trackSettings.add(track, new Pattern(s));
    return this;
  }

  /**
   * <p>Getter for the field <code>trackSettings</code>.</p>
   *
   * @param track a int.
   * @return a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public PatternProducer getTrackSettings(int track) {
    return this.trackSettings.get(track);
  }

  /**
   * <p>getPatternAt.</p>
   *
   * @param column a int.
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public Pattern getPatternAt(int column) {
    Pattern columnPattern = new Pattern();
    for (List<PatternProducer> track : tracks) {
      PatternProducer p = track.get(column);
      columnPattern.add(new Pattern(p).setVoice(tracks.indexOf(track)));
    }
    return columnPattern;
  }

  /** {@inheritDoc} */
  @Override
  public Pattern getPattern() {
    Pattern pattern = new Pattern();
    int trackCounter = 0;

    // First, add the track settings
    for (PatternProducer trackSetting : trackSettings) {
      if (!trackSetting.toString().equals("")) {
        pattern.add(new Pattern(trackSetting).setVoice(trackCounter));
      }
      trackCounter++;
    }

    // Next, for each track, add it to the pattern
    for (List<PatternProducer> track : tracks) {
      for (PatternProducer p : track) {
        pattern.add(new Pattern(p).setVoice(tracks.indexOf(track)));
      }
    }
    return pattern;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getPattern().toString();
  }
}

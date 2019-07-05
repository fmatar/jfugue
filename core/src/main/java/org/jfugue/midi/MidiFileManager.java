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

package org.jfugue.midi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

/**
 * <p>MidiFileManager class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class MidiFileManager {

  private static void save(Sequence sequence, OutputStream out) throws IOException {
    int[] writers = MidiSystem.getMidiFileTypes(sequence);
    if (writers.length == 0) {
      return;
    }
    MidiSystem.write(sequence, writers[0], out);
  }

  /**
   * Convenience method to make it easier to save a file
   *
   * @param sequence a {@link javax.sound.midi.Sequence} object.
   * @param file a {@link java.io.File} object.
   * @throws java.io.IOException if any.
   */
  public static void save(Sequence sequence, File file) throws IOException {
    MidiFileManager.save(sequence, new FileOutputStream(file));
  }

  /**
   * <p>load.</p>
   *
   * @param file a {@link java.io.File} object.
   * @return a {@link javax.sound.midi.Sequence} object.
   * @throws java.io.IOException if any.
   * @throws javax.sound.midi.InvalidMidiDataException if any.
   */
  public static Sequence load(File file) throws IOException, InvalidMidiDataException {
    return MidiSystem.getSequence(file);
  }

  private static void savePatternToMidi(PatternProducer patternProducer, OutputStream out)
    throws IOException {
    MidiFileManager.save(new Player().getSequence(patternProducer), out);
  }

  /**
   * Convenience method to make it easier to save a file
   *
   * @param patternProducer a {@link org.jfugue.pattern.PatternProducer} object.
   * @param file a {@link java.io.File} object.
   * @throws java.io.IOException if any.
   */
  public static void savePatternToMidi(PatternProducer patternProducer, File file)
    throws IOException {
    MidiFileManager.savePatternToMidi(patternProducer, new FileOutputStream(file));
  }

  private static Pattern loadPatternFromMidi(InputStream in)
    throws IOException, InvalidMidiDataException {
    MidiParser midiParser = new MidiParser();
    StaccatoParserListener staccatoListener = new StaccatoParserListener();
    midiParser.addParserListener(staccatoListener);
    midiParser.parse(MidiSystem.getSequence(in));
    return staccatoListener.getPattern();
  }

  /**
   * Convenience method to make it easier to load a Pattern from a file
   *
   * @param file a {@link java.io.File} object.
   * @return a {@link org.jfugue.pattern.Pattern} object.
   * @throws java.io.IOException if any.
   * @throws javax.sound.midi.InvalidMidiDataException if any.
   */
  public static Pattern loadPatternFromMidi(File file)
    throws IOException, InvalidMidiDataException {
    return MidiFileManager.loadPatternFromMidi(new FileInputStream(file));
  }

  /**
   * Convenience method to make it easier to load a Pattern from a URL
   *
   * @param url a {@link java.net.URL} object.
   * @return a {@link org.jfugue.pattern.Pattern} object.
   * @throws java.io.IOException if any.
   * @throws javax.sound.midi.InvalidMidiDataException if any.
   */
  public static Pattern loadPatternFromMidi(URL url) throws IOException, InvalidMidiDataException {
    return MidiFileManager.loadPatternFromMidi(url.openStream());
  }

}

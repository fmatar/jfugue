package org.jfugue.manualtest.midi;

/*
 * JFugue - API for Music Programming
 * This class copyright (C) 2003-2010  Karl Helgason and David Koelle
 *
 * http://www.jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.jfugue.midi.PatchProvider;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.player.Player;
import org.jfugue.player.SequencerManager;

/**
 * This utility generates a WAV file from the given MIDI sequence. You can also provide a soundbank
 * file and specific patches in the soundbank to load.
 */
class Midi2WavUtils {

  // Private class, no instantiation
  private Midi2WavUtils() {
  }

  /**
   * Helper function, not part of the API TODO: getSequenceFromPattern probably not necessary - most
   * same as player.getSequence
   */
  private static Sequence getSequenceFromPattern(PatternProducer pattern)
    throws MidiUnavailableException {
    SequencerManager.getInstance().setSequencer(MidiSystem.getSequencer(false));
    Player player = new Player();
    return player.getSequence(pattern);
  }

  /**
   * Creates a WAV file based on the Pattern, using the sounds from the specified soundbank; to
   * prevent memory problems, this method asks for an array of patches (instruments) to load.
   */
  public static void createWavFile(PatternProducer pattern, File outputFile)
    throws MidiUnavailableException, InvalidMidiDataException, IOException {
    createWavFile(AudioUtils.DEFAULT_PATCH_PROVIDER, getSequenceFromPattern(pattern), outputFile);
  }

  /**
   * Creates a WAV file based on the Pattern, using the sounds from the specified soundbank; to
   * prevent memory problems, this method asks for an array of patches (instruments) to load.
   */
  public static void createWavFile(PatchProvider patchProvider, PatternProducer pattern,
    File outputFile) throws MidiUnavailableException, InvalidMidiDataException, IOException {
    createWavFile(patchProvider, getSequenceFromPattern(pattern), outputFile);
  }

  /**
   * Creates a WAV file based on the Pattern, using the sounds from the specified soundbank; to
   * prevent memory problems, this method asks for an array of patches (instruments) to load.
   */
  public static void createWavFile(Sequence sequence, File outputFile)
    throws MidiUnavailableException, InvalidMidiDataException, IOException {
    createWavFile(AudioUtils.DEFAULT_PATCH_PROVIDER, sequence, outputFile);
  }

  /**
   * Creates a WAV file based on the Sequence, using the sounds from the specified soundbank; to
   * prevent memory problems, this method asks for an array of patches (instruments) to load.
   */
  private static void createWavFile(PatchProvider patchProvider, Sequence sequence, File outputFile)
    throws MidiUnavailableException, InvalidMidiDataException, IOException {
    // Open the Synthesizer and load the requested instruments
    try (AudioInputStream stream = AudioUtils
      .getAudioInputStream(sequence, patchProvider, AudioUtils.DEFAULT_AUDIO_FORMAT,
        AudioUtils.DEFAULT_INFO)) {
      AudioSystem.write(stream, AudioFileFormat.Type.WAVE, outputFile);
    }
  }
}
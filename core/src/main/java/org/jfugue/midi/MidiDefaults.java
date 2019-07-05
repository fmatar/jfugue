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

import org.jfugue.theory.TimeSignature;

/**
 * <p>MidiDefaults interface.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public interface MidiDefaults {

  /** Constant <code>DEFAULT_DIVISION_TYPE=0.0f</code> */
  float DEFAULT_DIVISION_TYPE = 0.0f;
  /** Constant <code>DEFAULT_RESOLUTION_TICKS_PER_BEAT=128</code> */
  int DEFAULT_RESOLUTION_TICKS_PER_BEAT = 128;
  /** Constant <code>DEFAULT_TEMPO_BEATS_PER_MINUTE=120</code> */
  int DEFAULT_TEMPO_BEATS_PER_MINUTE = 120;
  /** Constant <code>DEFAULT_TEMPO_BEATS_PER_WHOLE=4</code> */
  int DEFAULT_TEMPO_BEATS_PER_WHOLE = 4;
  /** Constant <code>DEFAULT_METRONOME_PULSE=24</code> */
  int DEFAULT_METRONOME_PULSE = 24;
  /** Constant <code>DEFAULT_THIRTYSECOND_NOTES_PER_24_MIDI_CLOCK_SIGNALS=8</code> */
  int DEFAULT_THIRTYSECOND_NOTES_PER_24_MIDI_CLOCK_SIGNALS = 8;
  /** Constant <code>TRACKS=16</code> */
  int TRACKS = 16;
  /** Constant <code>LAYERS=16</code> */
  int LAYERS = 16;
  /** Constant <code>MS_PER_MIN=60000.0d</code> */
  double MS_PER_MIN = 60000.0d;
  /** Constant <code>DEFAULT_MPQ=50</code> */
  int DEFAULT_MPQ = 50; // Milliseconds per quarter note
  /** Constant <code>SET_TEMPO_MESSAGE_TYPE=0x51</code> */
  byte SET_TEMPO_MESSAGE_TYPE = 0x51;
  /** Constant <code>PERCUSSION_TRACK=9</code> */
  byte PERCUSSION_TRACK = 9;
  /** Constant <code>MIN_PERCUSSION_NOTE=35</code> */
  byte MIN_PERCUSSION_NOTE = 35;
  /** Constant <code>MAX_PERCUSSION_NOTE=81</code> */
  byte MAX_PERCUSSION_NOTE = 81;
  /** Constant <code>MIN_ON_VELOCITY=0</code> */
  byte MIN_ON_VELOCITY = 0;
  /** Constant <code>MAX_ON_VELOCITY=127</code> */
  byte MAX_ON_VELOCITY = 127;
  /** Constant <code>MIDI_DEFAULT_ON_VELOCITY=64</code> */
  byte MIDI_DEFAULT_ON_VELOCITY = 64; // See also DefaultNoteSettingsManager
  /** Constant <code>MIN_OFF_VELOCITY=0</code> */
  byte MIN_OFF_VELOCITY = 0;
  /** Constant <code>MAX_OFF_VELOCITY=127</code> */
  byte MAX_OFF_VELOCITY = 127;
  /** Constant <code>MIDI_DEFAULT_OFF_VELOCITY=64</code> */
  byte MIDI_DEFAULT_OFF_VELOCITY = 64; // See also DefaultNoteSettingsManager
  /** Constant <code>DEFAULT_PATCH_BANK=0</code> */
  int DEFAULT_PATCH_BANK = 0;
  /** Constant <code>DEFAULT_TIME_SIGNATURE</code> */
  TimeSignature DEFAULT_TIME_SIGNATURE = new TimeSignature(4, 4);


  // Meta Message Type Values
  /** Constant <code>META_SEQUENCE_NUMBER=0x00</code> */
  byte META_SEQUENCE_NUMBER = 0x00;
  /** Constant <code>META_TEXT_EVENT=0x01</code> */
  byte META_TEXT_EVENT = 0x01;
  /** Constant <code>META_COPYRIGHT_NOTICE=0x02</code> */
  byte META_COPYRIGHT_NOTICE = 0x02;
  /** Constant <code>META_SEQUENCE_NAME=0x03</code> */
  byte META_SEQUENCE_NAME = 0x03;
  /** Constant <code>META_INSTRUMENT_NAME=0x04</code> */
  byte META_INSTRUMENT_NAME = 0x04;
  /** Constant <code>META_LYRIC=0x05</code> */
  byte META_LYRIC = 0x05;
  /** Constant <code>META_MARKER=0x06</code> */
  byte META_MARKER = 0x06;
  /** Constant <code>META_CUE_POINT=0x07</code> */
  byte META_CUE_POINT = 0x07;
  /** Constant <code>META_MIDI_CHANNEL_PREFIX=0x20</code> */
  byte META_MIDI_CHANNEL_PREFIX = 0x20;
  /** Constant <code>META_END_OF_TRACK=0x2F</code> */
  byte META_END_OF_TRACK = 0x2F;
  /** Constant <code>META_TEMPO=0x51</code> */
  byte META_TEMPO = 0x51;
  /** Constant <code>META_SMTPE_OFFSET=0x54</code> */
  byte META_SMTPE_OFFSET = 0x54;
  /** Constant <code>META_TIMESIG=0x58</code> */
  byte META_TIMESIG = 0x58;
  /** Constant <code>META_KEYSIG=0x59</code> */
  byte META_KEYSIG = 0x59;
  /** Constant <code>META_VENDOR=0x7F</code> */
  byte META_VENDOR = 0x7F;
}

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

import org.jfugue.midi.MidiDefaults;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.provider.NoteProviderFactory;
import org.staccato.DefaultNoteSettingsManager;
import org.staccato.NoteSubparser;

/**
 * <p>Note class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Note implements PatternProducer {

  /** Constant <code>NOTE_NAMES_COMMON</code> */
  public final static String[] NOTE_NAMES_COMMON = new String[]{"C", "C#", "D", "Eb", "E", "F",
    "F#", "G", "G#", "A", "Bb", "B"};
  /** Constant <code>PERCUSSION_NAMES</code> */
  public final static String[] PERCUSSION_NAMES = new String[]{
    // Percussion Name		// MIDI Note Value
    "ACOUSTIC_BASS_DRUM",  //       35
    "BASS_DRUM",      //       36
    "SIDE_STICK",      //       37
    "ACOUSTIC_SNARE",    //       38
    "HAND_CLAP",      //       39
    "ELECTRIC_SNARE",    //       40
    "LO_FLOOR_TOM",    //       41
    "CLOSED_HI_HAT",    //       42
    "HIGH_FLOOR_TOM",    //       43
    "PEDAL_HI_HAT",    //       44
    "LO_TOM",        //       45
    "OPEN_HI_HAT",        //       46
    "LO_MID_TOM",      //       47
    "HI_MID_TOM",      //       48
    "CRASH_CYMBAL_1",    //       49
    "HI_TOM",        //       50
    "RIDE_CYMBAL_1",    //       51
    "CHINESE_CYMBAL",    //       52
    "RIDE_BELL",      //       53
    "TAMBOURINE",      //       54
    "SPLASH_CYMBAL",    //       55
    "COWBELL",        //       56
    "CRASH_CYMBAL_2",    //       57
    "VIBRASLAP",      //       58
    "RIDE_CYMBAL_2",    //       59
    "HI_BONGO",      //       60
    "LO_BONGO",      //       61
    "MUTE_HI_CONGA",    //       62
    "OPEN_HI_CONGA",    //       63
    "LO_CONGA",      //       64
    "HI_TIMBALE",      //       65
    "LO_TIMBALE",      //       66
    "HI_AGOGO",      //       67
    "LO_AGOGO",      //       68
    "CABASA",        //       69
    "MARACAS",        //       70
    "SHORT_WHISTLE",    //       71
    "LONG_WHISTLE",    //       72
    "SHORT_GUIRO",      //       73
    "LONG_GUIRO",      //       74
    "CLAVES",        //       75
    "HI_WOOD_BLOCK",    //       76
    "LO_WOOD_BLOCK",    //       77
    "MUTE_CUICA",      //       78
    "OPEN_CUICA",      //       79
    "MUTE_TRIANGLE",    //       80
    "OPEN_TRIANGLE"      //       81
  };
  /** Constant <code>REST</code> */
  public static final Note REST = new Note(0).setRest(true);
  /** Constant <code>OCTAVE=12</code> */
  public static final byte OCTAVE = 12;
  /** Constant <code>MIN_OCTAVE=0</code> */
  public static final byte MIN_OCTAVE = 0;
  /** Constant <code>MAX_OCTAVE=10</code> */
  public static final byte MAX_OCTAVE = 10;
  private final static String[] NOTE_NAMES_SHARP = new String[]{"C", "C#", "D", "D#", "E", "F",
    "F#",
    "G", "G#", "A", "A#", "B"};
  private final static String[] NOTE_NAMES_FLAT = new String[]{"C", "Db", "D", "Eb", "E", "F", "Gb",
    "G", "Ab", "A", "Bb", "B"};
  public String originalString;
  private byte value;
  private double duration;
  private boolean wasOctaveExplicitlySet;
  private boolean wasDurationExplicitlySet;
  private byte onVelocity;
  private byte offVelocity;
  private boolean isRest;
  private boolean isStartOfTie;
  private boolean isEndOfTie;
  private boolean isFirstNote = true;
  private boolean isMelodicNote;
  private boolean isHarmonicNote;
  private boolean isPercussionNote;

  /**
   * <p>Constructor for Note.</p>
   */
  public Note() {
    this.onVelocity = DefaultNoteSettingsManager.getInstance().getDefaultOnVelocity();
    this.offVelocity = DefaultNoteSettingsManager.getInstance().getDefaultOffVelocity();
  }

  /**
   * <p>Constructor for Note.</p>
   *
   * @param note a {@link java.lang.String} object.
   */
  public Note(String note) {
    this(NoteProviderFactory.getNoteProvider().createNote(note));
  }

  /**
   * <p>Constructor for Note.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  public Note(Note note) {
    this.value = note.value;
    this.duration = note.duration;
    this.wasOctaveExplicitlySet = note.wasOctaveExplicitlySet;
    this.wasDurationExplicitlySet = note.wasDurationExplicitlySet;
    this.onVelocity = note.onVelocity;
    this.offVelocity = note.offVelocity;
    this.isRest = note.isRest;
    this.isStartOfTie = note.isStartOfTie;
    this.isEndOfTie = note.isEndOfTie;
    this.isFirstNote = note.isFirstNote;
    this.isMelodicNote = note.isMelodicNote;
    this.isHarmonicNote = note.isHarmonicNote;
    this.isPercussionNote = note.isPercussionNote;
    this.originalString = note.originalString;
  }

  /**
   * <p>Constructor for Note.</p>
   *
   * @param value a int.
   */
  public Note(int value) {
    this((byte) value);
  }

  /**
   * <p>Constructor for Note.</p>
   *
   * @param value a byte.
   */
  public Note(byte value) {
    this();
    this.value = value;
    this.setOctaveExplicitlySet(false);
    useDefaultDuration();
  }

  /**
   * <p>Constructor for Note.</p>
   *
   * @param value a int.
   * @param duration a double.
   */
  public Note(int value, double duration) {
    this((byte) value, duration);
  }

  private Note(byte value, double duration) {
    this();
    this.value = value;
    setDuration(duration);
  }

  /**
   * <p>isSameNote.</p>
   *
   * @param note1 a {@link java.lang.String} object.
   * @param note2 a {@link java.lang.String} object.
   * @return a boolean.
   */
  public static boolean isSameNote(String note1, String note2) {
    if (note1.equalsIgnoreCase(note2)) {
      return true;
    }
    for (int i = 0; i < NOTE_NAMES_COMMON.length; i++) {
      if (note1.equalsIgnoreCase(NOTE_NAMES_FLAT[i]) && note2
        .equalsIgnoreCase(NOTE_NAMES_SHARP[i])) {
        return true;
      }
      if (note1.equalsIgnoreCase(NOTE_NAMES_SHARP[i]) && note2
        .equalsIgnoreCase(NOTE_NAMES_FLAT[i])) {
        return true;
      }
    }
    return false;
  }

  /**
   * This is just Bubble Sort, but allows you to pass a Note.SortingCallback that returns a value
   * that you want to sort for a note. For example, to sort based on position in octave, your
   * SortingCallback would return note.getPositionInOctave(). This lets you sort by note value,
   * octave, position in octave, duration, velocity, and so on.
   *
   * @param notes an array of {@link org.jfugue.theory.Note} objects.
   * @param callback a {@link org.jfugue.theory.Note.SortingCallback} object.
   */
  public static void sortNotesBy(Note[] notes, SortingCallback callback) {
    Note temp;
    for (int i = 0; i < notes.length - 1; i++) {
      for (int j = 1; j < notes.length - i; j++) {
        if (callback.getSortingValue(notes[j - 1]) > callback.getSortingValue(notes[j])) {
          temp = notes[j - 1];
          notes[j - 1] = notes[j];
          notes[j] = temp;
        }
      }
    }
  }

  /**
   * <p>createRest.</p>
   *
   * @param duration a double.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public static Note createRest(double duration) {
    return new Note().setRest(true).setDuration(duration);
  }

  /**
   * Returns a MusicString representation of the given MIDI note value, which indicates a note and
   * an octave.
   *
   * @param noteValue this MIDI note value, like 60
   * @return a MusicString value, like C5
   */
  public static String getToneString(byte noteValue) {
    String buddy = getToneStringWithoutOctave(noteValue)
      + noteValue / Note.OCTAVE;
    return buddy;
  }

  /**
   * Returns a MusicString representation of the given MIDI note value, but just the note - not the
   * octave. This means that the value returned can not be used to accurately recalculate the
   * noteValue, since information will be missing. But this is useful for knowing what note within
   * any octave the corresponding value belongs to.
   *
   * @param noteValue this MIDI note value, like 60
   * @return a MusicString value, like C
   */
  public static String getToneStringWithoutOctave(byte noteValue) {
    return NOTE_NAMES_COMMON[noteValue % Note.OCTAVE];
  }

  /**
   * Returns a MusicString representation of the given MIDI note value, just the note (not the
   * octave), disposed to use either flats or sharps. Pass -1 to get a flat name and +1 to get a
   * sharp name for any notes that are accidentals.
   *
   * @param dispose -1 to get a flat value, +1 to get a sharp value
   * @param noteValue this MIDI note value, like 61
   * @return a MusicString value, like Db if -1 or C# if +1
   */
  public static String getDispositionedToneStringWithoutOctave(int dispose, byte noteValue) {
    if (dispose == -1) {
      return NOTE_NAMES_FLAT[noteValue % Note.OCTAVE];
    } else {
      return NOTE_NAMES_SHARP[noteValue % Note.OCTAVE];
    }
  }

  /**
   * Returns a MusicString representation of the given MIDI note value using the name of a
   * percussion instrument.
   *
   * @param noteValue this MIDI note value, like 60
   * @return a MusicString value, like [AGOGO]
   */
  public static String getPercussionString(byte noteValue) {
    String buddy = "["
      + PERCUSSION_NAMES[noteValue - 35]
      + "]";
    return buddy;
  }

  /**
   * Returns the frequency, in Hertz, for the given note. For example, the frequency for A5 (MIDI
   * note 69) is 440.0
   *
   * @return frequency in Hertz
   * @param note a {@link java.lang.String} object.
   */
  public static double getFrequencyForNote(String note) {
    return (note.toUpperCase().startsWith("R")) ? 0.0d
      : getFrequencyForNote(NoteProviderFactory.getNoteProvider().createNote(note).getValue());
  }

  /**
   * Returns the frequency, in Hertz, for the given note value. For example, the frequency for A5
   * (MIDI note 69) is 440.0
   *
   * @param noteValue the MIDI note value
   * @return frequency in Hertz
   */
  public static double getFrequencyForNote(int noteValue) {
    return truncateTo3DecimalPlaces(getPreciseFrequencyForNote(noteValue));
  }

  private static double truncateTo3DecimalPlaces(double preciseNumber) {
    return Math.rint(preciseNumber * 10000.0) / 10000.0;
  }

  private static double getPreciseFrequencyForNote(int noteValue) {
    return getFrequencyAboveBase(noteValue / 12.0);
  }

  private static double getFrequencyAboveBase(double octavesAboveBase) {
    return 8.1757989156 * Math.pow(2.0, octavesAboveBase);
  }

  /**
   * <p>isValidNote.</p>
   *
   * @param candidateNote a {@link java.lang.String} object.
   * @return a boolean.
   */
  public static boolean isValidNote(String candidateNote) {
    return NoteSubparser.getInstance().matches(candidateNote);
  }

  /**
   * <p>isValidQualifier.</p>
   *
   * @param candidateQualifier a {@link java.lang.String} object.
   * @return a boolean.
   */
  public static boolean isValidQualifier(String candidateQualifier) {
    return true; // TODO: Implement Note.isValidQualifier when necessary
  }

  /**
   * Returns a MusicString representation of a decimal duration.  This code currently only converts
   * single duration values representing whole, half, quarter, eighth, etc. durations; and dotted
   * durations associated with those durations (such as "h.", equal to 0.75).  This method does not
   * convert combined durations (for example, "hi" for 0.625). For these values, the original
   * decimal duration is returned in a string, prepended with a "/" to make the returned value a
   * valid MusicString duration indicator. It does handle durations greater than 1.0 (for example,
   * "wwww" for 4.0).
   *
   * @param decimalDuration The decimal value of the duration to convert
   * @return a MusicString fragment representing the duration
   */
  public static String getDurationString(double decimalDuration) {
    double originalDecimalDuration = decimalDuration;
    StringBuilder buddy = new StringBuilder();
    if (decimalDuration >= 1.0) {
      int numWholeDurations = (int) Math.floor(decimalDuration);
      buddy.append("w");
      if (numWholeDurations > 1) {
        buddy.append(numWholeDurations);
      }
      decimalDuration -= numWholeDurations;
    }
    if (decimalDuration == 0.75) {
      buddy.append("h.");
    } else if (decimalDuration == 0.5) {
      buddy.append("h");
    } else if (decimalDuration == 0.375) {
      buddy.append("q.");
    } else if (decimalDuration == 0.25) {
      buddy.append("q");
    } else if (decimalDuration == 0.1875) {
      buddy.append("i.");
    } else if (decimalDuration == 0.125) {
      buddy.append("i");
    } else if (decimalDuration == 0.09375) {
      buddy.append("s.");
    } else if (decimalDuration == 0.0625) {
      buddy.append("s");
    } else if (decimalDuration == 0.046875) {
      buddy.append("t.");
    } else if (decimalDuration == 0.03125) {
      buddy.append("t");
    } else if (decimalDuration == 0.0234375) {
      buddy.append("x.");
    } else if (decimalDuration == 0.015625) {
      buddy.append("x");
    } else if (decimalDuration == 0.01171875) {
      buddy.append("o.");
    } else if (decimalDuration == 0.0078125) {
      buddy.append("o");
    } else if (decimalDuration == 0.0) {
    } else {
      return "/" + originalDecimalDuration;
    }
    return buddy.toString();
  }

  /**
   * <p>getDurationStringForBeat.</p>
   *
   * @param beat a int.
   * @return a {@link java.lang.String} object.
   */
  public static String getDurationStringForBeat(int beat) {
    switch (beat) {
      case 2:
        return "h";
      case 4:
        return "q";
      case 8:
        return "i";
      case 16:
        return "s";
      default:
        return "/" + (1.0 / (double) beat);
    }
  }

  /**
   * <p>Getter for the field <code>value</code>.</p>
   *
   * @return a byte.
   */
  public byte getValue() {
    return isRest() ? 0 : this.value;
  }

  /**
   * <p>Setter for the field <code>value</code>.</p>
   *
   * @param value a byte.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setValue(byte value) {
    this.value = value;
    return this;
  }

  /**
   * <p>changeValue.</p>
   *
   * @param delta a int.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note changeValue(int delta) {
    this.setValue((byte) (getValue() + delta));
//		this.originalString = null;
    return this;
  }

  /**
   * <p>getOctave.</p>
   *
   * @return a byte.
   */
  public byte getOctave() {
    return isRest() ? 0 : (byte) (this.getValue() / Note.OCTAVE);
  }

  /**
   * <p>Getter for the field <code>duration</code>.</p>
   *
   * @return a double.
   */
  public double getDuration() {
    return this.duration;
  }

  /**
   * <p>Setter for the field <code>duration</code>.</p>
   *
   * @param d a double.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setDuration(double d) {
    this.duration = d;
    this.wasDurationExplicitlySet = true;
    return this;
  }

  /**
   * <p>Setter for the field <code>duration</code>.</p>
   *
   * @param duration a {@link java.lang.String} object.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setDuration(String duration) {
    return setDuration(NoteProviderFactory.getNoteProvider().getDurationForString(duration));
  }

  /**
   * <p>useDefaultDuration.</p>
   *
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note useDefaultDuration() {
    this.duration = DefaultNoteSettingsManager.getInstance().getDefaultDuration();
    // And do not set wasDurationExplicitlySet
    return this;
  }

  /**
   * <p>useSameDurationAs.</p>
   *
   * @param note2 a {@link org.jfugue.theory.Note} object.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note useSameDurationAs(Note note2) {
    this.duration = note2.duration;
    this.wasDurationExplicitlySet = note2.wasDurationExplicitlySet;
    return this;
  }

  /**
   * <p>useSameExplicitOctaveSettingAs.</p>
   *
   * @param note2 a {@link org.jfugue.theory.Note} object.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note useSameExplicitOctaveSettingAs(Note note2) {
    this.wasOctaveExplicitlySet = note2.wasOctaveExplicitlySet;
    return this;
  }

  /**
   * <p>isDurationExplicitlySet.</p>
   *
   * @return a boolean.
   */
  public boolean isDurationExplicitlySet() {
    return this.wasDurationExplicitlySet;
  }

  /**
   * <p>isOctaveExplicitlySet.</p>
   *
   * @return a boolean.
   */
  public boolean isOctaveExplicitlySet() {
    return this.wasOctaveExplicitlySet;
  }

  /**
   * <p>setOctaveExplicitlySet.</p>
   *
   * @param set a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setOctaveExplicitlySet(boolean set) {
    this.wasOctaveExplicitlySet = set;
    return this;
  }

  /**
   * FOR TESTING PURPOSES ONLY - avoids setting "isDurationExplicitlySet" - Please use setDuration
   * instead!
   *
   * @param d a double.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setImplicitDurationForTestingOnly(double d) {
    this.duration = d;
    // And do not set wasDurationExplicitlySet
    return this;
  }

  /**
   * <p>isRest.</p>
   *
   * @return a boolean.
   */
  public boolean isRest() {
    return this.isRest;
  }

  /**
   * <p>setRest.</p>
   *
   * @param rest a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setRest(boolean rest) {
    this.isRest = rest;
    return this;
  }

  private boolean isPercussionNote() {
    return this.isPercussionNote;
  }

  /**
   * <p>setPercussionNote.</p>
   *
   * @param perc a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setPercussionNote(boolean perc) {
    this.isPercussionNote = perc;
    return this;
  }

  /**
   * <p>Getter for the field <code>onVelocity</code>.</p>
   *
   * @return a byte.
   */
  public byte getOnVelocity() {
    return this.onVelocity;
  }

  /**
   * <p>Setter for the field <code>onVelocity</code>.</p>
   *
   * @param velocity a byte.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setOnVelocity(byte velocity) {
    this.onVelocity = velocity;
    return this;
  }

  /**
   * <p>Getter for the field <code>offVelocity</code>.</p>
   *
   * @return a byte.
   */
  public byte getOffVelocity() {
    return this.offVelocity;
  }

  /**
   * <p>Setter for the field <code>offVelocity</code>.</p>
   *
   * @param velocity a byte.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setOffVelocity(byte velocity) {
    this.offVelocity = velocity;
    return this;
  }

  /**
   * <p>isStartOfTie.</p>
   *
   * @return a boolean.
   */
  public boolean isStartOfTie() {
    return isStartOfTie;
  }

  /**
   * <p>setStartOfTie.</p>
   *
   * @param isStartOfTie a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setStartOfTie(boolean isStartOfTie) {
    this.isStartOfTie = isStartOfTie;
    return this;
  }

  /**
   * <p>isEndOfTie.</p>
   *
   * @return a boolean.
   */
  public boolean isEndOfTie() {
    return isEndOfTie;
  }

  /**
   * <p>setEndOfTie.</p>
   *
   * @param isEndOfTie a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setEndOfTie(boolean isEndOfTie) {
    this.isEndOfTie = isEndOfTie;
    return this;
  }

  /**
   * <p>isFirstNote.</p>
   *
   * @return a boolean.
   */
  public boolean isFirstNote() {
    return this.isFirstNote;
  }

  /**
   * <p>setFirstNote.</p>
   *
   * @param isFirstNote a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setFirstNote(boolean isFirstNote) {
    this.isFirstNote = isFirstNote;
    return this;
  }

  /**
   * <p>isMelodicNote.</p>
   *
   * @return a boolean.
   */
  public boolean isMelodicNote() {
    return this.isMelodicNote;
  }

  /**
   * <p>setMelodicNote.</p>
   *
   * @param isMelodicNote a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setMelodicNote(boolean isMelodicNote) {
    this.isMelodicNote = isMelodicNote;
    return this;
  }

  /**
   * <p>isHarmonicNote.</p>
   *
   * @return a boolean.
   */
  public boolean isHarmonicNote() {
    return this.isHarmonicNote;
  }

  /**
   * <p>setHarmonicNote.</p>
   *
   * @param isHarmonicNote a boolean.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setHarmonicNote(boolean isHarmonicNote) {
    this.isHarmonicNote = isHarmonicNote;
    return this;
  }

  /**
   * <p>Getter for the field <code>originalString</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getOriginalString() {
    return this.originalString;
  }

  /**
   * <p>Setter for the field <code>originalString</code>.</p>
   *
   * @param originalString a {@link java.lang.String} object.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note setOriginalString(String originalString) {
    this.originalString = originalString;
    return this;
  }

  /**
   * <p>getMicrosecondDuration.</p>
   *
   * @param mpq a double.
   * @return a double.
   */
  public double getMicrosecondDuration(double mpq) {
    return (this.duration * 4.0f) * mpq;
  }

  /**
   * <p>getPositionInOctave.</p>
   *
   * @return a byte.
   */
  public byte getPositionInOctave() {
    return isRest() ? 0 : (byte) (getValue() % Note.OCTAVE);
  }

  /**
   * <p>getVelocityString.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getVelocityString() {
    StringBuilder buddy = new StringBuilder();
    if (this.onVelocity != DefaultNoteSettingsManager.getInstance().getDefaultOnVelocity()) {
      buddy.append("a").append(getOnVelocity());
    }
    if (this.offVelocity != DefaultNoteSettingsManager.getInstance().getDefaultOffVelocity()) {
      buddy.append("d").append(getOffVelocity());
    }
    return buddy.toString();
  }

  /**
   * {@inheritDoc}
   *
   * Returns a pattern representing this note. Does not return indicators of whether the note is
   * harmonic or melodic.
   */
  @Override
  public Pattern getPattern() {
    String buddy = toStringWithoutDuration()
      + getDecoratorString();
    return new Pattern(buddy);
  }

  /**
   * <p>getPercussionPattern.</p>
   *
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public Pattern getPercussionPattern() {
    if (getValue() < MidiDefaults.MIN_PERCUSSION_NOTE
      || getValue() > MidiDefaults.MAX_PERCUSSION_NOTE) {
      return getPattern();
    }
    String buddy = Note.getPercussionString(getValue())
      + getDecoratorString();
    return new Pattern(buddy);
  }

  /**
   * <p>toString.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String toString() {
    return getPattern().toString();
  }

  private String toStringWithoutDuration() {
    if (isRest()) {
      return "R";
    } else if (isPercussionNote()) {
      return Note.getPercussionString(this.getValue());
    } else {
      return (originalString != null) ? this.originalString : Note.getToneString(this.getValue());
    }
  }

  /**
   * <p>getToneString.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getToneString() {
    if (isRest) {
      return "R";
    }

    StringBuilder buddy = new StringBuilder();
    buddy.append(Note.getToneStringWithoutOctave(getValue()));
    if (this.wasOctaveExplicitlySet) {
      buddy.append(getOctave());
    }
    return buddy.toString();
  }

  /**
   * Returns the "decorators" to the base note, which includes the duration if one is explicitly
   * specified, and velocity dynamics if provided
   */
  private String getDecoratorString() {
    StringBuilder buddy = new StringBuilder();
    if (isDurationExplicitlySet()) {
      buddy.append(Note.getDurationString(this.duration));
    }
    buddy.append(getVelocityString());
    return buddy.toString();
  }

  /** {@inheritDoc} */
  public boolean equals(Object o) {
    if (!(o instanceof Note)) {
      return false;
    }

    Note n2 = (Note) o;
    boolean originalStringsMatchSufficientlyWell =
      ((n2.originalString == null) || (this.originalString == null)) || n2.originalString
        .equalsIgnoreCase(this.originalString);
    return ((n2.value == this.value) &&
      (n2.duration == this.duration) &&
      (n2.wasOctaveExplicitlySet == this.wasOctaveExplicitlySet) &&
      (n2.wasDurationExplicitlySet == this.wasDurationExplicitlySet) &&
      (n2.isEndOfTie == this.isEndOfTie) &&
      (n2.isStartOfTie == this.isStartOfTie) &&
      (n2.isMelodicNote == this.isMelodicNote) &&
      (n2.isHarmonicNote == this.isHarmonicNote) &&
      (n2.isPercussionNote == this.isPercussionNote) &&
      (n2.isFirstNote == this.isFirstNote) &&
      (n2.isRest == this.isRest) &&
      (n2.onVelocity == this.onVelocity) &&
      (n2.offVelocity == this.offVelocity) &&
      originalStringsMatchSufficientlyWell);
  }

  /**
   * <p>toDebugString.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String toDebugString() {
    String buddy = "Note:"
      + " value=" + this.getValue()
      + " duration=" + this.getDuration()
      + " wasOctaveExplicitlySet=" + this.isOctaveExplicitlySet()
      + " wasDurationExplicitlySet=" + this.isDurationExplicitlySet()
      + " isEndOfTie=" + this.isEndOfTie()
      + " isStartOfTie=" + this.isStartOfTie()
      + " isMelodicNote=" + this.isMelodicNote()
      + " isHarmonicNote=" + this.isHarmonicNote()
      + " isPercussionNote=" + this.isPercussionNote()
      + " isFirstNote=" + this.isFirstNote()
      + " isRest=" + this.isRest()
      + " onVelocity=" + this.getOnVelocity()
      + " offVelocity=" + this.getOffVelocity()
      + " originalString=" + this.getOriginalString();
    return buddy;
  }

  /**
   * For use with Note.sortNotesBy()
   */
  interface SortingCallback {

    /**
     * Must return an int. If you want to sort by duration (which is decimal), you'll need to work
     * around this.
     */
    int getSortingValue(Note note);
  }
}


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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.provider.ChordProviderFactory;

/**
 * <p>Chord class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Chord implements PatternProducer {

  /** Constant <code>MAJOR_INTERVALS</code> */
  public static final Intervals MAJOR_INTERVALS = new Intervals("1 3 5");
  /** Constant <code>MINOR_INTERVALS</code> */
  public static final Intervals MINOR_INTERVALS = new Intervals("1 b3 5");
  /** Constant <code>DIMINISHED_INTERVALS</code> */
  public static final Intervals DIMINISHED_INTERVALS = new Intervals("1 b3 b5");
  /** Constant <code>MAJOR_SEVENTH_INTERVALS</code> */
  public static final Intervals MAJOR_SEVENTH_INTERVALS = new Intervals("1 3 5 7");
  /** Constant <code>MINOR_SEVENTH_INTERVALS</code> */
  public static final Intervals MINOR_SEVENTH_INTERVALS = new Intervals("1 b3 5 b7");
  /** Constant <code>DIMINISHED_SEVENTH_INTERVALS</code> */
  public static final Intervals DIMINISHED_SEVENTH_INTERVALS = new Intervals("1 b3 b5 6");
  /** Constant <code>MAJOR_SEVENTH_SIXTH_INTERVALS</code> */
  public static final Intervals MAJOR_SEVENTH_SIXTH_INTERVALS = new Intervals("1 3 5 6 7");
  /** Constant <code>MINOR_SEVENTH_SIXTH_INTERVALS</code> */
  public static final Intervals MINOR_SEVENTH_SIXTH_INTERVALS = new Intervals("1 3 5 6 7");
  /** Constant <code>chordMap</code> */
  public static final Map<String, Intervals> chordMap;
  private static final Map<String, String> humanReadableMap;

  static {
    // @formatter:off
    chordMap = new TreeMap<>(new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        int result = compareLength(s1, s2);
        if (result == 0) {
          result = s1.compareTo(s2);
        }
        return result;
      }

      /**
       * Compare two strings and the bigger of the two is deemed to come first in order
       */
      private int compareLength(String s1, String s2) {
        return Integer.compare(s2.length(), s1.length());
      }
    });

    // Major Chords
    chordMap.put("MAJ", new Intervals("1 3 5"));
    chordMap.put("MAJ6", new Intervals("1 3 5 6"));
    chordMap.put("MAJ7", new Intervals("1 3 5 7"));
    chordMap.put("MAJ9", new Intervals("1 3 5 7 9"));
    chordMap.put("ADD9", new Intervals("1 3 5 9"));
    chordMap.put("MAJ6%9", new Intervals("1 3 5 6 9"));
    chordMap.put("MAJ7%6", new Intervals("1 3 5 6 7"));
    chordMap.put("MAJ13", new Intervals("1 3 5 7 9 13"));

    // Minor Chords
    chordMap.put("MIN", new Intervals("1 b3 5"));
    chordMap.put("MIN6", new Intervals("1 b3 5 6"));
    chordMap.put("MIN7", new Intervals("1 b3 5 b7"));
    chordMap.put("MIN9", new Intervals("1 b3 5 b7 9"));
    chordMap.put("MIN11", new Intervals("1 b3 5 b7 9 11"));
    chordMap.put("MIN7%11", new Intervals("1 b3 5 b7 11"));
    chordMap.put("MINADD9", new Intervals("1 b3 5 9"));
    chordMap.put("MIN6%9", new Intervals("1 b3 5 6"));
    chordMap.put("MINMAJ7", new Intervals("1 b3 5 7"));
    chordMap.put("MINMAJ9", new Intervals("1 b3 5 7 9"));

    // Dominant Chords
    chordMap.put("DOM7", new Intervals("1 3 5 b7"));
    chordMap.put("DOM7%6", new Intervals("1 3 5 6 b7"));
    chordMap.put("DOM7%11", new Intervals("1 3 5 b7 11"));
    chordMap.put("DOM7SUS", new Intervals("1 4 5 b7"));
    chordMap.put("DOM7%6SUS", new Intervals("1 4 5 6 b7"));
    chordMap.put("DOM9", new Intervals("1 3 5 b7 9"));
    chordMap.put("DOM11", new Intervals("1 3 5 b7 9 11"));
    chordMap.put("DOM13", new Intervals("1 3 5 b7 9 13"));
    chordMap.put("DOM13SUS", new Intervals("1 3 5 b7 11 13"));
    chordMap.put("DOM7%6%11", new Intervals("1 3 5 b7 9 11 13"));

    // Augmented Chords
    chordMap.put("AUG", new Intervals("1 3 #5"));
    chordMap.put("AUG7", new Intervals("1 3 #5 b7"));

    // Diminished Chords
    chordMap.put("DIM", new Intervals("1 b3 b5"));
    chordMap.put("DIM7", new Intervals("1 b3 b5 6"));

    // Suspended Chords
    chordMap.put("SUS4", new Intervals("1 4 5"));
    chordMap.put("SUS2", new Intervals("1 2 5"));

    // Human readable names for some of the more cryptic chord strings
    humanReadableMap = new HashMap<>();
    humanReadableMap.put("MAJ6%9", "6/9");
    humanReadableMap.put("MAJ7%6", "7/6");

    // @formatter:on
  }

  private Note rootNote;
  private Intervals intervals;
  private int inversion;

  /**
   * <p>Constructor for Chord.</p>
   *
   * @param s a {@link java.lang.String} object.
   */
  public Chord(String s) {
    this(ChordProviderFactory.getChordProvider().createChord(s));
  }

  private Chord(Chord chord) {
    this.rootNote = chord.getRoot();
    this.intervals = chord.getIntervals();
    this.inversion = chord.getInversion();
  }

  /**
   * <p>Constructor for Chord.</p>
   *
   * @param root a {@link org.jfugue.theory.Note} object.
   * @param intervals a {@link org.jfugue.theory.Intervals} object.
   */
  public Chord(Note root, Intervals intervals) {
    this.rootNote = root;
    this.intervals = intervals;
  }

  /**
   * <p>Constructor for Chord.</p>
   *
   * @param key a {@link org.jfugue.theory.Key} object.
   */
  public Chord(Key key) {
    this.rootNote = key.getRoot();
    this.intervals = key.getScale().getIntervals();
  }

  /**
   * <p>getChordNames.</p>
   *
   * @return an array of {@link java.lang.String} objects.
   */
  public static String[] getChordNames() {
    return chordMap.keySet().toArray(new String[0]);
  }

  /**
   * <p>addChord.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @param intervalPattern a {@link java.lang.String} object.
   */
  public static void addChord(String name, String intervalPattern) {
    Chord.addChord(name, new Intervals(intervalPattern));
  }

  private static void addChord(String name, Intervals intervalPattern) {
    chordMap.put(name, intervalPattern);
  }

  /**
   * <p>Getter for the field <code>intervals</code>.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link org.jfugue.theory.Intervals} object.
   */
  public static Intervals getIntervals(String name) {
    return chordMap.get(name);
  }

  /**
   * <p>removeChord.</p>
   *
   * @param name a {@link java.lang.String} object.
   */
  public static void removeChord(String name) {
    chordMap.remove(name);
  }

  private static String getChordType(Intervals intervals) {
    for (Map.Entry<String, Intervals> entry : chordMap.entrySet()) {
      if (intervals.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * <p>putHumanReadable.</p>
   *
   * @param chordName a {@link java.lang.String} object.
   * @param humanReadableName a {@link java.lang.String} object.
   */
  public static void putHumanReadable(String chordName, String humanReadableName) {
    humanReadableMap.put(chordName, humanReadableName);
  }

  /**
   * Returns a human readable chord name if one exists, otherwise returns the same chord name that
   * was passed in
   */
  private static String getHumanReadableName(String chordName) {
    if (humanReadableMap.containsKey(chordName)) {
      return humanReadableMap.get(chordName);
    }
    return chordName;
  }

  /**
   * Returns true if the passed string contains a note, a known chord, and optionally an octave or
   * duration.
   *
   * @param candidateChordMusicString the chord music string
   * @return a boolean indicating whether the chord is valid or not
   */
  public static boolean isValidChord(String candidateChordMusicString) {
    String musicString = candidateChordMusicString.toUpperCase();
    for (String chordName : chordMap.keySet()) {
      if (musicString.contains(chordName)) {
        int index = musicString.indexOf(chordName);
        String possibleNote = musicString.substring(0, index);
        String qualifiers = musicString
          .substring(index + chordName.length() - 1, musicString.length() - 1);
        if ((Note.isValidNote(possibleNote)) && (Note.isValidQualifier(qualifiers))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * <p>fromNotes.</p>
   *
   * @param noteString a {@link java.lang.String} object.
   * @return a {@link org.jfugue.theory.Chord} object.
   */
  public static Chord fromNotes(String noteString) {
    return fromNotes(noteString.split(" "));
  }

  /**
   * <p>fromNotes.</p>
   *
   * @param noteStrings an array of {@link java.lang.String} objects.
   * @return a {@link org.jfugue.theory.Chord} object.
   */
  public static Chord fromNotes(String[] noteStrings) {
    List<Note> notes = new ArrayList<>();
    for (String noteString : noteStrings) {
      notes.add(new Note(noteString));
    }
    return fromNotes(notes.toArray(new Note[0]));
  }

  /**
   * <p>fromNotes.</p>
   *
   * @param notes an array of {@link org.jfugue.theory.Note} objects.
   * @return a {@link org.jfugue.theory.Chord} object.
   */
  public static Chord fromNotes(Note[] notes) {
    return new Chord(getChordFromNotes(notes));
  }

  /**
   * Flatten the notes - meaning, multiple of the same pitches in different durations should be
   * represented only once, but maintain their position relative each other so the chord has the
   * right bass note
   */
  private static Note[] flattenNotesByPositionInOctave(Note[] notes) {
    Map<Integer, Note> noteMap = new HashMap<>();
    List<Integer> noteOrder = new ArrayList<>();
    for (Note note : notes) {
      int positionInOctave = note.getPositionInOctave();
      if (!noteMap.containsKey(positionInOctave)) {
        noteMap.put(positionInOctave, note);
        noteOrder.add(positionInOctave);
      }
    }

    Note[] retVal = new Note[noteMap.size()];
    int counter = 0;
    for (Integer positionInOctave : noteOrder) {
      retVal[counter++] = noteMap.get(positionInOctave);
    }
    return retVal;
  }

  /**
   * Returns best-matching chord type with the given set of intervals
   */
  private static String getChordFromNotes(Note[] notes) {
    boolean returnNonOctaveNotes = false;

    // Sorting notes by their value will let us know which is the bass note
    Note.sortNotesBy(notes, Note::getValue);

    // If the distance between the lowest note and the highest note is greater than 12,
    // we have a chord that spans octaves and we should return a chord in which the
    // notes have no octave.
    if (notes[notes.length - 1].getValue() - notes[0].getValue() > Note.OCTAVE) {
      returnNonOctaveNotes = true;
    }
    Note bassNote = notes[0];

    // Sorting notes by position in octave will let us know which chord we have
    Note.sortNotesBy(notes, Note::getPositionInOctave);
    notes = flattenNotesByPositionInOctave(notes);

    String[] possibleChords = new String[notes.length];
    for (int i = 0; i < notes.length; i++) {
      Note[] notesToCheck = new Note[notes.length];
      for (int u = 0; u < notes.length; u++) {
        notesToCheck[u] = notes[(i + u) % notes.length];
      }
      possibleChords[i] = Chord.getChordType(Intervals.createIntervalsFromNotes(notesToCheck));
    }

    // Now, return the first non-null string
    for (int i = 0; i < possibleChords.length; i++) {
      if (possibleChords[i] != null) {
        StringBuilder sb = new StringBuilder();
        if (returnNonOctaveNotes) {
          sb.append(Note.getToneStringWithoutOctave(notes[i].getValue()));
        } else {
          sb.append(notes[i]);
        }
        sb.append(possibleChords[i]);
        if (!bassNote.equals(notes[i])) {
          sb.append("^");
          sb.append(bassNote);
        }
        return sb.toString();
      }
    }

    return null;
  }

  /**
   * Returns a count of the number of carets at the end of the chord string. Given Cmaj^^, this will
   * return 2.
   *
   * @param chordString The chord represented as a string
   * @return the inversion result
   */

  //TODO: Does not give the correct value of the inversion is indicated with a bass note, like Cmaj^E
  public static int getInversionFromChordString(String chordString) {
    int counter = 0;
    for (char c : chordString.toCharArray()) {
      if (c == '^') {
        counter++;
      }
    }
    return counter;
  }

  /**
   * <p>getRoot.</p>
   *
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note getRoot() {
    return this.rootNote;
  }

  /**
   * <p>Getter for the field <code>intervals</code>.</p>
   *
   * @return a {@link org.jfugue.theory.Intervals} object.
   */
  public Intervals getIntervals() {
    return this.intervals;
  }

  /**
   * <p>Getter for the field <code>inversion</code>.</p>
   *
   * @return a int.
   */
  public int getInversion() {
    return this.inversion;
  }

  /**
   * <p>Setter for the field <code>inversion</code>.</p>
   *
   * @param nth a int.
   * @return a {@link org.jfugue.theory.Chord} object.
   */
  public Chord setInversion(int nth) {
    this.inversion = nth;
    return this;
  }

  /**
   * <p>getBassNote.</p>
   *
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note getBassNote() {
    int bassNoteValue = rootNote.getValue() - Note.OCTAVE + Intervals
      .getHalfsteps(this.intervals.getNthInterval(this.inversion));
//	    Note r = new Note(bassNoteValue).setOriginalString(Note.NOTE_NAMES_COMMON[bassNoteValue % Note.OCTAVE]).useSameExplicitOctaveSettingAs(getRoot());
    return new Note(Note.NOTE_NAMES_COMMON[bassNoteValue % Note.OCTAVE])
      .useSameExplicitOctaveSettingAs(getRoot());
  }

  /**
   * Same as <code>setBassNote</code> with a string representation of the note instead
   *
   * @param newBass New base note
   * @return a {@link org.jfugue.theory.Chord} object.
   */
  public Chord setBassNote(String newBass) {
    return setBassNote(new Note(newBass));
  }

  /**
   * Although setBassNote takes a Note, it doesn't just set a local value to the incoming note.
   * Instead, it uses the incoming note to compute the inversion for this chord, and sets the
   * inversion. getBassNote() reconstructs the bass note using the inversion. If the rootNote is
   * null, this method returns without taking any action.
   *
   * @param newBass New base note
   */
  private Chord setBassNote(Note newBass) {
    if (rootNote == null) {
      return this;
    }

    for (int i = 0; i < intervals.size(); i++) {
      if (newBass.getValue() % 12
        == (rootNote.getValue() + Intervals.getHalfsteps(intervals.getNthInterval(i))) % 12) {
        this.inversion = i;
      }
    }

    return this;
  }

  /**
   * <p>setOctave.</p>
   *
   * @param octave a int.
   * @return a {@link org.jfugue.theory.Chord} object.
   */
  public Chord setOctave(int octave) {
    this.rootNote.setValue((byte) (this.rootNote.getPositionInOctave() + octave * Note.OCTAVE));
    return this;
  }

  /**
   * <p>getNotes.</p>
   *
   * @return an array of {@link org.jfugue.theory.Note} objects.
   */
  public Note[] getNotes() {
    int[] halfsteps = this.intervals.toHalfstepArray();
    Note[] retVal = new Note[halfsteps.length];
    retVal[0] = new Note(this.getRoot());
    for (int i = 0; i < halfsteps.length - 1; i++) {
      retVal[i + 1] = new Note(retVal[i].getValue() + halfsteps[i + 1] - halfsteps[i])
        .setFirstNote(false).setMelodicNote(false).setHarmonicNote(true)
        .useSameDurationAs(getRoot()).useSameExplicitOctaveSettingAs(getRoot());
      if (!this.getRoot().isOctaveExplicitlySet()) {
        retVal[i + 1].setOriginalString(Note.getToneStringWithoutOctave(
          (byte) (retVal[i].getValue() + halfsteps[i + 1] - halfsteps[i])));
      }
    }

    // Now calculate inversion
    // 2017-02-17: It looks like this is putting notes up, instead of moving other notes down
    for (int i = 0; i < getInversion(); i++) {
      if (i < retVal.length) {
        retVal[i].setValue((byte) (retVal[i].getValue() + Note.OCTAVE));
      }
    }

    // Rotate the returned notes based on the inversion
    // Cmaj should return C E G, but Cmaj^^ should return G C E
    Note[] retVal2 = new Note[retVal.length];
    for (int i = 0; i < retVal.length; i++) {
      retVal2[i] = retVal[(i + getInversion()) % retVal.length];
    }

    return retVal2;
  }

  private String insertChordNameIntoNote(Note note, String chordName) {
    StringBuilder buddy = new StringBuilder();
//		buddy.append(Note.getToneString(note.getValue()));
    buddy.append(note.getToneString());
    buddy.append(chordName);
    if (note.isDurationExplicitlySet()) {
      buddy.append(Note.getDurationString(note.getDuration()));
    }
    buddy.append(note.getVelocityString());
    return buddy.toString();
  }

  /**
   * <p>getChordType.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getChordType() {
    for (Map.Entry<String, Intervals> entry : chordMap.entrySet()) {
      if (this.getIntervals().equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public Pattern getPattern() {
    Pattern pattern = new Pattern();
    boolean foundChord = false;
    String chordName = getChordType();
    if (chordName != null) {
      StringBuilder sb = new StringBuilder();
      sb.append(insertChordNameIntoNote(this.rootNote, chordName));
      for (int i = 0; i < getInversion(); i++) {
        sb.append("^");
      }
      pattern.add(sb.toString());
      foundChord = true;
    }
    if (!foundChord) {
      return getPatternWithNotes();
    }
    return pattern;
  }

  /**
   * <p>getPatternWithNotes.</p>
   *
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public Pattern getPatternWithNotes() {
    // A better way of creating a Chord: Check to see if the intervals are in the map; if so, use the associated name.
    // (Then you'd need to check for inversions, too)
    StringBuilder buddy = new StringBuilder();
    Note[] notes = getNotes();
    for (int i = 0; i < notes.length - 1; i++) {
      buddy.append(notes[i].getPattern());
      buddy.append("+");
    }
    buddy.append(notes[notes.length - 1]);
    return new Pattern(buddy.toString());
  }

  /**
   * <p>getPatternWithNotesExceptRoot.</p>
   *
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public Pattern getPatternWithNotesExceptRoot() {
    StringBuilder buddy = new StringBuilder();
    Note[] notes = getNotes();
    for (Note note : notes) {
      if (note.getPositionInOctave() != getRoot().getPositionInOctave()) {
        buddy.append(note.getPattern());
        buddy.append("+");
      }
    }
    buddy.deleteCharAt(buddy.length() - 1);
    return new Pattern(buddy.toString());
  }

  /**
   * <p>getPatternWithNotesExceptBass.</p>
   *
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public Pattern getPatternWithNotesExceptBass() {
    StringBuilder buddy = new StringBuilder();
    Note[] notes = getNotes();
    for (int i = 0; i < notes.length - 1; i++) {
      if (notes[i].getValue() % Note.OCTAVE != getBassNote().getValue() % Note.OCTAVE) {
        buddy.append(notes[i].getPattern());
        buddy.append("+");
      }
    }
    buddy.append(notes[notes.length - 1]);
    return new Pattern(buddy.toString());
  }

  /**
   * <p>isMajor.</p>
   *
   * @return a boolean.
   */
  public boolean isMajor() {
    return this.intervals.equals(MAJOR_INTERVALS);
  }

  /**
   * <p>isMinor.</p>
   *
   * @return a boolean.
   */
  public boolean isMinor() {
    return this.intervals.equals(MINOR_INTERVALS);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Chord)) {
      return false;
    }

    Chord c2 = (Chord) o;
    return (c2.rootNote.equals(rootNote) &&
      c2.intervals.equals(intervals) &&
      (c2.inversion == inversion));
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(rootNote, intervals, inversion);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getPattern().toString();
  }

  /**
   * Returns a string consisting of the notes in the chord. For example, new
   * Chord("Cmaj").toNoteString() returns "(C+E+G)" TODO: Update with Java 8 String Joiner
   *
   * @return a {@link java.lang.String} object.
   */
  public String toNoteString() {
    StringBuilder buddy = new StringBuilder();
    buddy.append("(");
    for (Note note : getNotes()) {
      buddy.append(note.toString());
      buddy.append("+");
    }
    buddy.deleteCharAt(buddy.length() - 1);
    buddy.append(")");
    return buddy.toString();
  }

  /**
   * <p>toHumanReadableString.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String toHumanReadableString() {
    return this.rootNote + Chord.getHumanReadableName(this.getChordType());
  }

  /**
   * <p>toDebugString.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String toDebugString() {
    StringBuilder buddy = new StringBuilder();
    int counter = 0;
    for (Note note : getNotes()) {
      buddy.append("Note ").append(counter++).append(": ").append(note.toDebugString())
        .append("\n");
    }
    buddy.append("Chord Intervals = ").append(getIntervals().toString()).append("\n");
    buddy.append("Inversion = ").append(inversion);
    return buddy.toString();
  }
}

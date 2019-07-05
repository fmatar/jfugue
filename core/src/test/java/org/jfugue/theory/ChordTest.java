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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jfugue.pattern.Pattern;
import org.junit.Test;

public class ChordTest {

  @Test
  public void testCreateChordByString() {
    Chord chord = new Chord("Cmaj");
    Pattern pattern = chord.getPattern();
    assertTrue(pattern.toString().equalsIgnoreCase("CMAJ"));
  }

  @Test
  public void testCreateChordByNumberedRoot() {
    Chord chord = new Chord("60maj");
    Pattern pattern = chord.getPattern();
    assertTrue(pattern.toString().equalsIgnoreCase("CMAJ"));
  }

  @Test
  public void testCreateChordByIntervals() {
    Chord chord = new Chord(new Note("D5h"), new Intervals("1 3 5"));
    Pattern pattern = chord.getPattern();
    assertTrue(pattern.toString().equalsIgnoreCase("D5MAJh"));
  }

  @Test
  public void testChordInversionByNumber() {
    Chord chord = new Chord("C4maj");
    chord.setInversion(1);
    Note[] notes = chord.getNotes();
    assertEquals(52, notes[0].getValue());
    assertEquals(55, notes[1].getValue());
    assertEquals(60, notes[2].getValue());

    chord.setInversion(2);
    notes = chord.getNotes();
    assertEquals(55, notes[0].getValue());
    assertEquals(60, notes[1].getValue());
    assertEquals(64, notes[2].getValue());
  }

  @Test
  public void testChordInversionByBass() {
    Chord chord = new Chord("C4maj");
    chord.setBassNote("E");
    Note[] notes = chord.getNotes();
    assertEquals(52, notes[0].getValue());
    assertEquals(55, notes[1].getValue());
    assertEquals(60, notes[2].getValue());

    chord.setBassNote("G");
    notes = chord.getNotes();
    assertEquals(55, notes[0].getValue());
    assertEquals(60, notes[1].getValue());
    assertEquals(64, notes[2].getValue());
  }

  @Test
  public void testCreateChordWithNotes_StringConstructor() {
    Chord chord = Chord.fromNotes("C E G");
    assertEquals(chord, new Chord("Cmaj"));
  }

  @Test
  public void testCreateChordWithNotes_StringArrayConstructor() {
    Chord chord = Chord.fromNotes(new String[]{"Bb", "Db", "F"});
    assertEquals(chord, new Chord("Bbmin^"));
  }

  @Test
  public void testCreateChordWithNotes_NoteArrayConstructor() {
    Chord chord = Chord.fromNotes(new Note[]{new Note("D"), new Note("F#"), new Note("A")});
    assertEquals(chord, new Chord("Dmaj"));
  }

  @Test
  public void testGetChordType() {
    Chord chord = new Chord("C5sus4");
    assertTrue(chord.getChordType().equalsIgnoreCase("sus4"));
  }

  @Test
  public void testCreateChordWithNotesInWrongOrder_ThreeNoteChord() {
    Chord chord = Chord.fromNotes("E G C");
    assertEquals(chord, new Chord("Cmaj"));
    assertEquals(0, chord.getInversion());
  }

  @Test
  public void testCreateChordWithNotesInvertedNotes_ThreeNoteChord() {
    Chord chord = Chord.fromNotes("E4 G4 C5");
    assertEquals(chord, new Chord("C5maj^"));
    assertEquals(1, chord.getInversion());
  }

  @Test
  public void testCreateChordWithInvertedNotes_FourNoteChord_FirstInversion() {
    Chord chord = Chord.fromNotes("E4 G4 B4 C5");
    assertEquals(chord, new Chord("C5maj7^"));
    assertEquals(1, chord.getInversion());
  }

  @Test
  public void testCreateChordWithInvertedNotes_FourNoteChord_SecondInversion() {
    Chord chord = Chord.fromNotes("G4 C5 E5 B5");
    assertEquals(chord, new Chord("Cmaj7^^"));
    assertEquals(2, chord.getInversion());
  }

  @Test
  public void testCreateChordWithInvertedNotes_FourNoteChord_ThirdInversion_AskBassNote() {
    Chord chord = Chord.fromNotes("B4 C5 E5 G5");
    assertEquals(chord, new Chord("C5maj7^^^"));
    assertEquals(3, chord.getInversion());
    assertEquals(chord.getBassNote(), new Note("B").setOctaveExplicitlySet(true));
  }

  @Test
  public void testGetBassNoteWithoutOctave() {
    Chord chord1 = new Chord("Cmaj^");
    assertEquals(chord1.getBassNote(), new Note("E"));
  }

  @Test
  public void testGetBassNoteWithOctave() {
    Chord chord2 = new Chord("C3maj^");
    assertEquals(chord2.getBassNote(), new Note("E").setOctaveExplicitlySet(true));
  }

  @Test
  public void testCreateChordWithNotesInDifferentOctaves() {
    Chord chord = Chord.fromNotes("C3 E5 G7");
    assertEquals(chord, new Chord("Cmaj"));
  }

  @Test
  public void testCreateChordWithManySimilarNotes() {
    Chord chord = Chord.fromNotes("F3 F4 F5 A6 A5 C4 C3");
    assertEquals(chord, new Chord("Fmaj^^"));
  }

  @Test
  public void testChords() {
    Chord chord1 = Chord.fromNotes("C4 G4 E5");
    assertEquals(chord1, new Chord("Cmaj"));

    Chord chord2 = Chord.fromNotes("G4 E5 C6");
    assertEquals(chord2, new Chord("Cmaj^^"));

    Chord chord3 = Chord.fromNotes("C4 G4 B4 E5");
    assertEquals(chord3, new Chord("Cmaj7"));
  }

  @Test
  public void testAddNewChord() {
    Chord.chordMap.put("POW", new Intervals("1 5"));
    Chord chord = new Chord("Cpow");
    Note[] notes = chord.getNotes();
    assertEquals(48, notes[0].getValue());
    assertEquals(55, notes[1].getValue());
  }

  @Test
  public void testGetPatternWithoutRoot() {
    assertEquals("E+G", new Chord("Cmaj").getPatternWithNotesExceptRoot().toString());
  }

  @Test
  public void testGetPatternWithoutRootFirstInversion() {
    assertEquals("F+A", new Chord("Dmin^").getPatternWithNotesExceptRoot().toString());
  }

  @Test
  public void testGetPatternWithoutRootSecondInversion() {
    assertEquals("B+G#", new Chord("Emaj^^").getPatternWithNotesExceptRoot().toString());
  }

  @Test
  public void testGetPatternWithoutBass() {
    assertEquals("E+G", new Chord("Cmaj").getPatternWithNotesExceptBass().toString());
  }

  @Test
  public void testGetPatternWithoutBassFirstInversion() {
    assertEquals("A+D", new Chord("Dmin^").getPatternWithNotesExceptBass().toString());
  }

  @Test
  public void testGetPatternWithoutBassSecondInversion() {
    assertEquals("E+G#", new Chord("Emaj^^").getPatternWithNotesExceptBass().toString());
  }

}

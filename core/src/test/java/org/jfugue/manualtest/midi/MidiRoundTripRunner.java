package org.jfugue.manualtest.midi;

import javax.sound.midi.Sequence;
import org.jfugue.midi.MidiParser;
import org.jfugue.midi.MidiParserListener;
import org.jfugue.pattern.Pattern;
import org.staccato.StaccatoParser;
import org.staccato.StaccatoParserListener;

class MidiRoundTripRunner {

  static void run() {
    // First, convert Staccto to MIDI
    Pattern pattern = new Pattern("TIME:4/4 KEY:C#min T90 I[Flute] :PW(8000) Cq Rw Cq");
    MidiParserListener midiParserListener = new MidiParserListener();
    StaccatoParser staccatoParser = new StaccatoParser();
    staccatoParser.addParserListener(midiParserListener);
    staccatoParser.parse(pattern);
    Sequence sequence = midiParserListener.getSequence();

    // Next, convert MIDI to Staccato
    StaccatoParserListener staccatoParserListener = new StaccatoParserListener();
    MidiParser midiParser = new MidiParser();
    midiParser.addParserListener(staccatoParserListener);
    midiParser.parse(sequence);
    Pattern reconstitutedPattern = staccatoParserListener.getPattern();

    System.out.println("Original Pattern:      " + pattern.toString());
    System.out.println("Reconstituted Pattern: " + reconstitutedPattern.toString());
  }
}

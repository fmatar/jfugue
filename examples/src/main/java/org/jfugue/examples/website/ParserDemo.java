package org.jfugue.examples.website;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import org.jfugue.midi.MidiParser;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

class ParserDemo {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   * @throws javax.sound.midi.InvalidMidiDataException if any.
   * @throws java.io.IOException if any.
   */
  public static void main(String[] args) throws InvalidMidiDataException, IOException {
    MidiParser parser = new MidiParser();
    StaccatoParserListener listener = new StaccatoParserListener();
    parser.addParserListener(listener);
    parser.parse(MidiSystem.getSequence(new File("C:\\My Media\\MIDI\\The_Way_I_Am.mid")));
    Pattern staccatoPattern = listener.getPattern();
    System.out.println(staccatoPattern);

    Player player = new Player();
    player.play(staccatoPattern);
  }
}



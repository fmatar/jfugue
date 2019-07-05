package org.jfugue.examples.website;

import java.util.Random;
import java.util.Scanner;
import javax.sound.midi.MidiUnavailableException;
import org.jfugue.pattern.Pattern;
import org.jfugue.realtime.RealtimePlayer;
import org.jfugue.theory.Note;

class RealtimeExample {

  private static final Pattern[] PATTERNS = new Pattern[]{
    new Pattern("Cmajq Dmajq Emajq"),
    new Pattern("V0 Ei Gi Di Ci  V1 Gi Ci Fi Ei"),
    new Pattern("V0 Cmajq V1 Gmajq")
  };

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   * @throws javax.sound.midi.MidiUnavailableException if any.
   */
  public static void main(String[] args) throws MidiUnavailableException {
    RealtimePlayer player = new RealtimePlayer();
    Scanner scanner = new Scanner(System.in);
    Random random = new Random();
    boolean quit = false;
    while (!quit) {
      System.out.print(
        "Enter a '+C' to start a note, '-C' to stop a note, 'i' for a random instrument, 'p' for a pattern, or 'q' to quit: ");
      String entry = scanner.next();
      if (entry.startsWith("+")) {
        player.startNote(new Note(entry.substring(1)));
      } else if (entry.startsWith("-")) {
        player.stopNote(new Note(entry.substring(1)));
      } else if (entry.equalsIgnoreCase("i")) {
        player.changeInstrument(random.nextInt(128));
      } else if (entry.equalsIgnoreCase("p")) {
        player.play(PATTERNS[random.nextInt(PATTERNS.length)]);
      } else if (entry.equalsIgnoreCase("q")) {
        quit = true;
      }
    }
    scanner.close();
    player.close();
  }
}

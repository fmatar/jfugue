package org.jfugue.examples.website;

import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;

class IntroToRhythms {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {
    Rhythm rhythm = new Rhythm()
      .addLayer("O..oO...O..oOO..")
      .addLayer("..S...S...S...S.")
      .addLayer("````````````````")
      .addLayer("...............+");
    Player player = new Player();
    player.play(rhythm.getPattern().repeat(2));
  }
}

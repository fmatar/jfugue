package org.jfugue.examples.website;

import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;

class AdvancedRhythms2 {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {
    Rhythm rhythm = new Rhythm().addLayer("O..oO...O..oOO..")
      .addLayer("..S...S...S...S.")
      .addLayer("````````````````")
      .addLayer("...............+")
      .addOneTimeAltLayer(3, 3, "...+...+...+...+")
      .addRangedAltLayer(2, 1, 2, "`...`.``...```.`")
      .addRecurringAltLayer(1, 1, 4, 2, ".sS..sS..sS..sS.")
      .setLength(8);
    new Player().play(rhythm.getPattern());
  }
}

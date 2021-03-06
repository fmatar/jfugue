package org.jfugue.examples.website;

import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.ChordProgression;

class TryThis {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {
    new Player().play(new ChordProgression("I IV vi V").eachChordAs("$_i $_i Ri $_i"),
      new Rhythm().addLayer("..X...X...X...XO"));
  }
}


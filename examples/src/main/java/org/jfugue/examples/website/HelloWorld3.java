package org.jfugue.examples.website;

import org.jfugue.player.Player;

class HelloWorld3 {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {
    Player player = new Player();
    player.play(
      "T[Adagio] V0 I[Guitar] m450.8q m460.8q m541.9q m342.5q | m310.7q m320.7q m450.8q m342.5q");
  }
}
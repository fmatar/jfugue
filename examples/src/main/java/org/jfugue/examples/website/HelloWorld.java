package org.jfugue.examples.website;

import org.jfugue.player.Player;

class HelloWorld {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {
    Player player = new Player();
    player.play("C D E F G A B");
  }
}

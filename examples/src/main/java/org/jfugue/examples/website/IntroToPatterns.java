package org.jfugue.examples.website;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

class IntroToPatterns {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String[] args) {
    Pattern p1 = new Pattern("V0 I[Piano] Eq Ch. | Eq Ch. | Dq Eq Dq Cq");
    Pattern p2 = new Pattern("V1 I[Flute] Rw     | Rw     | GmajQQQ  CmajQ");
    Player player = new Player();
    player.play(p1, p2);
  }
}

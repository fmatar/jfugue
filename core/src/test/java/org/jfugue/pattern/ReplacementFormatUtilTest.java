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

package org.jfugue.pattern;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.jfugue.theory.Chord;
import org.junit.Test;

public class ReplacementFormatUtilTest {

  @Test
  public void testSimpleReplacement() {
    Pattern[] patterns = new Pattern[3];
    patterns[0] = new Pattern("C");
    patterns[1] = new Pattern("D");
    patterns[2] = new Pattern("E");

    Pattern result = ReplacementFormatUtil
      .replaceDollarsWithCandidates("$0 $1 $2 $!", patterns, new Pattern("C+D+E"));
    assertEquals("C D E C+D+E", result.toString());
  }

  @Test
  public void testCharactersThatDontGetReplaced() {
    Pattern[] patterns = new Pattern[3];
    patterns[0] = new Pattern("C");

    Pattern result = ReplacementFormatUtil
      .replaceDollarsWithCandidates("$0 T U V", patterns, null);
    assertEquals("C T U V", result.toString());
  }

  @Test
  public void testSpecialReplacement() {
    Pattern[] patterns = new Pattern[3];
    patterns[0] = new Pattern("C");

    Map<String, PatternProducer> specialMap = new HashMap<>();
    specialMap.put("T", new Pattern("E"));

    Pattern result = ReplacementFormatUtil
      .replaceDollarsWithCandidates("$0 T U V $T", patterns, null, specialMap, " ", " ",
        null);
    assertEquals("C T U V E", result.toString());
  }

  @Test
  public void testReplacementWithIndividualAppenders() {
    Pattern[] patterns = new Pattern[3];
    patterns[0] = new Pattern("C");
    patterns[1] = new Pattern("D");
    patterns[2] = new Pattern("E");

    Pattern result = ReplacementFormatUtil
      .replaceDollarsWithCandidates("$0q $1h $2w $!q", patterns, new Pattern("(C+D+E)"));
    assertEquals("Cq Dh Ew (C+D+E)q", result.toString());
  }

  @Test
  public void testReplacementWithChord() {
    Chord chord = new Chord("Cmaj");

    Pattern result = ReplacementFormatUtil
      .replaceDollarsWithCandidates("$0q $1h $2w $!q", chord.getNotes(), chord);
    assertEquals("Cq Eh Gw CMAJq", result.toString());
  }

  @Test
  public void testUnderscoreAndPlus() {
    Chord chord = new Chord("Cmaj");

    Pattern result = ReplacementFormatUtil
      .replaceDollarsWithCandidates("$0q+$1q $2h $1h+$2q_$0q", chord.getNotes(), chord);
    assertEquals("Cq+Eq Gh Eh+Gq_Cq", result.toString());
  }

  @Test
  public void testReplacementWithLongSpecialMapKeys() {
    Chord chord = new Chord("Cmaj");

    Map<String, PatternProducer> specialMap = new HashMap<>();
    specialMap.put("URANUS", new Pattern("C D E F"));
    specialMap.put("NEPTUNE", new Pattern("A B Ab B"));
    specialMap.put("JUPITER", new Pattern("C G C G"));
    specialMap.put("MARS", new Pattern("F E D C"));

    Pattern result = ReplacementFormatUtil
      .replaceDollarsWithCandidates("$0q,$1h,$2w,$URANUSq,Rq,$NEPTUNEq,$!q", chord.getNotes(),
        chord, specialMap, ",", " ", ".");
    assertEquals("Cq. Eh. Gw. Cq. Dq. Eq. Fq. Rq. Aq. Bq. Abq. Bq. CMAJq.", result.toString());
  }

}

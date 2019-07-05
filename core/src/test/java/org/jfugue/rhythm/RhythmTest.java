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

package org.jfugue.rhythm;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import org.junit.Test;

public class RhythmTest {

  @Test
  public void testReplacingWithDefaultRhythmKit() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer(".OoSs^`*+x");
    assertEquals(
      "V9 L0 Ri [BASS_DRUM]i Rs [BASS_DRUM]s [ACOUSTIC_SNARE]i Rs [ACOUSTIC_SNARE]s [PEDAL_HI_HAT]i [PEDAL_HI_HAT]s Rs [CRASH_CYMBAL_1]i [CRASH_CYMBAL_1]s Rs Rs [HAND_CLAP]s",
      rhythm.getPattern().toString());
  }

  @Test
  public void testReplacingWithProvidedRhythmKit() {
    Rhythm rhythm = new Rhythm();
    rhythm.setRhythmKit(new HashMap<Character, String>() {{
      put('a', "[BASS_DRUM]i");
      put('b', "[ACOUSTIC_SNARE]s Rs");
      put('.', "Ri");
    }});
    rhythm.addLayer("ab.");
    assertEquals("V9 L0 [BASS_DRUM]i [ACOUSTIC_SNARE]s Rs Ri", rhythm.getPattern().toString());
  }

  @Test
  public void testReplacingAndMultipleLayers() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer(".");
    rhythm.addLayer("O");
    rhythm.addLayer("o");
    assertEquals("V9 L0 Ri L1 [BASS_DRUM]i L2 Rs [BASS_DRUM]s", rhythm.getPattern().toString());
  }


  @Test
  public void testRhythmWithLength() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(3);
    assertEquals("SoSoSo", rhythm.getRhythm()[0]);
  }

  @Test
  public void testMultiLayerRhythmWithLength() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").addLayer(".^").setLength(5);
    assertEquals("SoSoSoSoSo", rhythm.getRhythm()[0]);
    assertEquals(".^.^.^.^.^", rhythm.getRhythm()[1]);
  }

  @Test
  public void testRhythmWithRangedAltLayer() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(5);
    rhythm.addRangedAltLayer(0, 1, 2, "PP");
    assertEquals("SoPPPPSoSo", rhythm.getRhythm()[0]);
  }

  @Test
  public void testRhythmWithOneTimeAltLayer() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(5);
    rhythm.addOneTimeAltLayer(0, 2, "PP");
    assertEquals("SoSoPPSoSo", rhythm.getRhythm()[0]);
  }

  @Test
  public void testRhythmWithRecurringAltLayerFrom0() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(10);
    rhythm.addRecurringAltLayer(0, 0, 10, 3, "..");
    assertEquals("..SoSo..SoSo..SoSo..", rhythm.getRhythm()[0]);
  }

  @Test
  public void testRhythmWithRecurringAltLayerFromIndex() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(10);
    rhythm.addRecurringAltLayer(0, 2, 7, 3, "..");
    assertEquals("SoSo..SoSo..SoSoSoSo", rhythm.getRhythm()[0]);
  }

  @Test
  public void testRhythmWithProvidingAltLayer() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(10);
    rhythm.addAltLayerProvider(0, segment -> {
      if ((segment == 2) || (segment == 5)) {
        return "PP";
      } else if (segment == 8) {
        return "ZZ";
      } else {
        return null;
      }
    });
    assertEquals("SoSoPPSoSoPPSoSoZZSo", rhythm.getRhythm()[0]);
  }

  @Test
  public void testAltsWithDefaultZOrder() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(10);
    rhythm.addRecurringAltLayer(0, 0, 7, 2, "..");
    rhythm.addRangedAltLayer(0, 1, 5, "GG");
    rhythm.addOneTimeAltLayer(0, 4, "**");
    assertEquals("..GGGGGG**GG..SoSoSo", rhythm.getRhythm()[0]);
  }

  @Test
  public void testAltsWithSpecifiedZOrder() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").setLength(10);
    rhythm.addRecurringAltLayer(0, 0, 10, 2, "..", 3);
    rhythm.addRangedAltLayer(0, 1, 5, "GG", 2);
    rhythm.addOneTimeAltLayer(0, 4, "**", 1);
    assertEquals("..GG..GG..GG..So..So", rhythm.getRhythm()[0]);
  }

  @Test
  public void testAltsInMultipleLayers() {
    Rhythm rhythm = new Rhythm();
    rhythm.addLayer("So").addLayer("xx").setLength(10);
    rhythm.addRecurringAltLayer(0, 0, 7, 2, "..");
    rhythm.addRangedAltLayer(1, 1, 5, "XX");
    rhythm.addOneTimeAltLayer(1, 4, "**");
    assertEquals("..So..So..So..SoSoSo", rhythm.getRhythm()[0]);
    assertEquals("xxXXXXXX**XXxxxxxxxx", rhythm.getRhythm()[1]);
  }

}

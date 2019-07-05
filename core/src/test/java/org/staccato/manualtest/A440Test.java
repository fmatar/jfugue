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

package org.staccato.manualtest;

import org.jfugue.manualtest.ManualTestPrint;
import org.jfugue.player.Player;

/**
 * This test ensures that A5 - the A above Middle-C - sounds the same as MIDI note 69 and frequency
 * 440Hz, and that A is the same as A5 (i.e., 5 is the default octave)
 */
class A440Test {

  public static void main(String[] args) {
    ManualTestPrint.expectedResult("These next four notes should sound exactly identical");
    Player player = new Player();
    player.play("A5 69 m440 A");
  }
}

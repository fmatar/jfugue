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

package org.staccato;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class MicrotonePreprocessorTest {

  private MicrotonePreprocessor preprocessor;
  private StaccatoParserContext context;

  @Before
  public void setUp() {
    preprocessor = MicrotonePreprocessor.getInstance();
    context = new StaccatoParserContext(null);
  }

  @Test
  public void testMicrotoneAdjustment() {
    System.out.println(MicrotonePreprocessor.convertFrequencyToStaccato(155.56, "s"));
    assertEquals("57s", MicrotonePreprocessor.convertFrequencyToStaccato(440.0, "s"));
    assertEquals("39q", MicrotonePreprocessor.convertFrequencyToStaccato(155.56, "q"));
  }

  @Test
  public void testDoesNotBreakRegularStringsWithM() {
    assertEquals("TIME:44/2 KEY:C", preprocessor.preprocess("TIME:44/2 KEY:C", context));
  }

  @Test
  public void testNoGivenDuration() {
    assertEquals("a :PitchWheel(13384) 59/0.25 :PitchWheel(8192) e",
      preprocessor.preprocess("a m512.3 e", context));
  }

  @Test
  public void testGivenLetterDuration() {
    assertEquals("a :PitchWheel(13384) 59h :PitchWheel(8192) e",
      preprocessor.preprocess("a m512.3h e", context));
  }

  @Test
  public void testGivenNumericDuration() {
    assertEquals("a :PitchWheel(13384) 59/0.5 :PitchWheel(8192) e",
      preprocessor.preprocess("a m512.3/0.5 e", context));
  }

  @Test
  public void testNoDecimalInFrequency() {
    assertEquals("a :PitchWheel(9937) 59/0.25 :PitchWheel(8192) e",
      preprocessor.preprocess("a m500 e", context));
  }

  @Test
  public void testMicrotoneParsedWhenFirst() {
    System.out.println(preprocessor.preprocess("m500 e", context));
    assertEquals(":PitchWheel(9937) 59/0.25 :PitchWheel(8192) e",
      preprocessor.preprocess("m500 e", context));
  }

  @Test
  public void testMicrotoneParsedWhenLast() {
    assertEquals("a :PitchWheel(9937) 59/0.25 :PitchWheel(8192)",
      preprocessor.preprocess("a m500", context));
  }

  @Test
  public void testMicrotoneParsedByItself() {
    assertEquals(":PitchWheel(9937) 59/0.25 :PitchWheel(8192)",
      preprocessor.preprocess("m500", context));
  }

  @Test
  public void testTwoMicrotonesParse() {
    assertEquals(
      "a :PitchWheel(13384) 59/0.25 :PitchWheel(8192) e :PitchWheel(9937) 59/0.25 :PitchWheel(8192) a",
      preprocessor.preprocess("a m512.3 e m500 a", context));
  }

  @Test
  public void testCarnaticValues() {
    assertEquals(":PitchWheel(8192) 48q :PitchWheel(8192) :PitchWheel(14942) 49q :PitchWheel(8192)",
      preprocessor.preprocess("m261.6256q m290.6951q", context));
  }

  @Test(expected = IllegalStateException.class)
  public void testBadDefinition() {
    fail(preprocessor.preprocess("a ma e", context));
  }

}

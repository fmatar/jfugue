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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jfugue.pattern.PatternProducer;

/**
 * Turns to uppercase all tokens that are not lyrics, markers, or functions
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class InstructionPreprocessor implements Preprocessor {

  private static final Pattern keyPattern = Pattern.compile("\\{\\p{ASCII}*?}");
  private static InstructionPreprocessor instance;
  private final Map<String, Instruction> instructions;

  private InstructionPreprocessor() {
    instructions = new HashMap<>();
  }

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.staccato.InstructionPreprocessor} object.
   */
  public static InstructionPreprocessor getInstance() {
    if (instance == null) {
      instance = new InstructionPreprocessor();
    }
    return instance;
  }

  /**
   * <p>addInstruction.</p>
   *
   * @param key a {@link java.lang.String} object.
   * @param value a {@link org.staccato.Instruction} object.
   */
  public void addInstruction(String key, Instruction value) {
    instructions.put(key, value);
  }

  /**
   * <p>addInstruction.</p>
   *
   * @param key a {@link java.lang.String} object.
   * @param value a {@link org.jfugue.pattern.PatternProducer} object.
   */
  public void addInstruction(String key, final PatternProducer value) {
    addInstruction(key, value.getPattern().toString());
  }

  /**
   * <p>addInstruction.</p>
   *
   * @param key a {@link java.lang.String} object.
   * @param value a {@link java.lang.String} object.
   */
  public void addInstruction(String key, final String value) {
    instructions.put(key, instructions -> value);
  }

  /** {@inheritDoc} */
  @Override
  public String preprocess(String s, StaccatoParserContext context) {
    StringBuilder buddy = new StringBuilder();
    int posPrev = 0;

    // Sort all of the instruction keys by length, so we'll deal with the longer ones first
    String[] sizeSortedInstructions = new String[instructions.size()];
    Arrays.sort(instructions.keySet().toArray(sizeSortedInstructions),
      (s1, s2) -> Integer.compare(s2.length(), s1.length()));

    boolean matchFound = false;
    Matcher m = keyPattern.matcher(s);
    while (m.find()) {
      String key = m.group();
      key = key.substring(1, key.length() - 1); // Remove the braces
      for (String possibleMatch : sizeSortedInstructions) {
        if (!matchFound) {
          if (key.startsWith(possibleMatch)) {
            Instruction instruction = instructions.get(possibleMatch);
            String value = key;
            if (instruction != null) {
              value = instruction.onInstructionReceived(key.split(" "));
            }

            buddy.append(s, posPrev, m.start());
            buddy.append(value);
            posPrev = m.end();
            matchFound = true;
          }
        }
      }
      if (!matchFound) {
        posPrev = m.end();
      }
    }

    buddy.append(s.substring(posPrev));
    return buddy.toString();
  }
}

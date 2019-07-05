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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;

/**
 * <p>Instruction interface.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public interface Instruction {

  /**
   * <p>onInstructionReceived.</p>
   *
   * @param instructions an array of {@link java.lang.String} objects.
   * @return a {@link java.lang.String} object.
   */
  String onInstructionReceived(String[] instructions);

  interface Splitter {

    Map<String, String> splitInstructionParameter(String parameter);
  }

  class Choice implements Instruction {

    private final List<String> choices;

    public Choice(String... choices) {
      this.choices = new ArrayList<>();
      this.choices.addAll(Arrays.asList(choices));
    }

    public Choice(int... choices) {
      this.choices = new ArrayList<>();
      for (int choice : choices) {
        this.choices.add(Integer.toString(choice));
      }
    }

    public Choice(PatternProducer... choices) {
      this.choices = new ArrayList<>();
      for (PatternProducer pattern : choices) {
        this.choices.add(pattern.getPattern().toString());
      }
    }

    public List<String> getChoices() {
      return this.choices;
    }


    @Override
    public String onInstructionReceived(String[] instructions) {
      int choice = Integer.decode(instructions[instructions.length - 1]);
      return choices.get(choice);
    }
  }

  class Switch implements Instruction {

    static final char REPLACE_CHAR = '$';
    private final String instruction;
    private final String offValue;
    private final String onValue;

    public Switch(String instruction, String offValue, String onValue) {
      this.instruction = instruction;
      this.offValue = offValue;
      this.onValue = onValue;
    }

    public Switch(String instruction, int offValue, int onValue) {
      this(instruction, Integer.toString(offValue), Integer.toString(onValue));
    }

    public Switch(String instruction, Pattern offValue, Pattern onValue) {
      this(instruction, offValue.toString(), onValue.toString());
    }

    @Override
    public String onInstructionReceived(String[] instructions) {
      StringBuilder buddy = new StringBuilder();
      int posDollar = instruction.indexOf(REPLACE_CHAR);
      buddy.append(instruction, 0, posDollar);
      if (instructions[instructions.length - 1].equalsIgnoreCase("ON")) {
        buddy.append(onValue);
      } else if (instructions[instructions.length - 1].equalsIgnoreCase("OFF")) {
        buddy.append(offValue);
      } else {
        buddy.append(REPLACE_CHAR);
      }
      buddy.append(instruction.substring(posDollar + 1));
      return buddy.toString();
    }
  }

  class LastIsValue implements Instruction {

    static final char REPLACE_CHAR = '$';
    private final String instruction;

    public LastIsValue(String instruction) {
      this.instruction = instruction;
    }

    @Override
    public String onInstructionReceived(String[] instructions) {
      StringBuilder buddy = new StringBuilder();
      int posDollar = instruction.indexOf(REPLACE_CHAR);
      buddy.append(instruction, 0, posDollar);
      buddy.append(instructions[instructions.length - 1]);
      buddy.append(instruction.substring(posDollar + 1));
      return buddy.toString();
    }
  }

  class LastIsValueToSplit implements Instruction {

    private final Splitter splitter;
    private String instruction;

    public LastIsValueToSplit(String instruction, Splitter splitter) {
      this.instruction = instruction;
      this.splitter = splitter;
    }

    @Override
    public String onInstructionReceived(String[] instructions) {
      Map<String, String> map = splitter
        .splitInstructionParameter(instructions[instructions.length - 1]);

      map.keySet().forEach(key -> instruction = instruction.replace(key, map.get(key)));

      return instruction;
    }
  }
}

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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>ReplacementMapPreprocessor class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class ReplacementMapPreprocessor implements Preprocessor {

  private static final Pattern replacementPatternWithBrackets = Pattern.compile("<\\S+>");
  private static final Pattern replacementPatternWithoutBrackets = Pattern.compile("\\S+");
  private static ReplacementMapPreprocessor instance;
  private Map<String, String> map;
  private boolean requiresAngleBrackets = true;
  private boolean caseSensitive = true;
  private int iterations = 1;

  private ReplacementMapPreprocessor() {
  }

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.staccato.ReplacementMapPreprocessor} object.
   */
  public static ReplacementMapPreprocessor getInstance() {
    if (instance == null) {
      instance = new ReplacementMapPreprocessor();
    }
    return instance;
  }

  /**
   * <p>setRequireAngleBrackets.</p>
   *
   * @param require a boolean.
   * @return a {@link org.staccato.ReplacementMapPreprocessor} object.
   */
  public ReplacementMapPreprocessor setRequireAngleBrackets(boolean require) {
    this.requiresAngleBrackets = require;
    return this;
  }

  private boolean requiresAngleBrackets() {
    return this.requiresAngleBrackets;
  }

  private boolean isCaseSensitive() {
    return this.caseSensitive;
  }

  /**
   * NOTE: This might seem a little backwards, but if your ReplacementMap is not case sensitive,
   * then you need to use only upper-case letters as keys in your map!
   *
   * @param caseSensitive a boolean.
   * @return a {@link org.staccato.ReplacementMapPreprocessor} object.
   */
  public ReplacementMapPreprocessor setCaseSensitive(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
    return this;
  }

  /**
   * <p>setReplacementMap.</p>
   *
   * @param map a {@link java.util.Map} object.
   * @return a {@link org.staccato.ReplacementMapPreprocessor} object.
   */
  public ReplacementMapPreprocessor setReplacementMap(Map<String, String> map) {
    this.map = map;
    return this;
  }

  /**
   * <p>Getter for the field <code>iterations</code>.</p>
   *
   * @return a int.
   */
  public int getIterations() {
    return this.iterations;
  }

  /**
   * <p>Setter for the field <code>iterations</code>.</p>
   *
   * @param iterations a int.
   * @return a {@link org.staccato.ReplacementMapPreprocessor} object.
   */
  public ReplacementMapPreprocessor setIterations(int iterations) {
    this.iterations = iterations;
    return this;
  }

  private Pattern getReplacementPattern() {
    return requiresAngleBrackets() ? replacementPatternWithBrackets
      : replacementPatternWithoutBrackets;
  }

  /** {@inheritDoc} */
  @Override
  public String preprocess(String s, StaccatoParserContext context) {
    String iteratingString = s;
    for (int i = 0; i < iterations; i++) {
      StringBuilder buddy = new StringBuilder();
      int posPrev = 0;

      Matcher m = getReplacementPattern().matcher(iteratingString);
      while (m.find()) {
        String foundKey =
          requiresAngleBrackets() ? m.group().substring(1, m.group().length() - 1) : m.group();
        buddy.append(iteratingString, posPrev, m.start());
        String lookupKey = isCaseSensitive() ? foundKey : foundKey.toUpperCase();
        String replacementValue = map.get(lookupKey);
        if (replacementValue != null) {
          buddy.append(map.get(lookupKey));
        } else {
          buddy.append(
            foundKey); // If the key doesn't have a value, just put the key back - it might be intended for another parser or purpose
        }
        posPrev = m.end();
      }

      buddy.append(iteratingString.substring(posPrev));
      iteratingString = buddy.toString();
    }
    return iteratingString;
  }
}

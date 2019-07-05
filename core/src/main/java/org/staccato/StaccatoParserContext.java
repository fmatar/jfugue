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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.jfugue.parser.Parser;
import org.jfugue.theory.Key;
import org.jfugue.theory.TimeSignature;


/**
 * <p>StaccatoParserContext class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class StaccatoParserContext {

  private final Parser parser;
  private final Map<String, Object> dictionary;
  private Key currentKey = Key.DEFAULT_KEY;
  private TimeSignature currentTimeSignature = TimeSignature.DEFAULT_TIMESIG;

  /**
   * <p>Constructor for StaccatoParserContext.</p>
   *
   * @param parser a {@link org.jfugue.parser.Parser} object.
   */
  public StaccatoParserContext(Parser parser) {
    this.parser = parser;
    this.dictionary = new HashMap<>();
  }

  /**
   * <p>Getter for the field <code>dictionary</code>.</p>
   *
   * @return a {@link java.util.Map} object.
   */
  public Map<String, Object> getDictionary() {
    return this.dictionary;
  }

  private StaccatoParserContext loadDictionary(Reader reader) throws IOException {
    BufferedReader bread = new BufferedReader(reader);
    while (bread.ready()) {
      String s = bread.readLine();
      if ((s != null) && (s.length() > 1)) {
        if (s.charAt(0) == '#') {
          // Skip this line, it's a comment
        } else if (s.charAt(0) == '$') {
          // This line is a definition
          String key = s.substring(1, s.indexOf('=')).trim();
          String value = s.substring(s.indexOf('=') + 1).trim();
          dictionary.put(key, value);
        }
      }
    }
    bread.close();

    return this;
  }

  /**
   * <p>Getter for the field <code>parser</code>.</p>
   *
   * @return a {@link org.jfugue.parser.Parser} object.
   */
  public Parser getParser() {
    return this.parser;
  }

  /**
   * <p>loadDictionary.</p>
   *
   * @param stream a {@link java.io.InputStream} object.
   * @return a {@link org.staccato.StaccatoParserContext} object.
   * @throws java.io.IOException if any.
   */
  public StaccatoParserContext loadDictionary(InputStream stream) throws IOException {
    return loadDictionary(new InputStreamReader(stream));
  }

  /**
   * <p>loadDictionary.</p>
   *
   * @param file a {@link java.io.File} object.
   * @return a {@link org.staccato.StaccatoParserContext} object.
   * @throws java.io.IOException if any.
   */
  public StaccatoParserContext loadDictionary(File file) throws IOException {
    return loadDictionary(new FileReader(file));
  }

  /**
   * <p>getKey.</p>
   *
   * @return a {@link org.jfugue.theory.Key} object.
   */
  public Key getKey() {
    return this.currentKey;
  }

  /**
   * <p>setKey.</p>
   *
   * @param key a {@link org.jfugue.theory.Key} object.
   * @return a {@link org.staccato.StaccatoParserContext} object.
   */
  public StaccatoParserContext setKey(Key key) {
    this.currentKey = key;
    return this;
  }

  /**
   * <p>getTimeSignature.</p>
   *
   * @return a {@link org.jfugue.theory.TimeSignature} object.
   */
  public TimeSignature getTimeSignature() {
    return this.currentTimeSignature;
  }

  /**
   * <p>setTimeSignature.</p>
   *
   * @param timeSignature a {@link org.jfugue.theory.TimeSignature} object.
   * @return a {@link org.staccato.StaccatoParserContext} object.
   */
  public StaccatoParserContext setTimeSignature(TimeSignature timeSignature) {
    this.currentTimeSignature = timeSignature;
    return this;
  }


}

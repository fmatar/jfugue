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

package org.staccato.functions;

import java.util.HashMap;
import java.util.Map;
import org.staccato.PreprocessorFunction;
import org.staccato.SubparserFunction;

/**
 * <p>FunctionManager class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class FunctionManager {

  private static FunctionManager instance;
  private final Map<String, PreprocessorFunction> preprocessorFunctions;
  private final Map<String, SubparserFunction> subparserFunctions;

  private FunctionManager() {
    this.preprocessorFunctions = new HashMap<>();
    this.subparserFunctions = new HashMap<>();
  }

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.staccato.functions.FunctionManager} object.
   */
  public static FunctionManager getInstance() {
    if (instance == null) {
      instance = new FunctionManager();
    }
    return instance;
  }

  //
  // Preprocessor Functions
  //

  /**
   * <p>addPreprocessorFunction.</p>
   *
   * @param function a {@link org.staccato.PreprocessorFunction} object.
   */
  public void addPreprocessorFunction(PreprocessorFunction function) {
    for (String name : function.getNames()) {
      preprocessorFunctions.put(name.toUpperCase(), function);
    }
  }

  /**
   * <p>removePreprocessorFunction.</p>
   *
   * @param function a {@link org.staccato.PreprocessorFunction} object.
   */
  public void removePreprocessorFunction(PreprocessorFunction function) {
    for (String name : function.getNames()) {
      preprocessorFunctions.remove(name.toUpperCase());
    }
  }

  /**
   * <p>getPreprocessorFunction.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link org.staccato.PreprocessorFunction} object.
   */
  public PreprocessorFunction getPreprocessorFunction(String name) {
    return preprocessorFunctions.get(name.toUpperCase());
  }

  //
  // Subparser Functions
  //

  /**
   * <p>addSubparserFunction.</p>
   *
   * @param function a {@link org.staccato.SubparserFunction} object.
   */
  public void addSubparserFunction(SubparserFunction function) {
    for (String name : function.getNames()) {
      subparserFunctions.put(name.toUpperCase(), function);
    }
  }

  /**
   * <p>removeSubparserFunction.</p>
   *
   * @param function a {@link org.staccato.SubparserFunction} object.
   */
  public void removeSubparserFunction(SubparserFunction function) {
    for (String name : function.getNames()) {
      subparserFunctions.remove(name.toUpperCase());
    }
  }

  /**
   * <p>getSubparserFunction.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link org.staccato.SubparserFunction} object.
   */
  public SubparserFunction getSubparserFunction(String name) {
    return subparserFunctions.get(name.toUpperCase());
  }
}

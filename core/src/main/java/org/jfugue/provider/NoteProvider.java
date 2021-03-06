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

package org.jfugue.provider;

import org.jfugue.theory.Note;

/**
 * This interface must be implemented by the parser responsible for Staccato strings
 *
 * @author fmatar
 * @version $Id: $Id
 */
public interface NoteProvider {

  /**
   * <p>createNote.</p>
   *
   * @param noteString a {@link java.lang.String} object.
   * @return a {@link org.jfugue.theory.Note} object.
   */
  Note createNote(String noteString);

  /**
   * <p>getMiddleC.</p>
   *
   * @return a {@link org.jfugue.theory.Note} object.
   */
  Note getMiddleC();

  /**
   * <p>getDurationForString.</p>
   *
   * @param string a {@link java.lang.String} object.
   * @return a double.
   */
  double getDurationForString(String string);
}

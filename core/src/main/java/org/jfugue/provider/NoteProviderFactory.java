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

import org.staccato.NoteSubparser;

/**
 * <p>NoteProviderFactory class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class NoteProviderFactory {

  private static NoteProvider noteProvider;

  /**
   * <p>Getter for the field <code>noteProvider</code>.</p>
   *
   * @return a {@link org.jfugue.provider.NoteProvider} object.
   */
  public static NoteProvider getNoteProvider() {
    if (noteProvider == null) {
      noteProvider = NoteSubparser.getInstance();
    }
    return noteProvider;
  }
}

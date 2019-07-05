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

package org.jfugue.parser;

/**
 * <p>ParserException class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class ParserException extends RuntimeException {

  private static final long serialVersionUID = -5224628162824708074L;

  private final String exception;
  private final String errantString;
  private int position = -1;

  /**
   * <p>Constructor for ParserException.</p>
   *
   * @param exception a {@link java.lang.String} object.
   * @param errantString a {@link java.lang.String} object.
   */
  public ParserException(String exception, String errantString) {
    super(exception + ": " + errantString);
    this.exception = exception;
    this.errantString = errantString;
  }

  /**
   * <p>Getter for the field <code>position</code>.</p>
   *
   * @return a int.
   */
  public int getPosition() {
    return this.position;
  }

  /**
   * <p>Setter for the field <code>position</code>.</p>
   *
   * @param position a int.
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * <p>getMessage.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getMessage() {
    if (position > -1) {
      return this.exception + ": " + errantString + " (Position " + position + ")";
    } else {
      return this.exception + ": " + errantString;
    }
  }
}

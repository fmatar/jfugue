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

package org.jfugue.testtools.parser;

import org.junit.Ignore;

@Ignore
class TwoByteArgEvent extends ArgEvent {

  private final byte value1;
  private final byte value2;

  public TwoByteArgEvent(String eventName, byte value1, byte value2) {
    super(eventName);
    this.value1 = value1;
    this.value2 = value2;
  }

  public byte getValue1() {
    return this.value1;
  }

  public byte getValue2() {
    return this.value2;
  }
}

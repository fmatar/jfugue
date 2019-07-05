package org.jfugue.parser;

import org.jfugue.theory.Note;

/**
 * <p>NoteEventListener interface.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public interface NoteEventListener {

  /**
   * <p>onNoteStarted.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  void onNoteStarted(Note note);

  /**
   * <p>onNoteFinished.</p>
   *
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  void onNoteFinished(Note note);
}

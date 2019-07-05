package org.jfugue.pattern;

import java.util.List;
import org.jfugue.theory.Note;

/**
 * <p>NoteProducer interface.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public interface NoteProducer {

  /**
   * <p>getNotes.</p>
   *
   * @return a {@link java.util.List} object.
   */
  List<Note> getNotes();
}

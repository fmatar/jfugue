package org.jfugue.pattern;

import org.jfugue.theory.Note;
import org.staccato.AtomSubparser;
import org.staccato.IVLSubparser;
import org.staccato.StaccatoParserContext;

/**
 * An Atom represents a single entity of a Voice+Layer+Instrument+Note and is useful especially when
 * using the Realtime Player, so all of the information about a specific note is conveyed at the
 * same time. Pattern now has an atomize() method that will turn the Pattern into a collection of
 * atoms.
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Atom implements PatternProducer {

  private byte voice;
  private byte layer;
  private byte instrument;
  private Note note;
  private String contents;

  /**
   * <p>Constructor for Atom.</p>
   *
   * @param voice a byte.
   * @param layer a byte.
   * @param instrument a byte.
   * @param note a {@link java.lang.String} object.
   */
  public Atom(byte voice, byte layer, byte instrument, String note) {
    this(voice, layer, instrument, new Note(note));
  }

  private Atom(byte voice, byte layer, byte instrument, Note note) {
    createAtom(voice, layer, instrument, note);
  }

  /**
   * <p>Constructor for Atom.</p>
   *
   * @param voice a {@link java.lang.String} object.
   * @param layer a {@link java.lang.String} object.
   * @param instrument a {@link java.lang.String} object.
   * @param note a {@link java.lang.String} object.
   */
  public Atom(String voice, String layer, String instrument, String note) {
    this(voice, layer, instrument, new Note(note));
  }

  /**
   * <p>Constructor for Atom.</p>
   *
   * @param voice a {@link java.lang.String} object.
   * @param layer a {@link java.lang.String} object.
   * @param instrument a {@link java.lang.String} object.
   * @param note a {@link org.jfugue.theory.Note} object.
   */
  public Atom(String voice, String layer, String instrument, Note note) {
    StaccatoParserContext context = new StaccatoParserContext(null);
    IVLSubparser.populateContext(context);

    // Need to use toUpperCase() and create a context... this is sounding very parser-ish
    // and should ideally happen elsewhere!
    createAtom(IVLSubparser.getInstance().getValue(voice.toUpperCase(), context),
      IVLSubparser.getInstance().getValue(layer.toUpperCase(), context),
      IVLSubparser.getInstance().getValue(instrument.toUpperCase(), context),
      new Note(note));
  }

  private void createAtom(byte voice, byte layer, byte instrument, Note note) {
    this.voice = voice;
    this.layer = layer;
    this.instrument = instrument;
    this.note = note;

    String buddy = String.valueOf(AtomSubparser.ATOM)
      + IVLSubparser.VOICE
      + voice
      + AtomSubparser.QUARK_SEPARATOR
      + IVLSubparser.LAYER
      + layer
      + AtomSubparser.QUARK_SEPARATOR
      + IVLSubparser.INSTRUMENT
      + instrument
      + AtomSubparser.QUARK_SEPARATOR
      + note;
    this.contents = buddy;
  }

  /**
   * <p>Getter for the field <code>voice</code>.</p>
   *
   * @return a byte.
   */
  public byte getVoice() {
    return this.voice;
  }

  /**
   * <p>Getter for the field <code>layer</code>.</p>
   *
   * @return a byte.
   */
  public byte getLayer() {
    return this.layer;
  }

  /**
   * <p>Getter for the field <code>instrument</code>.</p>
   *
   * @return a byte.
   */
  public byte getInstrument() {
    return this.instrument;
  }

  /**
   * <p>Getter for the field <code>note</code>.</p>
   *
   * @return a {@link org.jfugue.theory.Note} object.
   */
  public Note getNote() {
    return this.note;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.contents;
  }

  /** {@inheritDoc} */
  @Override
  public Pattern getPattern() {
    return new Pattern(contents);
  }
}

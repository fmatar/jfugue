package org.staccato;

import org.jfugue.midi.MidiDefaults;
import org.jfugue.theory.Note;

/**
 * <p>DefaultNoteSettingsManager class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class DefaultNoteSettingsManager {

  /** Constant <code>DEFAULT_DEFAULT_OCTAVE=5</code> */
  public static final byte DEFAULT_DEFAULT_OCTAVE = 5;
  /** Constant <code>DEFAULT_DEFAULT_BASS_OCTAVE=4</code> */
  public static final byte DEFAULT_DEFAULT_BASS_OCTAVE = 4; // Updated in JFugue5; in previous versions, bass was Octave 3
  /** Constant <code>DEFAULT_DEFAULT_DURATION=0.25d</code> */
  public static final double DEFAULT_DEFAULT_DURATION = 0.25d;
  /** Constant <code>DEFAULT_DEFAULT_ON_VELOCITY=MidiDefaults.MIDI_DEFAULT_ON_VELOCITY</code> */
  public static final byte DEFAULT_DEFAULT_ON_VELOCITY = MidiDefaults.MIDI_DEFAULT_ON_VELOCITY;
  /** Constant <code>DEFAULT_DEFAULT_OFF_VELOCITY=MidiDefaults.MIDI_DEFAULT_OFF_VELOCITY</code> */
  public static final byte DEFAULT_DEFAULT_OFF_VELOCITY = MidiDefaults.MIDI_DEFAULT_OFF_VELOCITY;
  private static final boolean DEFAULT_ADJUST_NOTES_BY_KEY_SIGNATURE = true;
  private static DefaultNoteSettingsManager instance;
  private byte defaultOctave = DEFAULT_DEFAULT_OCTAVE;
  private byte defaultBassOctave = DEFAULT_DEFAULT_BASS_OCTAVE;
  private double defaultDuration = DEFAULT_DEFAULT_DURATION;
  private byte defaultOnVelocity = MidiDefaults.MIDI_DEFAULT_ON_VELOCITY;
  private byte defaultOffVelocity = MidiDefaults.MIDI_DEFAULT_OFF_VELOCITY;
  private boolean adjustNotesByKeySignature = DEFAULT_ADJUST_NOTES_BY_KEY_SIGNATURE;

  private DefaultNoteSettingsManager() {
  }

  /**
   * <p>Getter for the field <code>instance</code>.</p>
   *
   * @return a {@link org.staccato.DefaultNoteSettingsManager} object.
   */
  public static DefaultNoteSettingsManager getInstance() {
    if (instance == null) {
      instance = new DefaultNoteSettingsManager();
    }
    return instance;
  }

  /**
   * <p>Getter for the field <code>defaultOctave</code>.</p>
   *
   * @return a byte.
   */
  public byte getDefaultOctave() {
    return this.defaultOctave;
  }

  /**
   * <p>Setter for the field <code>defaultOctave</code>.</p>
   *
   * @param octave a byte.
   */
  public void setDefaultOctave(byte octave) {
    assert (octave >= Note.MIN_OCTAVE) && (octave <= Note.MAX_OCTAVE);
    this.defaultOctave = octave;
  }

  /**
   * <p>Getter for the field <code>defaultBassOctave</code>.</p>
   *
   * @return a byte.
   */
  public byte getDefaultBassOctave() {
    return this.defaultBassOctave;
  }

  /**
   * <p>Setter for the field <code>defaultBassOctave</code>.</p>
   *
   * @param octave a byte.
   */
  public void setDefaultBassOctave(byte octave) {
    assert (octave >= Note.MIN_OCTAVE) && (octave <= Note.MAX_OCTAVE);
    this.defaultBassOctave = octave;
  }

  /**
   * <p>Getter for the field <code>defaultDuration</code>.</p>
   *
   * @return a double.
   */
  public double getDefaultDuration() {
    return this.defaultDuration;
  }

  /**
   * <p>Setter for the field <code>defaultDuration</code>.</p>
   *
   * @param duration a double.
   */
  public void setDefaultDuration(double duration) {
    this.defaultDuration = duration;
  }

  /**
   * <p>Getter for the field <code>defaultOnVelocity</code>.</p>
   *
   * @return a byte.
   */
  public byte getDefaultOnVelocity() {
    return this.defaultOnVelocity;
  }

  /**
   * <p>Setter for the field <code>defaultOnVelocity</code>.</p>
   *
   * @param attack a byte.
   */
  public void setDefaultOnVelocity(byte attack) {
    assert (attack >= MidiDefaults.MIN_ON_VELOCITY) && (attack <= MidiDefaults.MAX_ON_VELOCITY);
    this.defaultOnVelocity = attack;
  }

  /**
   * <p>Getter for the field <code>defaultOffVelocity</code>.</p>
   *
   * @return a byte.
   */
  public byte getDefaultOffVelocity() {
    return this.defaultOffVelocity;
  }

  /**
   * <p>Setter for the field <code>defaultOffVelocity</code>.</p>
   *
   * @param decay a byte.
   */
  public void setDefaultOffVelocity(byte decay) {
    assert (decay >= MidiDefaults.MIN_OFF_VELOCITY) && (decay <= MidiDefaults.MAX_OFF_VELOCITY);
    this.defaultOffVelocity = decay;
  }

  /**
   * <p>Getter for the field <code>adjustNotesByKeySignature</code>.</p>
   *
   * @return a boolean.
   */
  public boolean getAdjustNotesByKeySignature() {
    return this.adjustNotesByKeySignature;
  }

  /**
   * <p>Setter for the field <code>adjustNotesByKeySignature</code>.</p>
   *
   * @param b a boolean.
   */
  public void setAdjustNotesByKeySignature(boolean b) {
    this.adjustNotesByKeySignature = b;
  }
}
